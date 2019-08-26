package com.example.watingshuttleformanager.register_restaurant_info;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.watingshuttleformanager.R;
import com.example.watingshuttleformanager.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import gun0912.tedbottompicker.TedBottomPicker;

public class AddRestaurant_Info_Activity extends AppCompatActivity {
    EditText editText_name;
    EditText editText_kind;
    EditText editText_tel;
    EditText editText_time;
    EditText editText_breaktime;
    EditText editText_day_off;
    EditText editText_price;
    ImageView imageView;
    Button button;
    Button button2;
    Bitmap bitmap;
    String login_session;
    private AlertDialog dialog;

    String tags="tags";
    String tags2="tags";

    //사용자 SharedPreference login_session
    private String login_session_user_id;
    private int login_session_key_id;
    private Boolean login_session_is_owner;
    //intent 값-식당 주소 테이블의 id 값
    private int addr_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        //Intent addr_id 넘겨받기 (주소 테이블의 id 값)
        Intent get_addr_id=getIntent();
        addr_id=get_addr_id.getIntExtra("addr_id", -1);
        Log.d("test_addr_id2", String.valueOf(addr_id));
        editText_name=findViewById(R.id.AddRestaurant_Info_Activity_edittext1);
        editText_kind=findViewById(R.id.AddRestaurant_Info_Activity_edittext2);
        editText_tel=findViewById(R.id.AddRestaurant_Info_Activity_edittext3);
        editText_time=findViewById(R.id.AddRestaurant_Info_Activity_edittext4);
        editText_day_off=findViewById(R.id.AddRestaurant_Info_Activity_edittext5);
        editText_breaktime=findViewById(R.id.AddRestaurant_Info_Activity_edittext6);

        editText_price=findViewById(R.id.AddRestaurant_Info_Activity_edittext7);
        button=findViewById(R.id.AddRestaurant_Info_Activity_button_ok);
        button2=findViewById(R.id.AddRestaurant_Info_Activity_button_cancel);
        imageView=findViewById(R.id.add_image);

        //사용자 정보 SharedPreference 가져오기
        SharedPreferences pref=getSharedPreferences("login_session", MODE_PRIVATE);
        login_session_user_id=pref.getString("user_id", null);
        login_session_key_id=pref.getInt("key_id", -1);
        login_session_is_owner=pref.getBoolean("is_owner", false);

        //checking the permission
        //if the permission is not given we will open setting to add permission
        //else app will not open
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }


        //adding click listener to button
        findViewById(R.id.add_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(AddRestaurant_Info_Activity.this)

                        .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {
                                // here is selected uri
                                Log.v("uri", String.valueOf(uri));
                                imageView.setImageURI(uri);



                                //getting the image Uri
                                Uri imageUri = uri;
                                try {
                                    //getting bitmap object from uri
                                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                                    //displaying selected image to imageview
                                    imageView.setImageBitmap(bitmap);

                                    //calling the method uploadBitmap to upload image
                                    uploadBitmap(bitmap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .create();

                tedBottomPicker.show(getSupportFragmentManager());

            }
        });
        //맨 하단 확인 버튼 클릭
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText_name.getText().toString().trim().isEmpty()) {
                    editText_name.setError("Enter tags first");
                    editText_name.requestFocus();
                    return;
                }

                final String name=editText_name.getText().toString().trim();
                final String kind=editText_kind.getText().toString().trim();
                final String tel=editText_tel.getText().toString().trim();
                final String time=editText_time.getText().toString().trim();
                final String breaktime=editText_breaktime.getText().toString().trim();
                final String day_off=editText_day_off.getText().toString().trim();
                final String price=editText_price.getText().toString().trim();
                //가게정보를 등록하시겠습니까 alertdialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AddRestaurant_Info_Activity.this);

                builder.setMessage("가게 정보를 등록 하시겠습니까?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //확인 버튼 누르면 서버 db에 식당 상세 정보 저장
                        //db 저장 요청
                        Response.Listener<String> responseListener = new Response.Listener<String>(){

                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonResponse = new JSONObject(response);
                                    //boolean success = jsonResponse.getBoolean("success");
                                    boolean success = jsonResponse.getBoolean("success");
                                    //식당 주소 테이블 id(key)값 가져오기-intent로 다음 액티비티에 전달
                                    //final int addr_id=jsonResponse.getInt("addr_id");
                                    Log.d("test_add_info", String.valueOf(addr_id));
                                    if(success){//저장되었다면

                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddRestaurant_Info_Activity.this);

                                        builder.setMessage("가게 정보가 등록되었습니다");
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //가게 정보가 등록 완료 돼야 다음 과정으로 넘어감
                                                Intent intent=new Intent(AddRestaurant_Info_Activity.this, MainActivity.class);

                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                                        builder.show();


                                    }else{//저장에 실패하면
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddRestaurant_Info_Activity.this);
                                        AlertDialog dialog2 = builder.setMessage("Register fail")
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

                        //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                        //SaveAddressRequest(String owner_id, String address, String longitude, String latitude, String owner_userID, Response.Listener<String> listener)
                        SaveInfoRequest saveRequest =
                                new SaveInfoRequest(Integer.toString(addr_id), Integer.toString(login_session_key_id),
                                        name, kind, tel, time, breaktime, day_off,price, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(AddRestaurant_Info_Activity.this);
                        queue.add(saveRequest);
                        //db 저장 요청
                    }
                });

                builder.show();


            }
        });
    }//onCreate


    /*
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using PNG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        //getting the tag from the edittext
        //final String tags;
        //final String tags2;

            //tags =editText_name.getText().toString().trim();
            //tags2 = editText_address.getText().toString().trim();


        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.UPLOAD_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            //JSONObject obj = new JSONObject(new String(response.data));
                            //JSONObject obj =new JSONObject(new String(response.data, "UTF-8")); //test
                            JSONObject obj =new JSONObject(new String(response.data)); //default

                            Log.d("test_response", "test_response");
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(getApplicationContext(), "JSONException e", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //String 으로 보내는 값 owner_id, addr_id, owner_userID
                params.put("name", tags);
                params.put("address", tags2);
                params.put("owner_id", Integer.toString(login_session_key_id));
                Log.d("param1", Integer.toString(login_session_key_id));
                params.put("addr_id", Integer.toString(addr_id));
                Log.d("param2", Integer.toString(addr_id));
                params.put("owner_userID", login_session_user_id);
                Log.d("param3", login_session_user_id);

                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    } //uploadbitmap()




}
