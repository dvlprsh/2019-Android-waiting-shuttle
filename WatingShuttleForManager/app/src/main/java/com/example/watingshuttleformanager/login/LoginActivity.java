package com.example.watingshuttleformanager.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.watingshuttleformanager.NavigationActivity;
import com.example.watingshuttleformanager.list_test.ListActivity;
import com.example.watingshuttleformanager.register_restaurant_address.HomeActivity;
import com.example.watingshuttleformanager.R;
import com.example.watingshuttleformanager.register_restaurant_address.AddRestaurant_Address_Activity;

import org.json.JSONObject;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.maps.helper.Utility.getPackageInfo;

public class LoginActivity extends AppCompatActivity {

//로그인 성공하면 sharedpreference에 아이디 저장


    private AlertDialog dialog;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        //키해시 구하기
        String keyhash=getKeyHash(this);
        Log.d("mykeyhash", keyhash);
        //키해시 구하기
        ImageView background=findViewById(R.id.loginActivity_background); //배경 이미지
        //배경 이미지 glide 로 부착
        RequestOptions options = new RequestOptions();
        options.centerCrop();

        Glide.with(this).load(R.drawable.wallpaper).apply(options).into(background);



        TextView registerButton = (TextView)findViewById(R.id.registerButton);


        //버튼이 눌리면 RegisterActivity로 가게함

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                Intent registerIntent = new Intent(LoginActivity.this, ListActivity.class);

                LoginActivity.this.startActivity(registerIntent);

            }

        });



        final EditText idText = (EditText) findViewById(R.id.idText);

        final EditText passwordText = (EditText) findViewById(R.id.passwordText);

        final Button loginButton = (Button)findViewById(R.id.loginButton);



        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                final String userID = idText.getText().toString();

                String userPassword = passwordText.getText().toString();



                Response.Listener<String> responseLisner = new Response.Listener<String>(){



                    @Override

                    public void onResponse(String response) {

                        try{

                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");



                            if(success){

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                                //로그인 정보 sharedpreference 에 저장
                                String getId=jsonResponse.getString("userID"); //사용자의 아이디
                                int id=jsonResponse.getInt("id"); //primary key(auto increment)
                                //제휴 회원 여부 가져오기
                                boolean is_owner=jsonResponse.getBoolean("is_owner");
                                Log.d("test_is_owenr", String.valueOf(is_owner));
                                Log.d("test_id", getId);
                                Log.d("key_id", String.valueOf(id));
                                SharedPreferences pref=getSharedPreferences("login_session", MODE_PRIVATE);
                                SharedPreferences.Editor editor=pref.edit();
                                editor.putString("user_id", getId);
                                editor.putInt("key_id", id);
                                editor.putBoolean("is_owner", is_owner);
                                editor.commit();
                                Log.d("test_id2", pref.getString("user_id", null));
                                Log.d("test_key_id", Integer.toString(pref.getInt("key_id", 0)));

                                dialog = builder.setMessage("로그인에 성공했습니다")

                                        .setPositiveButton("확인", null)

                                        .create();

                                dialog.show();
                                //is_owner==true 이면 main
                                if(is_owner){
                                    Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);

                                    LoginActivity.this.startActivity(intent);

                                    finish();
                                }else{
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                                    LoginActivity.this.startActivity(intent);

                                    finish();
                                }


                            }else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

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



                LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseLisner);

                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

                queue.add(loginRequest);



            }

        });



    }//onCreate



    @Override

    protected void onStop(){

        super.onStop();

        if(dialog != null){//다이얼로그가 켜져있을때 함부로 종료가 되지 않게함

            dialog.dismiss();

            dialog = null;

        }

    }

    public void UTFtest(View view) {

        startActivity(new Intent(LoginActivity.this, AddRestaurant_Address_Activity.class));
    }

//키 해시 구하기 for kakao api 사용
public static String getKeyHash(final Context context) {
    PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
    if (packageInfo == null)
        return null;

    for (Signature signature : packageInfo.signatures) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            Log.w("TAG", "Unable to get MessageDigest. signature=" + signature, e);
        }
    }
    return null;
}//키 해시 구하기 for kakao api 사용



}