# 지역 기반 관광지 추천 서비스 API
### 목차
1. [개요](https://github.com/seunghyunyyy/ssu_open_pj#개요)
2. [사용한 오픈소스 및 API](https://github.com/seunghyunyyy/ssu_open_pj#사용한-오픈소스-및-api)
3. [개발 환경](https://github.com/seunghyunyyy/ssu_open_pj#개발-환경)
4. [데이터베이스 구조 (User Entity)](https://github.com/seunghyunyyy/ssu_open_pj#데이터베이스-구조-user-entity)
5. [REST-API](https://github.com/seunghyunyyy/ssu_open_pj#rest-api)
## 
### 개요
* 사용자가 원하는 국내 여행지와 추천 개수를 입력하면 입력한 도시해서 입력한 개수만큼 관광지를 추천해주는 서비스.
* 회원가입, 로그인, 계정 정보 수정, 계정 탈퇴, 계정 찾기, 비밀번호 재설정, 도시명으로 관광지 불러오기, 도시의 날씨 정보 구하기, 관광지 추천받기, 추천받은 관광지 지도에 표시하기 등의 기능이 있음.
## 
### 사용한 오픈소스 및 API
>API 이름 클릭 시 해당 API를 사용할 수 있는 사이트로 이동 가능
* [한국관광공사_국문 관광정보 서비스_GW](https://www.data.go.kr/iim/api/selectAPIAcountView.do) : 국내 관광지 공공 데이터를 불러오는 API
* [Weathermap-weather](https://openweathermap.org/price#weather) : 인자로 입력한 도시의 날씨 정보를 불러오는 API
* [Korean-Romanizer](https://github.com/crizin/korean-romanizer) : 한글을 로마자 표기법에 맞게 변환해주는 오픈소스
* [Kakao Map API](https://apis.map.kakao.com) : 추천 관광지를 지도 내 마커로 표시할 카카오 지도 API
* [Chat GPT API](https://platform.openai.com) : 국내 관광지 데이터에서 추천해줄 Chat GPT API
##
### 개발 환경
* JDK : 17.0.8
* Spring Boot 3.2.0
* IntelliJ IDEA 2023.2.5 Ultimate Edition
* MySQL 8.0.34 Community
##
### 데이터베이스 구조 (User Entity)
![스크린샷 2023-12-11 오전 12 29 43](https://github.com/seunghyunyyy/ssu_open_pj/assets/99075653/42c16ed3-4537-43fc-a83c-32379e5c48f9)
##
## REST-API
1. [POST /user/v1/signUP](https://github.com/seunghyunyyy/ssu_open_pj#post-userv1signup--회원가입)
2. [POST /user/v1/signIn](https://github.com/seunghyunyyy/ssu_open_pj#post-userv1signin--로그인)
3. [PATCH /user/v1/{userId}/update/password](https://github.com/seunghyunyyy/ssu_open_pj#patch-userv1useridupdatepassword--비밀번호-변경)
4. [PATCH /user/v1/{userId}/update/info](https://github.com/seunghyunyyy/ssu_open_pj#patch-userv1useridupdateinfo--회원-정보-변경)
5. [GET /user/v1/find/id](https://github.com/seunghyunyyy/ssu_open_pj#get-userv1findid--회원-id-찾기)
6. [GET /user/v1/find/password](https://github.com/seunghyunyyy/ssu_open_pj#get-userv1findpassword--회원-password-찾기)
7. [DELETE /user/v1/withdrawal](https://github.com/seunghyunyyy/ssu_open_pj#delete-userv1withdrawal--회원-정보-삭제)
8. [GET /api/v1/searchKeyword](https://github.com/seunghyunyyy/ssu_open_pj#get-apiv1searchkeyword--도시별-관광지-불러오기)
9. [GET /api/v1/weather](https://github.com/seunghyunyyy/ssu_open_pj#get-apiv1weather--도시-날씨-정보-불러오기)
10. [GET /api/v1/chat](https://github.com/seunghyunyyy/ssu_open_pj#get-apiv1chat--chat-gpt)
11. [GET /api/v1/recommend](https://github.com/seunghyunyyy/ssu_open_pj#get-apiv1recommend--관광지-추천)
12. [GET /api/vi/map](https://github.com/seunghyunyyy/ssu_open_pj#get-apivimap--지도에-관광지-마커-표시)
##
### POST /user/v1/signUP : 회원가입
  * Request Body JSON에 동일한 두 개의 비밀번호를 입력. 불일치 시 회원가입 실패.
  * role은 USER와 ADMIN 둘 중 하나만 입력 가능.
  * 기본적으로 USER만 가입할 수 있으며 추후 ADMIN 권한 부여할 수 있도록 수정해야함.
###
    // Request Body JSON 구조
    {
        "userId" : String,
        "password" : {
            "pw1" : String,
            "pw2" : String
        },
        "name" : String,
        "phone" : String,
        "email" : String,
        "travleDestination" : String,
        "style" : String,
        "privacy" : String,
        "role" : String
    }
##
### POST /user/v1/signIn : 로그인
  * Request Body JSON에 회원가입을 끝내고 Database 상에 존재하는 유저 ID와 Password를 입력.
  * 불일치 시 로그인 실패.
###
    // Request Body JSON 구조
    {
        "userId" : String,
        "password" : String
    }
##
### PATCH /user/v1/{userId}/update/password : 비밀번호 변경
  * userId에는 존재하는 유저의 ID를 String 형태로 입력.
  * Request Body JSON에 동일한 두 개의 비밀번호를 입력.
  * 불일치 시 변경 실패.
###
    // Request Body JSON 구조
    {
        "pw1" : String,
        "pw2" : String
    }
##
### PATCH /user/v1/{userId}/update/info : 회원 정보 변경
  * userId에는 존재하는 유저의 ID를 String 형태로 입력.
  * Request Body JSON에 동일한 두 개의 비밀번호를 입력.
  * 불일치 시 변경 실패.
###
    // Request Body JSON 구조
    {
        "password" : {
            "pw1" : String,
            "pw2" : String
        },
        "name" : String,
        "phone" : String,
        "email" : String,
        "travelDestination" : String,
        "style" : String
    }
##
### GET /user/v1/find/id : 회원 ID 찾기
  * name, phone, email이 모두 일치하는 회원이 Database 상에 존재해야함.
  * 존재하지 않을 시 ID 찾기 실패.
###
    // Request Params 목록
    String name;
    String phone;
    String email;
###
    // 반환 값
    String userId;
##
### GET /user/v1/find/password : 회원 Password 찾기
  * userId, phone이 모두 일치하는 회원이 Database 상에 존재해야함.
  * 존재하지 않을 시 ID 찾기 실패.
  * 패스워드는 SHA256 함수를 통해 암호화 된 문자열을 반환받음.
  * 패스워드 찾기는 일단 구현하긴 했으나 패스워드 찾기보다 새로운 패스워드로 설정하는 것이 보안상 더 나은 것 같아 사용하지 않을 예정.
###
    // Request Params 목록
    String userId;
    String phone;
###
    // 반환 값
    String password;
##
### DELETE /user/v1/withdrawal : 회원 정보 삭제
  * userId에는 존재하는 유저의 ID를 String 형태로 입력.
  * Request Body JSON에 userId와 이에 일치하는 password를 입력.
  * 불일치 시 삭제 실패.
###
    // Request Body JSON 구조
    {
        "userId" : String,
        "password" : String
    }
##
### GET /api/v1/searchKeyword : 도시별 관광지 불러오기
  * [한국관광공사_국문 관광정보 서비스_GW](https://www.data.go.kr/iim/api/selectAPIAcountView.do)를 사용하여 입력한 도시의 관광지 목록을 불러옴.
###
    // Request Params 목록
    String city;
    Integer pageNo;
    Integer numOfRows;
###
    // 반환 값 (JSONArray)
    [
        {
            "title" : String,
            "addr1" : String,
            "addr2" : String,
            "tel" : String,
            "mapx" : String,
            "mapy" : String,
            "firstimage" : String,
            "firstimage2" : String
        }
    ]
##
### GET /api/v1/weather : 도시 날씨 정보 불러오기
  * [Weathermap-weather](https://openweathermap.org/price#weather)를 사용하여 입력한 도시의 날씨 정보를 불러옴.
  * 인자로 입력하는 city는 영어만 입력 가능.
###
    // Request Params 목록
    String city;
###
    // 반환 값 (JSON Object)
    {
        "rain": {
            "1h": Double
        },
        "visibility": Long,
        "timezone": Long,
        "main": {
            "temp": Double,
            "temp_min": Double,
            "humidity": Long,
            "pressure": Long,
            "feels_like": Double,
            "temp_max": Double
        },
        "clouds": {
            "all": Long
        },
        "sys": {
            "country": String,
            "sunrise": Long,
            "sunset": Long,
            "id": Long,
            "type": Long
        },
        "dt": Long,
        "coord": {
            "lon": Double,
            "lat": Double
        },
        "weather": [
            {
                "icon": String,
                "description": String,
                "main": String,
                "id": Long
            }
        ],
        "name": String,
        "cod": Long,
        "id": Long,
        "base": String,
        "wind": {
            "deg": Long,
            "speed": Double
        }
    }
##
### GET /api/v1/chat : Chat GPT
  * [Chat GPT API](https://platform.openai.com)를 사용하여 인자로 입력한 prompt에 대한 답변을 반환받음.
  * 인자로 입력하는 city는 영어만 입력 가능.
###
    // Request Params 목록
    String prompt;
###
    // 반환값
    String answer;
##
### GET /api/v1/recommend : 관광지 추천
  * [한국관광공사_국문 관광정보 서비스_GW](https://www.data.go.kr/iim/api/selectAPIAcountView.do)를 통해 인자로 입력받은 city의 관광지 목록을 불러옴.
  * [Chat GPT API](https://platform.openai.com)를 사용하여 불러온 관광지 목록과 사전에 입력한 prompt를 입력하여 인자로 입력받은 num 개수만큼 추천받음.
  * [Korean-Romanizer](https://github.com/crizin/korean-romanizer)를 사용하여 인자로 입력받은 city의 로마자 표기법을 반환받은 후 [Weathermap-weather](https://openweathermap.org/price#weather)를 사용하여 도시의 날씨 정보를 가져옴.
###
    // Request Params 목록
    String city;
    Integer num;
###
    // 반환값 (JSON Object)
    {
    "city": String,
    "weather": {
        "temp": Double,
        "temp_min": Double,
        "grnd_level": Integer,
        "humidity": Integer,
        "description": String,
        "pressure": Integer,
        "sea_level": Integer,
        "feels_like": Double,
        "temp_max": Double
    },
    "recommend": [
        {
            "addr2": String,
            "addr1": String,
            "tel": String,
            "firstimage2": String,
            "title": String,
            "mapy": String,
            "mapx": String,
            "firstimage": String
        }
    ]
}
##
### GET /api/vi/map : 지도에 관광지 마커 표시
  * [한국관광공사_국문 관광정보 서비스_GW](https://www.data.go.kr/iim/api/selectAPIAcountView.do)를 통해 인자로 입력받은 city의 관광지 목록을 불러옴.
  * [Chat GPT API](https://platform.openai.com)를 사용하여 불러온 관광지 목록과 사전에 입력한 prompt를 입력하여 인자로 입력받은 num 개수만큼 추천받음.
  * [Korean-Romanizer](https://github.com/crizin/korean-romanizer)를 사용하여 인자로 입력받은 city의 로마자 표기법을 반환받은 후 [Weathermap-weather](https://openweathermap.org/price#weather)를 사용하여 도시의 날씨 정보를 가져옴.
  * [Kakao Map API](https://apis.map.kakao.com)을 사용하여 추천받은 관광지를 카카오 지도에서 마커로 표시함.
###
    // Request Params 목록
    String city;
    Integer num;
###
    // 반환값
    map.html
##
