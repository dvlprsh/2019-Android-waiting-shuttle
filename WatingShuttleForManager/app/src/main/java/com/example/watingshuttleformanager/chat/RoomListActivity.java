package com.example.watingshuttleformanager.chat;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.watingshuttleformanager.NavigationActivity;
import com.example.watingshuttleformanager.R;
import com.example.watingshuttleformanager.list_test.Product;
import com.example.watingshuttleformanager.list_test.ProductsAdapter;
import com.example.watingshuttleformanager.mode.GetWaitingListRequest;
import com.example.watingshuttleformanager.mode.WaitingProduct;
import com.example.watingshuttleformanager.mode.WaitingProductsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomListActivity extends AppCompatActivity {

    public static final String KEY_SIMPLE_DATA="data";
    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work
    private static final String URL_PRODUCTS = "http://13.125.147.57/owners_waiting_shuttle/Chat/getRoomList.php";

    //a list to store all the products
    List<ChatRoom> roomList;
    UsersAdapter adapter;
    //the recyclerview
    RecyclerView recyclerView;
    Button button;
    private String my_userID;
    private int owner_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);
        //로그인 세션
        SharedPreferences pref=getSharedPreferences("login_session",MODE_PRIVATE);
        my_userID=pref.getString("user_id", null);
        //editor.putInt("key_id", id);
        owner_id=pref.getInt("key_id", -1);
        Log.d("session test", my_userID);
        //로그인 세션
        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.room_list_activity_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        roomList= new ArrayList<>();

        adapter = new UsersAdapter(RoomListActivity.this, roomList);
        recyclerView.setAdapter(adapter);
        //this method will fetch and parse json
        //to display it in recyclerview
        loadProducts();
    }//onCreate

    private void loadProducts() {

        Response.Listener<String> responseListener = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try{

//converting the string to json array object
                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting product object from json array
                        JSONObject product = array.getJSONObject(i);

                        Log.d("waiting_test", product.toString());
                        // public ChatRoom(int room_number, int owner_id, int user_id, String restaurant_name, String userID) {
                        roomList.add(new ChatRoom(
                                product.getInt("room_number"),
                                product.getInt("owner_id"),
                                product.getInt("user_id"),
                                product.getString("restaurant_name"),
                                product.getString("userID")

                        ));


                    }

                    adapter.notifyDataSetChanged();
                    //creating adapter object and setting it to recyclerview
                    //UsersAdapter adapter = new UsersAdapter(RoomListActivity.this, roomList);
                    //recyclerView.setAdapter(adapter);

                }
                catch(JSONException e){
                    e.printStackTrace();
                }
            }

        };
        GetRoomListRequest saveRequest =
                new GetRoomListRequest(Integer.toString(owner_id), responseListener);
        RequestQueue queue = Volley.newRequestQueue(RoomListActivity.this);
        queue.add(saveRequest);
    }
    //커스텀 StringRequest
    public class GetRoomListRequest extends StringRequest {

        final static private String URL = "http://13.125.147.57/owners_waiting_shuttle/Chat/getRoomList.php";
        private Map<String, String> parameters;
        //owner_id, 전화번호, 일행 수
        public GetRoomListRequest(String owner_id, Response.Listener<String> listener){
            super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
            parameters = new HashMap<>();
            parameters.put("owner_id", owner_id);

        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return parameters;
        }
    }//커스텀 StringRequest

}
