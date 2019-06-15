package com.example.assortment.volley;

import android.content.Context;
import android.location.Location;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.example.assortment.model.Nearobservatory;
import com.example.assortment.model.TmLocation;
import com.google.gson.JsonSyntaxException;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VolleyService {
    private static String KEY_MEASURE_PLACE_TOKEN = "xhDtPXt9wyLH3nA1E1EHfDoOyjiSS0mg6df0fxgjNgXx4Nnup45AnaS23rKyNihhgVLUYp9aLEBKi45No%2B7tcQ%3D%3D";
    private static String KEY_SERVER_TOKEN = "xhDtPXt9wyLH3nA1E1EHfDoOyjiSS0mg6df0fxgjNgXx4Nnup45AnaS23rKyNihhgVLUYp9aLEBKi45No%2B7tcQ%3D%3D";
    private static String KEY_TM_TOKEN = "261b5c63a15536286de812713e966601";
    private VolleyResult resultCallback = null;
    private Context mContext = null;


    private Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            resultCallback.notifySuccess(null, response);
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            resultCallback.notifyError(error);
        }
    };

    public VolleyService(VolleyResult resultCallback, Context context) {
        this.resultCallback = resultCallback;
        mContext = context;
    }

    public void getGeoWTM(Location location) {
        //위치정보 좌표를 TM좌표로 변환한다.
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);


        String url = "https://dapi.kakao.com/v2/local/geo/transcoord.json?" + "x=" + location.getLongitude() + "&y=" + location.getLatitude() + "&input_coord=WGS84&output_coord=TM";

//        L.e("::::getGeoWTM URL : " + url);

        Request<JSONObject> req = new Request<JSONObject>(Request.Method.GET, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultCallback.notifyError(error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "KakaoAK " + KEY_TM_TOKEN);
                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(
                            response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(json), HttpHeaderParser.parseCacheHeaders(response));

                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JsonSyntaxException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                resultCallback.notifySuccess("", response);
            }
        };
        requestQueue.add(req);
    }

    public void getSearchMesureDnsty(String title) {
        //대기질을 가져온다.
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);


        final String url = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?sidoName=" + title +
                "&searchCondition=DAILY&pageNo=1&numOfRows=10&ServiceKey=9HdsYPXrtfkswuTTa%2BWLZXU2C1FgJT1wRMguaTKm3h4g1iUGfTrgvFjCheFxbb6acKR7dzoumtwhD6pyHhxd0Q%3D%3D&_returnType=json";

//        L.e("::::[getAllMesureDnsty] : " + url);

        Request<JSONObject> req = new Request<JSONObject>(Request.Method.GET, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultCallback.notifyError(error);
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(
                            response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(json), HttpHeaderParser.parseCacheHeaders(response));

                } catch (UnsupportedEncodingException e) {
                    ;
                    return Response.error(new ParseError(e));
                } catch (JsonSyntaxException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                resultCallback.notifySuccess("", response);
            }
        };
        requestQueue.add(req);
    }

    public void getMesureDnsty(Nearobservatory nearobservatory) {
        //대기질을 가져온다.
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);


        final String url = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?"
                + "stationName=" + nearobservatory.getStationName() + "&dataTerm=month&pageNo=1&numOfRows=10&ServiceKey=9HdsYPXrtfkswuTTa%2BWLZXU2C1FgJT1wRMguaTKm3h4g1iUGfTrgvFjCheFxbb6acKR7dzoumtwhD6pyHhxd0Q%3D%3D&ver=1.3&_returnType=json";

//        L.e("::::[getMesureDnsty] : " + url);

        Request<JSONObject> req = new Request<JSONObject>(Request.Method.GET, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultCallback.notifyError(error);
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(
                            response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(json), HttpHeaderParser.parseCacheHeaders(response));

                } catch (UnsupportedEncodingException e) {
                    ;
                    return Response.error(new ParseError(e));
                } catch (JsonSyntaxException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                resultCallback.notifySuccess("", response);
            }
        };
        requestQueue.add(req);
    }


    public void getNearobservatory(TmLocation location) {
        //가장 가까운 관측소를 가져온다.
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);


        final String url = "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?"
                + "tmX=" + location.getX() + "&tmY=" + location.getY() + "&ServiceKey=" + KEY_MEASURE_PLACE_TOKEN + "&_returnType=json";

//        L.e("::::[getNearobservatory] : " + url);

        Request<JSONObject> req = new Request<JSONObject>(Request.Method.GET, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultCallback.notifyError(error);
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(
                            response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(json), HttpHeaderParser.parseCacheHeaders(response));

                } catch (UnsupportedEncodingException e) {
                    ;
                    return Response.error(new ParseError(e));
                } catch (JsonSyntaxException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                resultCallback.notifySuccess("", response);
            }
        };
        requestQueue.add(req);
    }


    public void getGeocoder(Location location) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        String output = "json";
        String parameter = "latlng=" + location.getLatitude() + "," + location.getLongitude() + "&language=ko" + "&key=" + "AIzaSyAggFaSQsTk8_Rsk_znu9QYMD7agMtoZVA";
        final String url = "https://maps.googleapis.com/maps/api/geocode/"
                + output + "?" + parameter;

//        L.e("::::geocoder : " + url);

        Request<JSONObject> req = new Request<JSONObject>(Request.Method.GET, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultCallback.notifyError(error);
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(
                            response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(json), HttpHeaderParser.parseCacheHeaders(response));

                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JsonSyntaxException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                resultCallback.notifySuccess("", response);
            }
        };
        requestQueue.add(req);
    }


}
