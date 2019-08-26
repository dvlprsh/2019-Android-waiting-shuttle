package com.example.waitingshuttle.list_test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waitingshuttle.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.util.List;

public class MapRestaurantActivity extends AppCompatActivity {
    public static final String KEY_SIMPLE_DATA="data";
    private MapView mapView; //다음 지도
    private RestaurantInfo restaurantInfo;
    private TextView textView_address;
    private TextView textView_tel;
    private String latitude_string;
    private String longitude_string;
    LocationManager manager;//위치정보
    double longitude;
    double latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_restaurant);

        mapView = new MapView(this);
        mapView.setDaumMapApiKey("dd3b5d35d09681aea177a50ef1e0bdce");
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_restaurant_activity_map_view);
        mapViewContainer.addView(mapView);
        //툴바
        Toolbar toolbar=(Toolbar)findViewById(R.id.map_restaurant_activity_toolbar);
        setSupportActionBar(toolbar);

        //툴바
        //TextView findViewByID
        textView_address=findViewById(R.id.map_restaurant_activity_textView_address);
         textView_tel=findViewById(R.id.map_restaurant_activity_textView_tel);
        //TextView findViewByID

        //인텐트로 식당정보 넘겨받기
        Intent intent=getIntent();
        if(intent!=null){
            Bundle bundle=intent.getExtras();
            restaurantInfo=(RestaurantInfo) bundle.getParcelable(KEY_SIMPLE_DATA);

            textView_address.setText(restaurantInfo.getAddress());
            textView_tel.setText(restaurantInfo.getTel());
            getSupportActionBar().setTitle(restaurantInfo.getName());


            //선택한 정보로 지도 갱신
            latitude_string=restaurantInfo.getLatitude();
            longitude_string=restaurantInfo.getLongitude();
            //daum.maps.LatLng(latitude, longitude)

            MapPoint mapPoint=MapPoint.mapPointWithGeoCoord(Double.parseDouble(latitude_string), Double.parseDouble(longitude_string));

            // 중심점 변경
            mapView.setMapCenterPoint(mapPoint, true);


            // 줌 레벨 변경
            mapView.setZoomLevel(2, true);
            // 줌 인
            mapView.zoomIn(true);
            //지도 마커 추가

            MapPOIItem marker = new MapPOIItem();

            marker.setItemName(restaurantInfo.getName());

            marker.setTag(0);

            marker.setMapPoint(mapPoint);

            //marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            //marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.

            marker.setCustomImageResourceId(R.drawable.placeholde1); // 마커 이미지.
            marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.

            marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

            mapView.addPOIItem(marker);

        }//if(intent!=null) //식당정보 인텐트로 넘겨받기
        startLocationService();
    }//onCreate
    private void startLocationService() {
        //LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = getLastKnownLocation2();
        if (location != null) {
            String msg = "Last known" + location.getLatitude() +"//"+ location.getLongitude();
            latitude=location.getLatitude();
            longitude=location.getLongitude();
            Log.i("SampleLocation", msg);
            //textView_location.setText(msg);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

        }
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
/*
        // 중심점 변경
        mapView.setMapCenterPoint(mapPoint, true);


        // 줌 레벨 변경
        mapView.setZoomLevel(2, true);
        // 줌 인
        mapView.zoomIn(true);*/
        //지도 마커 추가


        //MapPoint MARKER_POINT=MapPoint.mapPointWithGeoCoord(37.5383764, 127.1363963);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("현재 위치");
        marker.setTag(0);
        marker.setMapPoint(mapPoint);

        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);

        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
        float minDistance = 0;
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);

        Toast.makeText(getApplicationContext(), "Location Service started", Toast.LENGTH_SHORT).show();

        MapReverseGeoCoder.ReverseGeoCodingResultListener reverseGeoCodingResultListener=new MapReverseGeoCoder.ReverseGeoCodingResultListener() {
            @Override
            public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
                Log.d("geocode_test", s);
                //textView_location.setText(s);
            }

            @Override
            public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
                Log.d("fuck", "fuck");
            }
        };
        MapReverseGeoCoder reverseGeoCoder = new MapReverseGeoCoder("dd3b5d35d09681aea177a50ef1e0bdce", mapPoint, reverseGeoCodingResultListener, this);
        reverseGeoCoder.startFindingAddress();


    }

    private class GPSListener implements LocationListener {

        public void onLocationChanged(Location location) {
            Double latitude2 = location.getLatitude();
            Double longitude2 = location.getLongitude();

            String msg = "Latitude : " + latitude2 + "\nLongitude : " + longitude2;
            Log.i("GPSListener", msg);
            latitude=location.getLatitude();
            longitude=location.getLongitude();
            //textView_location.setText("내 위치 " + latitude2 + ", " + longitude2);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        public void onProviderDisabled(String provider) {

        }
    }

    private Location getLastKnownLocation2() {
        manager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = manager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return bestLocation;
            }
            Location l = manager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }




}
