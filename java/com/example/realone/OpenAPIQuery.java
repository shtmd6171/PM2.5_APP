package com.example.realone;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class OpenAPIQuery {
    private final String strServiceKey = "9HdsYPXrtfkswuTTa%2BWLZXU2C1FgJT1wRMguaTKm3h4g1iUGfTrgvFjCheFxbb6acKR7dzoumtwhD6pyHhxd0Q%3D%3D";


    private final int QueryTypeNONE = 0;
    private final int QueryTypeGetStationNamefromTM = 1;

    private final int QueryTypeGetAirDatafromStationName = 2;

    private int m_iQueryType =QueryTypeNONE;
    private resultCallback m_callback = null;

    private OpenAPIThreadTask mThreadAPI = null;

    interface resultCallback { // 인터페이스는 외부에 구현해도 상관 없습니다.
        void callbackOpenAPI_GetAirDatafromStationName(String result);
        void callbackOpenAPI_GetStationNamefromTM(String result);
        void callbackOpenAPI_Error(String errReport);
    }

    public OpenAPIQuery(resultCallback callback){
        m_callback = callback;
    }

    public void StopQuery(){
        Log.d("linsoo", "queryThreadStop");
        if(mThreadAPI !=null){
            mThreadAPI.cancel(true);
            mThreadAPI = null;
        }
    }

    public  void queryGetStationNamefromTM(double tmX, double tmY){
        Log.d("linsoo", "queryGetStationNamefromTM");
        try{
            m_iQueryType = QueryTypeGetStationNamefromTM;
            StringBuilder urlBuilder = new StringBuilder("http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+ strServiceKey);
            urlBuilder.append("&" + URLEncoder.encode("tmX","UTF-8") + "=" + URLEncoder.encode(Double.toString(tmX), "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("tmY","UTF-8") + "=" + URLEncoder.encode(Double.toString(tmY), "UTF-8"));

            if(mThreadAPI != null)
                StopQuery();
            mThreadAPI = new OpenAPIThreadTask();
            mThreadAPI.execute(urlBuilder.toString(),null,null);
        }catch (Exception e){ Log.e("linsoo", "queryGetStationNamefromTM="+e.getMessage());}
    }

    public  void queryGetAirDatafromStationName(String stationName){
        Log.d("linsoo", "queryGetAirDatafromStationName");
        try{
            m_iQueryType = QueryTypeGetAirDatafromStationName;
            StringBuilder urlBuilder = new StringBuilder("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+ strServiceKey);
            urlBuilder.append("&" + URLEncoder.encode("stationName","UTF-8") + "=" + URLEncoder.encode(stationName, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("dataTerm","UTF-8") + "=" + URLEncoder.encode("DAILY", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("ver","UTF-8") + "=" + URLEncoder.encode("1.1", "UTF-8"));

            if(mThreadAPI != null)
                StopQuery();
            mThreadAPI = new OpenAPIThreadTask();
            mThreadAPI.execute(urlBuilder.toString(),null,null);

        }catch (Exception e){ Log.e("linsoo", "queryGetAirDatafromStationName="+e.getMessage());}
    }

    private class OpenAPIThreadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urlString) {
            Log.d("linsoo", "doInBackground");
            try{
                URL url = new URL(urlString[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");
                System.out.println("Response code: " + conn.getResponseCode());
                BufferedReader rd;
                if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                conn.disconnect();
                return sb.toString();

            }catch (Exception e){
                Log.e("linsoo", "error="+e.getMessage());
                if (m_callback != null){
                    m_callback.callbackOpenAPI_Error(e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("linsoo", "onPostExecute");
            mThreadAPI = null;
            if(result != null) {
                if (m_callback != null){
                    switch (m_iQueryType){
                        case QueryTypeGetStationNamefromTM:  m_callback.callbackOpenAPI_GetStationNamefromTM(result);   break;
                        case QueryTypeGetAirDatafromStationName: m_callback.callbackOpenAPI_GetAirDatafromStationName(result);  break;
                    }
                }
            }
        }


    }
}
