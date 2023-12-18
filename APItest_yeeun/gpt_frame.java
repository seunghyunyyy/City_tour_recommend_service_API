import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class gpt {
    public static void main(String[] args) {
        System.out.println(chatGPT("where should I visit?"));
    }

    public static String chatGPT(String message) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "------------"; // API key
        String model = "gpt-3.5-turbo"; //chatgpt api 현재 모델

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection(); //웹페이지 URL 연결
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json"); //Request body 전달시 application/json으로 서버에 전달

            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"에서 방문할 만한 장소를 추천해줘. }]}";
            con.setDoOutput(true); //outputstream으로 post를 넘겨줌
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body); //request body에 data 셋팅
            writer.flush(); //request body에 data 입력
            writer.close(); //outputstream 종료

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 추출된 response 내용 리턴
            return extractContentFromResponse(response.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // chat gpt의 예상된 response을 추출하고 리턴
    public static String extractContentFromResponse(String response) {
        int startMarker = response.indexOf("content")+11;
        int endMarker = response.indexOf("\"", startMarker);
        return response.substring(startMarker, endMarker); // response만 포함하여 리턴
    }
}

