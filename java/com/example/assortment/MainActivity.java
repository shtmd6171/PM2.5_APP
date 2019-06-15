package com.example.assortment;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.VolleyError;
import com.example.assortment.model.MesureDust;
import com.example.assortment.model.Nearobservatory;
import com.example.assortment.model.TmLocation;
import com.example.assortment.volley.DataCallback;
import com.example.assortment.volley.LocationProvider;
import com.example.assortment.volley.VolleyResult;
import com.example.assortment.volley.VolleyService;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // 상수
    private static final int CURRENT_STATTE_TIMER_UPDATE_INTERVAL = 1000 * 10;  // 10초, 현재 위치 알림을 위한 미세먼지 체크 시간 간격

    // 위젯
    private CustomDustMapView dustMapView = null;

    // 내부객체
    // 전국의 미세먼지를 구하기위해 각 위도경도를 포함하는 리스트
    private ArrayList<Location> mArrLocations = null;

    // 현재 내 위치의 미세먼지 정보가 나쁨으로 변경될때 알림을 주기위해
    // 타이머를 사용하여 지정된 시간 단위로 체크하여 알림을 발생시킨다.
    private Timer mCurrentStateCheckTimer = null;                                                               // 타이머 객체
    private CurrentStateCheckTimerTask mCurrentStateCheckTimerTask = null;                                      // 타이머 태스크
    private CurrentStateCheckTimerHandler mCurrentStateCheckTimerHandler = new CurrentStateCheckTimerHandler(); // 타이머 핸들러
    private int mOldPm10Value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // 전체 지도 뷰
        dustMapView = (CustomDustMapView) findViewById(R.id.dustMapView);

        // 전체 지도 정보 획득
        loadTotalDustInfo();

        // 10초마다 반복적으로 현재 위치의 데이터를 불러와서 상태 변경시 알람을 띄우는
        startCurrentStateCheck();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 현재업데이트 타이머 해제
        stopCurrentStateCheck();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Dexter.withActivity(MainActivity.this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if (id == R.id.Real_time) {
                                Intent Intent = new Intent(MainActivity.this, RealTime_Activity.class);
                                startActivity(Intent);
                            } else if (id == R.id.Search) {
                                Intent Intent = new Intent(MainActivity.this, Search_Activity.class);
                                startActivity(Intent);
                            } else if (id == R.id.Select) {
                                Intent Intent = new Intent(MainActivity.this, Select_Activity.class);
                                startActivity(Intent);
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    // 전체 정보로드 : 비동기 처리
    private void loadTotalDustInfo() {

        /*
         위도경도 참조 : http://blog.naver.com/PostView.nhn?blogId=money4584&logNo=220830013865
         */

        // 초기화 및 설정
        if (mArrLocations == null) {
            mArrLocations = new ArrayList<Location>();

            {
                Location location = new Location("서울");
                location.setLatitude(37.487935);
                location.setLongitude(126.857758);
                mArrLocations.add(location);
            }
            {
                Location location = new Location("경기도");
                location.setLatitude(36.571424);
                location.setLongitude(128.722687);
                mArrLocations.add(location);
            }
            {
                Location location = new Location("강원도");
                location.setLatitude(37.570705);
                location.setLongitude(126.981354);
                mArrLocations.add(location);
            }
            {
                Location location = new Location("충청북도");
                location.setLatitude(37.885693);
                location.setLongitude(127.733917);
                mArrLocations.add(location);
            }
            {
                Location location = new Location("충청남도");
                location.setLatitude(35.160337);
                location.setLongitude(126.824799);
                mArrLocations.add(location);
            }
            {
                Location location = new Location("경상북도");
                location.setLatitude(35.860118);
                location.setLongitude(128.563385);
                mArrLocations.add(location);
            }
            {
                Location location = new Location("경상남도");
                location.setLatitude(35.556809);
                location.setLongitude(129.247284);
                mArrLocations.add(location);
            }
            {
                Location location = new Location("전라북도");
                location.setLatitude(36.650793);
                location.setLongitude(127.478485);
                mArrLocations.add(location);
            }
            {
                Location location = new Location("전라남도");
                location.setLatitude(36.820279);
                location.setLongitude(127.10495);
                mArrLocations.add(location);
            }
            {
                Location location = new Location("제주도");
                location.setLatitude(35.958);
                location.setLongitude(126.712189);
                mArrLocations.add(location);
            }
        }

        if (mArrLocations != null) {
            for (Location location : mArrLocations) {

                if (location != null) {
                    getGeoWTM(location, objLoc -> {
                        TmLocation tmLocation = (TmLocation) objLoc;
                        if (tmLocation != null) {
                            getNearobservatory(tmLocation, objTmLoc -> {
                                ArrayList<Nearobservatory> list = (ArrayList<Nearobservatory>) objTmLoc;
                                if (list != null && list.size() > 0) {
                                    Nearobservatory nearobservatory = list.get(0);
                                    getMesureDnsty(nearobservatory, objNearobservatory -> {
                                        MesureDust mesureDustRes = (MesureDust) objNearobservatory;
                                        if (mesureDustRes != null) {

                                            // 지도에 업데이트
                                            dustMapView.updateDustInfo(location.getProvider(), mesureDustRes.getPm10Value(), mesureDustRes.getPm25Value());
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        }
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

                            try {
                                item.setPm10Value(Integer.valueOf(pm10Value));
                            } catch (NumberFormatException ex) {
                                item.setPm10Value(0);
                            }

                            try {
                                item.setPm10Value24(Integer.valueOf(pm10Value24));
                            } catch (NumberFormatException ex) {
                                item.setPm10Value24(0);
                            }

                            try {
                                item.setPm25Value(Integer.valueOf(pm25Value));
                            } catch (NumberFormatException ex) {
                                item.setPm25Value(0);
                            }

                            try {
                                item.setPm25Value24(Integer.valueOf(pm25Value24));
                            } catch (NumberFormatException ex) {
                                item.setPm25Value24(0);
                            }
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


    // 현재 위치 정보 획득으로 알림처리 타이머 시작
    private void startCurrentStateCheck() {
        if (mCurrentStateCheckTimer == null) {
            mCurrentStateCheckTimerTask = new CurrentStateCheckTimerTask();

            mCurrentStateCheckTimer = new Timer();
            mCurrentStateCheckTimer.schedule(mCurrentStateCheckTimerTask, 1000, CURRENT_STATTE_TIMER_UPDATE_INTERVAL);
        }
    }

    // 현재 위치 정보 획득으로 알림처리 타이머 정지
    private void stopCurrentStateCheck() {
        if (mCurrentStateCheckTimer != null) {
            mCurrentStateCheckTimer.cancel();
            mCurrentStateCheckTimer.purge();
            mCurrentStateCheckTimer = null;

            mCurrentStateCheckTimerTask.cancel();
            mCurrentStateCheckTimerTask = null;
        }
    }

    // 현재 위치 정보 획득으로 알림처리 타이머 핸들러
    private class CurrentStateCheckTimerTask extends TimerTask {
        @Override
        public void run() {

            // 메인스레드에서 구동되어야 하기 때문에 핸들러를 지정하고 핸들러로 메시지를 보낸다.
            mCurrentStateCheckTimerHandler.sendEmptyMessage(0);
        }
    }

    // 현재 위치 정보 획득으로 알림처리 타이머 핸들러
    private class CurrentStateCheckTimerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 핸들러 호출시 현재위치를 받고 미세먼지를 측정한다.
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
                                        getMesureDnsty(nearobservatory, obj2 -> {
                                            //받은 측정소를 getMesureDnsty() 인자에 담아서 보내줘서 측정소에서 측정된 최신 미세먼지값을 받아온다.
                                            MesureDust mesureDust = (MesureDust) obj2;
                                            if (mesureDust != null) {
                                                int curPm10Value = mesureDust.getPm10Value();

                                                // 알림을 강제로 띄우기 위한 테스트 코드
//                                                if (mOldPm10Value < 70) curPm10Value = 80;
                                                Log.i("TEST", "미세먼지 :: 이전(" + mOldPm10Value + "), 현재(" + curPm10Value + ")");

                                                // 나쁨으로 변경 체크
                                                if (mOldPm10Value <= 80 && curPm10Value > 80) {
                                                    int finalCurPm10Value = curPm10Value;
                                                    runOnUiThread(() -> {
                                                        updateNotification(MainActivity.this, "미세먼지 알림", "현재 미세먼지 수치가 나빠졌어요! (" + finalCurPm10Value + " ㎍/㎥" +")", 100);
                                                    });
                                                }

                                                // 이전 상태 저장
                                                mOldPm10Value = curPm10Value;
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
    }

    // 알람을 띄게 한다.
    public static void updateNotification(Context context, String title, String msg, int idx) {

        // 알림 설정
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder;
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "assortment_service_channel";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Assortment Service", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(false);

            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);

        } else {
            mBuilder = new NotificationCompat.Builder(context);
        }

        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setTicker("현재 미세먼지 지수가 '나쁨' 상태입니다.");
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(msg);
        mBuilder.setContentIntent(null);
//        mBuilder.setOnlyAlertOnce(true);
        mBuilder.setAutoCancel(true);
        mBuilder.setSound(defaultSoundUri);

        // 안드로이드 7.0 이상일 경우 그룹코드를 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
            mBuilder.setGroup("assortment_noti");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(idx, mBuilder.build());
    }
}
