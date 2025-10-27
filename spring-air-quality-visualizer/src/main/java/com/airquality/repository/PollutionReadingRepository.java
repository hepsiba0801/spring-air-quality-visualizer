package com.airquality.repository;

import com.airquality.model.PollutionReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PollutionReadingRepository extends JpaRepository<PollutionReading, Long> {

    @Query("SELECT DISTINCT p.city FROM PollutionReading p ORDER BY p.city")
    List<String> findDistinctCities();

    List<PollutionReading> findByCityOrderByTimestampAsc(String city);

    List<PollutionReading> findByCityAndTimestampBetweenOrderByTimestampAsc(String city, LocalDate start,
            LocalDate end);
}
