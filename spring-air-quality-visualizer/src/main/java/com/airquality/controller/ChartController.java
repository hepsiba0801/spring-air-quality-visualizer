package com.airquality.controller;

import com.airquality.model.PollutionReading;
import com.airquality.repository.PollutionReadingRepository;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ChartController {

    private final PollutionReadingRepository repository;

    public ChartController(PollutionReadingRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/chart.png", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateChart(@RequestParam String city,
            @RequestParam String pollutant) throws IOException {

        // Add cache busting parameter to avoid caching issues
        System.out.println("Generating chart for city=" + city + ", pollutant=" + pollutant);

        // Fetch all readings for the given city
        List<PollutionReading> readings = repository.findByCityOrderByTimestampAsc(city);
        System.out.println("Found " + readings.size() + " records for city: " + city);

        // Create a TimeSeries for the selected pollutant
        TimeSeries series = new TimeSeries(pollutant.toUpperCase() + " - " + city);

        for (PollutionReading reading : readings) {
            Double value = getPollutantValue(reading, pollutant);

            if (value != null && value >= 0) {
                LocalDate date = reading.getTimestamp();
                Calendar calendar = new GregorianCalendar(
                        date.getYear(),
                        date.getMonthValue() - 1,
                        date.getDayOfMonth());
                Day day = new Day(calendar.getTime());
                series.addOrUpdate(day, value);
            }
        }

        // Create a TimeSeriesCollection and add the series
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);

        // Create the chart
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                pollutant.toUpperCase() + " Levels in " + city, // title
                "Date", // x-axis label
                pollutant.toUpperCase() + " (µg/m³)", // y-axis label
                dataset, // data
                true, // include legend
                true, // include tooltips
                false // include URLs
        );

        // Configure chart appearance
        chart.setBackgroundPaint(java.awt.Color.WHITE);

        // Write the chart to a ByteArrayOutputStream as PNG
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(baos, chart, 800, 400);

        return baos.toByteArray();
    }

    @GetMapping("/api/data-info")
    public ResponseEntity<Map<String, Object>> getDataInfo(@RequestParam String city,
            @RequestParam String pollutant) {
        
        System.out.println("API call: getDataInfo for city=" + city + ", pollutant=" + pollutant);
        
        List<PollutionReading> readings = repository.findByCityOrderByTimestampAsc(city);
        
        // Calculate statistics
        double sum = 0;
        double min = Double.MAX_VALUE;
        double max = 0;
        int validCount = 0;
        LocalDate minDate = null;
        LocalDate maxDate = null;
        
        for (PollutionReading reading : readings) {
            Double value = getPollutantValue(reading, pollutant);
            if (value != null && value >= 0) {
                sum += value;
                min = Math.min(min, value);
                max = Math.max(max, value);
                validCount++;
                
                if (minDate == null || reading.getTimestamp().isBefore(minDate)) {
                    minDate = reading.getTimestamp();
                }
                if (maxDate == null || reading.getTimestamp().isAfter(maxDate)) {
                    maxDate = reading.getTimestamp();
                }
            }
        }
        
        double average = validCount > 0 ? sum / validCount : 0;
        
        Map<String, Object> result = new HashMap<>();
        result.put("city", city);
        result.put("pollutant", pollutant);
        result.put("recordsFound", validCount);
        result.put("average", Math.round(average * 100.0) / 100.0);
        result.put("minimum", min == Double.MAX_VALUE ? 0 : Math.round(min * 100.0) / 100.0);
        result.put("maximum", Math.round(max * 100.0) / 100.0);
        result.put("startDate", minDate != null ? minDate.toString() : "N/A");
        result.put("endDate", maxDate != null ? maxDate.toString() : "N/A");
        result.put("status", validCount > 0 ? "success" : "no_data");
        
        System.out.println("Data info result: " + result);
        
        return ResponseEntity.ok(result);
    }

    private Double getPollutantValue(PollutionReading reading, String pollutant) {
        return switch (pollutant.toLowerCase()) {
            case "pm25" -> reading.getPm25();
            case "pm10" -> reading.getPm10();
            case "no2" -> reading.getNo2();
            case "so2" -> reading.getSo2();
            case "co" -> reading.getCo();
            default -> null;
        };
    }
}
