package com.example.daymarker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyGridAdapter extends ArrayAdapter {
    List<Date> dates;
    Calendar currentDate;
    List<Events> events;
    LayoutInflater inflater;
    Context context;

    public MyGridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate, List<Events> events ) {
        super(context, R.layout.single_cell_layout);
        this.context=context;
        this.dates=dates;
        this.currentDate=currentDate;
        this.events=events;
        inflater=LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Date monthDate=dates.get(position);
        Calendar dateCalendar=Calendar.getInstance();
        dateCalendar.setTime(monthDate);
        int DayNo=dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth=dateCalendar.get(Calendar.MONTH)+1;
        int displayYear=dateCalendar.get(Calendar.YEAR);
        int currentMonth=currentDate.get(Calendar.MONTH)+1;
        int currentYear=currentDate.get(Calendar.YEAR);

        Handler mHandler = new Handler();

        View view=convertView;
        TextView txt_Date;


        if(view==null){
            view=inflater.inflate(R.layout.single_cell_layout,parent,false);

        }
        if(displayMonth==currentMonth && displayYear==currentYear){
            txt_Date=view.findViewById(R.id.calendar_day);
            txt_Date.setTextColor(Color.parseColor("#000000"));

//            view.setBackgroundColor(getContext().getResources().getColor(R.color.green));
        }
        else
        {
            txt_Date=view.findViewById(R.id.calendar_day);
            txt_Date.setTextColor(Color.parseColor("#cccccc"));

//            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        TextView Day_Number=view.findViewById(R.id.calendar_day);
        TextView EventNumber=view.findViewById(R.id.event_id);
        Day_Number.setText(String.valueOf(DayNo));
//        EventNumber.setText("event");
        Calendar eventCalendar = Calendar.getInstance();


                for (int i = 0; i < events.size(); i++) {
//            EventNumber.setText("hey");
            eventCalendar.setTime(ConvertStringToDate(events.get(i).getDATE()));

            if(DayNo==eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth==eventCalendar.get(Calendar.MONTH)+1 &&
            displayYear==eventCalendar.get(Calendar.YEAR)){
//                arrayList.add(events.get(i).getEVENT());
//                EventNumber.setText(arrayList.size()+""+events);

                        EventNumber.setText(events.get(i).getEVENT());

//                EventNumber.setText(events.get(i).getEVENT());

            }
        }
                EventNumber.postInvalidate();

        return view;
    }

    private Date ConvertStringToDate(String eventDate){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date=null;
        try{
            date=format.parse(eventDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public int getPosition(@Nullable Object item) {

        return dates.indexOf(item);
    }
}
