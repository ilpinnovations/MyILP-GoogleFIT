package com.ilp.ilpschedule;

import android.content.Context;
import android.opengl.EGLConfig;
import android.opengl.GLU;
import android.util.Log;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.interfaces.OpenGLRenderer;
import edu.dhbw.andar.util.GraphicsUtil;

public class CustomRenderer implements OpenGLRenderer {

	private Square      square;    
	private float[] ambientlight1 = {.3f, .3f, .3f, 1f};
	private float[] diffuselight1 = {.7f, .7f, .7f, 1f};
	private float[] specularlight1 = {0.6f, 0.6f, 0.6f, 1f};
	private float[] lightposition1 = {20.0f,-40.0f,100.0f,1f};
	private Context context;

	private FloatBuffer lightPositionBuffer1 =  GraphicsUtil.makeFloatBuffer(lightposition1);
	private FloatBuffer specularLightBuffer1 = GraphicsUtil.makeFloatBuffer(specularlight1);
	private FloatBuffer diffuseLightBuffer1 = GraphicsUtil.makeFloatBuffer(diffuselight1);
	private FloatBuffer ambientLightBuffer1 = GraphicsUtil.makeFloatBuffer(ambientlight1);
	

	public final void draw(GL10 gl) {
	}

	public CustomRenderer()
	{
		 this.square     = new Square();  
	}
	public void onDrawFrame(GL10 gl) {
		
			        // clear Screen and Depth Buffer
		
			        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		
		
			 
		
			        // Reset the Modelview Matrix
		
			        gl.glLoadIdentity();
		
			 
		
			        // Drawing
		
			        gl.glTranslatef(0.0f, 0.0f, -5.0f);     // move 5 units INTO the screen
		
			                                                // is the same as moving the camera 5 units away
		
			        square.draw(gl);                        // Draw the triangle
		
			 
		
			    }
		
			 
		
			    public void onSurfaceChanged(GL10 gl, int width, int height) {
		
			        if(height == 0) {                       //Prevent A Divide By Zero By
		
			            height = 1;                         //Making Height Equal One
		
			        }
		
			 
		
			        gl.glViewport(0, 0, width, height);     //Reset The Current Viewport
		
			        gl.glMatrixMode(GL10.GL_PROJECTION);    //Select The Projection Matrix
		
			        gl.glLoadIdentity();                    //Reset The Projection Matrix
		
			 
		
			        //Calculate The Aspect Ratio Of The Window
		
			        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
		
			 
		
			        gl.glMatrixMode(GL10.GL_MODELVIEW);     //Select The Modelview Matrix
		
			        gl.glLoadIdentity();                    //Reset The Modelview Matrix
		
			    }
		
			 
		
			    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
			    Log.d("inside", "onsurface created");
			    	/*square.loadGLTexture(gl, this.context);
			    
			    	
			    	    gl.glEnable(GL10.GL_TEXTURE_2D);            //Enable Texture Mapping ( NEW )
			    	
			    	    gl.glShadeModel(GL10.GL_SMOOTH);            //Enable Smooth Shading
			    	
			    	    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    //Black Background
			    	
			    	    gl.glClearDepthf(1.0f);                     //Depth Buffer Setup
			    	
			    	    gl.glEnable(GL10.GL_DEPTH_TEST);            //Enables Depth Testing
			    	
			    	    gl.glDepthFunc(GL10.GL_LEQUAL);             //The Type Of Depth Testing To Do
			    	
			    	 
			    	
			    	    //Really Nice Perspective Calculations
			    	
			    	    gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
*/
			    	
			    }
		
			
	
	/**
	 * Directly called before each object is drawn. Used to setup lighting and
	 * other OpenGL specific things.
	 */
	public final void setupEnv(GL10 gl) {
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, ambientLightBuffer1);
		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, diffuseLightBuffer1);
		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_SPECULAR, specularLightBuffer1);
		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, lightPositionBuffer1);
		gl.glEnable(GL10.GL_LIGHT1);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    gl.glDisable(GL10.GL_TEXTURE_2D);
		initGL(gl);
	}
	
	/**
	 * Called once when the OpenGL Surface was created.
	 */
	public final void initGL(GL10 gl) {
		gl.glDisable(GL10.GL_COLOR_MATERIAL);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glDisable(GL10.GL_COLOR_MATERIAL);
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_NORMALIZE);
	}
}
