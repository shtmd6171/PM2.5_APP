package com.example.assortment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.assortment.model.SearchMesureDust;
import com.example.assortment.volley.DataCallback;
import com.example.assortment.volley.VolleyResult;
import com.example.assortment.volley.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class Search_Activity extends AppCompatActivity {

    EditText edit;
    TextView text;

    XmlPullParser xpp;
    String key = "iOsw4MlgRU0JZpvuR5AkLUfkX%2FAOl0Q03HF78VRzR2g0dz6iD0esiw6HmLHKly6PVvGVP2PPgRpqtpULJBWSHg%3D%3D";
    String data;


    Button btnSearch;
    RecyclerView recyclerView;
    SearchAdapter adapter;


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
}

