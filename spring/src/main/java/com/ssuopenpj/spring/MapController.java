package com.ssuopenpj.spring;

import com.ssuopenpj.spring.API.API;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/api/v1")
public class MapController {
    private final API api;

    public MapController(API api) {
        this.api = api;
    }

    @GetMapping("/map")
    public String map(Model model, @RequestParam String city, @RequestParam String num) {
        Double avgLat = 0.0, avgLng = 0.0;
        JSONObject result = api.getRecommend(city, num);
        JSONObject weather = (JSONObject) result.get("weather");
        JSONArray recommend = (JSONArray) result.get("recommend");
        JSONArray jsonArray = new JSONArray();

        //System.out.println("map weather : " + weather.toJSONString());

        String title, lat, lng;
        Double temp, temp_min, feels_like, temp_max;
        Long humidity, pressure;
        String description;

        for (int i = 0; i < recommend.size(); i++) {
            JSONObject tmp1 = new JSONObject();
            JSONObject tmp2 = (JSONObject) recommend.get(i);

            title = (String) tmp2.get("title");
            lat = (String) tmp2.get("mapy");
            lng = (String) tmp2.get("mapx");

            tmp1.put("title", title);
            tmp1.put("lat", lat);
            tmp1.put("lng", lng);

            jsonArray.add(tmp1);
        }

        int level;
        double minLat = Double.MAX_VALUE;
        double maxLat = Double.MIN_VALUE;
        double minLng = Double.MAX_VALUE;
        double maxLng = Double.MIN_VALUE;

        // 각 요소에 대해 반복
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject tmp = (JSONObject) jsonArray.get(i);

            Double tmpLat = Double.parseDouble((String)tmp.get("lat"));
            Double tmpLng = Double.parseDouble((String)tmp.get("lng"));

            // 최대, 최소 값 업데이트
            if (tmpLat < minLat) minLat = tmpLat;
            if (tmpLat > maxLat) maxLat = tmpLat;
            if (tmpLng < minLng) minLng = tmpLng;
            if (tmpLng > maxLng) maxLng = tmpLng;
        }
        avgLat = (minLat + maxLat) / 2;
        avgLng = (minLng + maxLng) / 2;

        /*System.out.println("avgLat : " + avgLat);
        System.out.println("avgLng : " + avgLng);
        System.out.println("minLat : " + minLat);
        System.out.println("maxLat : " + maxLat);
        System.out.println("minLng : " + minLng);
        System.out.println("maxLng : " + minLng);*/

        Double tmpLat = maxLat - minLat;

        if (tmpLat < 0.01875) {
            level = 5;
        } else if (0.01875 <= tmpLat && tmpLat < 0.0375) {
            level = 6;
        } else if (0.0375 <= tmpLat && tmpLat < 0.075) {
            level = 7;
        } else if (0.075 <= tmpLat && tmpLat < 0.15) {
            level = 8;
        } else if (0.15 <= tmpLat && tmpLat < 0.3) {
            level = 9;
        } else if (0.3 <= tmpLat && tmpLat < 0.6) {
            level = 10;
        } else if (0.6 <= tmpLat && tmpLat < 1.2){
            level = 11;
        } else
            level = 12;

        temp = (Double) weather.get("temp");
        temp_min = (Double) weather.get("temp_min");
        temp_max = (Double) weather.get("temp_max");
        feels_like = (Double) weather.get("feels_like");
        pressure = (Long) weather.get("pressure");
        humidity = (Long) weather.get("humidity");
        description = (String) weather.get("description");

        model.addAttribute("level", level);
        model.addAttribute("temp", temp);
        model.addAttribute("temp_min", temp_min);
        model.addAttribute("temp_max", temp_max);
        model.addAttribute("pressure", pressure);
        model.addAttribute("humidity", humidity);
        model.addAttribute("feels_like", feels_like);
        model.addAttribute("description", description);

        model.addAttribute("city", city);

        model.addAttribute("avgLat", avgLat);
        model.addAttribute("avgLng", avgLng);

        model.addAttribute("list", jsonArray.toString());

        return "map";
    }
}
