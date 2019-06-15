package com.example.assortment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.assortment.model.MesureDust;
import com.example.assortment.model.Nearobservatory;
import com.example.assortment.model.TmLocation;
import com.example.assortment.volley.DataCallback;
import com.example.assortment.volley.LocationProvider;
import com.example.assortment.volley.VolleyResult;
import com.example.assortment.volley.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RealTime_Activity extends AppCompatActivity {

    private TextView tvLocation;
    private TextView tvDust10;
    private TextView tvDust25;
    private TextView Tomo_tvDust10;
    private TextView Tomo_tvDust25;
    private TextView After_Tomo_tvDust10;
    private TextView After_Tomo_tvDust25;
    public ProgressDialog mProgressDialog;

    private RelativeLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.rgb(3, 169, 244));
        }
        StrictMode.enableDefaults();
        mLayout = (RelativeLayout) findViewById(R.id.background);

        tvLocation = (TextView) findViewById(R.id.location);
        tvDust10 = (TextView) findViewById(R.id.dust1_value);
        tvDust25 = (TextView) findViewById(R.id.dust2_value);
        Tomo_tvDust10 = (TextView) findViewById(R.id.c1);
        Tomo_tvDust25 = (TextView) findViewById(R.id.c2);
        After_Tomo_tvDust10 = (TextView) findViewById(R.id.c3);
        After_Tomo_tvDust25 = (TextView) findViewById(R.id.c4);

        showProgressDialog("위치정보를 요청중입니다. 잠시만 기다려주세요.");
        new Handler().postDelayed(() -> {
            new LocationProvider().getLocation(getApplicationContext(), new LocationProvider.LocationResultCallback() {
                //먼저 내위치정보부터 가져온다.
                @Override
                public void gotLocation(Location curLocation) {
                    //위치정보 정보를 Callback 받는다.
                    if (curLocation != null) {
                        getGeoWTM(curLocation, obj -> {
                            //받은 위치정보를 이용해 기상청에서 요구하는 TM 좌표로 변환 을 받아와야한다 위 getGeoWTM() 통해 받아온후 Callback을 받는다.
                            TmLocation tmLocation = (TmLocation) obj;
                            if (tmLocation != null) {
                                getNearobservatory(tmLocation, obj1 -> {
                                    //받은 위치정보를 이용해 기상청에서 요구하는 TM 좌표로 변환받은후 getNearobservatory() 함수를 통해 TM 인자를 넘겨주고 내 위치에서 가장 가까운 측정소를 받아온다.
                                    ArrayList<Nearobservatory> list = (ArrayList<Nearobservatory>) obj1;
                                    if (list != null && list.size() > 0) {
                                        Nearobservatory nearobservatory = list.get(0);
                                        tvLocation.setText(nearobservatory.getStationName());
                                        getMesureDnsty(nearobservatory, obj2 -> {
                                            //받은 측정소를 getMesureDnsty() 인자에 담아서 보내줘서 측정소에서 측정된 최신 미세먼지값을 받아온다.
                                            MesureDust mesureDust = (MesureDust) obj2;
                                            if (mesureDust != null) {
                                                tvDust10.setText(mesureDust.getPm10Value() + " ㎍/㎥");
                                                tvDust25.setText(mesureDust.getPm25Value() + " ㎍/㎥");
                                                Tomo_tvDust10.setText(mesureDust.getPm10Value24() + " ㎍/㎥");
                                                Tomo_tvDust25.setText(mesureDust.getPm25Value24() + " ㎍/㎥");
                                                After_Tomo_tvDust10.setText(mesureDust.getPm10Value24() + 1 + " ㎍/㎥");
                                                After_Tomo_tvDust25.setText(mesureDust.getPm25Value24() + 1 + " ㎍/㎥");

                                                if(mesureDust.getPm10Value() <=30) // 파랑
                                                {
                                                    mLayout.setBackgroundColor(Color.rgb(3,169,244));
                                                    getWindow().setStatusBarColor(Color.rgb(3,169,244));
                                                }
                                                else if(30< mesureDust.getPm10Value() &&  mesureDust.getPm10Value() <=80) // 초록
                                                {
                                                    mLayout.setBackgroundColor(Color.rgb(14,206,22));
                                                    getWindow().setStatusBarColor(Color.rgb(14,206,22));

                                                }
                                                else if(80< mesureDust.getPm10Value() &&  mesureDust.getPm10Value() <=150) { //주황
                                                    mLayout.setBackgroundColor(Color.rgb(241,151,20));
                                                    getWindow().setStatusBarColor(Color.rgb(241,151,20));
                                                }
                                                else if(151<= mesureDust.getPm10Value()) { //빨강
                                                    mLayout.setBackgroundColor(Color.rgb(241,48,32)); // 빨강
                                                    getWindow().setStatusBarColor(Color.rgb(241,48,32));
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        });

                        hideProgressDialog();
                    } else {
                        showSnakbar("연결상태가 일시적으로 불안정합니다\n다시 시도해주세요", 1500);
                        hideProgressDialog();
                    }
                }
            });
        }, 250);
    }

    private void getMesureDnsty(Nearobservatory nearobservatory, DataCallback callback) {
        VolleyService volleyService = new VolleyService(new VolleyResult() {
            @Override
            public void notifySuccess(String type, JSONObject response) {
                try {
                    JSONArray ja = response.getJSONArray("list");
                    if (ja.length() > 0) {

                        MesureDust item = new MesureDust();

                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject mesureJson = ja.getJSONObject(i);

                            String pm10Value = mesureJson.getString("pm10Value");
                            String pm10Value24 = mesureJson.getString("pm10Value24");
                            String pm25Value = mesureJson.getString("pm25Value");
                            String pm25Value24 = mesureJson.getString("pm25Value24");

                            item.setPm10Value(Integer.valueOf(pm10Value));
                            item.setPm10Value24(Integer.valueOf(pm10Value24));
                            item.setPm25Value(Integer.valueOf(pm25Value));
                            item.setPm25Value24(Integer.valueOf(pm25Value24));
                            break;
                        }


                        callback.success(item);
                    }
                } catch (JSONException e) {

                }
            }

            @Override
            public void notifyError(VolleyError error) {

            }
        }, getApplicationContext());
        volleyService.getMesureDnsty(nearobservatory);
    }

    private void getNearobservatory(TmLocation location, DataCallback callback) {
        VolleyService volleyService = new VolleyService(new VolleyResult() {
            @Override
            public void notifySuccess(String type, JSONObject response) {

                try {
                    ArrayList<Nearobservatory> list = new ArrayList<>();
                    JSONArray ja = response.getJSONArray("list");
                    if (ja.length() > 0) {

                        for (int i = 0; i < ja.length(); i++) {
                            Nearobservatory item = new Nearobservatory();
                            JSONObject nearJson = ja.getJSONObject(i);

                            String address = nearJson.getString("addr");
                            String stationName = nearJson.getString("stationName");

                            item.setAddress(address);
                            item.setStationName(stationName);
                            list.add(item);
                        }

                        callback.success(list);
                    }
                } catch (JSONException e) {

                }
            }

            @Override
            public void notifyError(VolleyError error) {

            }
        }, getApplicationContext());
        volleyService.getNearobservatory(location);
    }

    private void getGeoWTM(Location location, DataCallback callback) {
        VolleyService volleyService = new VolleyService(new VolleyResult() {
            @Override
            public void notifySuccess(String type, JSONObject response) {

                try {
                    TmLocation tmLocation = new TmLocation();
                    JSONArray ja = response.getJSONArray("documents");
                    if (ja.length() > 0) {
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject wtmJson = ja.getJSONObject(i);
                            if (wtmJson.has("x")) {
                                String x = wtmJson.getString("x");
                                tmLocation.setX(Double.valueOf(x));
                            }

                            if (wtmJson.has("y")) {
                                String y = wtmJson.getString("y");
                                tmLocation.setY(Double.valueOf(y));
                            }
                        }

                        callback.success(tmLocation);
                    }
                } catch (JSONException e) {

                }
            }

            @Override
            public void notifyError(VolleyError error) {

            }
        }, getApplicationContext());
        volleyService.getGeoWTM(location);
    }


    public void showProgressDialog(String title) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(title);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }

        mProgressDialog.show();
    }


    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void showSnakbar(String text, int priod) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), text, priod);
            snackbar.show();
        } else {
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        }
    }


}
