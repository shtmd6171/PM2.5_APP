소프트웨어 명세서
======================

## 1. 실시간 정보 제공 방식
* 실시간 정보는 API의 필요 내용을 각 객체로 흡수(JSON방식의 파싱)하여 XML 방식에 적용하였음
### 핵심 코드 내용
<pre>
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
</pre>
* 해당 방식에서는 INTERNET사용으로 인한, 인터넷 연결이 필수적으로 진행되어야 함 (위치정보 수집을 위함)
* 애플리케이션의 위치정보 접근을 허락하여야 함
* 애플리케이션은 미세먼지 정보와 초미세먼지 정보를 제공, 날짜에 따른 내일과 모레의 정보도 같이 제공함


## 2. 관측소 검색 정보 제공 방식
* 어댑터에서 정보값의 대한 객체 전체를 리스트화 시켜 검색할 정보를 JSON의 배열 형태로, 수치값을 저장하고 어댑터를 통해 XML 화면에 출력
### 핵심 코드 내용
<pre>
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setStatusBarColor(Color.rgb(3,169,244));

        edit = (EditText) findViewById(R.id.find_box);
        //text = (TextView) findViewById(R.id.list);
        btnSearch = (Button) findViewById(R.id.find_btn);
        //리스트뷰를 셋팅한다.
        recyclerView = (RecyclerView) findViewById(R.id.list);


        //리스트뷰에 커스텀으로 구현한 adapter를 연결해준다.
        adapter = new SearchAdapter(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        btnSearch.setOnClickListener(v -> {
            if (TextUtils.isEmpty(edit.getText().toString()) || edit.getText().toString().equalsIgnoreCase("")) {
                return;
            }

            getSearchMesureDnsty(edit.getText().toString(), obj -> {
                ArrayList<SearchMesureDust> list = (ArrayList<SearchMesureDust>) obj;
                if (list.size() > 0) {
                    adapter.addAll(list);
                }

            });


//            tvSearchResult.setText(dust.equalsIgnoreCase("") ? "결과없음" : dust);


        });
    }

    private void getSearchMesureDnsty(String title, DataCallback callback) {
        VolleyService volleyService = new VolleyService(new VolleyResult() {
            @Override
            public void notifySuccess(String type, JSONObject response) {

                try {
                    JSONArray ja = response.getJSONArray("list");
                    ArrayList<SearchMesureDust> list = new ArrayList<>();
                    if (ja.length() > 0) {
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject searhJson = ja.getJSONObject(i);
                            SearchMesureDust searchMesureDust = new SearchMesureDust();
                            searchMesureDust.setCityName(searhJson.getString("cityName"));
                            searchMesureDust.setPm10Value(searhJson.getString("pm10Value"));
                            searchMesureDust.setPm25Value(searhJson.getString("pm25Value"));
                            list.add(searchMesureDust);

                        }
                        callback.success(list);
                    }
                } catch (JSONException e) {

                }
            }

            @Override
            public void notifyError(VolleyError error) {
                TextView textView = (TextView) findViewById(R.id.head_list);
                textView.setText("올바른 이름으로 다시 검색해 주십시오");

            }
        }, getApplicationContext());
        volleyService.getSearchMesureDnsty(title);
    }
</pre>
* 해당 검색 기준은 시도를 기준으로 검색 되어야 제대로 된 관측소 정보를 제공한다 (서울, 경기, 강원 etc)
* 검색 시 확인 할 수 있는 관측소 정보는 해당 관측소의 현재 미세먼지와 초미세먼지 정보이다.
* 애플리케이션의 위치정보 접근을 허락하여야 함



## 3. 관측소 정보 검색 제공 방식
* 실제 제공되는 API의 맵 형태 구현을 제외시키고, 지도 이미지를 통한 실제 수치 값을 이미지 위로 표현하는 방식으로 구현함
* Xml 내부에서 구현을 하지않고, java코드를 통해서 비트맵을 적용함
### 핵심 코드 내용

