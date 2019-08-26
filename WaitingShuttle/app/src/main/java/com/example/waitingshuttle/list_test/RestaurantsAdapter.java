package com.example.waitingshuttle.list_test;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.waitingshuttle.R;

import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantViewHolder>  {

    private Restaurant restaurant;
    public static final String KEY_SIMPLE_DATA="data";
    private Context mCtx;
    private List<Restaurant> restaurantList;
    //클릭 이벤트처리

    //클릭 이벤트처리
    public RestaurantsAdapter(Context mCtx, List<Restaurant> restaurantList) {
        this.mCtx = mCtx;
        this.restaurantList = restaurantList;
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //LayoutInflater inflater = LayoutInflater.from(mCtx);
        //View view = inflater.inflate(R.layout.product_list, null);
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);


        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, final int position) {
        restaurant = restaurantList.get(position);

        //loading the image
        Glide.with(mCtx)
                .load(restaurant.getImage())
                .into(holder.imageView);
        //TextView textView_name, textView_kind, textView_address, textView_waiting;
        holder.textView_name.setText(restaurant.getName());
        holder.textView_kind.setText(restaurant.getKind());
        holder.textView_address.setText(restaurant.getAddress());
        holder.textView_waiting.setText(Integer.toString(restaurant.getWaiting()));
        //리스트 아이템 클릭
        holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //Toast.makeText(mCtx, "click", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(mCtx, DetailRestaurantActivity.class);
        //restaurant=(Restaurant)restaurantList.get(position);
        Restaurant restaurant2=(Restaurant)restaurantList.get(position);
        Log.d("item_click_test", Integer.toString(restaurant2.getOwner_id()));
        intent.putExtra(KEY_SIMPLE_DATA, restaurant2);
        mCtx.startActivity(intent);
    }
});


    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    class RestaurantViewHolder extends RecyclerView.ViewHolder {

        TextView textView_name, textView_kind, textView_address, textView_waiting;
        ImageView imageView;

        public RestaurantViewHolder(View itemView) {
            super(itemView);

            textView_name = itemView.findViewById(R.id.item_restaurant_textView_name);
            textView_kind = itemView.findViewById(R.id.item_restaurant_textView_kind);

            textView_address = itemView.findViewById(R.id.item_restaurant_textView_address);
            textView_waiting = itemView.findViewById(R.id.item_restaurant_textView_waiting);
            imageView = itemView.findViewById(R.id.item_restaurant_imageView);
        }
    }
}