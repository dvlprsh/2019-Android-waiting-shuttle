package com.example.watingshuttleformanager.mode;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddOfflineWaitingRequest extends StringRequest {

    final static private String URL = "http://13.125.147.57/owners_waiting_shuttle/Waiting/add_waiting_off.php";
    private Map<String, String> parameters;
    //owner_id, 전화번호, 일행 수
    public AddOfflineWaitingRequest(String owner_id, String phone, String person_count, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
        parameters = new HashMap<>();
        parameters.put("owner_id", owner_id);
        parameters.put("phone", phone);
        parameters.put("person_count", person_count);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}


//출처: //http://gakari.tistory.com/entry/안드로이드-4-수강신청앱-회원-가입-기능-구현?category=414830 [가카리의 공부방]