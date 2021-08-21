package com.example.daymarker;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class notesAdapter extends RecyclerView.Adapter<notesAdapter.MyViewHolder> {

    private List<notes> notesList=new ArrayList<notes>();

    public notesAdapter(List<notes> notesList) {
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public notesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_layout,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull notesAdapter.MyViewHolder holder, int position) {
        String date=notesList.get(position).getDATE();
        String note=notesList.get(position).getNOTE();
        holder.txt_note.setText(note);
        holder.txt_date.setText(date);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_date,txt_note;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_date=itemView.findViewById(R.id.txt_date);
            txt_note=itemView.findViewById(R.id.txt_note);

        }
    }
}
