package com.weatherwhere.weatherservice.service.date;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@Service
@Log4j2
public class DateServiceImpl implements DateService {
    @Override
    public String[] getDaysAfterToday(int start, int end) {
        String[] daysAfterToday = new String[end - start + 1];

        // 데이터포맷
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // Calendar 클래스 생성
        Calendar calendar = Calendar.getInstance();

        // 3일 뒤부터 7일 뒤까지 계산
        calendar.add(Calendar.DAY_OF_MONTH, +start - 1);
        for (int i = 0; i <= end - start; i++) {
            daysAfterToday[i] = sdf.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, +1);
        }

        return daysAfterToday;
    }

    @Override
    public String getTmfc() {
        String tmfc;
        LocalDateTime now = LocalDateTime.now();
        Integer currentHour = now.getHour();

        if (currentHour >= 6 && currentHour < 18) {
            // 현재 시간이 06시 ~ 18시 사이일 경우
            tmfc = now.format(DateTimeFormatter.ofPattern("yyyyMMdd0600"));
        } else if (currentHour >= 18 && currentHour < 24) {
            // 현재 시간이 18시 ~ 24시 사이일 경우
            tmfc = now.format(DateTimeFormatter.ofPattern("yyyyMMdd1800"));
        } else {
            // 현재 시간이 00시 ~ 06시인 경우 어제 날짜의 18시
            LocalDateTime yesterday = now.minusDays(1);
            tmfc = yesterday.format(DateTimeFormatter.ofPattern("yyyyMMdd1800"));
        }
        return tmfc;
    }
}
