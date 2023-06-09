package com.weatherwhere.weatherservice.service.date;

import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortRequestDTO;
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
    /**
     * (start일 후 - end일 후) 까지의 날짜를 'yyyyMMdd' 형식의 List<String>으로 리턴하는 메서드
     *
     * @param start 시작 날짜
     * @param end 마지막 날짜
     * @return 현재 시간을 기준으로 (start일 후 - end일 후)의 리스트를 담은 daysAfterToday를 리턴
     */
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
    /**
     * openAPI를 호출할 때 매개변수로 전달할 tmfc를 'yyyyMMdd0600' 혹은 'yyyyMMdd1800'으로 리턴하는 메서드
     *
     * @return 현재 시간 이후를 기준으로 가장 가까운 06시 혹은 18시를 찾아 tmfc로 리턴
     */
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

    @Override
    public WeatherShortRequestDTO getBaseDateTime(WeatherShortRequestDTO weatherShortRequestDTO){

        LocalDateTime now = LocalDateTime.now();

        Integer currentHour = now.getHour();
        Integer currentMinute = now.getMinute();

        String baseDate;
        String baseTime;

        if ((currentHour == 2 && currentMinute >= 10) ||
                (currentHour > 2 && currentHour < 5) ||
                (currentHour == 5 && currentMinute < 10)) {

//            System.out.println("2시10분 이상 5시10분 미만");
            baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            baseTime = now.format(DateTimeFormatter.ofPattern("0200"));

        } else if ((currentHour == 5 && currentMinute >= 10) ||
                (currentHour > 5 && currentHour < 8) ||
                (currentHour == 8 && currentMinute < 10)) {
//            System.out.println("5시10분 이상 8시10분 미만");
            baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            baseTime = now.format(DateTimeFormatter.ofPattern("0500"));

        } else if ((currentHour == 8 && currentMinute >= 10) ||
                (currentHour > 8 && currentHour < 10) ||
                (currentHour == 14 && currentMinute < 10)) {
//            System.out.println("8시10분 이상 14시10분 미만");
            baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            baseTime = now.format(DateTimeFormatter.ofPattern("0800"));

        } else if ((currentHour == 14 && currentMinute >= 10) ||
                (currentHour > 14 && currentHour < 17) ||
                (currentHour == 17 && currentMinute < 10)) {
//            System.out.println("14시10분 이상 17시10분 미만");
            baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            baseTime = now.format(DateTimeFormatter.ofPattern("1400"));

        } else if ((currentHour == 17 && currentMinute >= 10) ||
                (currentHour > 17 && currentHour < 24)) {
//            System.out.println("17시10분 이상 24시 미만");
            baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            baseTime = now.format(DateTimeFormatter.ofPattern("1700"));

        } else {
//            System.out.println("24시이상 2시 10분 미만");
            baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            baseTime = now.format(DateTimeFormatter.ofPattern("0200"));

        }

        weatherShortRequestDTO.setBaseDate(baseDate);
        weatherShortRequestDTO.setBaseTime(baseTime);

        return weatherShortRequestDTO;

    }

    //어제 발표날짜 조회(최고기온 비교용)
    @Override
    public WeatherShortRequestDTO getMinusBaseDateTime(WeatherShortRequestDTO weatherShortRequestDTO) {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

        String beforeBaseDate = yesterday.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = yesterday.format(DateTimeFormatter.ofPattern("1100"));
        weatherShortRequestDTO.setBeforeBaseDate(beforeBaseDate);
        weatherShortRequestDTO.setBaseTime(baseTime);
        return weatherShortRequestDTO;
    }




}
