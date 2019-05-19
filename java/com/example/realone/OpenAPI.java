package com.example.realone;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class OpenAPI extends Activity {

    public static void main(String[] args){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_page_four);

        StrictMode.enableDefaults();

        TextView status1 = (TextView) findViewById(R.id.result);

        boolean initem = false, indataTime = false, inmangName = false, inso2Value = false, incoValue = false;
        boolean ino3Value = false, inno2Value = false, inpm10Value = false, inpm10Value24 = false, inpm25Value = false;
        boolean inpm25Value24 = false, inkhaiValue = false, inkhaiGrade = false, inso2Grade = false, incoGrade = false;
        boolean ino3Grade = false, inno2Grade = false, inpm10Grade = false, inpm25Grade = false, inpm10Grade1h = false;
        boolean inpm25Grade1h = false;

        String dataTime = null, mangName = null, so2Value = null, coValue = null, o3Value = null, no2Value = null;
        String pm10Value = null, pm10Value24 = null, pm25Value = null, pm25Value24 = null, khaiValue = null;
        String khaiGrade = null, so2Grade = null, coGrade = null, o3Grade = null, no2Grade = null, pm10Grade = null;
        String pm25Grade = null, pm10Grade1h = null, pm25Grade1h = null;

        try {
            URL url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName=종로구&dataTerm=month&pageNo=1&numOfRows=10&ServiceKey=9HdsYPXrtfkswuTTa%2BWLZXU2C1FgJT1wRMguaTKm3h4g1iUGfTrgvFjCheFxbb6acKR7dzoumtwhD6pyHhxd0Q%3D%3D&ver=1.3"); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();
            InputStream in =url.openStream();
            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("dataTime")) { //title 만나면 내용을 받을수 있게 하자
                            indataTime = true;
                        }
                        if (parser.getName().equals("mangName")) { //address 만나면 내용을 받을수 있게 하자
                            inmangName = true;
                        }
                        if (parser.getName().equals("so2Value")) { //address 만나면 내용을 받을수 있게 하자
                            inso2Value = true;
                        }
                        if (parser.getName().equals("coValue")) { //address 만나면 내용을 받을수 있게 하자
                            incoValue = true;
                        }
                        if (parser.getName().equals("o3Value")) { //address 만나면 내용을 받을수 있게 하자
                            ino3Value = true;
                        }
                        if (parser.getName().equals("no2Value")) { //address 만나면 내용을 받을수 있게 하자
                            inno2Value = true;
                        }
                        if (parser.getName().equals("pm10Value")) { //address 만나면 내용을 받을수 있게 하자
                            inpm10Value = true;
                        }
                        if (parser.getName().equals("pm10Value24")) { //address 만나면 내용을 받을수 있게 하자
                            inpm10Value24 = true;
                        }
                        if (parser.getName().equals("pm25Value")) { //address 만나면 내용을 받을수 있게 하자
                            inpm25Value = true;
                        }
                        if (parser.getName().equals("pm25Value24")) { //address 만나면 내용을 받을수 있게 하자
                            inpm25Value24 = true;
                        }
                        if (parser.getName().equals("khaiValue")) { //address 만나면 내용을 받을수 있게 하자
                            inkhaiValue = true;
                        }
                        if (parser.getName().equals("khaiGrade")) { //address 만나면 내용을 받을수 있게 하자
                            inkhaiGrade = true;
                        }
                        if (parser.getName().equals("so2Grade")) { //address 만나면 내용을 받을수 있게 하자
                            inso2Grade = true;
                        }
                        if (parser.getName().equals("coGrade")) { //address 만나면 내용을 받을수 있게 하자
                            incoGrade = true;
                        }
                        if (parser.getName().equals("o3Grade")) { //address 만나면 내용을 받을수 있게 하자
                            ino3Grade = true;
                        }
                        if (parser.getName().equals("no2Grade")) { //address 만나면 내용을 받을수 있게 하자
                            inno2Grade = true;
                        }
                        if (parser.getName().equals("pm10Grade")) { //address 만나면 내용을 받을수 있게 하자
                            inpm10Grade = true;
                        }
                        if (parser.getName().equals("pm25Grade")) { //address 만나면 내용을 받을수 있게 하자
                            inpm25Grade = true;
                        }
                        if (parser.getName().equals("pm10Grade1h")) { //address 만나면 내용을 받을수 있게 하자
                            inpm10Grade1h = true;
                        }
                        if (parser.getName().equals("pm25Grade1h")) { //address 만나면 내용을 받을수 있게 하자
                            inpm25Grade1h = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (indataTime) { //isTitle이 true일 때 태그의 내용을 저장.
                            dataTime = parser.getText();
                            indataTime = false;
                        }
                        if (inmangName) { //isTitle이 true일 때 태그의 내용을 저장.
                            mangName = parser.getText();
                            inmangName = false;
                        }
                        if (inso2Value) { //isTitle이 true일 때 태그의 내용을 저장.
                            so2Value = parser.getText();
                            inso2Value = false;
                        }
                        if (incoValue) { //isTitle이 true일 때 태그의 내용을 저장.
                            coValue = parser.getText();
                            incoValue = false;
                        }
                        if (ino3Value) { //isTitle이 true일 때 태그의 내용을 저장.
                            o3Value = parser.getText();
                            ino3Value = false;
                        }
                        if (inno2Value) { //isTitle이 true일 때 태그의 내용을 저장.
                            no2Value = parser.getText();
                            inno2Value = false;
                        }
                        if (inpm10Value) { //isTitle이 true일 때 태그의 내용을 저장.
                            pm10Value = parser.getText();
                            inpm10Value = false;
                        }
                        if (inpm10Value24) { //isTitle이 true일 때 태그의 내용을 저장.
                            pm10Value24 = parser.getText();
                            inpm10Value24 = false;
                        }
                        if (inpm25Value) { //isTitle이 true일 때 태그의 내용을 저장.
                            pm25Value = parser.getText();
                            inpm25Value = false;
                        }
                        if (inpm25Value24) { //isTitle이 true일 때 태그의 내용을 저장.
                            pm25Value24 = parser.getText();
                            inpm25Value24 = false;
                        }
                        if (inkhaiValue) { //isTitle이 true일 때 태그의 내용을 저장.
                            khaiValue = parser.getText();
                            inkhaiValue = false;
                        }
                        if (inkhaiGrade) { //isTitle이 true일 때 태그의 내용을 저장.
                            khaiGrade = parser.getText();
                            inkhaiGrade = false;
                        }
                        if (inso2Grade) { //isTitle이 true일 때 태그의 내용을 저장.
                            so2Grade = parser.getText();
                            inso2Grade = false;
                        }
                        if (incoGrade) { //isTitle이 true일 때 태그의 내용을 저장.
                            coGrade = parser.getText();
                            incoGrade = false;
                        }
                        if (ino3Grade) { //isTitle이 true일 때 태그의 내용을 저장.
                            o3Grade = parser.getText();
                            ino3Grade = false;
                        }
                        if (inno2Grade) { //isTitle이 true일 때 태그의 내용을 저장.
                            no2Grade = parser.getText();
                            inno2Grade = false;
                        }
                        if (inpm10Grade) { //isTitle이 true일 때 태그의 내용을 저장.
                            pm10Grade = parser.getText();
                            inpm10Grade = false;
                        }
                        if (inpm25Grade) { //isTitle이 true일 때 태그의 내용을 저장.
                            pm25Grade = parser.getText();
                            inpm25Grade = false;
                        }
                        if (inpm10Grade1h) { //isTitle이 true일 때 태그의 내용을 저장.
                            pm10Grade1h = parser.getText();
                            inpm10Grade1h = false;
                        }
                        if (inpm25Grade1h) { //isTitle이 true일 때 태그의 내용을 저장.
                            pm25Grade1h = parser.getText();
                            inpm25Grade1h = false;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            status1.setText(status1.getText() + "측정일 : " + dataTime + "\n 측정망 정보 : " + mangName + "\n 아황산가스 농도: " + so2Value
                                    + "\n 일산화탄소 농도 : " + coValue + "\n 오존 농도 : " + o3Value + "\n 이산화질소 농도 : " + no2Value
                                    + "\n 미세먼지(PM10) 농도 : " + pm10Value + "\n 미세먼지(PM10)24시간예측이동농도 : " + pm10Value24 + "\n 미세먼지(PM2.5) 농도: " + pm25Value
                                    + "\n 미세먼지(PM2.5)24시간 예측 이동농도 : " + pm25Value24 + "\n 통합대기환경수치 : " + khaiValue + "\n"
                                    +
                                    "통합대기환경지수 : " + khaiGrade + "\n 아황산가스 지수 : " + so2Grade + "\n"
                                    +
                                    "일산화탄소 지수 : " + coGrade + "\n 오존 지수 : " + o3Grade + "\n"
                                    +
                                    "이산화질소 지수 : " + no2Grade + "\n 미세먼지(PM10)24시간등급 :" + pm10Grade + "\n"
                                    +
                                    "미세먼지(PM25)24시간등급 : " + pm25Grade + "\n 미세먼지(PM10)1시간등급 : " + pm10Grade1h + "\n"
                                    +
                                    "미세먼지(PM25)1시간등급 : " + pm25Grade1h + "\n\n\n");
                            initem = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
            status1.setText(e.getMessage());
        }
    }
}


/*public class OpenAPI {
    public static void main(String[] args){
        BufferedReader br = null;
        try{
            String urlstr ="http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName=종로구&dataTerm=month&pageNo=1&numOfRows=10&ServiceKey=9HdsYPXrtfkswuTTa%2BWLZXU2C1FgJT1wRMguaTKm3h4g1iUGfTrgvFjCheFxbb6acKR7dzoumtwhD6pyHhxd0Q%3D%3D&ver=1.3";
            URL url = new URL(urlstr);
            HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
            urlconnection.setRequestMethod("GET");
            br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(),"UTF-8"));
            String result = "";
            String line;
            while ((line = br.readLine()) != null){
                result = result + line + "\n";
            }
            System.out.println(result);
         }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }*/

