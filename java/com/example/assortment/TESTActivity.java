package com.example.realone.manager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.realone.R;
import com.example.realone.manager.ApiParsingManager;
import com.example.realone.manager.MinuDustFrcstDspth;

import java.util.ArrayList;

public class TESTActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);



        final TextView text = findViewById(R.id.text);

        //여기서부터 사용 예시이니 보고 적용.



        //메니저 생성.
        ApiParsingManager apiParsingManager = new ApiParsingManager(getApplicationContext());

        //리스너 등록 및 데이터 요청.
        /**
         *
         * @param1 2019-05-21
         * @param2 (PM10 : 미세먼지, PM25 : 초미세먼지, O3 : 오존)
         * @param3 가져올 개수.
         *
         */
        //3 대기질 예보통보 조회
        apiParsingManager.getMinuDustFrcstDspth("2019-05-21", "PM10", 3, new ApiParsingManager.OnGetMinuDustFrcstDspthListener() {

            @Override
            public void onSuccess(ArrayList<MinuDustFrcstDspth> result) {


                //example
                text.append("getMinuDustFrcstDspth size : " + result.size());
                text.append("\n");
                text.append("--------------------------------------------------\n");
                for (int i = 0; i < result.size(); i++) {
                    text.append("getDataTime : " + result.get(i).getDataTime() + "\n");
                    text.append("getImageUrl1 : " + result.get(i).getImageUrl1() + "\n");
                    text.append("getImageUrl2 : " + result.get(i).getImageUrl2() + "\n");
                    text.append("getImageUrl3 : " + result.get(i).getImageUrl3() + "\n");
                    text.append("getImageUrl4 : " + result.get(i).getImageUrl4() + "\n");
                    text.append("getImageUrl5 : " + result.get(i).getImageUrl5() + "\n");
                    text.append("getImageUrl6 : " + result.get(i).getImageUrl6() + "\n");
                    text.append("getImageUrl7 : " + result.get(i).getImageUrl7() + "\n");
                    text.append("getImageUrl8 : " + result.get(i).getImageUrl8() + "\n");
                    text.append("getImageUrl9 : " + result.get(i).getImageUrl9() + "\n");
                    text.append("getInformCause : " + result.get(i).getInformCause() + "\n");
                    text.append("getInformOverall : " + result.get(i).getInformOverall() + "\n");
                    text.append("--------------------------------------------------\n");
                }

            }

            @Override
            public void onFail(String error) {

                Log.e("error", error);

            }
        });


        //4.시도별 실시간 측정정보 조회
        apiParsingManager.getCtprvnRltmMesureDnsty("인천", 5, new ApiParsingManager.OnGetCtprvnRltmMesureDnstyListener() {

            @Override
            public void onSuccess(ArrayList<CtprvnMesure> result) {


                //example
                text.append("#####################################################\n");
                text.append("getCtprvnRltmMesureDnsty size : " + result.size());
                text.append("\n");
                text.append("--------------------------------------------------\n");
                for (int i = 0; i < result.size(); i++) {
                    text.append("getDataTime : " + result.get(i).getDataTime() + "\n");
                    text.append("getCoGrade : " + result.get(i).getCoGrade() + "\n");
                    text.append("getCoValue : " + result.get(i).getCoValue() + "\n");
                    text.append("getKhai : " + result.get(i).getKhai() + "\n");
                    text.append("getNo2Grade : " + result.get(i).getNo2Grade() + "\n");
                    text.append("getNo2Value : " + result.get(i).getNo2Value() + "\n");
                    text.append("getO3Grade : " + result.get(i).getO3Grade() + "\n");
                    text.append("getPm25Grade : " + result.get(i).getPm25Grade() + "\n");
                    text.append("getSo2Grade : " + result.get(i).getSo2Grade() + "\n");
                    text.append("--------------------------------------------------\n");
                }

            }

            @Override
            public void onFail(String error) {

                Log.e("error", error);

            }
        });

    }
}
