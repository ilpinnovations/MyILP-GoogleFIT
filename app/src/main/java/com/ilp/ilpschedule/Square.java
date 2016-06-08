package com.ilp.ilpschedule;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.util.GraphicsUtil;

public class Square extends Activity {
	public static int cnt=0;
	private int[] textures = new int[1];
	
		
        private FloatBuffer box;
        private FloatBuffer normals;
        public Square() {
                float boxf[] =  {
                                // FRONT
                                -25.0f, -25.0f,  25.0f,
                                 25.0f, -25.0f,  25.0f,
                                -25.0f,  25.0f,  25.0f,
                                 25.0f,  25.0f,  25.0f,
                                // BACK
                                -25.0f, -25.0f, -25.0f,
                                -25.0f,  25.0f, -25.0f,
                                 25.0f, -25.0f, -25.0f,
                                 25.0f,  25.0f, -25.0f,
                                // LEFT
                                -25.0f, -25.0f,  25.0f,
                                -25.0f,  25.0f,  25.0f,
                                -25.0f, -25.0f, -25.0f,
                                -25.0f,  25.0f, -25.0f,
                                // RIGHT
                                 25.0f, -25.0f, -25.0f,
                                 25.0f,  25.0f, -25.0f,
                                 25.0f, -25.0f,  25.0f,
                                 25.0f,  25.0f,  25.0f,
                                // TOP
                                -25.0f,  25.0f,  25.0f,
                                 25.0f,  25.0f,  25.0f,
                                 -25.0f,  25.0f, -25.0f,
                                 25.0f,  25.0f, -25.0f,
                                // BOTTOM
                                -25.0f, -25.0f,  25.0f,
                                -25.0f, -25.0f, -25.0f,
                                 25.0f, -25.0f,  25.0f,
                                 25.0f, -25.0f, -25.0f,
                        };
                float normalsf[] =  {
                                // FRONT
                                0.0f, 0.0f,  1.0f,
                                0.0f, 0.0f,  1.0f,
                                0.0f, 0.0f,  1.0f,
                                0.0f, 0.0f,  1.0f,
                                // BACK
                                0.0f, 0.0f,  -1.0f,
                                0.0f, 0.0f,  -1.0f,
                                0.0f, 0.0f,  -1.0f,
                                0.0f, 0.0f,  -1.0f,
                                // LEFT
                                -1.0f, 0.0f,  0.0f,
                                -1.0f, 0.0f,  0.0f,
                                -1.0f, 0.0f,  0.0f,
                                -1.0f, 0.0f,  0.0f,
                                // RIGHT
                                1.0f, 0.0f,  0.0f,
                                1.0f, 0.0f,  0.0f,
                                1.0f, 0.0f,  0.0f,
                                1.0f, 0.0f,  0.0f,
                                // TOP
                                0.0f, 1.0f,  0.0f,
                                0.0f, 1.0f,  0.0f,
                                0.0f, 1.0f,  0.0f,
                                0.0f, 1.0f,  0.0f,
                                // BOTTOM
                                0.0f, -1.0f,  0.0f,
                                0.0f, -1.0f,  0.0f,
                                0.0f, -1.0f,  0.0f,
                                0.0f, -1.0f,  0.0f,
                        };
                
                box = GraphicsUtil.makeFloatBuffer(boxf);
                normals = GraphicsUtil.makeFloatBuffer(normalsf);
        }
        
        public final void video()
        {
        	Log.d("video", "playing");
        //	Toast.makeText(getApplicationContext(), "testing", Toast.LENGTH_SHORT).show();
        	
        }
        public void draw(javax.microedition.khronos.opengles.GL10 gl) {

        	FloatBuffer textureBuffer;  // buffer holding the texture coordinates
        	
        		float texture[] = {         
        	
        
        		        0.0f, 1.0f,     // top left     (V2)
        
        		        0.0f, 0.0f,     // bottom left  (V1)
        	
        		        1.0f, 1.0f,     // top right    (V4)
        	
        		        1.0f, 0.0f      // bottom right (V3)
        	
        		};
        	
        		
        		
        			    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
        		
        			    byteBuffer.order(ByteOrder.nativeOrder());
        		
        			    textureBuffer = byteBuffer.asFloatBuffer();
        		
        			    textureBuffer.put(texture);
        		
        			    textureBuffer.position(0);
        	 FloatBuffer vertexBuffer;   // buffer holding the vertices
        	    float vertex[] = {
        	    			35.9f, 35.9f,  0.0f,  // 0. left-bottom
        	               -35.9f, 35.9f,  0.0f,  // 1. right-bottom
        	              35.9f,  -35.9f,  0.0f,  // 2. left-top
        	               -35.9f,  -35.9f,  0.0f   // 3. right-top
        	    };

        	    float texel[] = new float[] {
       		         0, 1,
       		         0, 0,
       		         1, 1,
       		         1, 0
       		   };

       		  ByteBuffer vb = ByteBuffer.allocateDirect(vertex.length * 4);
       		  vb.order(ByteOrder.nativeOrder());
       		  vertexBuffer = vb.asFloatBuffer();
       		  vertexBuffer.put(vertex);
       		  vertexBuffer.position(0);

       		  ByteBuffer tb = ByteBuffer.allocateDirect(texel.length * 4);
       		  tb.order(ByteOrder.nativeOrder());
       		  FloatBuffer texelBuffer = tb.asFloatBuffer();
       		  texelBuffer.put(texel);
       		  texelBuffer.position(0);
       		  
       	      gl.glDisable(GL10.GL_BLEND);
       	      gl.glEnable(GL10.GL_TEXTURE_2D);
       	   //   bindTexture(gl); // this is my own funtction but just binds an allocated tex
       	      gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
       	      gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
       	      gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texelBuffer);
       	      gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
       	      gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        }
        /*public final void draw(GL10 gl) {       
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
            
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, box);
            gl.glNormalPointer(GL10.GL_FLOAT,0, normals);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        }*/
}
