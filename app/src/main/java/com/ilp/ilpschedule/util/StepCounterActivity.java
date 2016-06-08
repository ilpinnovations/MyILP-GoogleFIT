package com.ilp.ilpschedule.util;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataDeleteRequest;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.ilp.ilpschedule.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

/**
 * This sample demonstrates how to use the History API of the Google Fit platform to insert data,
 * query against existing data, and remove data. It also demonstrates how to authenticate
 * a user with Google Play Services and how to properly represent data in a {@link DataSet}.
 */
public class StepCounterActivity extends AppCompatActivity {
    public static final String TAG = "BasicHistoryApi";
    private static final int REQUEST_OAUTH = 1;
    private static final String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";
    //   ArrayList<Long> steps=new ArrayList<Long>();


    /**
     * Track whether an authorization activity is stacking over the current activity, i.e. when
     * a known auth error is being resolved, such as showing the account chooser or presenting a
     * consent dialog. This avoids common duplications as might happen on screen rotations, etc.
     */
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    public static String reference = "day";
    public GoogleApiClient mClient = null;
    public static int progress;
    String flag = "hi";
    private int n = 0;
    public static long[] steps = new long[32];
    long[] date = new long[32];
    long[] month = new long[12];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        // This method sets up our custom logger, which will print all log messages to the device
        // screen, as well as to adb logcat.
        initializeLogging();


        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }


        buildFitnessClient();
    }


    /**
     * Build a {@link GoogleApiClient} that will authenticate the user and allow the application
     * to connect to Fitness APIs. The scopes included should match the scopes your app needs
     * (see documentation for details). Authentication will occasionally fail intentionally,
     * and in those cases, there will be a known resolution, which the OnConnectionFailedListener()
     * can address. Examples of this include the user never having signed in before, or
     * having multiple accounts on the device and needing to specify which account to use, etc.
     */
    public void buildFitnessClient() {
        // Create the Google API Client

        //  Intent i=getIntent();

        mClient = new GoogleApiClient.Builder(StepCounterActivity.this)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(Bundle bundle) {
                                Log.i(TAG, "Connected!!!");
                                // Now you can make calls to the Fitness APIs.  What to do?
                                // Look at some data!!
                                new InsertAndVerifyDataTask().execute();
                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                // If your connection to the sensor gets lost at some point,
                                // you'll be able to determine the reason and react to it here.
                                if (i == ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                    Log.i(TAG, "Connection lost.  Cause: Network Lost.");
                                } else if (i == ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                    Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
                                }
                            }
                        }
                )
                .enableAutoManage(this, 0, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.i(TAG, "Google Play services connection failed. Cause: " +
                                result.toString());
                        Snackbar.make(
                                StepCounterActivity.this.findViewById(R.id.main_activity_view),
                                "Exception while connecting to Google Play services: " +
                                        result.getErrorMessage(),
                                Snackbar.LENGTH_LONG).show();
                    }
                })
                .build();
    }

    /**
     * Create a {@link DataSet} to insert data into the History API, and
     * then create and execute a {@link DataReadRequest} to verify the insertion succeeded.
     * By using an {@link AsyncTask}, we can schedule synchronous calls, so that we can query for
     * data after confirming that our insert was successful. Using asynchronous calls and callbacks
     * would not guarantee that the insertion had concluded before the read request was made.
     * An example of an asynchronous call using a callback can be found in the example
     * on deleting data below.
     */
    public class InsertAndVerifyDataTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            //First, create a new dataset and insertion request.
            DataSet dataSet = insertFitnessData();

            // [START insert_dataset]
            // Then, invoke the History API to insert the data and await the result, which is
            // possible here because of the {@link AsyncTask}. Always include a timeout when calling
            // await() to prevent hanging that can occur from the service being shutdown because
            // of low memory or other conditions.
            Log.i(TAG, "Inserting the dataset in the History API");
            com.google.android.gms.common.api.Status insertStatus =
                    Fitness.HistoryApi.insertData(mClient, dataSet)
                            .await(1, TimeUnit.MINUTES);

            // Before querying the data, check to see if the insertion succeeded.
            if (!insertStatus.isSuccess()) {
                Log.i(TAG, "There was a problem inserting the dataset.");
                return null;
            }

            // At this point, the data has been inserted and can be read.
            Log.i(TAG, "Data insert was successful!");
            // [END insert_dataset]

            // Begin by creating the query.
            DataReadRequest readRequest = queryFitnessData();

            // [START read_dataset]
            // Invoke the History API to fetch the data with the query and await the result of
            // the read request.
            DataReadResult dataReadResult =
                    Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);
            // [END read_dataset]

            // For the sake of the sample, we'll print the data so we can see what we just added.
            // In general, logging fitness information should be avoided for privacy reasons.
            printData(dataReadResult);

            return null;
        }
    }

    /**
     * Create and return a {@link DataSet} of step count data for the History API.
     */
    public DataSet insertFitnessData() {
        Log.i(TAG, "Creating a new data insert request");

        // [START build_insert_data_request]
        // Set a start and end time for our data, using a start time of 1 hour before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String currentDateandTime = sdf2.format(new Date());
        String string = currentDateandTime;
        String[] parts = string.split("-");
        String part1 = parts[0]; // 004
        String part2 = parts[1];
        String part3 = parts[2];
        String part4 = parts[3];
        String part5 = parts[4];
        String part6 = parts[5];

        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(part4));

        long endTime = System.currentTimeMillis();

        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        long startTime = cal.getTimeInMillis();
        //long startTime=MainActivity.stime;
        // Create a data source
        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(this)
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setName(TAG + " - step count")
                .setType(DataSource.TYPE_RAW)
                .build();

        // Create a data set
        int stepCountDelta = 1;
        DataSet dataSet = DataSet.create(dataSource);
        // For each data point, specify a start time, end time, and the data value -- in this case,
        // the number of new steps.
        DataPoint dataPoint = dataSet.createDataPoint()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        dataPoint.getValue(Field.FIELD_STEPS).setInt(stepCountDelta);
        dataSet.add(dataPoint);
        // [END build_insert_data_request]

        return dataSet;
    }

    /**
     * Return a {@link DataReadRequest} for all step count changes in the past week.
     */
    public DataReadRequest queryFitnessData() {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
/*

*/
        Date n = new Date();
        cal.setTime(n);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String currentDateandTime = sdf.format(new Date());
        String string = currentDateandTime;
        String[] parts = string.split("-");
        String part1 = parts[0]; // 004
        String part2 = parts[1];
        String part3 = parts[2];
        String part4 = parts[3];
        String part5 = parts[4];
        String part6 = parts[5];

        Log.d("cuurenttime", currentDateandTime + "");
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(n);
        cal2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(part4));
        cal2.set(Calendar.MINUTE, Integer.parseInt(part5));
        cal2.set(Calendar.SECOND, Integer.parseInt(part6));
        Log.d("rndtime", n.getDate() + n.getMonth() + n.getHours() + n.getMinutes() + "");

        long endTime = cal2.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH,0);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        //long startTime = cal.getTimeInMillis();
        long startTime = cal.getTimeInMillis();

        //  long startTime=MainActivity.stime;


        java.text.DateFormat dateFormat = getDateInstance();
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest = new DataReadRequest.Builder()
                // The data request can specify multiple data types to return, effectively
                // combining multiple data queries into one call.
                // In this example, it's very unlikely that the request is for several hundred
                // datapoints each consisting of a few steps and a timestamp.  The more likely
                // scenario is wanting to see how many steps were walked per day, for 7 days.
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                        // Analogous to a "Group By" in SQL, defines how data should be aggregated.
                        // bucketByTime allows for a time span, whereas bucketBySession would allow
                        // bucketing by "sessions", which would need to be defined in code.
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        // [END build_read_data_request]

        return readRequest;
    }

    /**
     * Log a record of the query result. It's possible to get more constrained data sets by
     * specifying a data source or data type, but for demonstrative purposes here's how one would
     * dump all the data. In this sample, logging also prints to the device screen, so we can see
     * what the query returns, but your app should not log fitness information as a privacy
     * consideration. A better option would be to dump the data you receive to a local data
     * directory to avoid exposing it to other applications.
     */
    public void printData(DataReadResult dataReadResult) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: "
                    + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet);

                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: "
                    + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet);
            }
        }
        // [END parse_read_data_result]
    }

    // [START parse_dataset]
    public void dumpDataSet(DataSet dataSet) {
        n++;
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));

            Log.d("number", n + "");
            for (Field field : dp.getDataType().getFields()) {

                Log.i(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));

                steps[n] = dp.getValue(field).asInt();
                Log.d("steps", n + " " + steps[n] + "");


                long x = dp.getValue(field).asInt();


                x = x + dp.getValue(field).asInt();
                Log.d(TAG, "count" + x + " ");
                progress = (int) steps[7];
            }
        }

        // mydb.insertData(steps[0],steps[1],steps[2],steps[3],steps[4],steps[5],steps[6],steps[7],steps[8],steps[9],
        //         steps[10],steps[11],steps[12],steps[13],steps[14],steps[15],steps[16],steps[17],steps[18],steps[19],
        //      steps[20],steps[21],steps[22],steps[23],steps[24],steps[25],steps[26],steps[27],steps[28],steps[29],
        //     steps[30],steps[31]);


    }
    // [END parse_dataset]

    /**
     * Delete a {@link DataSet} from the History API. In this example, we delete all
     * step count data for the past 24 hours.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize a custom log class that outputs both to in-app targets and logcat.
     */
    public void initializeLogging() {

    }
}
