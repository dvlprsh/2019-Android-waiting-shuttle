package com.example.watingshuttleformanager.mode;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.watingshuttleformanager.R;

import java.util.List;

public class WaitingProductsAdapter extends RecyclerView.Adapter<WaitingProductsAdapter.ProductViewHolder> {
//네비게이션 뷰의 현재 대기현황 가져옴

    private Context mCtx;
    private List<WaitingProduct> productList;

    public WaitingProductsAdapter(Context mCtx, List<WaitingProduct> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        //View view = inflater.inflate(R.layout.item_waiting, null);
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waiting, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        WaitingProduct product = productList.get(position);

        //loading the image
        /*Glide.with(mCtx)
                .load(product.getImage())
                .into(holder.imageView);*/

        holder.person_count.setText(product.getPerson_number()+" 명");
        holder.phone.setText(product.getPhone());
        holder.count.setText(Integer.toString(position+1));

        if(!product.getUser_userID().equals("null")){
            holder.userID.setText(product.getUser_userID());
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView person_count, phone, count, userID;


        public ProductViewHolder(View itemView) {
            super(itemView);

            person_count = itemView.findViewById(R.id.item_waiting_textview_number);
            userID=itemView.findViewById(R.id.item_waiting_textview_userID);
            phone = itemView.findViewById(R.id.item_waiting_textview_phone);
            count = itemView.findViewById(R.id.item_waiting_textview_count);
        }
    }
}