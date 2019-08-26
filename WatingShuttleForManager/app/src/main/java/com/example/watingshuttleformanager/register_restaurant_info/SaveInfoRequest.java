package com.example.watingshuttleformanager.register_restaurant_info;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SaveInfoRequest extends StringRequest {

    final static private String URL = "http://13.125.147.57/owners_waiting_shuttle/AddRestaurant/add_info.php";
    private Map<String, String> parameters;

    public SaveInfoRequest(String addr_id, String owner_id, String name, String kind, String tel,String time, String breaktime, String day_off,String price, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
        parameters = new HashMap<>();
        parameters.put("addr_id", addr_id);
        parameters.put("owner_id", owner_id);
        parameters.put("name", name);
        parameters.put("kind", kind);
        parameters.put("tel", tel);
        parameters.put("time", time);
        parameters.put("breaktime", breaktime);
        parameters.put("day_off", day_off);
        parameters.put("price", price);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}


//출처: //http://gakari.tistory.com/entry/안드로이드-4-수강신청앱-회원-가입-기능-구현?category=414830 [가카리의 공부방]