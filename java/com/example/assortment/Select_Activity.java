package com.example.assortment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.assortment.model.DbHelper;
import com.example.assortment.model.SearchMesureDust;
import com.example.assortment.volley.DataCallback;
import com.example.assortment.volley.VolleyResult;
import com.example.assortment.volley.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Select_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    Search_Adapter adapter;


    ArrayList<DbHelper.MiseModel> favor_list;
    ArrayList<String> root_arr = new ArrayList<>();
    ArrayList<SearchMesureDust> arrayList = new ArrayList<>();
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        adapter = new Search_Adapter(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        favor_list = ((BaseApp)getApplication()).getDbHelper().getResult();

        for (DbHelper.MiseModel mise : favor_list){
            if (root_arr.size() == 0)
                root_arr.add(mise.getRoot());
            boolean flag = true;
            for (String str : root_arr){
                if (str.equals(mise.getRoot())){
                    flag = false;
                }

            }
            if (flag){
                root_arr.add(mise.getRoot());
            }

        }

        for (int i = 0; i < root_arr.size() ; i++) {
            getSearchMesureDnsty(root_arr.get(i), obj -> {
                ArrayList<SearchMesureDust> list = (ArrayList<SearchMesureDust>) obj;
                Log.d("<TAG>>>",list.size() + " SIZE");
                count++;
                if (list.size() > 0) {
                    arrayList.addAll(list);
                }
                if (count == root_arr.size()){
                    adapter.mSearchMesureDustList.clear();
                    for (SearchMesureDust mesureDust : arrayList){
                        for (DbHelper.MiseModel model : favor_list){
                            if (model.getItem().equals(mesureDust.getCityName()))
                                adapter.mSearchMesureDustList.add(mesureDust);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

            });
        }

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
                            searchMesureDust.setPm10Value(searhJson.getString("pm10Value"));//+ " ㎍/㎥"
                            searchMesureDust.setPm25Value(searhJson.getString("pm25Value"));
                            list.add(searchMesureDust);

                        }
                        callback.success(list);
                    }
                } catch (JSONException e) {
                    Log.e("<TAG>>>",e.toString());

                }
            }

            @Override
            public void notifyError(VolleyError error) {

            }
        }, getApplicationContext());
        volleyService.getSearchMesureDnsty(title);
    }
}
