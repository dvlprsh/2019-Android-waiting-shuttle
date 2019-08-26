package com.example.waitingshuttle.profile;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.waitingshuttle.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import gun0912.tedbottompicker.TedBottomPicker;

public class ProfileActivity extends AppCompatActivity {

    ImageView imageView;
    Button button_cancel;
    Button button_ok;
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
        setContentView(R.layout.activity_profile);
        //Intent addr_id 넘겨받기 (주소 테이블의 id 값)
        Intent get_addr_id=getIntent();
        addr_id=get_addr_id.getIntExtra("addr_id", -1);
        Log.d("test_addr_id2", String.valueOf(addr_id));

        button_cancel=findViewById(R.id.profile_activity_button_cancel);
        button_ok=findViewById(R.id.profile_activity_button_ok);
        imageView=findViewById(R.id.profile_activity_imageView);

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
        findViewById(R.id.profile_activity_imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(ProfileActivity.this)

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
