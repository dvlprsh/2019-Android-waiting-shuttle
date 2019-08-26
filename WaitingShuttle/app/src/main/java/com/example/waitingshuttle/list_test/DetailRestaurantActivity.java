package com.example.waitingshuttle.list_test;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.waitingshuttle.chat_test.ChatTestActivity;
import com.example.waitingshuttle.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailRestaurantActivity extends AppCompatActivity {
    private AlertDialog dialog;
    private TextView textView_people_number;
    //현재 페이지 식당정보
    private int owner_id;
    private Restaurant restaurant;
    private RestaurantInfo restaurantInfo;
    //현재 페이지 식당정보
    public static final String KEY_SIMPLE_DATA="data";

    private int people_number=1;
    //상세정보 textview
    private TextView textview_name;
    private TextView textview_kind;
    private TextView textView_address;
    private TextView textView_tel;
    private TextView textView_time;
    private TextView textView_breaktime;
    private TextView textView_day_off;
    private TextView textView_price;
    //상세정보 textview
    Button button;
    ImageView imageView;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);
        //툴바
        Toolbar toolbar=(Toolbar)findViewById(R.id.detail_restaurant_toolbar);
        setSupportActionBar(toolbar);
        //툴바
        //상세정보 textview findViewByID
        imageView=findViewById(R.id.detail_restaurant_imageView);
        textview_name=findViewById(R.id.detail_restaurant_textView_name);
        textview_kind=findViewById(R.id.detail_restaurant_textView_kind);
        textView_address=findViewById(R.id.detail_restaurant_textView_address);
        textView_tel=findViewById(R.id.detail_restaurant_textView_tel);
        textView_time=findViewById(R.id.detail_restaurant_textView_time);
        textView_breaktime=findViewById(R.id.detail_restaurant_textView_breaktime);
        textView_day_off=findViewById(R.id.detail_restaurant_textView_day_off);
        textView_price=findViewById(R.id.detail_restaurant_textView_price);
        //상세정보 textview findViewByID

        Log.d("detail_test", "test");
        pref=getSharedPreferences("login_session", MODE_PRIVATE);//SharedPreference 로그인 세션
        //식당 리스트 아이템 클릭 후 해당 식당정보 인텐트로 넘겨받기
        Intent intent=getIntent();
        if(intent!=null){
            Bundle bundle=intent.getExtras();
            restaurant=(Restaurant) bundle.getParcelable(KEY_SIMPLE_DATA);
            owner_id=restaurant.getOwner_id();
            Log.d("intent_owner_id", Integer.toString(owner_id));
            Log.d("intent_owner_id", restaurant.getName());
            Log.d("intent_owner_id", restaurant.getAddress());
            //가게 정보 받아오기
            Response.Listener<String> responseListener = new Response.Listener<String>(){



                @Override

                public void onResponse(String response) {

                    try{

                        JSONObject jsonResponse = new JSONObject(response);
                        Log.d("jsonResponse_test", jsonResponse.toString());
                        boolean success = jsonResponse.getBoolean("success");


                            int image_id=jsonResponse.getInt("image_id");
                            int addr_id=jsonResponse.getInt("addr_id");
                            String longitude=jsonResponse.getString("longitude");
                            String latitude=jsonResponse.getString("latitude");
                            String tel=jsonResponse.getString("tel");
                            String time=jsonResponse.getString("time");
                            String breaktime=jsonResponse.getString("breaktime");
                            String day_off=jsonResponse.getString("day_off");
                            String price=jsonResponse.getString("price");
                            int waiting=jsonResponse.getInt("waiting");
                            //public RestaurantInfo(Restaurant restaurant, int image_id, int addr_id, String longitude, String latitude, String tel,
                            //                          String time, String breaktime, String day_off, String price, int waiting) {

                            restaurantInfo=new RestaurantInfo( restaurant, image_id,  addr_id,  longitude,  latitude,  tel,
                                                               time, breaktime, day_off, price, waiting);
                            Glide.with(DetailRestaurantActivity.this).load(restaurant.getImage()).into(imageView);
                            textview_name.setText(restaurant.getName());
                            textview_kind.setText(restaurant.getKind());
                            textView_address.setText(restaurantInfo.getAddress());
                            textView_tel.setText(restaurantInfo.getTel());
                            textView_time.setText(restaurantInfo.getTime());
                            textView_breaktime.setText(restaurantInfo.getBreaktime());
                            textView_day_off.setText(restaurantInfo.getDay_off());
                            textView_price.setText(restaurantInfo.getPrice());


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };

            GetRestaurantInfoRequest getInfoRequest = new GetRestaurantInfoRequest(Integer.toString(owner_id), responseListener);

            RequestQueue queue = Volley.newRequestQueue(DetailRestaurantActivity.this);

            queue.add(getInfoRequest);
            //가게 정보 받아오기



        }//if(intent!=null) //식당 리스트 아이템 클릭 후 해당 식당정보 인텐트로 넘겨받기

        textView_people_number=findViewById(R.id.textview_people_number);

        ImageButton plus=findViewById(R.id.textview_plus);
        ImageButton minus=findViewById(R.id.textview_minus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //한 팀당 인원은 10명까지
                if(people_number<10){
                    people_number++;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    // Update UI elements
                                    textView_people_number.setText(Integer.toString(people_number));
                                }
                            });
                        }
                    }).start();
                }


                //textView_people_number.setText(people_number);
            }
        });


        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //한 팀당 인원은 1명 이상이어야 함
                if(people_number>1){
                    people_number--;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    // Update UI elements
                                    textView_people_number.setText(Integer.toString(people_number));
                                }
                            });
                        }
                    }).start();
                }



            }
        });

        button=findViewById(R.id.detail_restaurant_button);
        //줄서기 버튼 클릭
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //확인 버튼 누르면 서버 db에 식당 주소 정보 저장
                //db 저장 요청
                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            //식당 주소 테이블 id(key)값 가져오기-intent로 다음 액티비티에 전달
                            //final int addr_id=jsonResponse.getInt("addr_id");
                            //Log.d("test_addr_id", String.valueOf(addr_id));
                            if(success){//사용할 수 있는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(DetailRestaurantActivity.this);

                                builder.setMessage("대기표가 발급되었습니다");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //가게 주소가 등록 완료 돼야 다음 과정으로 넘어감
                                        /*
                                        Intent intent=new Intent(OfflineActivity.this, AddRestaurant_Info_Activity.class);
                                        intent.putExtra("addr_id", addr_id); //restaurant_addr 테이블의 id값 넘겨주기
                                        startActivity(intent);*/
                                        finish();
                                    }
                                });

                                builder.show();
                                /*
                                dialog = builder.setMessage("가게 주소가 등록되었습니다")
                                        .setPositiveButton("OK", null)
                                        .create();
                                dialog.show();*/

                            }else{//사용할 수 없는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(DetailRestaurantActivity.this);
                                dialog = builder.setMessage("Register fail")
                                        .setNegativeButton("OK", null)
                                        .create();
                                dialog.show();
                            }

                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };//Response.Listener 완료
                String userPhone=pref.getString("userPhone", null);
                int user_id=pref.getInt("key_id", -1);
                String user_userID=pref.getString("user_id", null);
                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                //owner_id, 전화번호, 일행 수
                AddWaitingRequest saveRequest =
                        new AddWaitingRequest(Integer.toString(restaurant.getOwner_id()),Integer.toString(user_id),user_userID,userPhone, Integer.toString(people_number), responseListener);
                RequestQueue queue = Volley.newRequestQueue(DetailRestaurantActivity.this);
                queue.add(saveRequest);
                //db 저장 요청


            }
        });//button클릭
    }//onCreate

    //툴바 버튼 추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_restaurant_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_chat) {
            Intent intent=new Intent(DetailRestaurantActivity.this, ChatTestActivity.class);
            //Intent intent=new Intent(DetailRestaurantActivity.this, ChatWithOwnerActivity.class);
            intent.putExtra("owner_id", owner_id);
            intent.putExtra("restaurant_name", restaurant.getName());
            startActivity(intent);
            return true;
        }
        if(id==R.id.action_map){
            Intent intent=new Intent(DetailRestaurantActivity.this, MapRestaurantActivity.class);
            intent.putExtra(KEY_SIMPLE_DATA, restaurantInfo);

            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }//툴바 버튼 추가



    public class GetRestaurantInfoRequest extends StringRequest {

        final static private String URL = "http://13.125.147.57/waiting_shuttle/RestaurantInfo/getRestaurantInfo.php";
        private Map<String, String> parameters;
        //owner_id, 전화번호, 일행 수
        public GetRestaurantInfoRequest(String owner_id, Response.Listener<String> listener){
            super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
            parameters = new HashMap<>();
            parameters.put("owner_id", owner_id);


        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return parameters;
        }
    }
}
