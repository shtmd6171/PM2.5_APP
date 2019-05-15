package com.example.realone;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegistRequest extends StringRequest {

    final static private String URL = ""; //호스팅 서버 주소의 php 값 입력하면 됨
    private Map<String, String> paramenters;

    public RegistRequest(String userID, String userPassword, String userGender, String userEmail, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        paramenters = new HashMap<>();
        paramenters.put("userID", userID);
        paramenters.put("userPassword", userPassword);
        paramenters.put("userGender", userGender);
        paramenters.put("userEmail", userEmail);
    }
    @Override
    public Map<String, String> getParams(){
        return paramenters;
    }
}
