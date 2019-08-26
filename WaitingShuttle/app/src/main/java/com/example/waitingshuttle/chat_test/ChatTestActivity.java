package com.example.waitingshuttle.chat_test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.waitingshuttle.chat.ChatListAdapter;
import com.example.waitingshuttle.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatTestActivity extends AppCompatActivity {
    private int curr_roomnumber; //현재 룸넘버
    private int owner_id; //인텐트로 식당 주인owner_id 넘겨받기
    private int user_id;
    private String user_userID;
    private String restaurant_name;
    private String last_message;
    private boolean is_room_exist=false;
    //recyclerview
    private ChatListAdapter mAdapter;
    private List<String> chatList;
    private RecyclerView.LayoutManager layoutManager;
    private int added_position;
    //recyclerview
    public static final int ROOM_NUMBER=1; //채팅방 넘버
    //private ChatRoom chatRoom; //현재 채팅방
    public static final String KEY_SIMPLE_DATA="data";
    private Handler mMainHandler;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private HandlerThread thread;
    private TCPClient client = null;

    private Socket socket;
    private BufferedReader networkReader = null;
    private BufferedWriter networkWriter = null;
    private Button connect;
    private Button finish;
    private Button start;
    private TextView text;
    private EditText editText_id;
    private String ip;
    private int port = 6000;
    private String TAG = "AndroidChattingClient";

    public static final int MSG_CONNECT = 1;
    public static final int MSG_STOP = 2;
    public static final int MSG_CLIENT_STOP = 3;
    public static final int MSG_SERVER_STOP = 4;
    public static final int MSG_START = 11;
    public static final int MSG_RECEIVE = 101;
    public static final int MSG_ERROR = 999;


    public static final int CHAT_START=5;
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (client != null) {
            /*
            Message msg = mServiceHandler.obtainMessage();
            msg.what = MSG_STOP;
            mServiceHandler.sendMessage(msg);*/

            Message msg = mServiceHandler.obtainMessage();
            msg.what = MSG_CLIENT_STOP;
            mServiceHandler.sendMessage(msg);

        }
        thread.quit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_owner);
        //로그인 세션
        /*SharedPreferences pref=getSharedPreferences("login_session", MODE_PRIVATE);
                                SharedPreferences.Editor editor=pref.edit();
                                editor.putString("user_id", getId);
                                editor.putString("userPhone", userPhone);
                                editor.putInt("key_id", id);
                                editor.putBoolean("is_owner", is_owner);*/
        SharedPreferences pref=getSharedPreferences("login_session", MODE_PRIVATE);
        user_id=pref.getInt("key_id", -1); //user_id(사용자 계정 번호)
        Log.d("user_id_test", Integer.toString(user_id));
        user_userID=pref.getString("user_id", null); //사용자 아이디
        //로그인 세션
        //editText_id = (EditText) findViewById(R.id.mainActivity_editText_id);

        final EditText et = (EditText) findViewById(R.id.editText3);
        //식당주인 owner_id 넘겨받기
        Intent intent=getIntent();
        if(intent!=null){
            //intent.putExtra("restaurant_name", restaurant.getName());
            owner_id=intent.getIntExtra("owner_id", -1);
            restaurant_name=intent.getStringExtra("restaurant_name");
            Log.d("intent_owner_id_test", Integer.toString(owner_id));
            Log.d("intent_owner_id_test2",restaurant_name);
        }
        //식당주인 owner_id 넘겨받기


        //roomnumber test
        TextView textView_roomnumber=findViewById(R.id.mainActivity_textView_roomnumber);
        //Intent intent=getIntent();
        //chatRoom=(ChatRoom) intent.getParcelableExtra(KEY_SIMPLE_DATA);
        //int curr_roomnumber=chatRoom.getRoomNumber();
        //Log.d("test_roomnumber", Integer.toString(curr_roomnumber));
        //textView_roomnumber.setText(Integer.toString(curr_roomnumber));
        connect = (Button) findViewById(R.id.button1);
        finish = (Button) findViewById(R.id.button2);
        start = (Button) findViewById(R.id.button3);
        text = (TextView) findViewById(R.id.textView1);

        //룸넘버 배정
        if(!is_room_exist){
            Response.Listener<String> responseListener = new Response.Listener<String>(){

                @Override
                public void onResponse(String response) {
                    try{

//converting the string to json array object
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("chat_json_test", jsonObject.toString());
                        //how_many_waiting=array.length();
                        //맨위 현재 대기 현황 텍스트 갱신
                        if(jsonObject.getBoolean("success")){
                            is_room_exist=true;
                            Log.d("room_test","확인 완료");
                            curr_roomnumber=jsonObject.getInt("room_number");


                            //ip = eip.getText().toString();
                            try {
                                port = 6000;
                            } catch (NumberFormatException e) {
                                port = 5000;
                                Log.d(TAG, "포트번호", e);
                            }

                            if (client == null) {
                                try {
                                    String ip=getString(R.string.ip);
                                    client = new TCPClient(ip, port);
                                    client.start();
                                } catch (RuntimeException e) {
                                    text.setText("IP 주소나 포트번호가 잘못되었습니다..");
                                    Log.d(TAG, "에러 발생", e);
                                }
                            }
                            //채팅 바로 접속 test

                        }



                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }

            };
            CreateChatRoomRequest chatRoomRequest =
                    new CreateChatRoomRequest(restaurant_name, Integer.toString(owner_id), Integer.toString(user_id), et.getText().toString(),user_userID,responseListener);
            RequestQueue queue = Volley.newRequestQueue(ChatTestActivity.this);
            queue.add(chatRoomRequest);
            //test
            Log.d("post_test", restaurant_name);
            Log.d("post_test2",  et.getText().toString());
        }
//룸 넘버 배정




//채팅 바로 접속 test
/*
        //ip = eip.getText().toString();
        try {
            port = 6000;
        } catch (NumberFormatException e) {
            port = 5000;
            Log.d(TAG, "포트번호", e);
        }

        if (client == null) {
            try {
                String ip=getString(R.string.ip);
                client = new TCPClient(ip, port);
                client.start();
            } catch (RuntimeException e) {
                text.setText("IP 주소나 포트번호가 잘못되었습니다..");
                Log.d(TAG, "에러 발생", e);
            }
        }
        //채팅 바로 접속 test*/
        //리사이클러뷰
        chatList = new ArrayList<>();
        RecyclerView recyclerView=findViewById(R.id.chatroom_recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ChatListAdapter(ChatTestActivity.this, chatList);
        recyclerView.setAdapter(mAdapter);



        added_position=chatList.size();
        Log.d("chatList size", Integer.toString(added_position));
        //리사이클러뷰
        connect.setEnabled(true);
        finish.setEnabled(false);
        start.setEnabled(false);

        thread = new HandlerThread("HandlerThread");
        thread.start();
        // 루퍼를 만든다.
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        mMainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String m;
                switch (msg.what) {
                    case CHAT_START:
                        m="채팅이 시작되었습니다";
                        text.setText(m + "\n");
                        break;

                    case MSG_CONNECT:
                        m = "정상적으로 서버에 접속하였습니다.";
                        connect.setEnabled(false);
                        finish.setEnabled(true);
                        start.setEnabled(true);
                        text.setText(m + "\n");
                        break;

                    case MSG_CLIENT_STOP:
                        text.setText((String) msg.obj + "\n");
                        connect.setEnabled(true);
                        finish.setEnabled(false);
                        start.setEnabled(false);
                        m = "클라이언트가 접속을 종료하였습니다.";
                        break;

                    case MSG_SERVER_STOP:
                        text.setText((String) msg.obj + "\n");
                        connect.setEnabled(true);
                        finish.setEnabled(false);
                        start.setEnabled(false);
                        m = "서버가 접속을 종료하였습니다.";
                        break;

                    case MSG_START:
                        m = "메세지 전송 완료!";
                        text.append((String) m+ "\n");
                        break;

                    case MSG_RECEIVE:
                        m = null;
                        text.append((String) msg.obj + "\n");
                        chatList.add((String) msg.obj);
                        Log.d("msg_receive",(String) msg.obj);
                        added_position++;
                        Log.d("size test",Integer.toString(chatList.size()));
                        mAdapter.notifyItemInserted(chatList.size()-1);
                        layoutManager.scrollToPosition(chatList.size()-1);
                        break;

                    default:
                        m = "에러 발생!";
                        text.append((String) msg.obj + "\n");
                        break;
                }
                if (m != null)
                    Toast.makeText(ChatTestActivity.this, m, Toast.LENGTH_SHORT).show();
                super.handleMessage(msg);
            }
        };

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                //ip = eip.getText().toString();
                try {
                    //port = Integer.parseInt(eport.getText().toString());
                } catch (NumberFormatException e) {
                    port = 5000;
                    Log.d(TAG, "포트번호", e);
                }

                if (client == null) {
                    try {
                        client = new TCPClient(ip, port);
                        client.start();
                    } catch (RuntimeException e) {
                        text.setText("IP 주소나 포트번호가 잘못되었습니다..");
                        Log.d(TAG, "에러 발생", e);
                    }
                }

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                if (client != null) {
                    Message msg = mServiceHandler.obtainMessage();
                    msg.what = MSG_CLIENT_STOP;
                    mServiceHandler.sendMessage(msg);
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                if (et.getText().toString() != null) {
                    //test
                    /*
                    if(!is_room_exist){
                        Response.Listener<String> responseListener = new Response.Listener<String>(){

                            @Override
                            public void onResponse(String response) {
                                try{

//converting the string to json array object
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("chat_json_test", jsonObject.toString());
                                    //how_many_waiting=array.length();
                                    //맨위 현재 대기 현황 텍스트 갱신
                                    if(jsonObject.getBoolean("success")){
                                        is_room_exist=true;
                                        Log.d("room_test","확인 완료");
                                    }



                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                            }

                        };
                        CreateChatRoomRequest chatRoomRequest =
                                new CreateChatRoomRequest(restaurant_name, Integer.toString(owner_id), Integer.toString(user_id), et.getText().toString(),user_userID,responseListener);
                        RequestQueue queue = Volley.newRequestQueue(ChatTestActivity.this);
                        queue.add(chatRoomRequest);
                        //test
                        Log.d("post_test", restaurant_name);
                        Log.d("post_test2",  et.getText().toString());
                    }*/


                    Message msg = mServiceHandler.obtainMessage();
                    msg.what = MSG_START;
                    JSONObject json_string=new JSONObject();
                    try{
                        json_string.put("status", "MSG_START");
                        json_string.put("message", et.getText().toString());
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    msg.obj =json_string.toString();
                    mServiceHandler.sendMessage(msg);

                    et.setText("");
                }
            }
        });
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHAT_START:
                    Message toMain = mMainHandler.obtainMessage();
                    try {
                        //BufferedWriter networkWriter;
                        Log.d("json_test22", Integer.toString(msg.what));
                        Log.d("json_test22", msg.obj.toString());
                        networkWriter.write((String)msg.obj);
                        networkWriter.newLine(); //줄바꿈
                        networkWriter.flush();
                        toMain.what = CHAT_START;
                    } catch (IOException e) {
                        toMain.what = MSG_ERROR;
                        Log.d(TAG, "에러 발생", e);
                    }
                    toMain.obj = msg.obj;
                    mMainHandler.sendMessage(toMain);
                    break;
                case MSG_START:
                    Message toMain2 = mMainHandler.obtainMessage();
                    try {
                        //BufferedWriter networkWriter;
                        networkWriter.write((String) msg.obj);
                        networkWriter.newLine(); //줄바꿈
                        networkWriter.flush();
                        toMain2.what = MSG_START;
                    } catch (IOException e) {
                        toMain2.what = MSG_ERROR;
                        Log.d(TAG, "에러 발생", e);
                    }
                    toMain2.obj = msg.obj;
                    mMainHandler.sendMessage(toMain2);
                    break;
                case MSG_STOP:
                case MSG_CLIENT_STOP:
                case MSG_SERVER_STOP:
                    client.quit();
                    client = null;
                    break;
            }
        }
    }

    public class TCPClient extends Thread {
        Boolean loop;
        SocketAddress socketAddress;
        String line;
        private final int connection_timeout = 3000;

        public TCPClient(String ip, int port) throws RuntimeException {
            socketAddress = new InetSocketAddress(ip, port);
        }

        @Override
        public void run() {
            try {
                socket = new Socket();
                socket.setSoTimeout(connection_timeout);
                socket.setSoLinger(true, connection_timeout);
                socket.connect(socketAddress, connection_timeout);

                networkWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                InputStreamReader i = new InputStreamReader(socket.getInputStream());
                networkReader = new BufferedReader(i);

                Message toMain = mMainHandler.obtainMessage();
                toMain.what = MSG_CONNECT;
                mMainHandler.sendMessage(toMain);
///test
                if(client!=null){
                    Message msg = mServiceHandler.obtainMessage();
                    msg.what = CHAT_START;
                    JSONObject json_string=new JSONObject();
                    try{
                        //int roomnumber=6;
                        json_string.put("status", "CHAT_START");
                        json_string.put("roomnumber2", 77); //채팅방 나누기
                        json_string.put("roomnumber", curr_roomnumber); //채팅방 나누기
                        json_string.put("nickname", user_userID);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }


                    //msg.obj = editText_id.getText().toString();
                    msg.obj=json_string.toString();
                    Log.d("json_test", msg.obj.toString());
                    mServiceHandler.sendMessage(msg);}
                //test


                loop = true;
            } catch (Exception e) {
                loop = false;
//                Log.d(TAG, "에러 발생", e);
                Message toMain = mMainHandler.obtainMessage();
                toMain.what = MSG_ERROR;
                toMain.obj = "소켓을 생성하지 못했습니다.";
                mMainHandler.sendMessage(toMain);
            }

            while (loop) {
                try {
                    line = networkReader.readLine();
                    if (line == null)
                        break;
                    Message toMain = mMainHandler.obtainMessage();
                    toMain.what = MSG_RECEIVE;
                    toMain.obj = line;
                    mMainHandler.sendMessage(toMain);
                } catch (InterruptedIOException e) {
                } catch (IOException e) {
                    Log.d(TAG, "네트워크 에러 발생", e);
/*
                    Message toMain = mMainHandler.obtainMessage();
                    toMain.what = MSG_ERROR;
                    toMain.obj = "네트워크에 예기치 못한 에러가 발생했습니다.";
                    mMainHandler.sendMessage(toMain);
                    */
                    break;
                }
            }

            try  {
                if (networkWriter != null) {
                    networkWriter.close();
                    networkWriter = null;
                }
                if (networkReader != null) {
                    networkReader.close();
                    networkReader = null;
                }
                if (socket != null) {
                    socket.close();
                    socket = null;
                }

                client = null;
                if (loop) {
                    loop = false;
                    Message toMain = mMainHandler.obtainMessage();
                    toMain.what = MSG_SERVER_STOP;
                    toMain.obj = "네트워크가 끊어졌습니다.";
                    mMainHandler.sendMessage(toMain);
                }
            } catch(IOException e ) {
                Log.d(TAG, "에러 발생", e);
                Message toMain = mMainHandler.obtainMessage();
                toMain.what = MSG_ERROR;
                toMain.obj = "소켓을 닫지 못했습니다..";
                mMainHandler.sendMessage(toMain);
            }
        }

        public void quit() {
            loop = false;

            try {
                if (socket != null) {
                    socket.close();
                    socket = null;

                    Message toMain = mMainHandler.obtainMessage();
                    toMain.what = MSG_CLIENT_STOP;
                    toMain.obj = "접속을 중단합니다.";
                    mMainHandler.sendMessage(toMain);
                }
            } catch (IOException e) {
                Log.d(TAG, "에러 발생", e);
            }
        }
    }


    //채팅방 생성 StringRequest
    public class CreateChatRoomRequest extends StringRequest {

        final static private String URL = "http://13.125.147.57/waiting_shuttle/Chat/createChatRoom.php";
        private Map<String, String> parameters;

        public CreateChatRoomRequest(String restaurant_name, String owner_id, String user_id, String last_message, String user_userID, Response.Listener<String> listener){
            super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
            parameters = new HashMap<>();
            parameters.put("restaurant_name", restaurant_name);
            parameters.put("owner_id", owner_id);
            parameters.put("user_id", user_id);
            parameters.put("last_message", last_message);
            parameters.put("userID", user_userID);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return parameters;
        }
    }
    //채팅방 생성 StringRequest
}
