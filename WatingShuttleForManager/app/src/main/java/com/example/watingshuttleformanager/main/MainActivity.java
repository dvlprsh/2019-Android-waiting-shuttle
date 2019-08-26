package com.example.watingshuttleformanager.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.watingshuttleformanager.R;
import com.example.watingshuttleformanager.login.LoginActivity;
import com.example.watingshuttleformanager.login.LoginRequest;
import com.example.watingshuttleformanager.register_restaurant_address.HomeActivity;
import com.example.watingshuttleformanager.register_restaurant_info.AddRestaurant_Info_Activity;
import com.example.watingshuttleformanager.register_restaurant_info.SaveInfoRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    //Volley를 이용해 등록한 가게 정보 불러오기
    String image_full_url; //최종 .png 주소값을 URL_PRODUCTS/에 붙인 주소

    TextView textview_name;

    TextView textview_address;
    TextView textview_time;

    TextView textview_price;


    Button button;
    ImageView imageView;
    AlertDialog dialog;
    RequestQueue requestQueue;  //Volley requestQueue
    int owner_id;
    private static final String URL_PRODUCTS = "http://13.125.147.57/owners_waiting_shuttle/MainActivity/main.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref=getSharedPreferences("login_session", MODE_PRIVATE);
        owner_id=pref.getInt("key_id", -1);
        Log.d("test_mainkey_id", String.valueOf(owner_id));
       // button=findViewById(R.id.search_button);
        imageView=findViewById(R.id.restaurant_image);
        textview_name=findViewById(R.id.restaurant_name);
        textview_address=findViewById(R.id.restaurant_address);
        textview_time=findViewById(R.id.textview_time);
        textview_price=findViewById(R.id.map_center_phone);

        Response.Listener<String> responseLisner = new Response.Listener<String>(){



            @Override

            public void onResponse(String response) {

                try{

                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");



                    if(success){


                        String test=jsonResponse.getString("test");
                        Log.d("test_is_owenr", test);
                        //로그인 정보 sharedpreference 에 저장
                        //String image=jsonResponse.getString("image"); //사용자의 아이디
                        //int id=jsonResponse.getInt("id"); //primary key(auto increment)
                        //제휴 회원 여부 가져오기


                        //Log.d("test_main_id", image);
                        //Log.d("key_id", String.valueOf(id));

                        //Glide.with(MainActivity.this).load(restaurant.getImage()).into(imageView);


                        //Restaurant(String image, String name, String kind, String address, String tel, String time, String day_off, String breaktime, String price)
                        String image=jsonResponse.getString("image");
                        Log.d("test_main1", image);
                        String name=jsonResponse.getString("name");
                        Log.d("test_main2", name);
                        String kind=jsonResponse.getString("kind");
                        Log.d("test_main2", kind);
                        String address=jsonResponse.getString("address");
                        String tel=jsonResponse.getString("tel");
                        String time=jsonResponse.getString("time");
                        String day_off=jsonResponse.getString("day_off");
                        String breaktime=jsonResponse.getString("breaktime");
                        String price=jsonResponse.getString("price");
                        Log.d("test_main2", time);
                        Log.d("test_main2", day_off);
                        Log.d("test_main2", breaktime);
                        Log.d("test_main288", price);
                        Restaurant restaurant=new Restaurant( image,  name,  kind,  address,  tel, time, day_off, breaktime, price);
                        Glide.with(MainActivity.this).load(restaurant.getImage()).into(imageView);
                        textview_name.setText(restaurant.getName());
                        textview_address.setText(restaurant.getAddress());
                        textview_time.setText(restaurant.getTime());
                        textview_price.setText(price);

                    }else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        dialog = builder.setMessage("계정을 다시 확인하세요")

                                .setNegativeButton("다시시도", null)

                                .create();

                        dialog.show();

                        //Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        //LoginActivity.this.startActivity(intent);

                        //finish();

                    }



                }catch (Exception e){

                    e.printStackTrace();

                }

            }

        };



        GetInfoRequest loginRequest = new GetInfoRequest(Integer.toString(owner_id), responseLisner);

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        queue.add(loginRequest);
        //가게 정보 불러오기
