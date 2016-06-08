package com.ilp.ilpschedule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ilp.ilpschedule.model.Questions;
import com.ilp.ilpschedule.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditQuestions extends AppCompatActivity {
EditText q,op1,op2,op3,op4;
    Spinner correct;
    Button submit;
    static  Map<String, String> myparams=null;
    int opt=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editquestion);
       final  Questions qq=(Questions) getIntent().getSerializableExtra("question");
        q=(EditText)findViewById(R.id.question);
        correct=(Spinner)findViewById(R.id.correct);
        op1=(EditText)findViewById(R.id.op1);
        op2=(EditText)findViewById(R.id.op2);
        op3=(EditText)findViewById(R.id.op3);
        op4=(EditText)findViewById(R.id.op4);

        q.setText(qq.getQuestion());
        op1.setText(qq.getOption1());
        op2.setText(qq.getOption2());

            op3.setText(qq.getOption3());
            op4.setText(qq.getOption4());
        List<String> categories = new ArrayList<String>();
        categories.add("Choose Correct Option");
        categories.add("Option 1");
        categories.add("Option 2");
        categories.add("Option 3");
        categories.add("Option 4");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        correct.setAdapter(dataAdapter);

        submit=(Button)findViewById(R.id.updatebtn);

correct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selecteditem=correct.getItemAtPosition(position).toString();
        if(selecteditem.equals("Option 1"))
        {
            opt=1;
            Toast.makeText(getApplicationContext(),"Option 1 Selected",Toast.LENGTH_LONG).show();
        }
        else if(selecteditem.equals("Option 2"))
        {
            opt=2;
            Toast.makeText(getApplicationContext(),"Option 2 Selected",Toast.LENGTH_LONG).show();
        }
        else if(selecteditem.equals("Option 3"))
        {
            opt=3;
            Toast.makeText(getApplicationContext(),"Option 3 Selected",Toast.LENGTH_LONG).show();
        }
        else if(selecteditem.equals("Option 4"))
        {
            opt=4;
            Toast.makeText(getApplicationContext(),"Option 4 Selected",Toast.LENGTH_LONG).show();
        }
        else
        {
            opt=0;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (opt == 0) {
                    Toast.makeText(getApplicationContext(), "Choose a valid option", Toast.LENGTH_LONG).show();
                } else {
                    myparams = new HashMap<>();
                    myparams.put("q_id", String.valueOf(qq.getQues_id()));
                    myparams.put("option1", op1.getText().toString());
                    myparams.put("option2", op2.getText().toString());
                    myparams.put("option3", op3.getText().toString());
                    myparams.put("option4", op4.getText().toString());
                    myparams.put("question", q.getText().toString());
                    myparams.put("correct_answer", String.valueOf(opt));
                    MyAsyncTask task = new MyAsyncTask();
                    String question = q.getText().toString().trim();
                    task.execute("http://theinspirer.in/iBuzzer/edit_question.php");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_questions, menu);
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

    private class MyAsyncTask extends AsyncTask<String,Void,String> {
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(EditQuestions.this);
            mProgressDialog.setMessage("Updating..");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            super.onPreExecute();



        }

        @Override
        protected String doInBackground(String... params) {

            String msg="";
            URL url;
            HttpURLConnection urlConnection = null;
            JSONArray response = new JSONArray();

            JSONObject obj = new JSONObject();
            try {
                url = new URL(params[0]);

                String body = Util.getUrlEncodedString(myparams);
                byte[] bytes = body.getBytes();
                HttpURLConnection conn = null;
                try {
                    Log.d("URL", "> " + url);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setFixedLengthStreamingMode(bytes.length);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded;charset=UTF-8");
                    // post the request
                    OutputStream out = conn.getOutputStream();
                    out.write(bytes);
                    out.close();
                    // handle the response
                    int status = conn.getResponseCode();
                    // If response is not success
                    if (status != 200) {
                        msg="ERROR" ;
                        throw new IOException("Post failed with error code "
                                + status);

                    }
                    else
                    {
                        msg="Success" ;
                    }
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return msg;


        }

        @Override
        protected void onPostExecute(String obj) {
            super.onPostExecute(obj);
            try {





                Toast.makeText(getApplicationContext(), "" + obj, Toast.LENGTH_LONG).show();
                Intent u=new Intent(EditQuestions.this,IBUZZER.class);
                finish();
                startActivity(u);



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
}
