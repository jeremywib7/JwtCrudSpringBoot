package com.j23.server.services.util;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HelperService {
    public long calculateEstimatedTime(int estHour, int estMinute, int estSecond) {
        // calculate estimated time
        int hourToSecond = (estHour * 60) * 60;
        int minuteToSecond = (estMinute * 60);

        return new Date().getTime() + (1000L * (hourToSecond + minuteToSecond + estSecond));
    }
}
