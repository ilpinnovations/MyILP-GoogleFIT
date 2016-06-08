package com.ilp.ilpschedule.util;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ilp.ilpschedule.R;
import com.ilp.ilpschedule.db.DataBaseHelper;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class SampleFit3Fragment extends SampleFragment {
    final private float[] mTrackBackWidth = {30f, 60f, 30f, 40f, 30f};
    final private float[] mTrackWidth = {30f, 30f, 30f, 30f, 30f};
    final private boolean[] mClockwise = {true, true, true, false, true};
    final private boolean[] mRounded = {true, true, true, true, true};
    final private boolean[] mPie = {false, false, false, false, true};
    final private int[] mTotalAngle = {360, 360, 320, 260, 360};
    final private int[] mRotateAngle = {0, 180, 180, 0, 270};
    private int mBackIndex;
    private int mSeries1Index;
    private int mSeries2Index;
    private int mStyleIndex;
    DataBaseHelper mydb;
    int goalcal;
    float flag,id;
    public SampleFit3Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.fragment_sample_fit3, container, false);
        mydb=new DataBaseHelper(getActivity());
        Toolbar toolbar=(Toolbar)rootview.findViewById(R.id.toolbarfit);

        try{
            Cursor cursor=mydb.getcaloriegoal();
            cursor.moveToLast();
            goalcal=cursor.getInt(0);
            for(int i=0;cursor.moveToNext();i++)
                if(cursor.getString(0)!=null)
                {
                    Log.d("sqlitesss",cursor.getInt(0)+"");
                    goalcal=cursor.getInt(0);
                }
            if(goalcal==0)
            {
                goalcal=10000;
            }

        }
        catch (Exception e){

        }

        ImageView img1=(ImageView)rootview.findViewById(R.id.cross);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),FitnessActivity.class);
                startActivity(intent);

            }
        });
        ImageView img2=(ImageView)rootview.findViewById(R.id.img2);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Daysvise.class);
                startActivity(intent);

            }
        });
        ImageView img3=(ImageView)rootview.findViewById(R.id.img3);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Calorieconsumedgraph.class);
                startActivity(intent);

            }
        });
        ImageView img4=(ImageView)rootview.findViewById(R.id.img4);
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),HeartRateChart.class);
                startActivity(intent);

            }
        });
        ImageView dist=(ImageView)rootview.findViewById(R.id.dist);
        dist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),DistanceView.class);
                startActivity(intent);

            }
        });
        TextView graph=(TextView)rootview.findViewById(R.id.onclickgraph);
        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Calorieconsumedgraph.class);
                startActivity(intent);


            }
        });
        TextView calheart=(TextView)rootview.findViewById(R.id.calheart);
        calheart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),HeartDataType.class);
                startActivity(intent);
            }
        });

        TextView goaltext = (TextView)rootview.findViewById(R.id.goalcalorie);
        goaltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("insidegoal", "inside");
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(getActivity());


                alertdialog.setTitle("Set Your Goal for Calorie Burned");

                alertdialog.setMessage("Calorie");

                final EditText input2 = new EditText(getActivity());
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                input2.setLayoutParams(lp);
                alertdialog.setView(input2);
                input2.setInputType(InputType.TYPE_CLASS_NUMBER);


                alertdialog.setIcon(R.drawable.calorie);
                alertdialog.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(getContext(), "positive", Toast.LENGTH_SHORT).show();
                        if(input2.getText().toString().matches("")||input2.getText().toString().equalsIgnoreCase("")){
                            Toast.makeText(getActivity(), "Goal field can't be set as blank", Toast.LENGTH_SHORT).show();
                            // input.setError("Goal field can't be left as blank");
                        }
                        else
                        {
                            goalcal= Integer.parseInt(input2.getText().toString());
                            mydb.insertcaloriegoal(goalcal);

                        }



                    }
                });
                alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertdialog.show();
            }
        });



      //  TextView thismonth=(TextView)rootview.findViewById(R.id.thismonth);
       // thismonth.setText("Calorie Burned of months");
        TextView textactivity=(TextView)rootview.findViewById(R.id.textActivity1);

        textactivity.setText("Distance"+"\n"+FitnessActivity.progress * 0.0004707356+"");


        return rootview;
    }

    @Override
    protected void createTracks() {
        setDemoFinished(false);
        Log.d("progressvalue", FitnessActivity.progress + "");


        final float seriesMax = 100f;
        final DecoView decoView = getDecoView();
        final View view = getView();
        if (decoView == null || view == null) {
            return;
        }
        decoView.deleteAll();
        decoView.configureAngles(mTotalAngle[mStyleIndex], mRotateAngle[mStyleIndex]);

        SeriesItem arcBackTrack = new SeriesItem.Builder(Color.argb(255, 228, 228, 228))
                .setRange(0, seriesMax,seriesMax)
                .setInitialVisibility(false)
                .setLineWidth(getDimension  (mTrackBackWidth[mStyleIndex]))
                .setChartStyle(mPie[mStyleIndex] ? SeriesItem.ChartStyle.STYLE_PIE : SeriesItem.ChartStyle.STYLE_DONUT)
                .build();

        mBackIndex = decoView.addSeries(arcBackTrack);

        float inset = 0;
        if (mTrackBackWidth[mStyleIndex] != mTrackWidth[mStyleIndex]) {
            inset = getDimension((mTrackBackWidth[mStyleIndex] - mTrackWidth[mStyleIndex]) / 2);
        }
        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 255, 165, 0))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(mTrackWidth[mStyleIndex]))
                .setInset(new PointF(-inset, -inset))
                .setSpinClockwise(mClockwise[mStyleIndex])
                .setCapRounded(mRounded[mStyleIndex])
                .setChartStyle(mPie[mStyleIndex] ? SeriesItem.ChartStyle.STYLE_PIE : SeriesItem.ChartStyle.STYLE_DONUT)
                .build();

        mSeries1Index = decoView.addSeries(seriesItem1);

        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(255, 255, 51, 51))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .setCapRounded(true)
                .setLineWidth(getDimension(mTrackWidth[mStyleIndex]))
                .setInset(new PointF(inset, inset))
                .setCapRounded(mRounded[mStyleIndex])
                .build();

        mSeries2Index = decoView.addSeries(seriesItem2);

        final TextView textPercent = (TextView) view.findViewById(R.id.textPercentage);
        if (textPercent != null) {
            textPercent.setText("");
            // addProgressListener(seriesItem1, textPercent, "%.0f%%");
            DecimalFormat df = new DecimalFormat("###.##");
            String calories = (df.format(CalorieConsumed.progress*100/goalcal));
            Log.d("decimalcal",calories);
            if (goalcal != 0) {
                textPercent.setText(calories + "%");
            } else {
                DecimalFormat df2 = new DecimalFormat("###.##");
                String defat=df2.format(CalorieConsumed.progress/100);
                textPercent.setText(defat  + "%");
            }
        }

        final TextView textToGo = (TextView) view.findViewById(R.id.textRemaining);
        textToGo.setText("");
        //   addProgressRemainingListener(seriesItem1, textToGo, "%.0f min to goal", seriesMax);
        // addProgressRemainingListener(seriesItem1, textToGo, "10 miles to goal" + "\n" + FitnessActivity.progress + "-Miles Today ", seriesMax);
        addProgressRemainingListener(seriesItem1,textToGo,goalcal+" calories to Goal"+"\n"+Math.round(CalorieConsumed.progress)+"\n"+"Calorie burned"+"\n"+ "Today",seriesMax);
        View layout = getView().findViewById(R.id.layoutActivities);
        layout.setVisibility(View.INVISIBLE);
        //  View layout2 = getView().findViewById(R.id.layoutActivities2);
        //layout2.setVisibility(View.INVISIBLE);

        final TextView textActivity1 = (TextView) getView().findViewById(R.id.textActivity1);
        // addProgressListener(seriesItem1, textActivity1, "%.0f Km");
        textActivity1.setText("Calorie Burned "+"\n"+"Today is "+Math.round(CalorieConsumed.progress) +"");
