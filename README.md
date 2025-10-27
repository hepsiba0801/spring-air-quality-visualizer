# Spring Air Quality Visualizer

A complete Spring Boot web application that ingests CSV air quality data, stores it in an SQLite database, and displays it dynamically on a web page with interactive charts.

## Features

✨ **Key Features:**
- 📊 Dynamic chart generation using JFreeChart
- 🗄️ SQLite database with Spring Data JPA
- 📁 CSV data ingestion on application startup
- 🎨 Beautiful, responsive web UI with Thymeleaf
- 🌍 Multi-city air quality monitoring
- 💨 Multiple pollutant tracking (PM2.5, PM10, NO₂, SO₂, CO)

## Technology Stack

- **Framework:** Spring Boot 3.2.0
- **Build Tool:** Maven
- **Database:** SQLite with Hibernate Community Dialects
- **ORM:** Spring Data JPA
- **Template Engine:** Thymeleaf
- **Chart Library:** JFreeChart
- **CSV Processing:** OpenCSV
- **Java Version:** Java 17

## Project Structure

```
spring-air-quality-visualizer/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/airquality/
    │   │   ├── SpringAirQualityVisualizerApplication.java
    │   │   ├── controller/
    │   │   │   ├── WebUiController.java
    │   │   │   └── ChartController.java
    │   │   ├── model/
    │   │   │   └── PollutionReading.java
    │   │   ├── repository/
    │   │   │   └── PollutionReadingRepository.java
    │   │   └── service/
    │   │       └── DataIngestionRunner.java
    │   └── resources/
    │       ├── application.properties
    │       ├── city_day.csv
    │       └── templates/
    │           └── index.html
    └── test/
        └── java/...
```

## Component Descriptions

### 1. **PollutionReading.java** (Entity)
- JPA entity representing air quality data
- Fields: id, city, timestamp, pm25, pm10, no2, so2, co, aqi
- Unique constraint on city + timestamp to prevent duplicates

### 2. **PollutionReadingRepository.java** (Repository)
- Spring Data JPA repository for database operations
- Custom query to fetch distinct cities
- Query methods to retrieve readings by city and date range

### 3. **DataIngestionRunner.java** (Service)
- CommandLineRunner that executes on application startup
- Reads data from `city_day.csv` using OpenCSV
- Parses CSV and saves records to database in batches
- Prevents duplicate ingestion on subsequent runs

### 4. **WebUiController.java** (Controller)
- Serves the main web page at `/`
- Fetches list of cities for dropdown selection
- Generates chart URL based on user selections
- Renders Thymeleaf template

### 5. **ChartController.java** (REST Controller)
- Generates PNG charts at `/chart.png`
- Accepts city and pollutant as query parameters
- Uses JFreeChart to create time series charts
- Streams image as byte array with PNG media type

### 6. **index.html** (Frontend)
- Beautiful, responsive web UI
- City and pollutant selection dropdowns
- Dynamic chart display
- Information messages for user guidance

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher
- Git (optional)

### Installation

1. **Clone or extract the project:**
   ```bash
   cd spring-air-quality-visualizer
   ```

2. **Prepare the CSV data file:**
   - Place your `city_day.csv` file in `src/main/resources/`
   - The CSV should have the following format (with headers):
     ```
     City,Date,PM2.5,PM10,NO2,SO2,CO,AQI
     Delhi,2023-01-01,150.5,200.3,45.2,25.1,1.2,250
     Mumbai,2023-01-01,95.2,120.5,35.1,15.3,0.8,180
     ...
     ```

3. **Build the project:**
   ```bash
   mvn clean package
   ```

4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

   Or after building:
   ```bash
   java -jar target/spring-air-quality-visualizer-1.0.0.jar
   ```

### Using the Application

1. **Access the web interface:**
   - Open your browser and navigate to: `http://localhost:8080`

