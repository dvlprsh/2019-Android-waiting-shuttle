package com.example.watingshuttleformanager.register_restaurant_address;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.example.watingshuttleformanager.R;

public class WebviewActivity extends AppCompatActivity {
    private WebView webView;
    private TextView result;
    private Handler handler;
    //Intent로 firstActivity2에 보낼 주소, 위도, 경도
    private String address;
    private String longitude;
    private String latitude;
    //참고 daum.maps.LatLng(latitude, longitude)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        result = (TextView) findViewById(R.id.WebviewActivity_result);


        Button button_ok=findViewById(R.id.webViewActivity_button_ok);
        Button button_cancel=findViewById(R.id.webViewActivity_button_cancel);

        //취소 버튼 누르면 이전 화면으로 돌아감
        button_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //확인 버튼 누르면 intent 결과로 주소와 좌표 넘겨주고 finish
                Intent resultIntent=new Intent();
                resultIntent.putExtra("address", address);
                resultIntent.putExtra("longitude", longitude);
                resultIntent.putExtra("latitude", latitude);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        // WebView 초기화
        init_webView();

        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();



    }//onCreate()


    public void init_webView() {
        // WebView 설정
        webView = (WebView) findViewById(R.id.WebviewActivity_webView);
        // JavaScript 허용
        webView.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new WebChromeClient());
        // webview url load
        webView.loadUrl("http://13.125.147.57/getAddress.php");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("test", "sdfdfddf");
                    result.setText(String.format("(%s) %s %s", arg1, arg2, arg3));

                    address=arg2+" "+arg3;

                    Log.d("address", address);
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    //init_webView();
                }
            });
        }

        @JavascriptInterface
        public void setGeocode(final String latitude1, final String longitude1){
            handler.post(new Runnable() {
                @Override
                public void run() {
                   longitude=longitude1;
                    latitude=latitude1;
                    //daum.maps.LatLng(latitude, longitude)
                    Log.d("geocode", longitude+latitude);
                    init_webView();

                }
            });


        }


    }
}