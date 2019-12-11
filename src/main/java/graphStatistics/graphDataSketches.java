package graphStatistics;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class graphDataSketches {

    public List<String> createSketches(String vertexCsvPath, String edgeCsvPatch) throws IOException { //Prova

        List<String> csvHeaders;

        try (
                Reader reader = Files.newBufferedReader(Paths.get(vertexCsvPath));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim());
        ) {
            csvHeaders = csvParser.getHeaderNames();
            System.out.println(csvHeaders.toString());
            return csvHeaders;
            /*
            for (CSVRecord csvRecord : csvParser) {
                // Accessing values by Header names
                String name = csvRecord.get("Name");
                String email = csvRecord.get("Email");
                String phone = csvRecord.get("Phone");
                String country = csvRecord.get("Country");

                System.out.println("Record No - " + csvRecord.getRecordNumber());
                System.out.println("---------------");
                System.out.println("Name : " + name);
                System.out.println("Email : " + email);
                System.out.println("Phone : " + phone);
                System.out.println("Country : " + country);
                System.out.println("---------------\n\n");
            }
             */
        }

    }

}
