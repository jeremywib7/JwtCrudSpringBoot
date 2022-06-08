package com.j23.server.services.time;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Service
public class TimeService {

  DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
  DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  public LocalDateTime getStartDateTimeOfCurrentMonth() {
    return LocalDateTime.parse(YearMonth.now().atDay(1).format(dateFormat) + " 00:00:00", df);
  }

  public LocalDateTime getEndDateTimeOfCurrentMonth() {
    return LocalDateTime.parse(YearMonth.now().atEndOfMonth().format(dateFormat) + " 00:00:00", df);

  }

}
