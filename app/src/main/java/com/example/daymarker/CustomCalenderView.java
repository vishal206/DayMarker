package com.example.daymarker;

import android.app.AlertDialog;
import android.content.Context;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomCalenderView extends LinearLayout {

    ImageButton btn_nxt,btn_previous;
    TextView CurrentDate;
    GridView gridView;
    private static final int MAX_CALENDER_DAYS=42;
    Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
    Context context;

    SimpleDateFormat dateFormat=new SimpleDateFormat("MMMM yyyy",Locale.ENGLISH);
    SimpleDateFormat monthFormat=new SimpleDateFormat("MMMM",Locale.ENGLISH);
    SimpleDateFormat yearFormat=new SimpleDateFormat("yyyy",Locale.ENGLISH);

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
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView=LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_event_layout,null);
                EditText EventName=addView.findViewById(R.id.edt_note);
                Button btn_done=addView.findViewById(R.id.btn_done);
                String date=dateFormat.format(dates.get(position));
                String month=monthFormat.format(dates.get(position));
                String year=yearFormat.format(dates.get(position));
                btn_done.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvent(EventName.getText().toString(),date,month,year);
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
    }

    private void IntializeLayout(){
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.calendar_layout,this);
        btn_nxt=view.findViewById(R.id.btn_nxt);
        btn_previous=view.findViewById(R.id.btn_previous);
        CurrentDate=view.findViewById(R.id.txt_month);
        gridView=view.findViewById(R.id.GridView);

    }
    private void SetUpCalender(){
        String currentDate=dateFormat.format(calendar.getTime());
        CurrentDate.setText(currentDate);
        dates.clear();
        Calendar monthCalendar=(Calendar)calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH,1);
        int FirstDayofMonth=monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH,-FirstDayofMonth);

        while(dates.size()<MAX_CALENDER_DAYS){
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH,1);

        }
        myGridAdapter=new MyGridAdapter(context,dates,calendar,eventsList);
        gridView.setAdapter(myGridAdapter);
    }
}