<pre>
 private void drawDustInfo(Canvas canvas, DustInfo dustInfo) {
        float pos_x = mMapArea.left + (dustInfo.SHOW_POS.x * mPosMatrixLineOne);
        float pos_y = mMapArea.top + (dustInfo.SHOW_POS.y * mPosMatrixRowOne);

        Rect textRect = new Rect();
        mBoxDustPatin.getTextBounds(dustInfo.DUST_TITLE, 0, dustInfo.DUST_TITLE.length(), textRect);
        canvas.drawText(dustInfo.DUST_TITLE, pos_x + (textRect.width() / 2), pos_y + textRect.height(), mBoxDustPatin);

        if (dustInfo.IS_INITED) {
            Rect textRect2 = new Rect();
            pos_y += textRect.height() + DpToPx(0);
            mBoxDustPatin.getTextBounds(dustInfo.getDustValue(), 0, dustInfo.getDustValue().length(), textRect2);
            canvas.drawText(dustInfo.getDustValue(), pos_x + (textRect.width() / 2), pos_y + textRect2.height(), mBoxDustPatin);
        }
    }

    // DP 정수 값을 PX로 변한하여 값을 변환함.
    private static int DpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    // 값 업데이트
    public void updateDustInfo(String area, int value_10, int value_25) {

        for (DustInfo info : mArrDustInfo) {
            if (info.DUST_TITLE.compareTo(area) == 0) {
                info.updateValue(value_10, value_25);
                invalidate();
                break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 드로잉 최초에 화면의 크기를 구하고 이미지의 크기 및 이미지의 박스 사이즈룰 설정하고
        // 위치 분활을 통해 각 위치 정보를 표시할 점을 획득한다.
        if (mViewWidth == -1 && mViewHeight == -1) {
            Bitmap map = BitmapFactory.decodeResource(getResources(), R.drawable.m_map);
            if (map != null) {
                int imgWidth = getWidth();
                int imgHeight = getResizeableHeight(map.getWidth(), map.getHeight(), imgWidth, true);
                if (getHeight() < imgHeight) {
                    imgHeight = getHeight();
                    imgWidth = getResizeableHeight(map.getWidth(), map.getHeight(), imgHeight, false);
                }

                mMapBitmap = resizeMap(map, imgWidth, imgHeight);
                if (mMapBitmap != null) {
                    mViewWidth = getWidth();
                    mViewHeight = getHeight();

                    mMapArea.left = ((mViewWidth - imgWidth) / 2);
                    mMapArea.right = mViewWidth - ((mViewWidth - imgWidth) / 2);
                    mMapArea.top = ((mViewHeight - imgHeight) / 2);
                    mMapArea.bottom = mViewHeight - ((mViewHeight - imgHeight) / 2);

                    mPosMatrixLineOne = (float) mMapArea.width() / POSITION_MATRIX_LINE;
                    mPosMatrixRowOne = (float) mMapArea.height() / POSITION_MATRIX_ROW;
                }
            }
        }
</pre>
* png 이미지 위에서 출력 값이 작동하고 출력되는 정보는 시도의 미세먼지, 초미세먼지 정보이다.

## 4.사용자 관심 정보 등록 방식
* sqlite(dbhelper)를 사용하여 각 도시별 정보를 저장(등록)
* sqlite의 핵심코드는 DbHelper.class전체
package com.example.assortment.model;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE MISE_TBL (_id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT,root TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String item,String root) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL( "INSERT INTO MISE_TBL VALUES(null, " +
                "'" + item + "','"+root+"');");
        db.close();
    }



    public void delete(String item) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM MISE_TBL WHERE item='" + item + "';");
        db.close();
    }
    public void delete(int idx) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM MISE_TBL WHERE _id =" + idx + ";");
        db.close();
    }
    public ArrayList<MiseModel> getResult() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM MISE_TBL", null);
        ArrayList<MiseModel> mise = new ArrayList<>();
        while (cursor.moveToNext()) {
            MiseModel miseModel = new MiseModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
            mise.add(miseModel);
            Log.d("<TAG>>",miseModel.item +  " ,  " + miseModel.getRoot());
        }

        return mise;
    }

    public class MiseModel{
        int idx;
        String item;
        String root;

        public MiseModel(int idx, String item, String root) {
            this.idx = idx;
            this.item = item;
            this.root = root;
        }

        public String getRoot() {
            return root;
        }

        public void setRoot(String root) {
            this.root = root;
        }

        public MiseModel(int idx, String item) {
            this.idx = idx;
            this.item = item;
        }

        public int getIdx() {
            return idx;
        }

        public void setIdx(int idx) {
            this.idx = idx;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }
    }
}


* 혹은 Search_Activity.xml에 onCreate 메소드이다
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_three_page);
        getWindow().setStatusBarColor(Color.rgb(3,169,244));

        edit = (EditText) findViewById(R.id.find_box);
        //text = (TextView) findViewById(R.id.list);
        btnSearch = (Button) findViewById(R.id.find_btn);
        //리스트뷰를 셋팅한다.
        recyclerView = (RecyclerView) findViewById(R.id.list);


        //리스트뷰에 커스텀으로 구현한 adapter를 연결해준다.
        adapter = new Search_Adapter(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        btnSearch.setOnClickListener(v -> {
            if (TextUtils.isEmpty(edit.getText().toString()) || edit.getText().toString().equalsIgnoreCase("")) {
                return;
            }

            getSearchMesureDnsty(edit.getText().toString(), obj -> {
                ArrayList<SearchMesureDust> list = (ArrayList<SearchMesureDust>) obj;
                Log.d("<TAG>>>",list.size() + " SIZE");
                if (list.size() > 0) {
                    adapter.addAll(list);
                }

            });


//            tvSearchResult.setText(dust.equalsIgnoreCase("") ? "결과없음" : dust);


        });
    }