2. **View air quality data:**
   - Select a city from the "City" dropdown
   - Select a pollutant (PM2.5, PM10, NO₂, SO₂, or CO)
   - Click "Generate Chart" to visualize the data
   - A time series chart will be displayed showing the pollution levels over time

3. **Clear selection:**
   - Click the "Clear Selection" button to reset the form

## Database

- **Database File:** `airquality.db` (created in the project root)
- **Tables:** `pollution_readings`
- **Auto-created:** On first run using Hibernate DDL

The database is automatically created and populated on the first application startup by the `DataIngestionRunner` component.

## CSV Data Format

The `city_day.csv` file should contain:
- **Headers:** City, Date, PM2.5, PM10, NO2, SO2, CO, AQI
- **Date Format:** YYYY-MM-DD
- **Numeric Values:** Can contain decimal numbers
- **NULL Values:** Can use "null" or empty strings for missing data

Example:
```csv
City,Date,PM2.5,PM10,NO2,SO2,CO,AQI
Delhi,2023-01-01,150.5,200.3,45.2,25.1,1.2,250
Mumbai,2023-01-01,95.2,120.5,35.1,15.3,0.8,180
Bangalore,2023-01-01,45.1,60.2,20.5,10.3,0.5,85
```

## Configuration

Edit `src/main/resources/application.properties` to customize:

```properties
# Database
spring.datasource.url=jdbc:sqlite:airquality.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update

# Server
server.port=8080

# Thymeleaf
spring.thymeleaf.cache=false
```

## Chart Features

The generated charts include:
- **Time Series Visualization:** Shows pollutant levels over time
- **Professional Styling:** White background with clear labeling
- **Interactive Tooltips:** Hover over data points for values
- **Responsive Size:** 800x400 pixels, adaptable to various screens
- **Pollution Units:** µg/m³ for all pollutants

## API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/` | GET | Displays the main web page |
| `/chart.png` | GET | Generates and returns a PNG chart |

### Query Parameters for `/chart.png`:
- `city` (required): The name of the city
- `pollutant` (required): One of pm25, pm10, no2, so2, co

Example:
```
http://localhost:8080/chart.png?city=Delhi&pollutant=pm25
```

## Troubleshooting

### Issue: "city_day.csv not found"
**Solution:** Ensure the CSV file is placed in `src/main/resources/` before building or running the application.

### Issue: Database is locked
**Solution:** Close any other instances of the application and delete `airquality.db` to start fresh.

### Issue: No data appears in dropdowns
**Solution:** Check that:
1. The CSV file is properly formatted
2. The CSV file is in the correct location
3. No errors appear in the console during startup
4. The database file exists and contains data

### Issue: Chart not generating
**Solution:** Verify that:
1. Both city and pollutant are selected
2. The city name matches exactly (case-sensitive)
3. The pollutant name is valid (pm25, pm10, no2, so2, co)

## Performance Considerations

- **Batch Processing:** CSV data is ingested in batches of 100 records for efficiency
- **Lazy Loading:** Thymeleaf uses efficient rendering
- **Chart Generation:** JFreeChart generates charts on-demand
- **Database:** SQLite is suitable for this data scale; consider migration to PostgreSQL for larger datasets

## Future Enhancements

Possible improvements:
- Add date range filtering for charts
- Support for multiple pollutants on a single chart
- Export data to CSV/Excel
- Real-time data updates with WebSockets
- User authentication and per-city alerts
- Data aggregation (daily, weekly, monthly averages)
- Mobile app integration

## License

This project is provided as-is for educational and demonstration purposes.

## Support

For issues or questions:
1. Check the troubleshooting section above
2. Review the console logs for error messages
3. Ensure all dependencies are correctly installed
4. Verify the Spring Boot version (3.2.0 or compatible)

---

**Version:** 1.0.0  
**Last Updated:** 2025  
**Spring Boot:** 3.2.0  
**Java:** 17+
