package com.airquality.service;

import com.airquality.model.PollutionReading;
import com.airquality.repository.PollutionReadingRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class DataIngestionRunner implements CommandLineRunner {

    private final PollutionReadingRepository repository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int BATCH_SIZE = 100;

    public DataIngestionRunner(PollutionReadingRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if database is already populated
        if (repository.count() == 0) {
            System.out.println("Database is empty. Starting data ingestion from city_day.csv...");
            ingestDataFromCSV();
            System.out.println("Data ingestion completed successfully!");
        } else {
            System.out.println("Database already contains data. Skipping ingestion.");
        }
    }

    private void ingestDataFromCSV() throws IOException {
        try (InputStreamReader reader = new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("city_day.csv")));
                CSVReader csvReader = new CSVReader(reader)) {

            String[] line;
            boolean isFirstLine = true;
            List<PollutionReading> batch = new ArrayList<>();

            while ((line = csvReader.readNext()) != null) {
                // Skip header row
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                try {
                    PollutionReading reading = parseCSVLine(line);
                    if (reading != null) {
                        batch.add(reading);

                        // Save batch when it reaches the batch size
                        if (batch.size() >= BATCH_SIZE) {
                            repository.saveAll(batch);
                            System.out.println("Saved batch of " + batch.size() + " records...");
                            batch.clear();
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line: " + String.join(",", line) + " - " + e.getMessage());
                }
            }

            // Save remaining records
            if (!batch.isEmpty()) {
                repository.saveAll(batch);
                System.out.println("Saved final batch of " + batch.size() + " records...");
            }

        } catch (CsvValidationException | IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            throw new IOException(e);
        }
    }

    private PollutionReading parseCSVLine(String[] line) {
        try {
            // Expected CSV format:
            // City,Date,PM2.5,PM10,NO,NO2,NOx,NH3,CO,SO2,O3,Benzene,Toluene,Xylene,AQI,AQI_Bucket
            // Indices: 0=City, 1=Date, 2=PM2.5, 3=PM10, 4=NO, 5=NO2, 6=NOx, 7=NH3, 8=CO,
            // 9=SO2, 10=O3, 11=Benzene, 12=Toluene, 13=Xylene, 14=AQI, 15=AQI_Bucket
            if (line.length < 15) {
                return null;
            }

            String city = line[0].trim();
            LocalDate date = LocalDate.parse(line[1].trim(), DATE_FORMATTER);
            Double pm25 = parseDouble(line[2]); // PM2.5
            Double pm10 = parseDouble(line[3]); // PM10
            Double no2 = parseDouble(line[5]); // NO2 (index 5)
            Double so2 = parseDouble(line[9]); // SO2 (index 9)
            Double co = parseDouble(line[8]); // CO (index 8)
            Integer aqi = parseInteger(line[14]); // AQI (index 14)

            // Handle missing values - use defaults
            if (pm25 == null)
                pm25 = 0.0;
            if (pm10 == null)
                pm10 = 0.0;
            if (no2 == null)
                no2 = 0.0;
            if (so2 == null)
                so2 = 0.0;
            if (co == null)
                co = 0.0;
            if (aqi == null)
                aqi = 0;

            // Skip records with all zero/null values
            if (pm25 == 0.0 && pm10 == 0.0 && no2 == 0.0 && so2 == 0.0 && co == 0.0) {
                return null;
            }

            return new PollutionReading(city, date, pm25, pm10, no2, so2, co, aqi);

        } catch (Exception e) {
            System.err.println("Exception parsing CSV line: " + e.getMessage());
            return null;
        }
    }

    private Double parseDouble(String value) {
        try {
            String trimmed = value.trim();
            if (trimmed.isEmpty() || trimmed.equalsIgnoreCase("null")) {
                return null;
            }
            return Double.parseDouble(trimmed);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInteger(String value) {
        try {
            String trimmed = value.trim();
            if (trimmed.isEmpty() || trimmed.equalsIgnoreCase("null")) {
                return null;
            }
            return Integer.parseInt(trimmed);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
