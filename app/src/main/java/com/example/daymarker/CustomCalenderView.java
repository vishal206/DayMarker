package com.example.daymarker;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Set;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CustomCalenderView extends LinearLayout {

    ImageButton btn_nxt,btn_previous,btn_addMarker;
    TextView CurrentDate;
    GridView gridView;
    private FirebaseAuth mAuth;
    private String uid;
    private static final int MAX_CALENDER_DAYS=42;
    Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
    Context context;

    FloatingActionButton btn_refresh;

    SimpleDateFormat dateFormat=new SimpleDateFormat("MMMM yyyy",Locale.ENGLISH);
    SimpleDateFormat monthFormat=new SimpleDateFormat("MMMM",Locale.ENGLISH);
    SimpleDateFormat yearFormat=new SimpleDateFormat("yyyy",Locale.ENGLISH);
    SimpleDateFormat eventDateFormat=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);

    MyGridAdapter myGridAdapter;

    private RecyclerView marker_recyclerView;

    AlertDialog alertDialog;
    List<Date> dates=new ArrayList<>();
    List<Events> eventsList=new ArrayList<>();

    EditText edt_title;
    private colorTitleAdapter.RecyclerViewClickListener listener;

    List<colorTitleClass> colorTitleClassList=new ArrayList<>();

    int mDefaultcolor;
    Button btn_select_color;
    TextView txt_showColor;

    TextView txt_title;
    private String title;
    private int color;



    public CustomCalenderView(Context context) {
        super(context);
    }

    public CustomCalenderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        IntializeLayout();
        SetUpCalender();
        CollectMarker();
        btn_refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SetUpCalender();
            }
        });
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

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setCancelable(true);

                View addView=LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_event_layout,null);

                EditText EventNote=addView.findViewById(R.id.edt_note);
                Button btn_done=addView.findViewById(R.id.btn_done);
                btn_addMarker=addView.findViewById(R.id.btn_add_marker);

//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                String date=eventDateFormat.format(dates.get(position));
                String month=monthFormat.format(dates.get(position));
                String year=yearFormat.format(dates.get(position));

                marker_recyclerView=addView.findViewById(R.id.marker_recyclerView);

                txt_title=addView.findViewById(R.id.txt_title);

                btn_done.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String noteit=EventNote.getText().toString();
                        SaveEvent(EventNote.getText().toString(),date,month,year,title,color);
//                        EventNote.setText(noteit);
                        SetUpCalender();
                        alertDialog.dismiss();

                    }
                });

                btn_addMarker.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setCancelable(true);

                        View addView=LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_marker,parent,false);


                        edt_title=addView.findViewById(R.id.edt_title);
                        btn_select_color=addView.findViewById(R.id.btn_SelectColor);
                        mDefaultcolor= ContextCompat.getColor(context,R.color.green);
                        txt_showColor=addView.findViewById(R.id.color_show);

                        btn_select_color.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                openColorPicker();
                            }
                        });

                        Button btn_save=addView.findViewById(R.id.btn_save);
                        btn_save.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Toast.makeText(context, "asasas", Toast.LENGTH_SHORT).show();


                                mAuth=FirebaseAuth.getInstance();
                                FirebaseUser user=mAuth.getCurrentUser();
                                uid=user.getUid();
                                DocumentReference ref= FirebaseFirestore.getInstance().collection("Users").document(uid).collection("markers").document(""+mDefaultcolor);
                                HashMap<String,Object> hashMap=new HashMap<>();
                                hashMap.put("color",mDefaultcolor);
                                hashMap.put("Title",edt_title.getText().toString());
                                ref.set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            colorTitleClass c1=new colorTitleClass(edt_title.getText().toString(),mDefaultcolor);
                                            colorTitleClassList.add(c1);
                                            setAdapter();
//                                            Toast.makeText(context, "saved"+colorTitleClassList.get(3).getTitle(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                alertDialog.dismiss();
                            }
                        });

                        builder.setView(addView);
                        alertDialog=builder.create();
                        alertDialog.show();
                    }
                });

                setAdapter();

                builder.setView(addView);
                alertDialog=builder.create();
                alertDialog.show();
            }

            private void setAdapter() {
                setAdapterOnClickListener();
                colorTitleAdapter adapter=new colorTitleAdapter(colorTitleClassList,listener);
                adapter.notifyDataSetChanged();
                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context);
                marker_recyclerView.setLayoutManager(layoutManager);
                marker_recyclerView.setItemAnimator(new DefaultItemAnimator());
                marker_recyclerView.setAdapter(adapter);
            }

            private void setAdapterOnClickListener() {
                listener=new colorTitleAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View v, int position) {
                        title=colorTitleClassList.get(position).getTitle();
                        color=colorTitleClassList.get(position).getColor();
                        Toast.makeText(context, "color selected", Toast.LENGTH_SHORT).show();
                        txt_title.setText(colorTitleClassList.get(position).getTitle());
                    }
                };
            }

            private void openColorPicker() {
                AmbilWarnaDialog colorPicker=new AmbilWarnaDialog(context, mDefaultcolor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        mDefaultcolor=color;
                        txt_showColor.setBackgroundColor(mDefaultcolor);
                    }
                });
                colorPicker.show();
            }
        });
    }


    

    public CustomCalenderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void SaveEvent(String event,String date,String month,String year,String title,int color){
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
        hashMap.put("title",title);
        hashMap.put("color",color);
        ref.set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
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
        btn_refresh=view.findViewById(R.id.btn_refresh);
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

        gridView.setAdapter(myGridAdapter);
    }

    private void CollectMarker(){
        FirebaseFirestore.getInstance().collection("Users").document(uid).collection("markers").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                colorTitleClass c1=new colorTitleClass(document.get("Title").toString(),Integer.parseInt(document.get("color").toString()));
                                colorTitleClassList.add(c1);
                                Log.d("Log.d",""+document.get("Title"));
                            }
                        }else{
                            Log.d("Log.d", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



    private void CollectEventPerMonth(){
        //database ,get-event,date,month,year, with that create a new Event(given below)

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
                                int ct=0;
                                if(eventsList.size()>0){
                                    for(int i=0;i<eventsList.size();i++){
                                        if(document.get("date")==eventsList.get(i).getDATE()){
                                            eventsList.get(i).setColor(Integer.parseInt(document.get("color").toString()));
                                            eventsList.get(i).setEVENT(document.get("note").toString());
                                            eventsList.get(i).setTITLE(document.get("title").toString());
                                            ct++;
                                            break;
                                        }
                                    }
                                    if(ct==0){
                                        Events events=new Events(document.get("note").toString(),document.get("date").toString(),document.get("month").toString(),document.get("year").toString(),document.get("title").toString(),Integer.parseInt(document.get("color").toString()));
                                        eventsList.add(events);
                                    }
                                }else{
                                    Events events=new Events(document.get("note").toString(),document.get("date").toString(),document.get("month").toString(),document.get("year").toString(),document.get("title").toString(),Integer.parseInt(document.get("color").toString()));
                                    eventsList.add(events);
                                }

                                Log.d("Log.d",""+document.get("note"));
                            }
                        } else {
                            Log.d("Log.d", "Error getting documents: ", task.getException());
                        }
                    }
                });



    }


}
