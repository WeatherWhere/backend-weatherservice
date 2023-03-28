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
    public List<RegionCodeDTO> ParseCSV() {
        ClassPathResource resource = new ClassPathResource("weatherMidRegionCode.csv");
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

                RegionCodeDTO regionCodeDTO = new RegionCodeDTO();
                regionCodeDTO.setRegionName(ar[0]);
                regionCodeDTO.setRegionCode(ar[1]);
                regionCodes.add(regionCodeDTO);
            }


        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return regionCodes;
    }
}
