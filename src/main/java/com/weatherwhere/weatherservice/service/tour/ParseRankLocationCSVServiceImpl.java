package com.weatherwhere.weatherservice.service.tour;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.weatherwhere.weatherservice.dto.tour.RankLocationNXYDTO;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ParseRankLocationCSVServiceImpl implements ParseRankLocationCSVService {
    @Override
    public List<RankLocationNXYDTO> ParseRankLovationCSV() {
        ClassPathResource resource = new ClassPathResource("rankLocationNXY.csv");
        List<RankLocationNXYDTO> locations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader (new InputStreamReader(resource.getInputStream()))) {

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

                RankLocationNXYDTO dto = RankLocationNXYDTO.builder()
                    .level1(ar[0])
                    .level2(ar[1])
                    .weatherX(Integer.parseInt(ar[2]))
                    .weatherY(Integer.parseInt(ar[3]))
                    .build();

                locations.add(dto);
            }


        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return locations;
    }
}
