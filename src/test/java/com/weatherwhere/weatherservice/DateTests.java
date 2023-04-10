//package com.weatherwhere.weatherservice;
//
//import com.weatherwhere.weatherservice.service.date.DateService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Arrays;
//
//@SpringBootTest
//public class DateTests {
//    @Autowired
//    private DateService dateService;
//
//    @Test
//    @DisplayName("오늘 날짜를 기준으로 '첫 번쨰 매개변수'일 후 - '두 번째 매개변수'일 후 까지의 날짜를 리스트로 리턴하는 테스트")
//    void testGetDaysAfterToday() {
//        System.out.println(Arrays.toString(dateService.getDaysAfterToday(3, 7)));
//    }
//
//    @Test
//    @DisplayName("openAPI에 변수로 넘겨줄 적절한 tmfc를 반환하는 메서드")
//    void testGetTmfc() {
//        System.out.println(dateService.getTmfc());
//    }
//}
