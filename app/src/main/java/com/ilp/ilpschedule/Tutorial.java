package com.ilp.ilpschedule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Tutorial extends AppCompatActivity {
Button tut,nxtbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        tut=(Button)findViewById(R.id.tutbtn);
        nxtbtn=(Button)findViewById(R.id.btnnext);
        tut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sdf=getSharedPreferences("tutorial", Context.MODE_PRIVATE);
                SharedPreferences.Editor e=sdf.edit();
                e.putBoolean("shown",true).commit();
                Intent k = new Intent(Tutorial.this, com.ilp.ilpschedule.Activity.class);
              finish();
                startActivity(k);
            }
        });
        nxtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(Tutorial.this, com.ilp.ilpschedule.Activity.class);
                finish();
                startActivity(k);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tutorial, menu);
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
}
