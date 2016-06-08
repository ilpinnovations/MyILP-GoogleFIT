package com.ilp.ilpschedule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class Question_display extends Activity {
TextView question;
    RadioGroup rg;
    RadioButton rb1,rb2,rb3,rb4;
    RadioButton rdbtn;
    static int option=0;
    ImageButton submit;
    ImageView back;
    static int correctanswer=0;
    int opt=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_display);


      final   Questions q=(Questions) getIntent().getSerializableExtra("question");
        correctanswer=q.getCorrect_ans();
back =(ImageView)findViewById(R.id.backbtn);
        question=(TextView)findViewById(R.id.dispques);
        rb1=(RadioButton)findViewById(R.id.rb1);
        rb2=(RadioButton)findViewById(R.id.rb2);
        rb3=(RadioButton)findViewById(R.id.rb3);
        rb4=(RadioButton)findViewById(R.id.rb4);
        if(q.getOption3().equals("") || q.getOption4().equals("null"))
        {
            rb3.setVisibility(View.GONE);
        }

         if(q.getOption4().equals("") || q.getOption4().equals("null"))
        {
            rb4.setVisibility(View.GONE);
        }

           rg=(RadioGroup)findViewById(R.id.rg);
        submit= (ImageButton) findViewById(R.id.submitbtn);
question.setText(q.getQuestion());
        rb1.setText(""+q.getOption1());
        rb2.setText(""+q.getOption2());
        rb3.setText(""+q.getOption3());
        rb4.setText(""+q.getOption4());
back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i=new Intent(Question_display.this,IBUZZER.class);
        startActivity(i);
        finish();
    }
});
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = rg.getCheckedRadioButtonId();

                if(rb1.isChecked())
                {
                    opt=1;
                }
                else if(rb2.isChecked())
                {
                    opt=2;
                }
                else if(rb3.isChecked())
                {
                    opt=3;
                }
                else if(rb4.isChecked())
                {
                    opt=4;
                }


               MyAsyncTask task = new MyAsyncTask();
                task.execute("http://theinspirer.in/iBuzzer/answer.php?q_id=" +q.getQues_id() + "&ans_option="+opt+"&emp_id="+ Util.getEmployee(Question_display.this).getEmpId());


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question_display, menu);
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
            mProgressDialog = new ProgressDialog(Question_display.this);
            mProgressDialog.setMessage("Submitting your answer..");
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
            try {
                JSONArray arr = obj.getJSONArray("data");

                    JSONObject O = arr.getJSONObject(0);

if(opt==correctanswer) {
    Toast.makeText(getApplicationContext(), "Your Answer is getting recorded"+" \r\n Your answer is correct", Toast.LENGTH_LONG).show();
}
                else
{
    Toast.makeText(getApplicationContext(), "Your Answer is getting recorded" + " \r\n Your answer is Incorrect", Toast.LENGTH_LONG).show();
}

                finish();


            } catch (Exception ex) {
                ex.printStackTrace();
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
