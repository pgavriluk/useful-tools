package utils;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashMap;

@Slf4j
public class AllureEnvironmentWriter {

    private static HashMap<String, String> mapValue = new HashMap<>();

    private static void allureEnvironmentWriter(HashMap<String, String> environmentValuesMap) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element environment = doc.createElement("environment");
            doc.appendChild(environment);
            environmentValuesMap.forEach((k, v) -> {
                Element parameter = doc.createElement("parameter");
                Element key = doc.createElement("key");
                Element value = doc.createElement("value");
                key.appendChild(doc.createTextNode(k));
                value.appendChild(doc.createTextNode(v));
                parameter.appendChild(key);
                parameter.appendChild(value);
                environment.appendChild(parameter);
            });

            // Write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File allureResultsDir = new File(System.getProperty("user.dir")
                    + "/target/allure-results");
            if (!allureResultsDir.exists()) {
                allureResultsDir.mkdirs();
            }
            StreamResult result = new StreamResult(
                    new File(System.getProperty("user.dir")
                            + "/target/allure-results/environment.xml"));
            transformer.transform(source, result);
            log.info("Allure environment data saved.");
        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }

    public static void addValue(String key, String value) {
        if (mapValue.isEmpty()) {
            addDefaultValues();
        }
        mapValue.put(key, value);
    }

    public static void writeToFile() {
        allureEnvironmentWriter(mapValue);
    }

    private static void addDefaultValues() {
        mapValue.put("Java.version", System.getProperty("java.version"));
        mapValue.put("Java.runtime.version", System.getProperty("java.runtime.version"));
        mapValue.put("User.name", System.getProperty("user.name"));
        mapValue.put("Os.name", System.getProperty("os.name"));
        mapValue.put("Os.version", System.getProperty("os.version"));
    }

}
