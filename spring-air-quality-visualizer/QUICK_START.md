# 🌍 Air Quality Visualizer - Quick Start Guide

## What's New? ✨

Your Air Quality Visualizer application has been completely updated with:

1. **Modern, Professional UI** - Beautiful gradient design with animations
2. **29,533 comprehensive air quality records** - Real-world dataset from India
3. **Improved Data Integration** - Enhanced form controls and statistics display
4. **Responsive Design** - Works perfectly on desktop, tablet, and mobile

---

## 🚀 Getting Started

### Option 1: Run the Pre-built JAR (Recommended)

```powershell
# Navigate to project directory
cd "C:\Users\lebiraja\projects\hepsi\java\spring-air-quality-visualizer"

# Run the JAR file
& "C:\Program Files\Java\jdk-25\bin\java.exe" -jar `
  target/spring-air-quality-visualizer-1.0.0.jar
```

### Option 2: Build and Run from Source

```powershell
# Navigate to project directory
cd "C:\Users\lebiraja\projects\hepsi\java\spring-air-quality-visualizer"

# Build the project
& "C:\Program Files\Apache\Maven\mvn\bin\mvn.cmd" clean package -DskipTests

# Run the application
& "C:\Program Files\Java\jdk-25\bin\java.exe" -jar `
  target/spring-air-quality-visualizer-1.0.0.jar
```

---

## 🌐 Access the Application

Once the application starts, open your web browser and go to:

```
http://localhost:8080
```

You should see a beautiful purple-themed interface with:
- **Header**: "🌍 Air Quality Visualizer"
- **Left Panel**: City and pollutant selection form
- **Right Panel**: Chart display area
- **Info Panels**: Pollutant information and dataset statistics

---

## 📊 How to Use

### Step 1: Select a City
Click on the **"City"** dropdown and choose from available cities in the database.

Example cities available:
- Ahmedabad
- And many more...

### Step 2: Select a Pollutant
Choose one of these air pollutants to visualize:

| Pollutant | Full Name | Source |
|-----------|-----------|--------|
| **PM2.5** | Fine Particulates | Vehicles, industries, biomass |
| **PM10** | Coarse Particulates | Dust, pollen, construction |
| **NO₂** | Nitrogen Dioxide | Vehicle emissions, power plants |
| **SO₂** | Sulfur Dioxide | Fossil fuel combustion |
| **CO** | Carbon Monoxide | Vehicle emissions, heating |

### Step 3: Generate Chart
Click the **"📈 Generate Chart"** button to visualize the pollution trends.

The application will display a time-series chart showing pollution levels over the historical data period (multiple years).

### Step 4: Analyze Data
- **Hover** over the chart to see exact values
- **Use the legend** to toggle pollutants on/off (if multiple)
- **Analyze trends** to understand pollution patterns

### Additional Actions:
- **🔄 Clear**: Reset the form and start over
- **Select New City**: Change city to compare different locations

---

## 📈 Understanding the Charts

### What You'll See:
- **X-Axis**: Date (spanning multiple years of historical data)
- **Y-Axis**: Pollution level (µg/m³ - micrograms per cubic meter)
- **Trend Line**: Shows how pollution levels changed over time
- **Data Points**: Individual daily measurements

### How to Interpret:
- **Upward Trends**: Increasing pollution over time
- **Downward Trends**: Improving air quality
- **Seasonal Patterns**: Peaks during winter, lows during monsoon
- **Spikes**: Sudden pollution events (holidays, festivities, weather events)

---

## 📋 Dataset Information

### Coverage:
- **Time Period**: 2015 to 2025+ (10+ years)
- **Records**: 29,533 measurements
- **Geographic Focus**: Indian cities
- **Update Frequency**: Daily measurements (when available)

### Data Points Per Record:
- City name
- Date (YYYY-MM-DD format)
- PM2.5 levels
- PM10 levels
- NO₂ levels
- SO₂ levels
- CO levels
- AQI (Air Quality Index)
- AQI Bucket classification

---

## 🎨 UI Features

### Design Elements:
✨ **Gradient Background** - Purple-blue gradient for visual appeal
🎯 **Responsive Layout** - Adapts to any screen size
⚡ **Smooth Animations** - Fade-in and slide-down effects
🎮 **Interactive Elements** - Hover effects on buttons and charts
📊 **Pollutant Legend** - Color-coded information panel
📈 **Chart Display** - Professional time-series visualization
🔘 **Form Controls** - Clean dropdown selections

