package com.ilp.ilpschedule;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class Cameraview extends Activity implements SurfaceHolder.Callback {
static int count=0;

      Camera camera=null ;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean preview = false;
    TextView t1;
     Animation myAnim;
    Button b1,b2,b3,b4;
    boolean allowed=false;
    String stringPath = "/sdcard/samplevideo.3gp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameraview);

        b1 = (Button) findViewById(R.id.ilp);
        b2 = (Button) findViewById(R.id.mac);
        b3 = (Button) findViewById(R.id.cas);
        b4 = (Button) findViewById(R.id.alg);

        t1 = (TextView) findViewById(R.id.welcome);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera != null) {
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                    preview = false;

                }
                Intent i = new Intent(Cameraview.this, ILpinnovations.class);
                startActivity(i);


            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera != null) {
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                    preview = false;

                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=UzxYlbK2c7E&list=PLA89DCFA6ADACE599"));
                startActivity(browserIntent);

            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera != null) {
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                    preview = false;

                }
                Intent i = new Intent(Cameraview.this, WebCaseStudy.class);
                startActivity(i);


            }
        });


        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera != null) {
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                    preview = false;

                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://machinelearningmastery.com/tutorial-to-implement-k-nearest-neighbors-in-python-from-scratch/"));
                startActivity(browserIntent);

            }
        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cameraview, menu);
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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

try {
    if(!preview ) {
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                camera = Camera.open();
                Configuration c = getResources().getConfiguration();

                if(c.orientation == Configuration.ORIENTATION_PORTRAIT ) {
                   camera.setDisplayOrientation(90);
                }
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1000);



    }
}
catch (Exception e)
{
    e.printStackTrace();
}

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


        if (camera == null || preview==false){
            try {
                new Handler().postDelayed(new Runnable() {


                    @Override
                    public void run() {
                        camera.startPreview();

                        preview = true;

                    }
                }, 1000);


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }




    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                if(camera!=null) {
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                    preview = false;
                    count = 2;

                }
            }
        }, 100);


    }


    /*@Override
    protected void onPause() {
        if(camera!=null) {
            camera.stopPreview();
            camera.release();
            camera = null;
            preview = false;
            super.onPause();
        }
    }

    @Override
    protected void onResume() {
       if(camera==null) {
           this.camera = Camera.open();

           try {
               camera.setPreviewDisplay(surfaceHolder);
               camera.startPreview();

               preview = true;
           } catch (IOException e) {
               e.printStackTrace();
           }
       }

        super.onResume();

    }*/

    @Override
    public void onBackPressed() {
        if(camera!=null) {
            camera.stopPreview();
            camera.release();
            camera = null;
            preview = false;

        }
        finish();
        System.exit(0);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
}
