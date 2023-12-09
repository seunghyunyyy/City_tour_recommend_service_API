package com.ssuopenpj.spring.API;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpUtility {
    /**
     * http request
     * @param urlString request url
     * @param parameter request parameter, expect query string(GET) or json string(POST, PUT)
     * @param method "GET", "POST", "PUT", ...
     * @return return response as string
     */
    public static JSONObject sendRequest(String urlString, String parameter, JSONObject body, String apiKey, String method){
        JSONParser jsonParser = new JSONParser();

        if (parameter != null)
            System.out.println("http-request : " + "[" + method + "] " + urlString + "?" + parameter);
        else
            System.out.println("http-request : " + "[" + method + "] " + urlString);

        if(method.equals("GET") && parameter != null) urlString += "?" + parameter;
        if(method.equals("POST")) {
            return postRequest(urlString, body, apiKey);
        }

        try{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Accept", "application/json");

            if(!method.equals("GET")){
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(parameter.getBytes(StandardCharsets.UTF_8));
                os.close();
            }

            conn.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) sb.append(line).append("\n");
            System.out.println("http-response" + sb.toString());

            return (JSONObject) jsonParser.parse(sb.toString());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.println("http-response" + "{{ empty string }}");
            return null;
        }
    }

    private static JSONObject postRequest(String targetUrl, JSONObject body, String apiKey) {
        JSONParser jsonParser = new JSONParser();
        JSONObject response = new JSONObject();

        try {
            URL url = new URL(targetUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST"); // 전송 방식
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            if(apiKey != null)  conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setDoOutput(true);	// URL 연결을 출력용으로 사용(true)

            String requestBody = body.toJSONString();
            System.out.println("requestBody:" + requestBody);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(requestBody);
            bw.flush();
            bw.close();

            Charset charset = Charset.forName("UTF-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));

            String inputLine;
            StringBuffer sb = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            br.close();

            response = (JSONObject) jsonParser.parse(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}