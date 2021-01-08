package utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class MapUtils {

    public synchronized static Map<String, String> convertObjectToMapOfStrings(Object object) {
        Method[] methods = object.getClass().getMethods();

        Map<String, String> map = new HashMap<>();

        for (Method m : methods) {
            if (m.getName().startsWith("get") && !m.getName().startsWith("getClass")) {
                Object value = null;
                try {
                    value = m.invoke(object);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                map.put(m.getName().substring(3, 4).toLowerCase() + m.getName().substring(4), (String) value);
            }
        }
        return map;
    }

    public synchronized static Map<String, Object> convertJsonStringToMapOfObjects(String json) {
        Map<String, Object> map = new HashMap<>();
        List<String> objectList = Arrays.asList(cleanString(json).split("\\{"));

        if (json.indexOf("[") < json.indexOf("{")) {
            map.put("[", fillList(objectList));
        } else {
            map = fillMap(objectList);
        }

        return map;
    }

    private synchronized static List<Map<String, Object>> fillList(List<String> list) {
        List<Map<String, Object>> listOfMaps = new ArrayList<>();

        for (int i = 1; i < list.size(); i++) {
            if (list.get(i - 1).contains("]")) {
                break;
            } else {
                listOfMaps.add(fillMap(list.subList(i, list.size())));
            }
        }

        return listOfMaps;
    }

    private synchronized static Map<String, Object> fillMap(List<String> list) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).trim().isEmpty() && !list.get(i).trim().equals("[")) {
                String[] subObjectList = list.get(i).split(",\n");
                for (String subObject : subObjectList) {
                    subObject = subObject.replace("\n", "");
                    if (subObject.substring(subObject.length() - 1).equalsIgnoreCase("}")) {
                        map.put(subObject.split(":")[0], subObject.split(":")[1].replace("}", ""));
                        return map;
                    } else if (subObject.substring(subObject.length() - 1).equalsIgnoreCase("]")) {
                        map.put(subObject.split(":")[0], subObject.split(":")[1].replace("]", ""));
                        return map;
                    } else if (subObject.substring(subObject.length() - 1).equalsIgnoreCase("[")) {
                        map.put(subObject.replace(":", "").replace("[", ""),
                                fillList(list.subList(++i, list.size())));
                        System.out.println(map);
                    } else if (subObject.split(":").length == 2) {
                        map.put(subObject.split(":")[0], subObject.split(":")[1]);
                    } else if (subObject.substring(subObject.length() - 1).equalsIgnoreCase(":")) {
                        map.put(subObject.replace(":", ""), fillMap(list.subList(++i, list.size())));
                    } else {
                        map.put(subObject.split(":", 2)[0], subObject.split(":", 2)[1]);
                    }
                }
            }
        }

        return map;
    }

    private synchronized static String cleanString(String str) {
        return str.replace(" ", "")
                .replace("\"", "");
    }
}
