package com.example.watingshuttleformanager.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.watingshuttleformanager.R;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ProductViewHolder>  {

    //private ChatRoom chatRoom;
    public static final String KEY_SIMPLE_DATA="data";
    private Context mCtx;
    private List<String> chatList;
    //클릭 이벤트처리

    //클릭 이벤트처리
    public ChatListAdapter(Context mCtx, List<String> chatList) {
        this.mCtx = mCtx;
        this.chatList = chatList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater inflater = LayoutInflater.from(mCtx);
        //View view = inflater.inflate(R.layout.product_list, null);
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);


        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        String chatString = chatList.get(position);
        /*
        //loading the image
        Glide.with(mCtx)
                .load(product.getImage())
                .into(holder.imageView);*/

        holder.textViewTitle.setText(chatString);





    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;


        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.item_room_textView);

        }
    }
}