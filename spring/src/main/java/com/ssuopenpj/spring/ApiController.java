package com.ssuopenpj.spring;

import com.ssuopenpj.spring.API.API;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Validated
@RestController
@RequiredArgsConstructor
public class ApiController {
    private final API api;

    @GetMapping("/searchKeyword")
    public JSONArray searchKeyword(@RequestParam String city, @RequestParam String pageNo, @RequestParam String numOfRows) {
        ArrayList<String> params = new ArrayList<>();
        params.add("title");
        params.add("addr1");
        params.add("addr2");
        params.add("mapx");
        params.add("mapy");
        params.add("tel");
        params.add("firstimage");
        params.add("firstimage2");
        return api.parseSearchKeywordJSON(api.getSearchKeyword(city, pageNo, numOfRows), params, city);
        //return api.getSearchKeyword(keyword, pageNo, numOfRows);
    }
    @GetMapping("/weather")
    public JSONObject getWeather(@RequestParam String city) {
        return api.getWeather(city);
    }

    @GetMapping("/recommend")
    public JSONObject getGpt(@RequestParam String city, @RequestParam String num) {
        return api.getRecommend(city, num);
    }
}
