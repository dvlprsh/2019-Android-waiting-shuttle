package com.example.watingshuttleformanager.register_restaurant_address;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.watingshuttleformanager.R;
import com.example.watingshuttleformanager.login.RegisterActivity;
import com.example.watingshuttleformanager.login.RegisterRequest;
import com.example.watingshuttleformanager.register_restaurant_info.AddRestaurant_Info_Activity;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONObject;


public class AddRestaurant_Address_Activity extends AppCompatActivity {

    static final int SHOW_WEBVIEW=3000; //intent request code

    private TextView result; //주소를 넣을 textview
    private AlertDialog dialog;
    private Button button; //주소 검색 버튼
    private MapView mapView; //다음 지도


    //주소
    String address;
    String longitude;
    String latitude;
    //로그인한 사용자 정보
    private String login_session_user_id;
    private int login_session_key_id;
    //하단 확인, 취소 버튼
    private Button button_ok;
    private Button button_cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_retaurant_address);

        result = (TextView) findViewById(R.id.firstActivity2_textview_address);
        button=findViewById(R.id.firstActivity2_button); //주소 검색 버튼
        button_ok=findViewById(R.id.AddRestaurantAddressActivity_button_ok); //하단 확인 버튼
        button_cancel=findViewById(R.id.AddRestaurantAddressActivity_button_cancel); //하단 취소 버튼

        //login정보
        SharedPreferences pref=getSharedPreferences("login_session", MODE_PRIVATE);
        login_session_user_id=pref.getString("user_id", null);
        login_session_key_id=pref.getInt("key_id", -1);
        //login정보

        //지도 초기화

        mapView = new MapView(this);
        mapView.setDaumMapApiKey("af545ad23d71af75b7f67ecbdd912f63");


        //지도가 들어갈 RelativeLayout
        RelativeLayout mapViewContainer = (RelativeLayout) findViewById(R.id.firstActivity2_map_view);
        mapViewContainer.addView(mapView);

        //사용자가 설정한 주소 좌표로 지도 중심점, 마커 갱신
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddRestaurant_Address_Activity.this, WebviewActivity.class);
                startActivityForResult(intent, SHOW_WEBVIEW);
            }
        });

        button_ok.setOnClickListener(new View.OnClickListener() {
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
                            final int addr_id=jsonResponse.getInt("addr_id");
                            Log.d("test_addr_id", String.valueOf(addr_id));
                            if(success){//사용할 수 있는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddRestaurant_Address_Activity.this);

                                builder.setMessage("가게 주소가 등록되었습니다");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //가게 주소가 등록 완료 돼야 다음 과정으로 넘어감
                                        Intent intent=new Intent(AddRestaurant_Address_Activity.this, AddRestaurant_Info_Activity.class);
                                        intent.putExtra("addr_id", addr_id); //restaurant_addr 테이블의 id값 넘겨주기
                                        startActivity(intent);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddRestaurant_Address_Activity.this);
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
                //SaveAddressRequest(String owner_id, String address, String longitude, String latitude, String owner_userID, Response.Listener<String> listener)
                SaveAddressRequest saveRequest =
                        new SaveAddressRequest(Integer.toString(login_session_key_id), address, longitude, latitude, login_session_user_id, responseListener);
                RequestQueue queue = Volley.newRequestQueue(AddRestaurant_Address_Activity.this);
                queue.add(saveRequest);
                //db 저장 요청

                //startActivity(new Intent(AddRestaurant_Address_Activity.this, AddRestaurant_Info_Activity.class));
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //도로명 주소 선택 후 확인버튼->주소, 좌표값 intent 로 넘겨받기
        if(resultCode==RESULT_OK && requestCode== SHOW_WEBVIEW){
            address=data.getStringExtra("address");
            longitude=data.getStringExtra("longitude");
            latitude=data.getStringExtra("latitude");
            result.setText(address);

            //선택한 정보로 지도 갱신
            //daum.maps.LatLng(latitude, longitude)

            MapPoint mapPoint=MapPoint.mapPointWithGeoCoord(Double.parseDouble(latitude), Double.parseDouble(longitude));

            // 중심점 변경
            mapView.setMapCenterPoint(mapPoint, true);


            // 줌 레벨 변경
            mapView.setZoomLevel(2, true);
            // 줌 인
            mapView.zoomIn(true);
            //지도 마커 추가

            MapPOIItem marker = new MapPOIItem();

            marker.setItemName("가게 위치");

            marker.setTag(0);

            marker.setMapPoint(mapPoint);

            //marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            //marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.

            marker.setCustomImageResourceId(R.drawable.placeholde1); // 마커 이미지.
            marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.

           marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

            mapView.addPOIItem(marker);


        }
    }

}