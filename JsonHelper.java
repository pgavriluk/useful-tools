package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonHelper {

    private static final String ROOT_PATH = "src/main/resources/";
    private static final String JSON_FILE_NAME = "body_request.json";
    private static ObjectMapper mapper = new ObjectMapper();

    public static void createJsonForBodyRequest(Object bodyRequest) {
        // Java object to JSON file
        try {
            mapper.writeValue(new File(ROOT_PATH + JSON_FILE_NAME), bodyRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getJsonAsAString(Object bodyRequest) {
        // Java objects to JSON string - compact-print
        String jsonString = null;
        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bodyRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    public static Object getBodyRequest() throws IOException {
        //JSON file to Java object

        return mapper.readValue(new File(ROOT_PATH + JSON_FILE_NAME), Object.class);
    }


}
