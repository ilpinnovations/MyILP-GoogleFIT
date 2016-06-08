package com.ilp.ilpschedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.ilp.ilpschedule.adapter.DrawerAdapter;
import com.ilp.ilpschedule.model.DrawerItemViewHolder;
import com.ilp.ilpschedule.model.Employee;
import com.ilp.ilpschedule.model.SlotViewHolder;
import com.ilp.ilpschedule.util.CalorieConsumed;
import com.ilp.ilpschedule.util.Constants;
import com.ilp.ilpschedule.util.FitnessActivity;
import com.ilp.ilpschedule.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity {
    public static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggler;
    private DrawerLayout drawerLayout;
    private DrawerAdapter drawerAdapter;
    private String currentTitle;
    private OnClickListener drawerItemClickListner;
    ;
    private OnClickListener scheduleItemClickListner;
    private boolean navigateBackToFragment = false;
    Button safe, help;
    EditText phno, helpquery;
    ImageButton helpsubmit;
    Dialog dialog;
    static Map<String, String> myparams = null;
    Dialog dialog2;
    int j;
    String qury = "";
    Employee employee;
    SharedPreferences chennai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        web_update();

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new ScheduleFragment(),
                            ScheduleFragment.TAG).commit();

            String valid_until = "01/02/2016";
            SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
            try {
                Date strDate = sdf.parse(valid_until);
                Date d = new Date();
                j = d.compareTo(strDate);

                {

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            employee = Util.getEmployee(this);
            currentTitle = getString(R.string.title_schedule);
        } else {
            currentTitle = savedInstanceState.getString("title");
        }

        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
        if (drawerAdapter == null)
            drawerAdapter = new DrawerAdapter(getApplicationContext(),
                    R.layout.drawer_item,
                    Util.getDrawerItemList(getApplicationContext()),
                    getDrawerItemClickListner());
        if (drawerList == null) {
            drawerList = (ListView) findViewById(R.id.listViewDrawerMenu);
            drawerList.setAdapter(drawerAdapter);
        }
        if (drawerLayout == null)
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        if (drawerToggler == null) {
            drawerToggler = new ActionBarDrawerToggle(this, drawerLayout,
                    toolbar, R.string.opne, R.string.close) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    if (getTitle().toString().equalsIgnoreCase(
                            getString(R.string.app_name)))
                        setTitle(currentTitle);

                    hide_keyboard_from(MainActivity.this, drawerView);

                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    currentTitle = getTitle().toString();
                    setTitle(R.string.app_name);
                    hide_keyboard_from(MainActivity.this, drawerView);
                    super.onDrawerOpened(drawerView);
                }

            };
        }
        drawerLayout.setDrawerListener(drawerToggler);
        drawerToggler.syncState();
        Util.resetProgressDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {

            logout();
            return true;
        } else if (id == R.id.Ibuzz) {
            Intent r = new Intent(MainActivity.this, IBUZZER.class);
            startActivity(r);
        } else if (id == R.id.safety) {

            dialog = new Dialog(MainActivity.this);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.chenna_alert);
            dialog.setCancelable(true);
                /*safe = (Button) dialog.findViewById(R.id.safe);*/
            help = (Button) dialog.findViewById(R.id.help);
            dialog.show();
				/*safe.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						MyAsyncTask task = new MyAsyncTask();
						task.execute("http://theinspirer.in/iBuzzer/chennai_inser_safe.php?emp_id=" + employee.getEmpId());
					}
				});*/


            help.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();

                    dialog2 = new Dialog(MainActivity.this);
                    dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog2.setContentView(R.layout.chennai_alert_help);
                    dialog2.setCancelable(true);

                    helpsubmit = (ImageButton) dialog2.findViewById(R.id.helpsubmit);
                    phno = (EditText) dialog2.findViewById(R.id.contact);
                    helpquery = (EditText) dialog2.findViewById(R.id.helpquery);
                    dialog2.show();
                    helpsubmit.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String contact = phno.getText().toString();
                            qury = helpquery.getText().toString();

                            String body = "TCS SAFETY FIRST \n \n You have an emergency message from Employee name: " + employee.getName() + " , EmpID :" + employee.getEmpId() + " ,located at: " + employee.getLocation() + ", Regarding: " + qury;
                            if (employee.getLocation().equals("Trivandrum")) {


                                sendSMSMessage("9249440406", body);
                                sendSMSMessage("9995470741", body);
                                sendSMSMessage("8907905830", body);

                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + "9249440406"));

                                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    startActivity(intent);
                                    return;
                                }

                            } else if (employee.getLocation().equals("Chennai")) {

                                sendSMSMessage("9840015200", body);
                                sendSMSMessage("9249440406", body);
                                sendSMSMessage("9884808019", body);

                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + "9249440406"));
                                startActivity(intent);
                            } else if (employee.getLocation().equals("Guwahati")) {
                                sendSMSMessage("9249440406", body);
                                sendSMSMessage("9207063135", body);
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + "9249440406"));
                                startActivity(intent);

                            } else if (employee.getLocation().equals("Ahmedabad")) {
                                sendSMSMessage("9249440406", body);
                                sendSMSMessage("9825702038", body);
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + "9249440406"));
                                startActivity(intent);

                            } else if (employee.getLocation().equals("Hyderabad")) {

                                sendSMSMessage("9246290041", body);
                                sendSMSMessage("9249440406", body);
                                sendSMSMessage("8886028661", body);
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + "9249440406"));
                                startActivity(intent);
                            }
                            if (helpquery.getText().toString().length() != 0) {
                                myparams = new HashMap<>();
                                myparams.put("emp_id", String.valueOf(employee.getEmpId()));
                                myparams.put("emp_name", employee.getName());
                                myparams.put("contact", contact);
                                myparams.put("email", employee.getEmail());
                                myparams.put("query", qury);

                                MyAsyncTask2 task = new MyAsyncTask2();

                                task.execute("http://theinspirer.in/iBuzzer/chennai_inser_help.php");
                            } else {
                                helpquery.setError("Problem descriptions is required !");
                            }
                        }
                    });
                }
            });
        } else if (id == R.id.rateme) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.ilp.ilpschedule" + "&hl=en"));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", currentTitle);
    }

    private void changeFragment(String tag, Bundle bundle) {
        Fragment fragment;
        fragment = getFragmentManager().findFragmentByTag(tag);

        if (fragment == null) {
            switch (tag) {
                case ScheduleFragment.TAG:
                    fragment = new ScheduleFragment();
                    currentTitle = getString(R.string.title_schedule);
                    break;
                case BadgeFragment.TAG:
                    fragment = new BadgeFragment();
                    currentTitle = getString(R.string.title_badges);
                    break;
                case ContactFragment.TAG:
                    fragment = new ContactFragment();
                    currentTitle = getString(R.string.title_contacts);
                    break;
                case NotificationFragment.TAG:
                    fragment = new NotificationFragment();
                    currentTitle = getString(R.string.title_notification);
                    break;
                case FeedbackFragment.TAG:
                    fragment = new FeedbackFragment();
                    currentTitle = getString(R.string.title_feedback);
                    navigateBackToFragment = true;
                    break;
                case LocationActivity.TAG:
                    startActivity(new Intent(getApplicationContext(),
                            LocationActivity.class));
                    currentTitle = getString(R.string.title_location);
                    break;


                case "AR LEARNING":
                    SharedPreferences sdf = getSharedPreferences("tutorial", Context.MODE_PRIVATE);
                    boolean shown = sdf.getBoolean("shown", false);

                    if (shown != false) {
                        Intent k = new Intent(MainActivity.this, com.ilp.ilpschedule.Activity.class);
                        startActivity(k);
                    } else {
                        Intent k = new Intent(MainActivity.this, Tutorial.class);
                        startActivity(k);
                    }

                    break;
                case FitnessActivity.TAG:
                    startActivity(new Intent(getApplicationContext(),
                            CalorieConsumed.class));
                    currentTitle = getString(R.string.title_activity_fitness);
                    break;

            }
        }
        if (tag.equalsIgnoreCase(FeedbackFragment.TAG))
            ((FeedbackFragment) fragment).setData(bundle);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager()
                    .beginTransaction().replace(R.id.container, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    public OnClickListener getScheduleItemClickListner() {
        if (scheduleItemClickListner == null) {
            scheduleItemClickListner = new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Employee emp = Util.getEmployee(getApplicationContext());
                    SlotViewHolder svh = (SlotViewHolder) view.getTag();
                    String emp_id = svh.getFacultyContent().getText()
                            .toString().replaceAll("[^0-9]", "");
                    Bundle bundle = new Bundle();
                    if (emp_id != null && emp_id.trim().length() > 0) {
                        if (Integer.parseInt(emp_id) == emp.getEmpId()) {
                            bundle.putBoolean(
                                    Constants.BUNDLE_KEYS.FEEDBACK_FRAGMENT.IS_FACULTY,
                                    true);
                        }
                    }
                    bundle.putCharSequence(
                            Constants.BUNDLE_KEYS.FEEDBACK_FRAGMENT.FACULTY,
                            svh.getFacultyContent().getText());
                    bundle.putCharSequence(
                            Constants.BUNDLE_KEYS.FEEDBACK_FRAGMENT.COURSE, svh
                                    .getCourseContent().getText());
                    bundle.putLong(
                            Constants.BUNDLE_KEYS.FEEDBACK_FRAGMENT.SLOT_ID,
                            svh.getId());
                    changeFragment(FeedbackFragment.TAG, bundle);
                }
            };
        }
        return scheduleItemClickListner;
    }

    public OnClickListener getDrawerItemClickListner() {
        if (drawerItemClickListner == null) {
            drawerItemClickListner = new OnClickListener() {
                @Override
                public void onClick(View view) {
                    DrawerItemViewHolder dvh = (DrawerItemViewHolder) view
                            .getTag();
                    drawerLayout.closeDrawers();
                    changeFragment(dvh.getTag(), null);
                }
            };
        }
        return drawerItemClickListner;
    }

    private void logout() {
        Util.clearPref(MainActivity.this);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
        System.exit(0);
    }

    @Override
    public void onBackPressed() {
        if (navigateBackToFragment) {
            getFragmentManager().popBackStack();
            navigateBackToFragment = false;
        } else
            super.onBackPressed();
    }

    public static void hide_keyboard_from(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private class MyAsyncTask extends AsyncTask<String, Void, JSONObject> {
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("Submitting your safety status");
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
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject O = arr.getJSONObject(i);
                    Toast.makeText(getApplicationContext(), O.getString("message"), Toast.LENGTH_LONG).show();
                }

                dialog.cancel();
                SharedPreferences sd = getSharedPreferences("chennai", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sd.edit();
                edit.putBoolean("response", true);
                edit.commit();
            } catch (Exception e) {

                Toast.makeText(getApplicationContext(), "Connection Problem.Make sure your internet connection is up.Try again", Toast.LENGTH_LONG).show();

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

    private class MyAsyncTask2 extends AsyncTask<String, Void, JSONObject> {
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("Please wait..");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            String msg = "";
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
                    if (status == 200) {
                        InputStream i = conn.getInputStream();
                        String responseString = readStream(conn.getInputStream());

                        Log.v("CatalogClient", responseString);

                        obj = new JSONObject(responseString);
                    } else {
                        msg = "Failure";
                    }
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj;
        }

        @Override
        protected void onPostExecute(JSONObject obj) {
            super.onPostExecute(obj);
            try {
                JSONArray arr = obj.getJSONArray("data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject O = arr.getJSONObject(i);

                    Toast.makeText(getApplicationContext(), "We will reach you immediately", Toast.LENGTH_LONG).show();
                }

                dialog2.cancel();

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

    protected void sendSMSMessage(String number, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(msg);
            smsManager.sendMultipartTextMessage(number, null, parts, null, null);

            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void web_update() {
        try {
            MyAsyncTask3 m = new MyAsyncTask3();
            m.execute("https://play.google.com/store/apps/details?id=" + "com.ilp.ilpschedule" + "&hl=en");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class MyAsyncTask3 extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String msg = "";
            try {
                String curVersion = getApplicationContext().getPackageManager().getPackageInfo("com.ilp.ilpschedule", 0).versionName;
                String newVersion = curVersion;
                newVersion = Jsoup.connect(params[0])
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();

                if (value(curVersion) < value(newVersion)) {
                    msg = "update";

                } else {
                    msg = "dontupdate";
                }
            } catch (Exception e) {
                msg = "fail";
                e.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String abc) {
            super.onPostExecute(abc);

            if (abc.equals("update")) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("Update available")
                        .setMessage("An update for MyILP is available on Play Store.Please update to experience all the new features")
                        .setNegativeButton("Update now", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            /* User clicked OK so do some stuff */
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.ilp.ilpschedule" + "&hl=en"));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("Later", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            /* User clicked Cancel */
                                dialog.cancel();
                            }
                        })

                        .show();

            } else {
                Log.d("failure", "failure");
            }

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

    private long value(String string) {
        string = string.trim();
        if (string.contains(".")) {
            final int index = string.lastIndexOf(".");
            return value(string.substring(0, index)) * 100 + value(string.substring(index + 1));
        } else {
            return Long.valueOf(string);
        }
    }
}