package com.example.assortment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
public class API_Pollution_Info extends Activity {



    public static void main(String[] args){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime);

        StrictMode.enableDefaults();

        TextView status1 = (TextView) findViewById(R.id.dust1_value);


        boolean initem = false, indataTime = false, ino3Value = false,inpm10Value = false, inpm25Value = false;
        boolean ino3Grade = false, inpm10Grade = false, inpm25Grade = false, inpm10Grade1h = false, inpm25Grade1h = false;

        String dataTime = null, o3Value = null, pm10Value = null, pm25Value = null;
        String o3Grade = null, pm10Grade = null, pm25Grade = null, pm10Grade1h = null, pm25Grade1h = null;

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
                        if (parser.getName().equals("o3Value")) { //address 만나면 내용을 받을수 있게 하자
                            ino3Value = true;
                        }
                        if (parser.getName().equals("pm10Value")) { //address 만나면 내용을 받을수 있게 하자
                            inpm10Value = true;
                        }
                        if (parser.getName().equals("pm25Value")) { //address 만나면 내용을 받을수 있게 하자
                            inpm25Value = true;
                        }
                        if (parser.getName().equals("o3Grade")) { //address 만나면 내용을 받을수 있게 하자
                            ino3Grade = true;
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
                        if (ino3Value) { //isTitle이 true일 때 태그의 내용을 저장.
                            o3Value = parser.getText();
                            ino3Value = false;
                        }
                        if (inpm10Value) { //isTitle이 true일 때 태그의 내용을 저장.
                            pm10Value = parser.getText();
                            inpm10Value = false;
                        }
                        if (inpm25Value) { //isTitle이 true일 때 태그의 내용을 저장.
                            pm25Value = parser.getText();
                            inpm25Value = false;
                        }
                        if (ino3Grade) { //isTitle이 true일 때 태그의 내용을 저장.
                            o3Grade = parser.getText();
                            ino3Grade = false;
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
                            status1 = (TextView) findViewById(R.id.dust1_value);

                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
        }


    }

}
