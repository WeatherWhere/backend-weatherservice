package com.weatherwhere.weatherservice.service.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherXY;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainApiRequestDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainDTO;
import com.weatherwhere.weatherservice.repository.weathershort.WeatherShortMainRepository;
import com.weatherwhere.weatherservice.repository.weathershort.WeatherShortSubRepository;
import com.weatherwhere.weatherservice.repository.weathershort.WeatherXYRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class WeatherShortMainApiServiceImpl implements WeatherShortMainApiService {

    private final WeatherXYRepository weatherXYRepository;

    private final WeatherShortMainRepository weatherShortMainRepository;

    private final WeatherShortSubRepository weatherShortSubRepository;


    /**
     * 위경도 좌표로 격자 X Y 좌표 구하기
     */
    public WeatherShortMainApiRequestDTO getGridXY(WeatherShortMainApiRequestDTO requestDTO) {

        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기1준점 Y좌표(GRID)

        try {
            double DEGRAD = Math.PI / 180.0;
            // double RADDEG = 180.0 / Math.PI;
            double re = RE / GRID;
            double slat1 = SLAT1 * DEGRAD;
            double slat2 = SLAT2 * DEGRAD;
            double olon = OLON * DEGRAD;
            double olat = OLAT * DEGRAD;

            double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
            sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
            double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
            sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
            double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
            ro = re * sf / Math.pow(ro, sn);
            double ra = Math.tan(Math.PI * 0.25 + (requestDTO.getLocationX()) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            double theta = requestDTO.getLocationY() * DEGRAD - olon;

            if (theta > Math.PI)
                theta -= 2.0 * Math.PI;

            if (theta < -Math.PI)
                theta += 2.0 * Math.PI;

            theta *= sn;

            requestDTO.setNx((int)Math.floor(ra * Math.sin(theta) + XO + 0.5));
            requestDTO.setNy((int)Math.floor(ro - ra * Math.cos(theta) + YO + 0.5));

            log.info("======= XY : " + requestDTO);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return requestDTO;

    }

    @Override
    //단기예보 메인 데이터 반환하는 서비스
    public List<WeatherShortMainDTO> getWeatherShortMainData(WeatherShortMainApiRequestDTO requestDTO) {
        getGridXY(requestDTO);
        List<WeatherShortMainDTO> mainDataList = new ArrayList<>();
        try {
            WeatherXY weatherXY = weatherXYRepository.findByWeatherXAndWeatherY(requestDTO.getNx(), requestDTO.getNy());
            for (int i = 0; i < 12; i++) {
                //여기 실시간 시간(분 없애기) 으로 수정하기
                LocalDateTime localDateTime = LocalDateTime.of(2023, 3, 29, 6, 0).plusHours(i);
                System.out.println(localDateTime);
                requestDTO.setFcstDateTime(localDateTime);
                WeatherShortMain weatherShortMain = weatherShortMainRepository.findByFcstDateTimeAndWeatherXY(requestDTO.getFcstDateTime(), weatherXY);
                mainDataList.add(entityToDTO(weatherShortMain));
            }
            return mainDataList;
        }catch (NullPointerException e){
            throw new NullPointerException();
        }
    }

}