/*
        final TextView textActivity2 = (TextView) getView().findViewById(R.id.textActivity22);
        textActivity2.setText("");
        addProgressListener(seriesItem2, textActivity2, "%.0f Km");
  */  }

    @Override
    protected void setupEvents() {
        final DecoView decoView = getDecoView();
        final View view = getView();
        if (decoView == null || decoView.isEmpty() || view == null) {
            return;
        }

        mUpdateListeners = true;
        final TextView textPercent = (TextView) view.findViewById(R.id.textPercentage);
        final TextView textToGo = (TextView) view.findViewById(R.id.textRemaining);
        final View layout = view.findViewById(R.id.layoutActivities);
        final View[] linkedViews = {textPercent, textToGo, layout};
        final int fadeDuration = 2000;

        if (mPie[mStyleIndex]) {
            decoView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                    .setIndex(mBackIndex)
                    .setDuration(2000)
                    .build());
        } else {
            decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT_FILL)
                    .setIndex(mBackIndex)
                    .setDuration(3000)
                    .build());

            decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                    .setIndex(mSeries1Index)
                    .setFadeDuration(fadeDuration)
                    .setDuration(2000)
                    .setDelay(1000)
                    .build());
        }

        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries2Index)
                .setLinkedViews(linkedViews)
                .setDuration(2000)
                .setDelay(1100)
                .build());
        if (goalcal != 0) {
            id = Math.round(CalorieConsumed.progress * 100)/goalcal;
        }
        else{
            id = CalorieConsumed.progress/100;
        }

        decoView.addEvent(new DecoEvent.Builder(id).setIndex(mSeries2Index).setDelay(3900).build());
        decoView.addEvent(new DecoEvent.Builder(id).setIndex(mSeries2Index).setDelay(7000).build());

        decoView.addEvent(new DecoEvent.Builder(50).setIndex(mSeries1Index).setDelay(3300).build());
        decoView.addEvent(new DecoEvent.Builder(100).setIndex(mSeries1Index).setDuration(1500).setDelay(9000).build());
        decoView.addEvent(new DecoEvent.Builder(0).setIndex(mSeries1Index).setDuration(500).setDelay(10500)
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent event) {
                        mUpdateListeners = false;
                    }

                    @Override
                    public void onEventEnd(DecoEvent event) {

                    }
                })
                .setInterpolator(new AccelerateInterpolator()).build());

        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                .setLinkedViews(linkedViews)
                .setIndex(mSeries1Index)
                .setDelay(11000)
                .setDuration(1200)
                .setDisplayText("GOAL!")
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent event) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent event) {
                        mStyleIndex++;
                        if (mStyleIndex >= mTrackBackWidth.length) {
                            mStyleIndex = 0;
                            setDemoFinished(true);
                            return;
                        }

                        createTracks();
                        setupEvents();
                    }
                })
                .build());
    }
}