/*
        Response.Listener<String> responseListener = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");
                    String test=jsonResponse.getString("test");
                    //식당 주소 테이블 id(key)값 가져오기-intent로 다음 액티비티에 전달
                    //final int addr_id=jsonResponse.getInt("addr_id");
                    Log.d("test_main", "test_main");
                    Log.d("test_main_response", "test");
                    if(success){//저장되었다면
                        //Restaurant(String image, String name, String kind, String address, String tel, String time, String day_off, String breaktime, String price)
                        String image=jsonResponse.getString("image");
                        Log.d("test_main1", image);
                        String name=jsonResponse.getString("name");
                        Log.d("test_main2", name);
                        String kind=jsonResponse.getString("kind");
                        Log.d("test_main2", kind);
                        String address=jsonResponse.getString("address");
                        String tel=jsonResponse.getString("tel");
                        String time=jsonResponse.getString("time");
                        String day_off=jsonResponse.getString("day_off");
                        String breaktime=jsonResponse.getString("breaktime");
                        String price=jsonResponse.getString("price");
                        Log.d("test_main2", time);
                        Log.d("test_main2", day_off);
                        Log.d("test_main2", breaktime);
                        Log.d("test_main2", price);
                        Restaurant restaurant=new Restaurant( image,  name,  kind,  address,  tel, time, day_off, breaktime, price);
                        Glide.with(MainActivity.this).load(restaurant.getImage()).into(imageView);
                        txtview.setText(restaurant.getName());
                    }else{//저장에 실패하면
                        Log.d("test_main", "fail");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        AlertDialog dialog2 = builder.setMessage("call fail")
                                .setNegativeButton("OK", null)
                                .create();
                        dialog2.show();
                    }

                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };//Response.Listener 완료
        GetInfoRequest getRequest =
                new GetInfoRequest(Integer.toString(owner_id), responseListener);
        Log.d("test_owner_id", Integer.toString(owner_id));
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(getRequest);*/
        //db 저장 요청

        //task.execute(image_full_url);

/*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");
                            //식당 주소 테이블 id(key)값 가져오기-intent로 다음 액티비티에 전달
                            //final int addr_id=jsonResponse.getInt("addr_id");
                            Log.d("test_main", "test_main");
                            if(success){//저장되었다면
                                //Restaurant(String image, String name, String kind, String address, String tel, String time, String day_off, String breaktime, String price)
                                String image=jsonResponse.getString("image");
                                Log.d("test_main1", image);
                                String name=jsonResponse.getString("name");
                                Log.d("test_main2", name);
                                String kind=jsonResponse.getString("kind");
                                Log.d("test_main2", kind);
                                String address=jsonResponse.getString("address");
                                String tel=jsonResponse.getString("tel");
                                String time=jsonResponse.getString("time");
                                String day_off=jsonResponse.getString("day_off");
                                String breaktime=jsonResponse.getString("breaktime");
                                String price=jsonResponse.getString("price");
                                Log.d("test_main2", time);
                                Log.d("test_main2", day_off);
                                Log.d("test_main2", breaktime);
                                Log.d("test_main2", price);
                                Restaurant restaurant=new Restaurant( image,  name,  kind,  address,  tel, time, day_off, breaktime, price);
                                Glide.with(MainActivity.this).load(restaurant.getImage()).into(imageView);
                                txtview.setText(restaurant.getName());
                            }else{//저장에 실패하면
                                Log.d("test_main", "fail");
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                AlertDialog dialog2 = builder.setMessage("call fail")
                                        .setNegativeButton("OK", null)
                                        .create();
                                dialog2.show();
                            }

                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };//Response.Listener 완료
                GetInfoRequest getRequest =
                        new GetInfoRequest(Integer.toString(owner_id), responseListener);
                Log.d("test_owner_id", Integer.toString(owner_id));
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(getRequest);
            }
        });*/
        //test

    }//onCreate




}