package com.example.realone.manager;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApiParsingManager {
    Context mContext;

    //통신 요청 큐.
    RequestQueue queue;

    private final static String SERVICE_KEY = "9HdsYPXrtfkswuTTa%2BWLZXU2C1FgJT1wRMguaTKm3h4g1iUGfTrgvFjCheFxbb6acKR7dzoumtwhD6pyHhxd0Q%3D%3D";


    //3
    ArrayList<MinuDustFrcstDspth> minuDustFrcstDspth = new ArrayList<>();
    //4
    ArrayList<CtprvnMesure> CtprvnMesureLIst = new ArrayList<>();


    public ApiParsingManager(Context context) {

        mContext = context;
        queue = Volley.newRequestQueue(mContext);
    }


    //3 대기질 예보통보 조회
    public void getMinuDustFrcstDspth(String searchDate, String InformCode, int numOfRows, final OnGetMinuDustFrcstDspthListener onGetMinuDustFrcstDspthListener) {

        String url = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMinuDustFrcstDspth?searchDate=" + searchDate + "&InformCode=" + InformCode + "&numOfRows=" + numOfRows + "&ServiceKey=" + SERVICE_KEY + "&_returnType=json";
        Log.d("getMsrstnList url", url);

        //정보 요청.
        final StringRequest request = new StringRequest(Request.Method.GET, url,
                //요청 성공 시
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //파싱.
                        try {
                            JSONArray list = new JSONObject(response).getJSONArray("list");
                            for (int i = 0; i < list.length(); i++) {//아이템 수만큼.

                                JSONObject data = (JSONObject) list.get(i);
                                minuDustFrcstDspth.add(new MinuDustFrcstDspth((String) data.get("dataTime"), (String) data.get("informCode"), (String) data.get("informOverall"), (String) data.get("informCause"), (String) data.get("informGrade"), (String) data.get("actionKnack"), (String) data.get("imageUrl1"), (String) data.get("imageUrl2"), (String) data.get("imageUrl3"), (String) data.get("imageUrl4"), (String) data.get("imageUrl5"), (String) data.get("imageUrl6"), (String) data.get("imageUrl7"), (String) data.get("imageUrl8"), (String) data.get("imageUrl9"), (String) data.get("informData")));

                            }

                            //return
                            onGetMinuDustFrcstDspthListener.onSuccess(minuDustFrcstDspth);

                        } catch (JSONException e) {
                            //e.printStackTrace();
                            onGetMinuDustFrcstDspthListener.onFail(e.toString());
                        }


                    }
                },
                // 에러 발생 시
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("error", "[" + error.getMessage() + "]");
                        onGetMinuDustFrcstDspthListener.onFail(error.getMessage());
                    }
                }
        );

        queue.add(request);

    }


    //4.시도별 실시간 측정정보 조회
    public void getCtprvnRltmMesureDnsty(String sidoName, int numOfRows, final OnGetCtprvnRltmMesureDnstyListener onGetCtprvnRltmMesureDnstyListener) {


        String url = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?sidoName=" + sidoName + "&numOfRows=" + numOfRows + "&ServiceKey=" + SERVICE_KEY + "&_returnType=json";
        Log.d("Ctprvn url", url);

        //정보 요청.
        final StringRequest request = new StringRequest(Request.Method.GET, url,
                //요청 성공 시
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //파싱.
                        try {
                            JSONArray list = new JSONObject(response).getJSONArray("list");
                            for (int i = 0; i < list.length(); i++) {//아이템 수만큼.

                                JSONObject data = (JSONObject) list.get(i);
                                CtprvnMesureLIst.add(new CtprvnMesure((String) data.get("stationName"), (String) data.get("mangName"), (String) data.get("dataTime"), (String) data.get("so2Value"), (String) data.get("coValue"), (String) data.get("o3Value"), (String) data.get("no2Value"), (String) data.get("pm10Value"), (String) data.get("pm10Value24"), (String) data.get("pm25Value"), (String) data.get("pm25Value24"), (String) data.get("khaiValue"), (String) data.get("khaiGrade"), (String) data.get("so2Grade"), (String) data.get("coGrade"), (String) data.get("o3Grade"), (String) data.get("no2Grade"), (String) data.get("pm10Grade"), (String) data.get("pm25Grade"), (String) data.get("pm10Grade1h"), (String) data.get("pm25Grade1h")));

                            }

                            //return
                            onGetCtprvnRltmMesureDnstyListener.onSuccess(CtprvnMesureLIst);

                        } catch (JSONException e) {
                           // e.printStackTrace();
                            onGetCtprvnRltmMesureDnstyListener.onFail(e.toString());
                        }


                    }
                },
                // 에러 발생 시
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("error", "[" + error.getMessage() + "]");
                        onGetCtprvnRltmMesureDnstyListener.onFail(error.getMessage());
                    }
                }
        );

        queue.add(request);


    }


    public interface OnGetMinuDustFrcstDspthListener {
        void onSuccess(ArrayList<MinuDustFrcstDspth> result);

        void onFail(String error);

    }

    public interface OnGetCtprvnRltmMesureDnstyListener {
        void onSuccess(ArrayList<CtprvnMesure> result);

        void onFail(String error);

    }


}
