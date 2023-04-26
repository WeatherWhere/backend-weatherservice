package com.weatherwhere.weatherservice.service.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;
import com.weatherwhere.weatherservice.dto.ResultDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainApiRequestDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortMainDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortSubDTO;

import java.util.List;

public interface WeatherShortMainApiService {

    default WeatherShortMainDTO nowEntityToDTO(WeatherShortMain entity, Double tmn, Double tmx, Double beforeTmx) {
        WeatherShortMainDTO dto = WeatherShortMainDTO.builder()
                .xyId(entity.getId().getWeatherXY().getId())
                .fcstDateTime(entity.getId().getFcstDateTime())
                .pop(entity.getPop())
                .pty(entity.getPty())
                .reh(entity.getReh())
                .sky(entity.getSky())
                .tmp(entity.getTmp())
                .wsd(entity.getWsd())
                .tmn(tmn)
                .tmx(tmx)
                .beforeTmx(beforeTmx)
                .build();
        return dto;
    }

    default WeatherShortMainDTO entityToDTO(WeatherShortMain entity) {
        WeatherShortMainDTO dto = WeatherShortMainDTO.builder()
                .xyId(entity.getId().getWeatherXY().getId())
                .fcstDateTime(entity.getId().getFcstDateTime())
                .pop(entity.getPop())
                .pty(entity.getPty())
                .reh(entity.getReh())
                .sky(entity.getSky())
                .tmp(entity.getTmp())
                .wsd(entity.getWsd())
                .build();
        return dto;
    }

    default WeatherShortSubDTO subEntityToDTO(WeatherShortSub entity) {
        WeatherShortSubDTO dto = WeatherShortSubDTO.builder()
                .xyId(entity.getId().getWeatherXY().getId())
                .fcstDateTime(entity.getId().getFcstDateTime())
                .pcp(entity.getPcp())
                .sno(entity.getSno())
                .uuu(entity.getUuu())
                .vvv(entity.getVvv())
                .wav(entity.getWav())
                .vec(entity.getVec())
                .build();
        return dto;
    }


    /**
     *  (단기예보 메인 12시간) 변환된 격자 x,y 값으로 현재 시간부터 12시간 후까지의 메인 날씨 정보를 찾은 뒤 mainDataList에 담아 리턴
     *
     * @param requestDTO 에서 set된 격자 x,y 값 받음
     * @return ResultDTO<List<WeatherShortMainDTO>>에 mainDataList를 담아 리턴, 실패시 예외처리
     */
    ResultDTO<List<WeatherShortMainDTO>> getWeatherShortMainData(WeatherShortMainApiRequestDTO requestDTO);

    /**
     * (단기예보 실시간) 변환된 격자 x,y 값으로 현재 날씨 정보를 찾은 뒤 mainData에 담아 리턴
     * @param requestDTO 에서 set된 격자 x,y 값 받음
     * @return ResultDTO<WeatherShortMainDTO>에 mainData를 담아 리턴, 실패시 예외처리
     */
    ResultDTO<WeatherShortMainDTO> getWeatherShortMainNowData(WeatherShortMainApiRequestDTO requestDTO);

    /**
     * (단기예보 서브 12시간) 변환된 격자 x,y 값으로 현재 시간부터 12시간 후까지의 서브 날씨 정보를 찾은 뒤 subDataList에 담아 리턴
     * @param requestDTO 에서 set된 격자 x,y 값 받음
     * @return ResultDTO<List<WeatherShortSubDTO>>에 subDataList를 담아 리턴, 실패시 예외처리
     */
    ResultDTO<List<WeatherShortSubDTO>> getWeatherShortSubData(WeatherShortMainApiRequestDTO requestDTO);
}
