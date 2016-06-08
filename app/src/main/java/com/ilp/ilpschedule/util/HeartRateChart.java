package com.ilp.ilpschedule.util;

import android.content.Intent;
import android.database.Cursor;
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

import com.ilp.ilpschedule.R;
import com.ilp.ilpschedule.db.DataBaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HeartRateChart extends AppCompatActivity {
    private Spinner spinner1,spinner2;
    String start;
    String flag="day";
    DataBaseHelper mydb;
    public static float[] heart = new float[50];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_chart);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbarfit);
        setSupportActionBar(toolbar);

        ImageView img1=(ImageView)findViewById(R.id.cross);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HeartRateChart.this,CalorieConsumed.class);
                startActivity(intent);
                finish();
            }
        });
        ImageView dist=(ImageView)findViewById(R.id.dist);
        dist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeartRateChart.this, DistanceView.class);
                startActivity(intent);
                finish();
            }
        });
        ImageView img2=(ImageView)findViewById(R.id.img2);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeartRateChart.this, ChartView.class);
                startActivity(intent);
                finish();
            }
        });
        ImageView img3=(ImageView)findViewById(R.id.img3);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeartRateChart.this, Calorieconsumedgraph.class);
                startActivity(intent);
                finish();
            }
        });
        ImageView img4=(ImageView)findViewById(R.id.img4);
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        BarChart chart = (BarChart) findViewById(R.id.chart2);
        mydb=new DataBaseHelper(HeartRateChart.this);
        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("My Chart");
        chart.animateXY(2000, 2000);
        chart.invalidate();
        spinner1=(Spinner)findViewById(R.id.spinner111);
        spinner2=(Spinner)findViewById(R.id.spinner222);



        final String[] calender={
                "Day"
        };
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(HeartRateChart.this,android.R.layout.simple_spinner_dropdown_item,calender);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String[] activity={
                "Heart Rate","Steps","Distance","Calorie Burned"
        };
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(HeartRateChart.this,android.R.layout.simple_spinner_dropdown_item,activity);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {

                }
                else if(position==1)
                {
                    Intent intent=new Intent(HeartRateChart.this,ChartView.class);
                    startActivity(intent);

                }
                else if(position==2)
                {
                    Intent intent=new Intent(HeartRateChart.this,DistanceView.class);
                    startActivity(intent);

                }
                else if(position==3)
                {
                    Intent intent=new Intent(HeartRateChart.this,Calorieconsumedgraph.class);
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
            String sdf = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            String[] parts = sdf.split("-");
            String part1 = parts[0]; // yyyy
            String part2 = parts[1];//mm
            String part3 = parts[2];//dd

            int date = Integer.parseInt(part3);
            int month = Integer.parseInt(part2);
            int year = Integer.parseInt(part1);
            int in = Integer.parseInt(part2);

            Log.d("yyyy",date+"-"+month+"-"+year);
            int r = in + 1;
            String h = r + "";

            String modipart2 = r + "";
            int count=0;
            Calendar cal=Calendar.getInstance();
            Date fg=new Date();
            cal.setTime(fg);

            Log.d("Endtime", cal.get(Calendar.DATE) + "/" + cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.YEAR));

            // cal.add(Calendar.DATE, -30);
            Log.d("Starttime",cal.get(Calendar.DATE) +"/"+cal.get(Calendar.MONTH));
            boolean flag=false;
            //for (int i = 1; i <31; i++) {

            Calendar calendar1=Calendar.getInstance();
                Date gh=new Date();
                calendar1.setTime(gh);
                long ends=calendar1.getTimeInMillis();
                calendar1.add(Calendar.DAY_OF_YEAR,-29);
                long starts=calendar1.getTimeInMillis();

                long oneDayMilSec = 86400000;


            for(long d=starts; d<=ends; d=d+oneDayMilSec){
                count=count+1;
                Log.d("counter",count+"");
                    Log.d("dates", "" + new Date(d));
                    Log.d("dates",""+new  Date(d).getYear());
                    Log.d("dates",""+new Date(d).getMonth());
                    //  xAxis.add(new Date(d).getYear()+"-"+new Date(d).getMonth()+"-"+new Date(d).getDate()+" "+new Date(d).getDay());
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                 String dateString = formatter.format(new Date(d));
               String[] datastr=dateString.split("/");
                String parta = datastr[0]; // dd
                String partb = datastr[1];//mm
                String partc = datastr[2];//yyyy

                Cursor cursor = mydb.getAllData();

                Log.d("outside", "outside cursor");


                for (int j = 0; cursor.moveToNext(); j++) {
                    Log.d("inside", "inside");
                    Log.d("cursormain", cursor.getString(1) + cursor.getInt(0) + cursor.getInt(2) + cursor.getInt(3));
                    if (Integer.parseInt(cursor.getString(2)) == Integer.parseInt(partb) && Integer.parseInt(cursor.getString(0)) == Integer.parseInt(parta) && Integer.parseInt(cursor.getString(3))==Integer.parseInt(partc)) {

                        Log.d("inside","success"+count);
                        Log.d("barentry", Float.parseFloat(cursor.getString(1)) +"="+ count + "");
                        // flag += Float.parseFloat(cursor.getString(1));
                        flag = true;
                        //BarEntry vcount = new BarEntry(cursor.getInt(1),count);
                        //valueSet1.add(vcount);
                        heart[count]=Float.parseFloat(cursor.getString(1));
                        break;
                    }

                }

                    if (flag == false) {

                        //BarEntry vcount = new BarEntry((float) 0.00, count);
                        Log.d("bar", count + "");
                        //valueSet1.add(vcount);
                        heart[count]=0;

                    }



            }
        }

        for (int i=0;i<30;i++)
        {
            BarEntry vi = new BarEntry(heart[i+1],i);
            valueSet1.add(vi);
            Log.d("counter3",i+"");

        }


         if(FitnessActivity.reference.equalsIgnoreCase("week")) {

            for (int i = 0; i < 7; i++){
                BarEntry vi = new BarEntry(FitnessActivity.steps[25+i],i);
                valueSet1.add(vi);
                Log.d("valuess",""+valueSet1.add(vi)+FitnessActivity.steps[25+i]);

            }
        }

         if(FitnessActivity.reference.equalsIgnoreCase("month")) {
            for (int i = 0; i < 12; i++) {

                BarEntry vi = new BarEntry(FitnessActivity.steps[i],i);
                valueSet1.add(vi);
            }
        }


