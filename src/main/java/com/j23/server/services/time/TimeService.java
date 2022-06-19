package com.j23.server.services.time;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Service
public class TimeService {

  DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
  DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  public LocalDateTime getStartOfTheDay() {
    return LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
  }

  public LocalDateTime getEndOfTheDay() {
    return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
  }

  public LocalDateTime getPrevious3Hours() {
    return LocalDateTime.now().minusHours(3);
//    return LocalTime.of(LocalTime.now().minusHours(3).getHour());
  }

  public LocalDateTime getCurrentHour() {
    return LocalDateTime.of(LocalDate.now(), LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute()));
  }

  public LocalDateTime getStartDateTimeOfCurrentMonth() {
    return LocalDateTime.of(YearMonth.now().atDay(1), LocalTime.MIDNIGHT);
//    LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
//    return LocalDateTime.parse(YearMonth.now().atDay(1).format(dateFormat) + " 00:00:00", df);
  }

  public LocalDateTime getEndDateTimeOfCurrentMonth() {
    return LocalDateTime.of(YearMonth.now().atEndOfMonth(), LocalTime.MAX);
//    return LocalDateTime.parse(YearMonth.now().atEndOfMonth().format(dateFormat) + " 00:00:00", df);

  }

}
