package com.example.watingshuttleformanager.register_restaurant_address;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SaveAddressRequest extends StringRequest {

    final static private String URL = "http://13.125.147.57/owners_waiting_shuttle/AddRestaurant/add_address.php";
    private Map<String, String> parameters;

    public SaveAddressRequest(String owner_id, String address, String longitude, String latitude, String owner_userID, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
        parameters = new HashMap<>();
        parameters.put("owner_id", owner_id);
        parameters.put("address", address);
        parameters.put("longitude", longitude);
        parameters.put("latitude", latitude);
        parameters.put("owner_userID", owner_userID);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}


//출처: //http://gakari.tistory.com/entry/안드로이드-4-수강신청앱-회원-가입-기능-구현?category=414830 [가카리의 공부방]