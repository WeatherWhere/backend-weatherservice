package com.weatherwhere.weatherservice.service.weathershort;

import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;
import com.weatherwhere.weatherservice.dto.ResultDTO;
import com.weatherwhere.weatherservice.dto.weathershort.*;

import java.util.List;

public interface WeatherShortMainApiService {

    default WeatherShortAllDTO nowEntityToDTO(WeatherShortMain entity, Double tmn, Double tmx, Double beforeTmx) {
        WeatherShortAllDTO dto = WeatherShortAllDTO.builder()
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

    default WeatherShortAllDTO entityToDTO(WeatherShortMain entity) {
        WeatherShortAllDTO dto = WeatherShortAllDTO.builder()
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

    default WeatherShortAllDTO subEntityToDTO(WeatherShortSub entity) {
        WeatherShortAllDTO dto = WeatherShortAllDTO.builder()
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
    ResultDTO<List<WeatherShortAllDTO>> getWeatherShortMainData(WeatherShortRequestDTO requestDTO) throws Exception;

    /**
     * (단기예보 실시간) 변환된 격자 x,y 값으로 현재 날씨 정보를 찾은 뒤 mainData에 담아 리턴
     * @param requestDTO 에서 set된 격자 x,y 값 받음
     * @return ResultDTO<WeatherShortMainDTO>에 mainData를 담아 리턴, 실패시 예외처리
     */
    ResultDTO<WeatherShortAllDTO> getWeatherShortMainNowData(WeatherShortRequestDTO requestDTO);

    /**
     * (단기예보 서브 12시간) 변환된 격자 x,y 값으로 현재 시간부터 12시간 후까지의 서브 날씨 정보를 찾은 뒤 subDataList에 담아 리턴
     * @param requestDTO 에서 set된 격자 x,y 값 받음
     * @return ResultDTO<List<WeatherShortSubDTO>>에 subDataList를 담아 리턴, 실패시 예외처리
     */
    ResultDTO<List<WeatherShortAllDTO>> getWeatherShortSubData(WeatherShortRequestDTO requestDTO) throws Exception;
}
