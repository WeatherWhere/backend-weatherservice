package com.weatherwhere.weatherservice.service.weathermid;

import com.weatherwhere.weatherservice.dto.weathermid.RegionCodeDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class ParseCSVServiceImpl implements ParseCSVService {
    @Override
    /**
     * csv 파일로부터 데이터를 읽어와 List<RegionCodeDTO>로 리턴하는 메서드
     *
     * @return List<RegionCodeDTO>
     */
    public List<RegionCodeDTO> ParseCSV() {
        ClassPathResource resource = new ClassPathResource("weatherMidRegionCodeEx.csv");
        List<RegionCodeDTO> regionCodes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

            boolean flag = false;

            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }

                // 첫 줄을 읽지 않음
                if (flag == false) {
                    flag = true;
                    continue;
                }

                String[] ar = line.split(",");

                RegionCodeDTO regionCodeDTO = RegionCodeDTO.builder()
                        .regionName(ar[0])
                        .regionCode(ar[1])
                        .city(ar[2]).build();

                regionCodes.add(regionCodeDTO);
            }


        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return regionCodes;
    }
}
