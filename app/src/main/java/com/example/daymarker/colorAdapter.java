package com.example.daymarker;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class colorAdapter extends RecyclerView.Adapter<colorAdapter.MyViewHolder> {

    private ArrayList<String> colorList;

    public colorAdapter(ArrayList<String> colorList) {
        this.colorList = colorList;
    }

    @NonNull
    @Override
    public colorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.color_list,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull colorAdapter.MyViewHolder holder, int position) {
        String clr=colorList.get(position);
        holder.colorCirle.setBackgroundColor(Color.parseColor(clr));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView colorCirle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            colorCirle=itemView.findViewById(R.id.txt_color_circle);
        }
    }
}
