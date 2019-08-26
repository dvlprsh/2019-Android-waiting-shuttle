package com.example.watingshuttleformanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.watingshuttleformanager.chat.RoomListActivity;
import com.example.watingshuttleformanager.list_test.Product;
import com.example.watingshuttleformanager.list_test.ProductsAdapter;
import com.example.watingshuttleformanager.main.MainActivity;
import com.example.watingshuttleformanager.mode.AddOfflineWaitingRequest;
import com.example.watingshuttleformanager.mode.GetWaitingListRequest;
import com.example.watingshuttleformanager.mode.OfflineActivity;
import com.example.watingshuttleformanager.mode.WaitingProduct;
import com.example.watingshuttleformanager.mode.WaitingProductsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //현재 대기 팀수
    private int how_many_waiting;
    //리사이클러뷰

    private TextView textView_waiting; //현재 대기인원
    //a list to store all the products
    List<WaitingProduct> productList;
    private int owner_id;
    //the recyclerview
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //현재 로그인한 사장 계정 user_id 넘겨주기
        SharedPreferences pref=getSharedPreferences("login_session", MODE_PRIVATE);
        owner_id=pref.getInt("key_id", -1);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        textView_waiting=findViewById(R.id.navigation_textview_waiting);

        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.navigation_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the productlist
        //productList = new ArrayList<>();

        //this method will fetch and parse json
        //to display it in recyclerview
        loadProducts();
    }//onCreate

    private void loadProducts() {
        productList = new ArrayList<>();
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
                                    textView_waiting.setText(Integer.toString(how_many_waiting));
                                }
                            });
                        }
                    }).start();


                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting product object from json array
                        JSONObject product = array.getJSONObject(i);
// public WaitingProduct(int id, int owner_id, int user_id, String user_userID, String phone, String person_number) {
                        //adding the product to product list

                        Log.d("waiting_test", product.toString());
                            productList.add(new WaitingProduct(


                                    product.getString("user_userID"),
                                    product.getString("phone"),
                                    product.getString("person_number")

                            ));


                    }


                    //creating adapter object and setting it to recyclerview
                    WaitingProductsAdapter adapter = new WaitingProductsAdapter(NavigationActivity.this, productList);
                    recyclerView.setAdapter(adapter);

                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

    };
        GetWaitingListRequest saveRequest =
                new GetWaitingListRequest(Integer.toString(owner_id), responseListener);
        RequestQueue queue = Volley.newRequestQueue(NavigationActivity.this);
        queue.add(saveRequest);
    }//loadproducts()

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
            startActivity(new Intent(NavigationActivity.this, OfflineActivity.class));
            return true;
        }
        if(id==R.id.action_refresh){
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

        if (id == R.id.nav_rest_info) {
            // 가게 정보로 이동
            startActivity(new Intent(NavigationActivity.this, MainActivity.class));
        } else if (id == R.id.nav_chat) {
            startActivity(new Intent(NavigationActivity.this, RoomListActivity.class));
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
