package com.ssuopenpj.spring.API;

import com.ssuopenpj.spring.ChatController;
import lombok.SneakyThrows;
import net.crizin.KoreanRomanizer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Service
public class API {
    private final ChatController chatController;

    public API(ChatController chatController) {
        this.chatController = chatController;
    }

    public JSONObject getRecommend(String city, String num) {
        String[] spilt;
        String gptResult, place = "";
        JSONArray searchResult, jsonArray;
        JSONObject weatherResult, jsonObject;
        ArrayList<String> params = new ArrayList<>();

        jsonArray = new JSONArray();
        jsonObject = new JSONObject();

        params.add("title");
        params.add("addr1");
        params.add("addr2");
        params.add("mapx");
        params.add("mapy");
        params.add("tel");
        params.add("firstimage");
        params.add("firstimage2");

        //System.out.println(KoreanRomanizer.romanize(city));
        weatherResult = parsWeatherJSON(getWeather(KoreanRomanizer.romanize(city)));
        searchResult = parseSearchKeywordJSON(getSearchKeyword(city, "1", "50"), params, city);
        //System.out.println(searchResult);

        for(int i = 0; i < searchResult.size(); i++) {
            JSONObject tmp = (JSONObject) searchResult.get(i);
            if(i == 0)
                place = (String) tmp.get("title");
            else
                place = place + ", " + tmp.get("title");
        }

        String prompt = place + " 중에서 " + num + "개를 뽑아서 알려줘. 너가 알려준 답변에는 내가 알려준 이름만 들어가면 되고 개수도 똑같아야돼.";

        gptResult = chatController.chat(prompt);
        //System.out.println(gptResult);

        spilt = gptResult.split(", ");

        for(int i = 0; i < spilt.length; i++) {
            for(int j = 0; j < searchResult.size(); j++) {
                JSONObject tmp = (JSONObject) searchResult.get(j);
                //System.out.println(tmp.get("title"));
                if(tmp.get("title").equals(spilt[i])) {
                    jsonArray.add(tmp);
                    break;
                }
            }
        }

        jsonObject.put("city", city);
        jsonObject.put("weather", weatherResult);
        jsonObject.put("recommend", jsonArray);

        return jsonObject;
    }
    @SneakyThrows
    public JSONObject getWeather(String InputCity) {
        String key = "e0a857fc1e3a20645c6bd4390793cf3e";
        String lang = "kr";
        String URL = "http://api.openweathermap.org/data/2.5/weather";
        String city = URLEncoder.encode(InputCity, StandardCharsets.UTF_8);
        String parameter = "q=" + city + "&" + "appid=" + key + "&" + "lang=" + lang + "&" + "units=metric";
        return HttpUtility.sendRequest(URL, parameter, null, null,"GET");
    }
    @SneakyThrows
    public JSONObject getSearchKeyword(String InputKeyword, String pageNo, String numOfRows) {
        String URL = "https://apis.data.go.kr/B551011/KorService1/searchKeyword1";
        String serviceKey = "F%2BDKDWNAn84d%2B5tTmzkjWKU%2BOlfeZ5Gv87uHqcNcs1iXhasBKKZBhEv%2BFxif%2Bb7aG6ZPvY2ocqdyN9287H7glg%3D%3D";
        String MobileOS = "ETC";
        String MobileApp = "AppTest";
        String _type = "json";
        String listYN = "Y";
        String arrange = "A";
        String contentTypeId = "12";
        String keyword = URLEncoder.encode(InputKeyword, StandardCharsets.UTF_8);

        String parameter = "serviceKey="+serviceKey+"&"+"numOfRows="+numOfRows+"&"+"pageNo="+pageNo+"&"+"MobileOS="+MobileOS+"&"+"MobileApp="+MobileApp+"&"+"_type="+_type+"&"+"listYN="+listYN+"&"+"arrange="+arrange+"&"+"keyword="+keyword+"&"+"contentTypeId="+contentTypeId;
        return HttpUtility.sendRequest(URL, parameter, null, null,"GET");
    }
    public JSONArray parseSearchKeywordJSON(JSONObject jsonObject, ArrayList<String> params, String city) {
        JSONArray result = new JSONArray();

        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray item = (JSONArray) items.get("item");

        for (int i = 0; i < item.size(); i++) {
            JSONObject tmpJsonObject = (JSONObject) item.get(i);
            JSONObject itemJson = new JSONObject();
            if (!tmpJsonObject.get("addr1").toString().contains(city)) {
                //System.out.println(tmpJsonObject.get("addr1").toString());
                continue;
            }
            for (String param : params) {
                itemJson.put(param, tmpJsonObject.get(param));
            }
            result.add(itemJson);
        }
        return result;
    }
    public JSONObject parsWeatherJSON(JSONObject jsonObject) {
        JSONArray weather = (JSONArray) jsonObject.get("weather");

        JSONObject result = (JSONObject) jsonObject.get("main");
        JSONObject tmp = (JSONObject) weather.get(0);

        String description = (String) tmp.get("description");

        result.put("description", description);

        return result;
    }
}
