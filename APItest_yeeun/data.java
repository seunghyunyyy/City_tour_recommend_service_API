

import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;

public class data {
    private static HttpURLConnection con;
    public static void main(String[] args) throws IOException {
        try{
            // 1. URL을 만들기 위한 StringBuilder.
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551011/KorService1/locationBasedList1?"); /*URL*/
            // 2. 오픈 API의요청 규격에 맞는 파라미터 생성, 발급받은 인증키.
            urlBuilder.append("&" + URLEncoder.encode("MobileOS","UTF-8") + "=" + URLEncoder.encode("ETC", "UTF-8")); 
            urlBuilder.append("&" + URLEncoder.encode("MobileApp","UTF-8") + "=" + URLEncoder.encode("TEST", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); 
            urlBuilder.append("&" + URLEncoder.encode("mapX", "UTF-8") + "=" + URLEncoder.encode("126.9572222", "UTF-8")); 
            urlBuilder.append("&" + URLEncoder.encode("mapY","UTF-8") + "=" + URLEncoder.encode("37.4963538", "UTF-8")); 
            urlBuilder.append("&" + URLEncoder.encode("radius","UTF-8") + "=" + URLEncoder.encode("10000", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("contentTypeId","UTF-8") + "=" + URLEncoder.encode("12", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("serviceKey","UTF-8") + "=W8nMEfGAjQ7UFw3paw4ARoNuuw4O7Z7lJC4BI9wxnyyRDiaqX5UdM%2FCY07wm4ge%2Bj6y4VS5y%2BNiQG574DH3gBQ%3D%3D"); /*Service Key*/
            // 3. URL 객체 생성.
            URL url = new URL(urlBuilder.toString());
            //URL에 접속하도록 설정
            con=(HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + con.getResponseCode());
            //전달받은 데이터를 버퍼 저장
            BufferedReader reader;
            if(con.getResponseCode() >= 200 && con.getResponseCode() <= 300) {
                reader=new BufferedReader(new InputStreamReader(con.getInputStream()));
            }else{
                reader=new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            //버퍼에 저장된 내용을 출력
            StringBuilder sb=new StringBuilder();
            String line;
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            //객체 해제
            reader.close();
            con.disconnect();  
            //전달받은 데이터 확인
            System.out.println(sb.toString());
        } catch (SocketTimeoutException e) {
        e.printStackTrace();
        }catch(IOException e) {
			System.out.println(e.getMessage());
		}
    }
}