/*
            BarEntry v1e1 = new BarEntry(0, 0); // Jan
            valueSet1.add(v1e1);
            BarEntry v1e2 = new BarEntry(4023, 1); // Feb
            valueSet1.add(v1e2);
            BarEntry v1e3 = new BarEntry(0, 2); // Mar
            valueSet1.add(v1e3);
            BarEntry v1e4 = new BarEntry(0, 3); // Apr
            valueSet1.add(v1e4);
            BarEntry v1e5 = new BarEntry(0, 4); // May
            valueSet1.add(v1e5);
            BarEntry v1e6 = new BarEntry(0, 5); // Jun
            valueSet1.add(v1e6);
            BarEntry v1e7 = new BarEntry(0, 6); // jul
            valueSet1.add(v1e7);
            BarEntry v1e8 = new BarEntry(0, 7); // aug
            valueSet1.add(v1e8);
            BarEntry v1e9 = new BarEntry(0, 8); // sep
            valueSet1.add(v1e9);
            BarEntry v1e10 = new BarEntry(0, 9); // oct
            valueSet1.add(v1e10);
            BarEntry v1e11 = new BarEntry(0, 10); // nov
            valueSet1.add(v1e11);
            BarEntry v1e12 = new BarEntry(0, 11); // dec
            valueSet1.add(v1e12);
            BarEntry v1e13 = new BarEntry(0, 12); // dec
            valueSet1.add(v1e13);
            BarEntry v1e14 = new BarEntry(0, 13); // dec
            valueSet1.add(v1e14);

            BarEntry v1e15 = new BarEntry(0, 14); // dec
            valueSet1.add(v1e15);

             BarEntry v1e16 = new BarEntry(0, 15); // dec
            valueSet1.add(v1e16);

            BarEntry v1e17 = new BarEntry(0, 16); // dec
            valueSet1.add(v1e17);

        BarEntry v1e18 = new BarEntry(0, 17); // dec
        valueSet1.add(v1e18);

        BarEntry v1e19 = new BarEntry(0, 18); // dec
        valueSet1.add(v1e19);

        BarEntry v1e20 = new BarEntry(0, 19); // dec
        valueSet1.add(v1e20);
        BarEntry v1e21 = new BarEntry(0, 20); // dec
        valueSet1.add(v1e21);

        BarEntry v1e22 = new BarEntry(0, 21); // dec
        valueSet1.add(v1e22);

        BarEntry v1e23 = new BarEntry(0, 22); // dec
        valueSet1.add(v1e23);

        BarEntry v1e24 = new BarEntry(0, 23); // dec
        valueSet1.add(v1e24);

        BarEntry v1e25 = new BarEntry(0, 24); // dec
        valueSet1.add(v1e25);

        BarEntry v1e26 = new BarEntry(0, 25); // dec
        valueSet1.add(v1e26);

        BarEntry v1e27 = new BarEntry(520, 26); // dec
        valueSet1.add(v1e27);

        BarEntry v1e28 = new BarEntry(0, 27); // dec
        valueSet1.add(v1e28);

        BarEntry v1e29 = new BarEntry(5200,28); // dec
        valueSet1.add(v1e29);

        BarEntry v1e30 = new BarEntry(4545, 29); // dec
        valueSet1.add(v1e30);

        BarEntry v1e31 = new BarEntry(0, 30); // dec
        valueSet1.add(v1e31);

*/

   /*     ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);
*/
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "HeartRate");
        barDataSet1.setColor(Color.rgb(74, 104, 255));
        //  barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
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
            calendar.add(Calendar.DAY_OF_YEAR, -30);

            start=sthisYear+"-"+sthisMonth+"-"+sthisDay;

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
            calendar.add(Calendar.DAY_OF_YEAR,-30);
            start=sthisYear+"-"+sthisMonth+"-"+sthisDay;
        }

        String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Log.d("start",start);
        Log.d("end",end);
        long oneDayMilSec = 86400000; // number of milliseconds in one day
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int counter=0;

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
                counter=counter+1;
                Log.d("counter2",counter+"");
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
        Intent intent =new Intent(HeartRateChart.this,CalorieConsumed.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent =new Intent(HeartRateChart.this,CalorieConsumed.class);
        startActivity(intent);
        finish();
    }
}