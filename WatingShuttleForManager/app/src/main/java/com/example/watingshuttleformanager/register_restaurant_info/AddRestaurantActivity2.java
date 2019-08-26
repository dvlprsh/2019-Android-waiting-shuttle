package com.example.watingshuttleformanager.register_restaurant_info;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
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

public class AddRestaurantActivity2 extends AppCompatActivity {
    EditText editText_name;
    EditText editText_address;
    ImageView imageView;
    Button button;
    Bitmap bitmap;
    String login_session;


String tags;
String tags2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        //임시 세션
        Intent intent2=getIntent();
        login_session=intent2.getExtras().getString("id");
        //임시 세션
        //editText_name=findViewById(R.id.add_restaurant_edittext_name);
        //editText_address=findViewById(R.id.add_restaurant_edittext_address);
        button=findViewById(R.id.AddRestaurant_Info_Activity_button_ok);

        imageView=findViewById(R.id.add_image);
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

                //if the tags edittext is empty
                //we will throw input error

                /*
                if (editText_name.getText().toString().trim().isEmpty()) {
                    editText_name.setError("Enter tags first");
                    editText_name.requestFocus();
                    return;
                } */
                /*
                //if everything is ok we will open image chooser
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
                */

                TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(AddRestaurantActivity2.this)

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
                                    /*
                                    //calling the method uploadBitmap to upload image
                                    uploadBitmap(bitmap); */
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .create();

                tedBottomPicker.show(getSupportFragmentManager());

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText_name.getText().toString().trim().isEmpty()) {
                    editText_name.setError("Enter tags first");
                    editText_name.requestFocus();
                    return;
                }


                if (editText_address.getText().toString().trim().isEmpty()) {
                    editText_address.setError("Enter tags first");
                    editText_address.requestFocus();
                    return;
                }
                uploadBitmap(bitmap);

                Intent intent=new Intent(AddRestaurantActivity2.this, MainActivity.class);
                intent.putExtra("name", editText_name.getText().toString().trim());
                intent.putExtra("id", login_session);
                startActivity(intent);
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

            tags =editText_name.getText().toString().trim();
            tags2 = editText_address.getText().toString().trim();


        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.UPLOAD_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            //JSONObject obj = new JSONObject(new String(response.data));
                            //JSONObject obj =new JSONObject(new String(response.data, "UTF-8")); //test
                            JSONObject obj =new JSONObject(new String(response.data)); //default


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
                params.put("name", tags);
                params.put("address", tags2);

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
