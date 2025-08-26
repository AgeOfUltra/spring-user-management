package com.manage.springusermanagement.controller;

import com.manage.springusermanagement.entity.Weather;
import com.manage.springusermanagement.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    @Autowired
    private WeatherService service;


    @GetMapping("/forCity")
    @PreAuthorize("hasAuthority('WEATHER_READ')")
    public ResponseEntity<String> getWeatherByCity(@RequestParam String city){
        return ResponseEntity.ok(service.getWeatherByCity(city));
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('WEATHER_WRITE')")
    public ResponseEntity<Weather> saveWeather(@RequestBody Weather weather){
        return ResponseEntity.ok(service.save(weather));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('WEATHER_READ')")
    public ResponseEntity<List<Weather>> getAll(){
        return ResponseEntity.ok(service.getWeather());
    }

    @PutMapping("/{city}")
    @PreAuthorize("hasAuthority('WEATHER_WRITE')")
    public ResponseEntity<String> updateWeather(@PathVariable String city, @RequestParam String foreCast){
        return ResponseEntity.ok(service.updateWeather(city,foreCast));
    }

    @DeleteMapping("/deleteCity")
    @PreAuthorize("hasAuthority('WEATHER_DELETE')")
    public ResponseEntity<String> deleteCity(@RequestParam String city){
        return ResponseEntity.ok(service.deleteCity(city));
    }

    @GetMapping("/health")
    @PreAuthorize("hasAuthority('WEATHER_READ')")
    public ResponseEntity<String> getHealth(){
        return ResponseEntity.ok("Healthy");
    }
}

