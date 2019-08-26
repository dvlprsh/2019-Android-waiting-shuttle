package com.example.watingshuttleformanager.mode;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.watingshuttleformanager.NavigationActivity;
import com.example.watingshuttleformanager.R;
import com.example.watingshuttleformanager.register_restaurant_address.AddRestaurant_Address_Activity;
import com.example.watingshuttleformanager.register_restaurant_address.SaveAddressRequest;
import com.example.watingshuttleformanager.register_restaurant_info.AddRestaurant_Info_Activity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class OfflineActivity extends AppCompatActivity {
    private AlertDialog dialog;
    private TextView textView_people_number;
    private TextView textView_crew_number; //일행 수
    private EditText editText;
    private int people_number=1;
    private Button button_waiting;
    //현재 대기 팀수
    private int how_many_waiting;

    private int owner_id;
private Button imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //현재 로그인한 사장 계정 user_id 넘겨주기
        SharedPreferences pref=getSharedPreferences("login_session", MODE_PRIVATE);
        owner_id=pref.getInt("key_id", -1);
        Log.d("session_test", Integer.toString(owner_id));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        editText=findViewById(R.id.edittext);
        editText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        textView_crew_number=findViewById(R.id.textview_crew_number); //일행수
imageView=findViewById(R.id.offline_activity_imageview);
        Button number1=findViewById(R.id.number_1);
        Button number2=findViewById(R.id.number_2);
        Button number3=findViewById(R.id.number_3);
        Button number4=findViewById(R.id.number_4);
        Button number5=findViewById(R.id.number_5);
        Button number6=findViewById(R.id.number_6);
        Button number7=findViewById(R.id.number_7);
        Button number8=findViewById(R.id.number_8);
        Button number9=findViewById(R.id.number_9);
        Button number0=findViewById(R.id.number_0);
imageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        loadProducts();
    }
});
        button_waiting=findViewById(R.id.button_waiting);
        number1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
                editText.append("1");
            }
        });
        number2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
                editText.append("2");
            }
        });
        number3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
                editText.append("3");
            }
        });
        number4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
                editText.append("4");
            }
        });
        number5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
                editText.append("5");
            }
        });
        number6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
                editText.append("6");
            }
        });
        number7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
                editText.append("7");
            }
        });
        number8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
                editText.append("8");
            }
        });
        number9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
                editText.append("9");
            }
        });
        number0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
                editText.append("0");
            }
        });


        Button button_back=findViewById(R.id.button_backspace);

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText() !=null){
                    String text = editText.getText().toString();
                    if(text.length() >0){
                        editText.setText(text.substring(0, text.length() - 1));

                        editText.setSelection(editText.length());


                    }
                }

                //editText.setText(text.substring(0, text.length() - 1));

            }
        });



        textView_people_number=findViewById(R.id.offline_activity_textview);

        Button plus=findViewById(R.id.textview_plus);
        Button minus=findViewById(R.id.textview_minus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //한 팀당 인원은 10명까지
                if(people_number<10){
                    people_number++;
                    textView_crew_number.setText(Integer.toString(people_number));
                }



            }
        });


        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //한 팀당 인원은 1명 이상이어야 함
                if(people_number>1){
                    people_number--;

                    textView_crew_number.setText(Integer.toString(people_number));
                }



            }
        });

        //줄서기 버튼 클릭
        button_waiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //확인 버튼 누르면 서버 db에 식당 주소 정보 저장
                //db 저장 요청
                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            Log.d("waiting_test", jsonResponse.toString());
                            boolean success = jsonResponse.getBoolean("success");
                            int waiting=jsonResponse.getInt("waiting");
                            Log.d("waiting_test", Integer.toString(waiting));
                            //식당 주소 테이블 id(key)값 가져오기-intent로 다음 액티비티에 전달
                            //final int addr_id=jsonResponse.getInt("addr_id");
                            //Log.d("test_addr_id", String.valueOf(addr_id));
                            if(success){//사용할 수 있는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(OfflineActivity.this);

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
                                AlertDialog.Builder builder = new AlertDialog.Builder(OfflineActivity.this);
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

                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                //owner_id, 전화번호, 일행 수
                AddOfflineWaitingRequest saveRequest =
                        new AddOfflineWaitingRequest(Integer.toString(owner_id), editText.getText().toString().trim(), Integer.toString(people_number), responseListener);
                RequestQueue queue = Volley.newRequestQueue(OfflineActivity.this);
                queue.add(saveRequest);
                //db 저장 요청


            }
        });//button클릭
        loadProducts();

    }//onCreate

    private void loadProducts() {

        //확인 버튼 누르면 서버 db에 식당 주소 정보 저장
        //db 저장 요청
        Response.Listener<String> responseListener = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try{

//converting the string to json array object
                    JSONArray array = new JSONArray(response);
                    how_many_waiting=array.length();
                    //맨위 현재 대기 현황 텍스트 갱신
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    // Update UI elements
                                    textView_people_number.setText(Integer.toString(how_many_waiting));
                                }
                            });
                        }
                    }).start();


                    //traversing through all the object



                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

        };
        GetWaitingListRequest saveRequest =
                new GetWaitingListRequest(Integer.toString(owner_id), responseListener);
        RequestQueue queue = Volley.newRequestQueue(OfflineActivity.this);
        queue.add(saveRequest);
    }//loadproducts()


}
