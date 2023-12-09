package com.ssuopenpj.spring;

import com.ssuopenpj.spring.API.API;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
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

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject tmp = (JSONObject) jsonArray.get(i);
            avgLat += Double.parseDouble((String)tmp.get("lat"));
            avgLng += Double.parseDouble((String)tmp.get("lng"));
        }

        avgLat /= Integer.parseInt(num);
        avgLng /= Integer.parseInt(num);

        temp = (Double) weather.get("temp");
        temp_min = (Double) weather.get("temp_min");
        temp_max = (Double) weather.get("temp_max");
        feels_like = (Double) weather.get("feels_like");
        pressure = (Long) weather.get("pressure");
        humidity = (Long) weather.get("humidity");
        description = (String) weather.get("description");

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
