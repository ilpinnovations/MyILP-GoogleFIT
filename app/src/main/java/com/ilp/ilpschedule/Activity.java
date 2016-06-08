package com.ilp.ilpschedule;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import edu.dhbw.andar.ARObject;
import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.exceptions.AndARException;


public class Activity extends AndARActivity {
		static int cnt=0;
    Camera camera ;
		private ARObject someObject;
	private ARToolkit artoolkit;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Context context=getBaseContext();
		Resources res=context.getResources();
		super.onCreate(savedInstanceState);
		CustomRenderer renderer = new CustomRenderer();
		setNonARRenderer(renderer);
		getSurfaceView().setOnTouchListener(new TouchEventHandler());
		getSurfaceView().getHolder().addCallback(this);
		
		try {
			artoolkit = getArtoolkit();
			 MediaPlayer mp = MediaPlayer.create(this, R.raw.big);
			
			
			
			someObject = new CustomObject2
			("test", "marker_peace16.patt", 80.0, new double[]{0,0});
			artoolkit.registerARObject(someObject);
			
			someObject = new CustomObject3
			("test", "marker_rupee16.patt", 80.0, new double[]{0,0});
			artoolkit.registerARObject(someObject);
			
			someObject = new CustomObject4
			("test", "marker_hand16.patt", 80.0, new double[]{0,0});
			artoolkit.registerARObject(someObject);
			someObject = new CustomObject1
					("test", "marker16.patt", 80.0, new double[]{0,0},mp,res);
					artoolkit.registerARObject(someObject);
		} catch (AndARException ex){
			System.out.println("");
		}		
		startPreview();

	}
	
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e("AndAR EXCEPTION", ex.getMessage());
		/*VideoActivity v1=new VideoActivity();
		v1.play();*/
		//finish();
	}
class TouchEventHandler implements OnTouchListener {
    	
    	private float lastX=0;
    	private float lastY=0;


		public boolean onTouch(View v, MotionEvent event) {
			Log.d("Ontouch", "success");

			if(cnt==0&&someObject.isVisible())
			{
                Cameraview.count++;

			Intent i=new Intent(Activity.this,Videoplay.class);
			startActivity(i);
finish();

			cnt++;

			}
			
			return true;
		}
    	
    }


}