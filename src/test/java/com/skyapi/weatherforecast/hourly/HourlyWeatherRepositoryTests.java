package com.skyapi.weatherforecast.hourly;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.HourlyWeatherId;
import com.skyapi.weatherforecast.common.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class HourlyWeatherRepositoryTests {
    @Autowired private HourlyWeatherRepository repo;

    @Test
    public void testAdd(){
        String locationCode = "LACA_USA";
        int hourOfDay = 12;

        Location location = new Location().code(locationCode);

        HourlyWeather forecast = new HourlyWeather().id(location, 10)
                .hourOfDay(hourOfDay)
                .temperature(13)
                .precipitation(17)
                .status("Cloudy");
        HourlyWeather updatedForecast = repo.save(forecast);

        assertThat(updatedForecast.getId().getLocation().getCode()).isEqualTo(locationCode);
        assertThat(updatedForecast.getId().getHourOfDay()).isEqualTo(hourOfDay);
    }

    @Test
    public void testDelete(){
        Location location = new Location().code("LACA_USA");
        HourlyWeatherId id = new HourlyWeatherId(10, location);
        repo.deleteById(id);
        Optional<HourlyWeather> result = repo.findById(id);
        assertThat(result).isNotPresent();

    }

    @Test
    public void testFindByLocationCodeFound(){
        String locationCode = "LACA_USA";
        int currentHour = 10;
        List<HourlyWeather> hourlyForecast = repo.findByLocationCode(locationCode, currentHour);
        assertThat(hourlyForecast).isNotEmpty();
    }

    @Test
    public void testFindByLocationCodeNotFound(){
        String locationCode = "NYC_USA";
        int currentHour = 6;
        List<HourlyWeather> hourlyForecast = repo.findByLocationCode(locationCode, currentHour);
        assertThat(hourlyForecast).isEmpty();
    }
}
