package com.weatherwhere.weatherservice.service.date;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
        for (int i = 0; i <= end - start; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, +1);
            daysAfterToday[i] = sdf.format(calendar.getTime());
        }

        return daysAfterToday;
    }
}
