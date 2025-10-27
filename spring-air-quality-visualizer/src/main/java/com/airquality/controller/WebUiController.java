package com.airquality.controller;

import com.airquality.repository.PollutionReadingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebUiController {

    private final PollutionReadingRepository repository;

    public WebUiController(PollutionReadingRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) String city,
            @RequestParam(required = false) String pollutant,
            Model model) {

        // Fetch all distinct cities
        List<String> cities = repository.findDistinctCities();
        model.addAttribute("cities", cities);

        // Add statistics
        model.addAttribute("totalRecords", repository.count());
        model.addAttribute("citiesCount", cities.size());

        // If both city and pollutant are provided, generate chart URL
        if (city != null && !city.isEmpty() && pollutant != null && !pollutant.isEmpty()) {
            String chartUrl = "/chart.png?city=" + city + "&pollutant=" + pollutant;
            model.addAttribute("chartUrl", chartUrl);
            model.addAttribute("city", city);
            model.addAttribute("pollutant", pollutant);
        }

        return "index";
    }
}
