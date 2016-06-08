package com.ilp.ilpschedule.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.hardware.Camera;
import android.os.PowerManager;
import android.provider.SyncStateContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.ilp.ilpschedule.R;
import com.ilp.ilpschedule.db.DataBaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class HeartDataType extends AppCompatActivity {

    private final String TAG = "HeartRateMonitor";
    private final AtomicBoolean processing = new AtomicBoolean(false);

    private SurfaceView preview = null;
    private SurfaceHolder previewHolder = null;
    private Camera camera = null;
    private View image = null;
    private ProgressWheel pw;
    private int MAX = 360;
    private int currentBeats = 0;
    private int[] hrms = new int[4];
    private int hrmCount = 0;
    private TextView txtResult;

    private PowerManager.WakeLock wakeLock = null;

    private int averageIndex = 0;
    private final int averageArraySize = 4;
    private final int[] averageArray = new int[averageArraySize];
    boolean isupdate=false;
    public enum TYPE {
        GREEN, RED
    };

    private static TYPE currentType = TYPE.GREEN;

    public static TYPE getCurrent() {
        return currentType;
    }

    private int beatsIndex = 0;
    private final int beatsArraySize = 3;
    private final int[] beatsArray = new int[beatsArraySize];
    private double beats = 0;
    private long startTime = 0;
    TextView textView1;
    DataBaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_data_type);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbarfit);
        setSupportActionBar(toolbar);
        ImageView img1=(ImageView)findViewById(R.id.cross);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeartDataType.this, CalorieConsumed.class);
                startActivity(intent);
                finish();
            }
        });
        ImageView img2=(ImageView)findViewById(R.id.img2);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeartDataType.this, Daysvise.class);
                startActivity(intent);
                finish();
            }
        });
        ImageView img3=(ImageView)findViewById(R.id.img3);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeartDataType.this, Calorieconsumedgraph.class);
                startActivity(intent);
                finish();
            }
        });
        ImageView img4=(ImageView)findViewById(R.id.img4);
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeartDataType.this, HeartRateChart.class);
                startActivity(intent);
                finish();
            }
        });
        ImageView dist=(ImageView)findViewById(R.id.dist);
        dist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeartDataType.this, DistanceView.class);
                startActivity(intent);
                finish();
            }
        });

        TextView heartchat=(TextView)findViewById(R.id.heartchart);
        heartchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HeartDataType.this,HeartRateChart.class);
                startActivity(intent);
                finish();
            }
        });
        // getSupportActionBar().setTitle("HeartRate");
        mydb=new DataBaseHelper(HeartDataType.this);
        // textView1=(TextView)findViewById(R.id.tview4);


        preview = (SurfaceView) findViewById(R.id.preview);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        image = findViewById(R.id.image);
        pw = (ProgressWheel)findViewById(R.id.pw_spinner);
        txtResult = (TextView)findViewById(R.id.txt_average);
        pw.setText(String.valueOf(currentBeats));

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");

        TextView textView=(TextView)findViewById(R.id.textView6);
      //  textView.setText("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_heart_data_type, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void update(int beat) {
        pw.setProgress(beat);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();
        try {
            wakeLock.acquire();

            camera = Camera.open();

            startTime = System.currentTimeMillis();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Please enable Camera permissions from app setings", Toast.LENGTH_LONG).show();

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();
        try {


            wakeLock.release();

            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Please enable Camera permissions from app setings",Toast.LENGTH_LONG).show();

        }
    }
    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null) throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();

            if (!processing.compareAndSet(false, true)) return;

            int width = size.width;
            int height = size.height;

            int imgAvg = com.ilp.ilpschedule.util.ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width);
            // Log.i(TAG, "imgAvg="+imgAvg);
            if (imgAvg == 0 || imgAvg == 255) {
                processing.set(false);
                return;
            }

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
            TYPE newType = currentType;
            if (imgAvg < rollingAverage) {
                newType = TYPE.RED;
                if (newType != currentType) {
                    beats++;
                    // Log.d(TAG, "BEAT!! beats="+beats);
                }
            } else if (imgAvg > rollingAverage) {
                newType = TYPE.GREEN;
            }

            if (averageIndex == averageArraySize) averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            // Transitioned from one state to another to the same
            if (newType != currentType) {
                currentType = newType;
                image.postInvalidate();
            }

            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            if (totalTimeInSecs >= 10) {
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180) {
                    startTime = System.currentTimeMillis();
                    beats = 0;
                    processing.set(false);
                    return;
                }

                // Log.d(TAG,
                // "totalTimeInSecs="+totalTimeInSecs+" beats="+beats);

                if (beatsIndex == beatsArraySize) beatsIndex = 0;
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;

                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                if (hrmCount < 4){
                    pw.setText(String.valueOf(beatsAvg));
                    hrms[hrmCount] = beatsAvg;
                    hrmCount +=1;
                    if (currentBeats < MAX){
                        currentBeats += 90;
                        update(currentBeats);
                    }

                    if (hrmCount == 4){
                        try{
                            Thread.sleep(10000);
                        }catch (Exception e){

                        }
                        int totalHrm = 0;
                        for (int i = 0; i < hrms.length; i++){
                            totalHrm += hrms[i];
                        }

                        int hrmAvg = totalHrm/4;
                        String status = (hrmAvg <= 100 && hrmAvg> 60) ? "Normal" : "Abnormal";
                        boolean isNormal = (hrmAvg <= 100 && hrmAvg> 60) ? true : false;
                        txtResult.setText(String.valueOf(hrmAvg) + " (" + status + ")");
                        showHrmDialog(hrmAvg, isNormal);
                    }
                }
                startTime = System.currentTimeMillis();
                beats = 0;
            }
            processing.set(false);
        }
    };

    private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                Log.e("surfaceCallback", "Exception in setPreviewDisplay()", t);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            try {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                Camera.Size size = getSmallestPreviewSize(width, height, parameters);
                if (size != null) {
                    parameters.setPreviewSize(size.width, size.height);
                    Log.d(TAG, "Using width=" + size.width + " height=" + size.height);
                }
                camera.setParameters(parameters);
                camera.startPreview();
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Please enable Camera permissions from app setings", Toast.LENGTH_LONG).show();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
        }
    };

    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea < resultArea) result = size;
                }
            }
        }

        return result;
    }

    public static void toHeartRateMonitor(Activity activity){
        Intent intent = new Intent(activity, HeartRateMonitor.class);
        activity.startActivity(intent);
    }

    private void showHrmDialog(final int hrmPoint, final boolean isNormal){
        String status = isNormal ? "Normal" : "Abnormal";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HeartDataType.this);
        // set title
        alertDialogBuilder.setTitle("HRM Result");
        // set dialog message
        alertDialogBuilder
                .setMessage("Your Heart Rate value is "+hrmPoint+" \n "+ "Do you really want to submit?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        //  AddNewVitalSignActivity.toAddNewVitalSign(HeartRateMonitor.this, hrmPoint, isNormal);
                        Calendar calendar=Calendar.getInstance();
                        Date now=new Date();
                        calendar.setTime(now);

                        String sdf = new SimpleDateFormat("yyyy-MM-dd").format(now);

                        String[] parts = sdf.split("-");
                        String part1 = parts[0]; // 004
                        String part2 = parts[1];
                        String part3=parts[2];

                        Cursor cursor=mydb.getAllData();
                        for(int i=0;cursor.moveToNext();i++)
                        {
                            if(cursor.getInt(0)==Integer.parseInt(parts[2])&&cursor.getInt(2)==Integer.parseInt(parts[1]) && cursor.getInt(3) == Integer.parseInt(parts[0]))
                            {

                                mydb.updateData(Integer.parseInt(parts[2]), hrmPoint, Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
                                Log.d("updateheartrate",hrmPoint+""+Integer.parseInt(parts[2]) + Integer.parseInt(parts[1]) + Integer.parseInt(parts[0]) + "");
                                isupdate=true;
                            }
                        }
                        if(!isupdate) {
                            mydb.insertData(Integer.parseInt(parts[2]), hrmPoint, Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
                            Log.d("insertdata", parts[0] + " " + parts[1] + " " + parts[2]);
                        }

                        Intent intent=new Intent(HeartDataType.this,HeartRateChart.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing

                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(HeartDataType.this,CalorieConsumed.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent=new Intent(HeartDataType.this,CalorieConsumed.class);
        startActivity(intent);
        finish();
    }
}