### Color Scheme:
- Primary Purple: #667eea
- Deep Purple: #764ba2
- Accent Colors: Red, Orange, Gold, Green, Blue (for pollutants)

---

## 🔧 System Requirements

- **Java**: Version 17+ (Currently running on Java 25)
- **Memory**: Minimum 512MB
- **Browser**: Any modern browser (Chrome, Firefox, Safari, Edge)
- **Port**: 8080 (must be available)
- **Database**: SQLite (embedded, no setup needed)

---

## 📊 Example Use Cases

### 1. **Environmental Analysis**
```
Select: Ahmedabad + PM2.5
Analyze: Seasonal pollution patterns
Observe: Higher levels in winter months
```

### 2. **Trend Identification**
```
Select: Any City + CO
Analyze: Long-term pollution trends
Observe: Multi-year air quality changes
```

### 3. **Pollutant Comparison**
```
Generate charts for same city with different pollutants
Compare which pollutants are highest
Identify main pollution sources
```

### 4. **Health Planning**
```
Use data to identify high-pollution periods
Plan outdoor activities during low-pollution days
Understand health risks by time of year
```

---

## 🐛 Troubleshooting

### Application Won't Start

**Error**: Port 8080 already in use
```powershell
# Find and kill the process using port 8080
Get-NetTCPConnection -LocalPort 8080 | Stop-Process -Force
```

**Error**: Java not found
```powershell
# Verify Java is installed
java -version

# If not found, install Java 17 or higher
```

### No Cities in Dropdown

- Database might be empty
- Delete `airquality.db` from project root and restart
- Application will re-ingest data from CSV

### Chart Not Displaying

- Ensure both city and pollutant are selected
- Check browser console for errors (F12)
- Verify data exists for selected city
- Try different city/pollutant combination

---

## 📝 Configuration

### To Change Port (Default: 8080)

Edit `src/main/resources/application.properties`:

```properties
server.port=8080
```

Change `8080` to your desired port number, then rebuild:

```powershell
& "C:\Program Files\Apache\Maven\mvn\bin\mvn.cmd" clean package -DskipTests
```

### To Disable Chart Cache

Edit `src/main/resources/application.properties`:

```properties
spring.thymeleaf.cache=false
```

---

## 📚 File Structure

```
spring-air-quality-visualizer/
├── README.md                 # Original documentation
├── UI_IMPROVEMENTS.md        # Detailed UI/UX updates (NEW)
├── DATASET_UPDATE.md         # Dataset integration details
├── PROJECT_SUMMARY.md        # Project overview
├── pom.xml                   # Maven configuration
├── src/main/
│   ├── java/com/airquality/
│   │   ├── controller/       # Web & Chart controllers
│   │   ├── model/            # PollutionReading entity
│   │   ├── repository/       # Database access
│   │   └── service/          # CSV data ingestion
│   └── resources/
│       ├── application.properties
│       ├── city_day.csv      # 29,533 air quality records
│       └── templates/
│           └── index.html    # Modern UI (UPDATED)
└── target/
    └── spring-air-quality-visualizer-1.0.0.jar
```

---

## 🔗 Related Documentation

- **UI Improvements**: See `UI_IMPROVEMENTS.md` for detailed design changes
- **Dataset Info**: See `DATASET_UPDATE.md` for data source and format
- **Project Details**: See `PROJECT_SUMMARY.md` for architecture overview

---

## 📞 Support

### Common Questions

**Q: How often is the data updated?**
A: Current data is from 2015-2025+ with daily granularity when available.

**Q: Can I add my own data?**
A: Yes, update `city_day.csv` with new data and restart the application.

**Q: Is this suitable for production?**
A: Current version is suitable for small to medium deployments. Consider scaling to PostgreSQL for larger datasets.

**Q: Can I export the charts?**
A: Yes, right-click on any chart and select "Save image as" to export as PNG.

---

## 🎯 Next Steps

1. **Explore the Data**: Select different cities and pollutants
2. **Analyze Trends**: Look for seasonal and long-term patterns
3. **Understand Pollutants**: Read the information panel
4. **Plan Extensions**: Consider additional features listed in UI_IMPROVEMENTS.md

---

## ✅ Checklist

- [ ] Application is running on http://localhost:8080
- [ ] Can see city dropdown populated with data
- [ ] Can select city and pollutant
- [ ] Chart generates and displays
- [ ] Chart shows historical trend data
- [ ] UI looks modern and responsive
- [ ] Buttons and interactions work smoothly

---

**Version**: 1.0.0
**Last Updated**: October 27, 2025
**Status**: ✅ Production Ready

Happy analyzing! 🌍📊✨
