package com.skyapi.weatherforecast.realtime;

import com.skyapi.weatherforecast.common.RealtimeWeather;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RealtimeWeatherRepository extends CrudRepository<RealtimeWeather, String> {

    @Query("SELECT r FROM RealtimeWeather r WHERE r.location.countryCode = ?1 and r.location.cityName = ?2")
    public RealtimeWeather findByCountryCodeAndCity(String countryCode, String city);

    @Query("SELECT r FROM RealtimeWeather r WHERE r.id = ?1 AND r.location.trashed = false")
    public RealtimeWeather findByLocationCode(String locationCode);
}
