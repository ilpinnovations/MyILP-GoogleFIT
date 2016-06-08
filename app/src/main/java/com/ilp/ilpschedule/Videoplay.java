package com.ilp.ilpschedule;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class Videoplay extends Activity {
Button skip;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
final VideoView videoView =(VideoView)findViewById(R.id.videoView2);

        //Creating MediaController  
MediaController mediaController= new MediaController(this);
    mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);
 skip=(Button)findViewById(R.id.skip);
		skip.setVisibility(View.INVISIBLE);

		new Handler().postDelayed(new Runnable() {


			@Override
			public void run() {
				skip.setVisibility(View.VISIBLE);
			}

			},10000);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				videoView.stopPlayback();
                Intent u=new Intent(Videoplay.this,Cameraview.class);
				startActivity(u);
                finish();

            }
        });
      //specify the location of media file  
           
   String uriPath = "android.resource://com.ilp.ilpschedule/raw/big";
   Uri uri = Uri.parse(uriPath);
   //video.setVideoURI(uri);
      //Setting MediaController and URI, then starting the videoView  
   /*videoView.setMediaController(mediaController);  */
   videoView.setVideoURI(uri);          
   videoView.requestFocus();  
   videoView.start();

		videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				//Start a new activity or do whatever else you want here
				Intent u=new Intent(Videoplay.this,Cameraview.class);
				startActivity(u);
                finish();
				mp.release();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.videoplay, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		/*if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}
}
