package com.skyapi.weatherforecast.hourly;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HourlyWeatherService {
    private HourlyWeatherRepository hourlyWeatherRepo;
    private LocationRepository locationRepo;

    public HourlyWeatherService(HourlyWeatherRepository hourlyWeatherRepo, LocationRepository locationRepo) {
        this.hourlyWeatherRepo = hourlyWeatherRepo;
        this.locationRepo = locationRepo;
    }

    public List<HourlyWeather> getByLocation(Location location, int currentHour) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        Location locationInDb = locationRepo.findByCountryCodeAndCityName(countryCode, cityName);

        if(locationInDb == null){
            throw new LocationNotFoundException(countryCode, cityName);
        }

        return hourlyWeatherRepo.findByLocationCode(locationInDb.getCode(), currentHour);
    }

    public List<HourlyWeather> getByLocationCode(String locationCode, int currentHour) {

        Location locationInDB = locationRepo.findByCode(locationCode);

        if (locationInDB == null) {
            throw new LocationNotFoundException(locationCode);
        }

        return hourlyWeatherRepo.findByLocationCode(locationCode, currentHour);
    }

    public List<HourlyWeather> updateByLocationCode(String locationCode, List<HourlyWeather> hourlyWeatherInRequest) {
        Location location = locationRepo.findByCode(locationCode);

        if(location == null){
            throw new LocationNotFoundException(locationCode);
        }

        for (HourlyWeather item : hourlyWeatherInRequest){
            item.getId().setLocation(location);
        }

        List<HourlyWeather> hourlyWeatherInDB = location.getListHourlyWeather();
        List<HourlyWeather> hourlyWeatherToBeRemoved = new ArrayList<>();

        for (HourlyWeather item : hourlyWeatherInDB){
            if(!hourlyWeatherInRequest.contains(item)){
                hourlyWeatherToBeRemoved.add(item.getShallowCopy());
            }
        }

        for (HourlyWeather item: hourlyWeatherToBeRemoved){
            hourlyWeatherInDB.remove(item);
        }

        return (List<HourlyWeather>) hourlyWeatherRepo.saveAll(hourlyWeatherInRequest);
    }
}
