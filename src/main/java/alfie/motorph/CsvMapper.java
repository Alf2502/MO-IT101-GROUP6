package alfie.motorph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvMapper {

    /**
     * Reads a CSV file and returns a list of rows as String arrays.
     *
     * @param filePath Path to the CSV file.
     * @return List of rows, where each row is a String array.
     */
    public static List<String[]> readCsv(String filePath) {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                rows.add(parts);
            }
        } catch (IOException e) {
            System.err.println("❌ Error reading CSV file: " + e.getMessage());
        }
        return rows;
    }
}