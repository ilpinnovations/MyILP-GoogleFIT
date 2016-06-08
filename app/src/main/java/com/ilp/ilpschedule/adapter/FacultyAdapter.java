package com.ilp.ilpschedule.adapter;

/**
 * Created by kaustav on 11/30/2015.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ilp.ilpschedule.EditQuestions;
import com.ilp.ilpschedule.R;
import com.ilp.ilpschedule.model.Questions;

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

/**
 * Created by kaustav on 11/29/2015.
 */
public class FacultyAdapter extends BaseAdapter {
    ArrayList<Questions> myList = new ArrayList<Questions>();
    LayoutInflater inflater;
    Context context;


    public FacultyAdapter(Context context, ArrayList<Questions> myList) {
        this.myList = myList;
        this.context = context;

    }

    @Override
    public int getCount() {
        return myList.size();

    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        final Questions q=myList.get(position);

        if (convertView == null) {
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.questionfaculty,null);

            }

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }


        mViewHolder = new MyViewHolder(convertView);
        convertView.setTag(mViewHolder);
        mViewHolder.tvquesfac.setText("Q." + (position + 1) + " :" + q.getQuestion());

        mViewHolder.tvquesfac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k=new Intent(context, EditQuestions.class);
                k.putExtra("question",q);
                context.startActivity(k);
            }
        });


        if(q.getStatus().equals("Active"))
        {
            mViewHolder.active.setChecked(true);
        }
        else if(q.getStatus().equals("Inactive"))
        {
            mViewHolder.active.setChecked(false);
        }
        mViewHolder.active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Questions q = myList.get(position);
                    MyAsyncTask task = new MyAsyncTask();
                    task.execute("http://theinspirer.in/iBuzzer/update_status.php?q_id=" + q.getQues_id() + "&status=" + 1);
                } else {
                    Questions q = myList.get(position);
                    MyAsyncTask task = new MyAsyncTask();
                    task.execute("http://theinspirer.in/iBuzzer/update_status.php?q_id=" + q.getQues_id() + "&status=" + 2);

                }
            }
        });


        return convertView;
    }
    private class MyViewHolder {
        TextView tvquestn,tvquesfac,tvempname,tvemploc,tvviews;
        Switch active;

        public MyViewHolder(View item) {
            tvquestn = (TextView) item.findViewById(R.id.ques);
            tvquesfac =(TextView) item.findViewById(R.id.quesfac);
            active=(Switch)item.findViewById(R.id.active);
            tvempname=(TextView)item.findViewById(R.id.name);
            tvemploc=(TextView)item.findViewById(R.id.loc);
            tvviews=(TextView)item.findViewById(R.id.views);
        }
    }
    private class MyAsyncTask extends AsyncTask<String,Void,JSONObject> {
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Updating Question Status..");
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

            Toast.makeText(context, "Status Updated", Toast.LENGTH_LONG).show();
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
