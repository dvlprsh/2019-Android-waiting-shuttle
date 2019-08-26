package com.example.watingshuttleformanager.login;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    final static private String URL = "http://13.125.147.57/waiting_shuttle/UserRegister.php";
    private Map<String, String> parameters;

    public RegisterRequest(String userID, String userPassword, String userName, String userRegiNum, String userEmail,String userPhone, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("userName", userName);
        parameters.put("userRegiNum", userRegiNum);
        parameters.put("userEmail", userEmail);
        parameters.put("userPhone", userPhone);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}


//출처: //http://gakari.tistory.com/entry/안드로이드-4-수강신청앱-회원-가입-기능-구현?category=414830 [가카리의 공부방]