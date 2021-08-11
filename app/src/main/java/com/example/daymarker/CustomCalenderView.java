package com.example.daymarker;

import android.content.Context;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
    }

    public CustomCalenderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
    }
}
