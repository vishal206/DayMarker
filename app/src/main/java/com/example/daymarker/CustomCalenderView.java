package com.example.daymarker;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CustomCalenderView extends LinearLayout {

    ImageButton btn_nxt,btn_previous,btn_addMarker;
    TextView CurrentDate;
    GridView gridView;
    private FirebaseAuth mAuth;
    private String uid;
    private static final int MAX_CALENDER_DAYS=42;
    Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
    Context context;

    SimpleDateFormat dateFormat=new SimpleDateFormat("MMMM yyyy",Locale.ENGLISH);
    SimpleDateFormat monthFormat=new SimpleDateFormat("MMMM",Locale.ENGLISH);
    SimpleDateFormat yearFormat=new SimpleDateFormat("yyyy",Locale.ENGLISH);
    SimpleDateFormat eventDateFormat=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);

    MyGridAdapter myGridAdapter;

    AlertDialog alertDialog;
    List<Date> dates=new ArrayList<>();
    List<Events> eventsList=new ArrayList<>();


    public CustomCalenderView(Context context) {
        super(context);
    }

    public CustomCalenderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        IntializeLayout();
        SetUpCalender();
        btn_previous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH,-1);
                SetUpCalender();
            }
        });
        btn_nxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH,1);
                SetUpCalender();
            }
        });
        btn_addMarker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView=LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_event_layout,null);
                EditText EventNote=addView.findViewById(R.id.edt_note);
                Button btn_done=addView.findViewById(R.id.btn_done);

                String date=eventDateFormat.format(dates.get(position));
                String month=monthFormat.format(dates.get(position));
                String year=yearFormat.format(dates.get(position));

                btn_done.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvent(EventNote.getText().toString(),date,month,year);
                        SetUpCalender();
                        alertDialog.dismiss();

                    }
                });
                builder.setView(addView);
                alertDialog=builder.create();
                alertDialog.show();
            }
        });
    }

    public CustomCalenderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void SaveEvent(String event,String date,String month,String year){
        //database
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        uid=user.getUid();
        final String detail=date;
        DocumentReference ref= FirebaseFirestore.getInstance().collection("Users").document(uid).collection("events").document(detail);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("date",date);
        hashMap.put("month",month);
        hashMap.put("year",year);
        hashMap.put("note",event);
        ref.set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
//                    Events events=new Events(event,date,month,year);
//                    eventsList.add(events);
                    Toast.makeText(context, "added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void IntializeLayout(){
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.calendar_layout,this);
        btn_nxt=view.findViewById(R.id.btn_nxt);
        btn_previous=view.findViewById(R.id.btn_previous);
        CurrentDate=view.findViewById(R.id.txt_month);
        gridView=view.findViewById(R.id.GridView);
        btn_addMarker=view.findViewById(R.id.btn_add_marker);

    }
    private void SetUpCalender(){
        String currentDate=dateFormat.format(calendar.getTime());
        CurrentDate.setText(currentDate);
        dates.clear();
        Calendar monthCalendar=(Calendar)calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH,1);
        int FirstDayofMonth=monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH,-FirstDayofMonth);

        CollectEventPerMonth();
        while(dates.size()<MAX_CALENDER_DAYS){
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH,1);

        }


        myGridAdapter=new MyGridAdapter(context,dates,calendar,eventsList);
//        myGridAdapter.notifyDataChanged();
//        gridView.invalidateViews();
        gridView.setAdapter(myGridAdapter);
    }


    private void CollectEventPerMonth(){
        //database ,get-event,date,month,year, with that create a new Event(given below)

//        Events events=new Events(event,date,Month,Year);
//        eventsList.add(events);
//        eventsList.clear();
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        uid=user.getUid();
        FirebaseFirestore.getInstance().collection("Users").document(uid).collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Events events=new Events(document.get("note").toString(),document.get("date").toString(),document.get("month").toString(),document.get("year").toString());
                                eventsList.add(events);
                                Log.d("Log.d",""+document.get("note"));
                            }
                        } else {
                            Log.d("Log.d", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}
