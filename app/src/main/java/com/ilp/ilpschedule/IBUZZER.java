package com.ilp.ilpschedule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ilp.ilpschedule.adapter.FacultyAdapter;
import com.ilp.ilpschedule.adapter.MyBaseAdapter;
import com.ilp.ilpschedule.model.Employee;
import com.ilp.ilpschedule.model.Questions;
import com.ilp.ilpschedule.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class IBUZZER extends ActionBarActivity {
ListView lvDetail;
    ImageView back;
    TextView welcm;
    public static boolean pickfacultylayout=false;
    Animation animation1,animation2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ibuzzer);
        back =(ImageView)findViewById(R.id.backbbtn);
      Employee emp= Util.getEmployee(this);
        lvDetail = (ListView) findViewById(R.id.queslistview);
        if (isNetworkAvailable(IBUZZER.this)) {
            MyAsyncTask task = new MyAsyncTask();
            task.execute("http://theinspirer.in/iBuzzer/is_faculty.php?emp_id=" + emp.getEmpId());
        } else {
            Toast.makeText(getApplicationContext(),"Please turn on data connection",Toast.LENGTH_LONG).show();
            finish();
        }

welcm=(TextView)findViewById(R.id.welcome);
        welcm.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IBUZZER.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(1000);
        animation1.setStartOffset(1000);
        animation1.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation2 when animation1 ends (continue)
                welcm.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

        });
        animation2 = new AlphaAnimation(1.0f, 0.0f);
        animation2.setDuration(1000);
        animation2.setStartOffset(1000);

        //animation2 AnimationListener
        animation2.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation1 when animation2 ends (repeat)
                welcm.startAnimation(animation1);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

        });

        welcm.startAnimation(animation1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ibuzzer, menu);
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
    private class MyAsyncTask extends AsyncTask<String,Void,JSONObject> {
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(IBUZZER.this);
            mProgressDialog.setMessage("Loading..");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            super.onPreExecute();



        }

        @Override
        protected JSONObject doInBackground(String... params) {


            URL url;
            HttpURLConnection urlConnection = null;
            JSONArray response = new JSONArray();

            JSONObject obj = new JSONObject();
            try {
                url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();

                if (responseCode == 200) {

                    InputStream i = urlConnection.getInputStream();
                    String responseString = readStream(urlConnection.getInputStream());

                    Log.v("CatalogClient", responseString);

                    obj = new JSONObject(responseString);
                    //response = new JSONArray(responseString);
                } else {
                    Log.v("CatalogClient", "Response code:" + responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
                if (urlConnection != null)
                    urlConnection.disconnect();
            }

            return obj;


        }

        @Override
        protected void onPostExecute(JSONObject obj) {
            super.onPostExecute(obj);

            ArrayList<Questions> questionlist = new ArrayList<Questions>();

            try {
                JSONArray arr = obj.getJSONArray("data_faculty");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject O = arr.getJSONObject(i);
                    Questions q=new Questions(O.getInt("emp_id"),O.getInt("ques_id"),O.getString("status"),O.getString("option1"),O.getString("option2"),O.getString("option3"),O.getString("option4"),O.getString("question"),O.getString("submit_time"),O.getInt("correct_ans"),O.getString("emp_name"),O.getString("emp_loc"),O.getInt("views"));
              questionlist.add(q);
                    pickfacultylayout=true;

                }


            } catch (Exception e) {
                try {
                    JSONArray arr = obj.getJSONArray("data_employee");
                    int rcount=0;
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject O = arr.getJSONObject(i);
                        Questions q= new Questions(O.getInt("emp_id"),O.getInt("ques_id"), O.getString("status"),O.getString("option1"),O.getString("option2"),O.getString("option3"),O.getString("option4"), O.getString("question"), O.getString("submit_time"), O.getInt("correct_ans"),O.getString("emp_name"),O.getString("emp_loc"),O.getInt("views"));
                        questionlist.add(q);
                        rcount++;
                        pickfacultylayout=false;

                    }
if(rcount==0)
{

    finish();

    Toast.makeText(getApplicationContext(),"No questions live now !",Toast.LENGTH_LONG).show();
}

                } catch (Exception ex) {
ex.printStackTrace();
                }
            }
            if(pickfacultylayout==false) {
                lvDetail.setAdapter(new MyBaseAdapter(IBUZZER.this, questionlist));
            }
            else
            {
                lvDetail.setAdapter(new FacultyAdapter(IBUZZER.this, questionlist));
            }
            mProgressDialog.dismiss();

        }
        private String readStream(InputStream in) throws UnsupportedEncodingException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuilder sb = new StringBuilder();
            try {

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return sb.toString();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        MyAsyncTask task = new MyAsyncTask();
        task.execute("http://theinspirer.in/iBuzzer/is_faculty.php?emp_id=" + Util.getEmployee(IBUZZER.this).getEmpId());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
