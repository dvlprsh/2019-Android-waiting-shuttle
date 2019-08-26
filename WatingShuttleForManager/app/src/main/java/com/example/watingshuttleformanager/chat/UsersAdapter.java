package com.example.watingshuttleformanager.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.RequestOptions;
import com.example.watingshuttleformanager.R;
import com.example.watingshuttleformanager.list_test.Product;
import com.example.watingshuttleformanager.list_test.ProductsAdapter;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>  {

    private ChatRoom chatRoom;
    public static final String KEY_SIMPLE_DATA="data";
    private Context mCtx;
    private List<ChatRoom> roomList;
    //클릭 이벤트처리

    //클릭 이벤트처리
    public UsersAdapter(Context mCtx, List<ChatRoom> roomList) {
        this.mCtx = mCtx;
        this.roomList = roomList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //LayoutInflater inflater = LayoutInflater.from(mCtx);
        //View view = inflater.inflate(R.layout.product_list, null);
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user, parent, false);


        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {
        final ChatRoom chatRoom = roomList.get(position);
        /*
        if(user.getImage_url()==null){
            //loading the image
            Glide.with(mCtx)
                    .load(R.drawable.wallpaper)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imageView);

        }else{
            //loading the image
            Glide.with(mCtx)
                    .load(user.getImage_url())
                    .into(holder.imageView);

        }*/
        Glide.with(mCtx)
                .load(R.drawable.wallpaper)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imageView);

        holder.textViewTitle.setText(chatRoom.getUserID());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //Toast.makeText(mCtx, "click", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(mCtx, ChatWithCustomerActivity.class);
        ChatRoom user_intent=roomList.get(position);
        intent.putExtra(KEY_SIMPLE_DATA, user_intent);
        mCtx.startActivity(intent);
    }
});


    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        ImageView imageView;

        public UserViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.item_chat_user_textView);

            imageView = itemView.findViewById(R.id.item_chat_user_imageView);
        }
    }
}