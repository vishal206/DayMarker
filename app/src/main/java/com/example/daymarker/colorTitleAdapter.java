package com.example.daymarker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class colorTitleAdapter extends RecyclerView.Adapter<colorTitleAdapter.MyViewHolder> {

    private List<colorTitleClass> colorTitleClassList;
    private RecyclerViewClickListener listener;

    public colorTitleAdapter(List<colorTitleClass> colorTitleClassList,RecyclerViewClickListener listener) {
        this.colorTitleClassList = colorTitleClassList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public colorTitleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.color_list,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull colorTitleAdapter.MyViewHolder holder, int position) {
        String title=colorTitleClassList.get(position).getTitle();
        int color=colorTitleClassList.get(position).getColor();
        holder.txt_color_bar.setText(title);
        holder.txt_color_bar.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return colorTitleClassList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txt_color_bar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_color_bar=itemView.findViewById(R.id.txt_color_bar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v,getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v,int position);
    }
}
