package com.example.indoornav1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {

    private List<Store> storeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sid, price, rating, quantity;

        public MyViewHolder(View view) {
            super(view);
            sid = (TextView) view.findViewById(R.id.sid);
            price = (TextView) view.findViewById(R.id.price);
            rating = (TextView) view.findViewById(R.id.rating);
            quantity = (TextView) view.findViewById(R.id.quantity);
        }
    }


    public StoreAdapter(List<Store> storeList) {
        this.storeList = storeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Store store = storeList.get(position);
        holder.sid.setText(store.getSid());
        holder.price.setText(store.getPrice());
        holder.rating.setText(store.getRating());
        holder.quantity.setText(store.getQuantity());
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }
}
