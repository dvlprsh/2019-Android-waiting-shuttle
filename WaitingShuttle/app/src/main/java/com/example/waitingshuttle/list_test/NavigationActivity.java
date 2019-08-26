package com.example.waitingshuttle.list_test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.waitingshuttle.profile.ProfileActivity;
import com.example.waitingshuttle.R;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
   // MapView mapView;
    double longitude;
    double latitude;

    private TextView textView_location;
    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work
    private static final String URL_PRODUCTS = "http://13.125.147.57/waiting_shuttle/RestaurantInfo/get_list_restaurant.php";
    private Restaurant restaurant;
    public static final String KEY_SIMPLE_DATA = "data";
    //a list to store all the products
    List<Restaurant> restaurantList;
    RestaurantsAdapter adapter;
    //the recyclerview
    RecyclerView recyclerView;
    Button button;
    LocationManager manager;//위치정보

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView_location = findViewById(R.id.navigation_textView);
//위치 test

       // mapView = new MapView(this);
        //mapView.setDaumMapApiKey("dd3b5d35d09681aea177a50ef1e0bdce");
        //ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.navigation_mapView);
        //mapViewContainer.addView(mapView);



        //MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);

        // 중심점 변경
        //mapView.setMapCenterPoint(mapPoint, true);


        // 줌 레벨 변경
        //mapView.setZoomLevel(2, true);
        // 줌 인
        //mapView.zoomIn(true);
        startLocationService();

        // 출처: https://bottlecok.tistory.com/54 [잡캐의 무한도전기]
        //위치 테스트

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //리사이클러뷰
        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.navigation_recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantList = new ArrayList<>();

        //creating adapter object and setting it to recyclerview
        //RestaurantsAdapter adapter = new RestaurantsAdapter(NavigationActivity.this, restaurantList);
        adapter = new RestaurantsAdapter(NavigationActivity.this, restaurantList);
        recyclerView.setAdapter(adapter);
        loadProducts();
        //리사이클러뷰
        ImageButton imageButton=findViewById(R.id.navigation_imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //위치 갱신
                startLocationService();

            }
        });




    }//onCreate

    private void loadProducts() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            restaurantList.clear();
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject jsonObject = array.getJSONObject(i);
                                Log.d("list_t", jsonObject.toString());
                                //adding the product to product list
                                //  public Restaurant(int owner_id, String image, String name, String kind, String address,int waiting) {

                                restaurantList.add(new Restaurant(
                                        //Product(String title, String image, String owner_userID) {
                                        jsonObject.getInt("owner_id"),
                                        jsonObject.getString("image"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("kind"),
                                        jsonObject.getString("address"),
                                        jsonObject.getInt("waiting") //현재 대기자 수
                                        //product.getDouble("price"),
                                        //product.getString("image")
                                ));
                            }
                            adapter.notifyDataSetChanged(); //test
/*
                            //creating adapter object and setting it to recyclerview
                            RestaurantsAdapter adapter = new RestaurantsAdapter(NavigationActivity.this, restaurantList);
                            recyclerView.setAdapter(adapter);*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            loadProducts();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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
            Log.d("위치 퍼미션 test", "퍼미션 nono");

            return;
        }
        Location location = getLastKnownLocation2();
        if (location != null) {
            String msg = "Last known" + location.getLatitude() +"//"+ location.getLongitude();
            latitude=location.getLatitude();
            longitude=location.getLongitude();
            Log.e("SampleLocation", msg);
            //textView_location.setText(msg);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

        }
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);

        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
        float minDistance = 0;
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);

        Toast.makeText(getApplicationContext(), "Location Service started", Toast.LENGTH_SHORT).show();

        MapReverseGeoCoder.ReverseGeoCodingResultListener reverseGeoCodingResultListener=new MapReverseGeoCoder.ReverseGeoCodingResultListener() {
            @Override
            public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
                Log.d("geocode_test", s);
                textView_location.setText(s);
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
            Log.d("GPSListener", msg);
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
