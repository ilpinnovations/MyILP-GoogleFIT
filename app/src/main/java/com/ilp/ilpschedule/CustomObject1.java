package com.ilp.ilpschedule;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLUtils;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import edu.dhbw.andar.ARObject;
import edu.dhbw.andar.pub.SimpleBox;
import edu.dhbw.andar.util.GraphicsUtil;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
@SuppressLint("NewApi")
public class CustomObject1 extends ARObject {
static MediaPlayer mp1;
	Context context;
	Resources res1;
	public CustomObject1(String name, String patternName,
			double markerWidth, double[] markerCenter,MediaPlayer mp,Resources res) {
		super(name, patternName, markerWidth, markerCenter);
		mp1=mp;
		/////////////////////////////////////////////////////////
		res1=res;
		float   mat_ambientf[]     = {1.0f, 0f, 0f, 1.0f};
		float   mat_flashf[]       = {1.0f, 0f, 0f, 1.0f};
		float   mat_diffusef[]       = {1.0f, 0f, 0f, 1.0f};
		float   mat_flash_shinyf[] = {50.0f};
		////////////////////////////////////////////////////////
		
		mat_ambient = GraphicsUtil.makeFloatBuffer(mat_ambientf);
		mat_flash = GraphicsUtil.makeFloatBuffer(mat_flashf);
		mat_flash_shiny = GraphicsUtil.makeFloatBuffer(mat_flash_shinyf);
		mat_diffuse = GraphicsUtil.makeFloatBuffer(mat_diffusef);
		
	}
	public CustomObject1(String name, String patternName,
			double markerWidth, double[] markerCenter, float[] customColor) {
		super(name, patternName, markerWidth, markerCenter);
		float   mat_flash_shinyf[] = {50.0f};

		mat_ambient = GraphicsUtil.makeFloatBuffer(customColor);
		mat_flash = GraphicsUtil.makeFloatBuffer(customColor);
		mat_flash_shiny = GraphicsUtil.makeFloatBuffer(mat_flash_shinyf);
		mat_diffuse = GraphicsUtil.makeFloatBuffer(customColor);
		
	}
	private Square square=new Square();
	private SimpleBox box = new SimpleBox();
	private FloatBuffer mat_flash;
	private FloatBuffer mat_ambient;
	private FloatBuffer mat_flash_shiny;
	private FloatBuffer mat_diffuse;

	@SuppressLint("NewApi")
	@Override
	public final void draw(GL10 gl) {
		super.draw(gl);
		
		/*gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR,mat_flash);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, mat_flash_shiny);	
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mat_diffuse);	
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mat_ambient);
	
	    gl.glColor4f(1.0f, 0, 0, 1.0f);
	    gl.glTranslatef( 0.0f, 0.0f, 12.5f );*/
	    //VideoActivity v=new VideoActivity();
	    
	    Log.d("inside", "initdraw");
	    gl.glEnable(GL10.GL_TEXTURE_2D);			//texture binding
	    int[] textureIDs = new int[2];
	   		gl.glGenTextures(1, textureIDs, 0);
	   		SurfaceTexture st = new SurfaceTexture(textureIDs[0]);
	   	 	MediaPlayer mp = new MediaPlayer();
	   	 	mp.setSurface(new Surface(st));
	   		
	       //load the textures into the graphics memory
	   		//Resources res = this.getContext().getResources();
	   		
	   		Bitmap bm = BitmapFactory.decodeResource(res1, R.drawable.playbtn);
	   		Log.d("bm value", String.valueOf(bm));
	   		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);
	   		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm, 0);
	   		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
	   		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

	   		bm.recycle();
	   		//mp1.start();
	    square.draw(gl);
	}
	
	
	@Override
	public void init(GL10 gl) {
		/*Log.d("inside","init");
gl.glEnable(GL10.GL_TEXTURE_2D);
 int[] textureIDs = new int[1];
		gl.glGenTextures(1, textureIDs, 0);
		//load the textures into the graphics memory
		//Resources res = this.getContext().getResources();
		Log.d("________________>","))))))))))");
		Bitmap bm = BitmapFactory.decodeResource(res1, R.drawable.attcs);
		Log.d("bm value",String.valueOf(bm));
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm,0);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		bm.recycle();*/
	}
}
