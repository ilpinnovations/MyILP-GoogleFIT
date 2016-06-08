package com.ilp.ilpschedule.util;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.google.android.gms.fitness.data.Session;
import com.ilp.ilpschedule.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Daysvise extends AppCompatActivity {
    private Spinner spinner1,spinner2;
    String start;
    String flag="day";
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daysvise);
        BarChart chart = (BarChart) findViewById(R.id.chart5);
//
        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("My Chart");
        chart.animateXY(2000, 2000);
        chart.invalidate();
        spinner1=(Spinner)findViewById(R.id.spinner5);
        spinner2=(Spinner)findViewById(R.id.spinner6);

        toolbar=(Toolbar)findViewById(R.id.toolbarfit);
        setSupportActionBar(toolbar);
        ImageView img1=(ImageView)findViewById(R.id.cross);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Daysvise.this,CalorieConsumed.class);
                startActivity(intent);
                finish();
            }
        });
        ImageView dist=(ImageView)findViewById(R.id.dist);
        dist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Daysvise.this, DistanceWeekView.class);
                startActivity(intent);
                finish();
            }
        });
        ImageView img2=(ImageView)findViewById(R.id.img2);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Daysvise.this, ChartView.class);
                startActivity(intent);
                finish();
            }
        });
        ImageView img3=(ImageView)findViewById(R.id.img3);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ImageView img4=(ImageView)findViewById(R.id.img4);
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Daysvise.this,HeartRateChart.class);
                startActivity(intent);
                finish();
            }
        });

        final String[] calender={
                "Week","Day"
        };
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(Daysvise.this,android.R.layout.simple_spinner_dropdown_item,calender);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    flag="day";
                  //  StepCounter stepcounter=new StepCounter();
                    //stepcounter.buildFitnessClient();
                    //stepcounter.new InsertAndVerifyDataTask().execute();

                }
                else if(position==1)
                {
                    flag="week";
                   // StepCounter.reference="week";
                    Intent intent=new Intent(Daysvise.this,ChartView.class);
                    startActivity(intent);
                   // StepCounter stepcounter=new StepCounter();

                    // intent.putExtra("refr",flag);
                    //startActivity(intent);
                    //  stepcounter.buildFitnessClient(flag);
                    //stepcounter.new InsertAndVerifyDataTask().execute();
                }
                else if(position==2)
                {
                    flag="month";
                   // StepCounter.reference="week";
             //ataTask().execute();
                    //stepcounter.buildFitnessClient(flag);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String[] activity={
                "Steps","Distance","Calorie Burned","Heart Rate"
        };
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(Daysvise.this,android.R.layout.simple_spinner_dropdown_item,activity);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {

                }
                else if(position==1)
                {
                    Intent intent=new Intent(Daysvise.this,DistanceWeekView.class);
                    startActivity(intent);
                }
                else if(position==2)
                {
                    Intent intent=new Intent(Daysvise.this,DaysviseCalorie.class);
                    startActivity(intent);

                }
                else if(position==3)
                {
                    Intent intent=new Intent(Daysvise.this,HeartDataType.class);
                    startActivity(intent);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    Calendar calendar=Calendar.getInstance();

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        String[] value=new String[31];

        if(FitnessActivity.reference.equalsIgnoreCase("day")) {
            for (int i = 0; i < 7; i++) {

                BarEntry vi = new BarEntry(FitnessActivity.steps[i+24], i);
                valueSet1.add(vi);
            }
        }

        else if(FitnessActivity.reference.equalsIgnoreCase("week")) {

            for (int i = 0; i < 7; i++){
                BarEntry vi = new BarEntry(FitnessActivity.steps[25+i],i);
                valueSet1.add(vi);
                Log.d("valuess", "" + valueSet1.add(vi) + FitnessActivity.steps[24 + i]);

            }
        }

        else if(FitnessActivity.reference.equalsIgnoreCase("month")) {
            for (int i = 0; i < 12; i++) {

                BarEntry vi = new BarEntry(FitnessActivity.steps[i],i);
                valueSet1.add(vi);
            }
        }




        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Steps");
        barDataSet1.setColor(Color.rgb(255,136,0));
       // barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
      /* BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
*/
        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        //      dataSets.add(barDataSet2);
        return dataSets;
    }




    private ArrayList<String> getXAxisValues() {

        ArrayList<String> xAxis = new ArrayList<>();
        Calendar calendar=Calendar.getInstance();
        Date now=new Date();
        // calendar.set(System.currentTimeMillis());

        int thisYear = calendar.get(Calendar.YEAR);
        Log.d("TAG", "# thisYear : " + thisYear);

        int thisMonth = calendar.get(Calendar.MONTH);
        Log.d("TAG", "@ thisMonth : " + thisMonth);

        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("TAG", "$ thisDay : " + thisDay);;

        // String end=thisYear+"-"+thisMonth+"-"+thisDay;

        int sthisYear = calendar.get(Calendar.YEAR);
        Log.d("TAG", "# sthisYear : " + sthisYear);

        int sthisMonth = calendar.get(Calendar.MONTH);
        Log.d("TAG", "@ sthisMonth : " + sthisMonth);

        int sthisDay = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("TAG", "$ ssthisDay : " + sthisDay);;
        if(FitnessActivity.reference.equalsIgnoreCase("day")) {


            Calendar cal=Calendar.getInstance();
            Date n=new Date();
            cal.setTime(n);
            cal.add(Calendar.DAY_OF_YEAR,-6);
            Date dt= cal.getTime();
            start=new SimpleDateFormat("yyyy-MM-dd").format(dt);

        }
        else if(FitnessActivity.reference.equalsIgnoreCase("week"))
        {   Calendar cal=Calendar.getInstance();
            Date n=new Date();
            cal.setTime(n);
            cal.add(Calendar.DAY_OF_YEAR,-6);
            Date dt= cal.getTime();
            start=new SimpleDateFormat("yyyy-MM-dd").format(dt);
        }
        else if(FitnessActivity.reference.equalsIgnoreCase("month"))
        {
            calendar.add(Calendar.MONTH,-1);
            start=sthisYear+"-"+sthisMonth+"-"+sthisDay;
        }

        String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Log.d("start",start);
        Log.d("end",end);
        long oneDayMilSec = 86400000; // number of milliseconds in one day
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        try {

            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);

            long startDateMilSec = startDate.getTime();
            long endDateMilSec = endDate.getTime();

            for(long d=startDateMilSec; d<=endDateMilSec; d=d+oneDayMilSec){
                Log.d("dates", "" + new Date(d));
                Log.d("dates",""+new  Date(d).getYear());
                Log.d("dates",""+new Date(d).getMonth());
                //  xAxis.add(new Date(d).getYear()+"-"+new Date(d).getMonth()+"-"+new Date(d).getDate()+" "+new Date(d).getDay());
                SimpleDateFormat formatter = new SimpleDateFormat("E dd/MM/yyyy");
                String dateString = formatter.format(new Date(d));
                xAxis.add(dateString)  ;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return xAxis;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(Daysvise.this,CalorieConsumed.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent=new Intent(Daysvise.this,CalorieConsumed.class);
        startActivity(intent);
        finish();
    }
}