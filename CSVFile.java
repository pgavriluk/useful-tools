package utils;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVFile {

    private static final String ROOT_PATH = "src/main/resources/";

    public static void writeResultsToCSVFile(String fileName, List<String[]> data, String[] columnNames) {

        List<String[]> rows = new ArrayList<>();
        rows.add(columnNames);
        rows.addAll(data);

        CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);

        ObjectWriter writer = mapper.writer(CsvSchema.builder().build().withLineSeparator("\n"));
        try {
            writer.writeValue(new File(ROOT_PATH + fileName), rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
