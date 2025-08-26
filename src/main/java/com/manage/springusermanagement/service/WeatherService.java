package com.manage.springusermanagement.service;

import com.manage.springusermanagement.entity.Weather;
import com.manage.springusermanagement.repo.WeatherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WeatherService {

    private final WeatherRepo repo;
    @Autowired
    WeatherService(WeatherRepo repo) {
        this.repo = repo;
    }

    public String getWeatherByCity(String city){
        System.out.println("fetching data from DB for city : "+city);
        Optional<Weather> weather = repo.getWeatherByCity(city);
        return weather.map(Weather::getForecast).orElse("no force cast available");
    }

    public Weather save(Weather weather) {
       return repo.save(weather);
    }

    public List<Weather> getWeather() {
        return repo.findAll();
    }

    public String updateWeather(String city, String foreCast) {
         Optional<Weather> current = repo.getWeatherByCity(city);
         if(current.isPresent()){
             Weather weather= current.get();
             weather.setForecast(foreCast);
             repo.save(weather);
             return weather.getForecast()+"saved successfully";
         }

        return "no city found";
    }

    @Transactional
    public String deleteCity(String city) {
        Optional<Weather> weather = repo.getWeatherByCity(city);
        if(weather.isPresent()){
            repo.delete(weather.get());
            return "success";
        }else{
            return "fail";
        }
    }
}
