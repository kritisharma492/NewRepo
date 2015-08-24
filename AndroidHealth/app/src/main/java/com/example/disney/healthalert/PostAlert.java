package com.example.disney.healthalert;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URLEncoder;

public class PostAlert extends AppCompatActivity {
    private Button Postclick;
    public String alertTopic,alertStory;
    private EditText topic,desc;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 5;
    private static final long MINIMUM_TIME_BETWEEN_UPDATES =100;
    protected LocationManager locationManager;
    public double applat,applongi;
    public String Utopic,UStory;
    private TextView tv9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_alert);
        Postclick=(Button)findViewById(R.id.button3);
        topic=(EditText)findViewById(R.id.editText3);
        desc=(EditText)findViewById(R.id.editText4);
        tv9=(TextView)findViewById(R.id.editText9);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());


    }

    private class MyLocationListener implements LocationListener {



        public void onLocationChanged(Location location) {

            String message = String.format(" service \n Longitude: %1$s \n Latitude: %2$s", location.getLongitude(), location.getLatitude());

            //Toast.makeText(PostAlert.this, message, Toast.LENGTH_SHORT).show();

        }



        public void onStatusChanged(String s, int i, Bundle b) {

            //Toast.makeText(PostAlert.this, "Provider status changed", Toast.LENGTH_LONG).show();

        }



        public void onProviderDisabled(String s) {

            Toast.makeText(PostAlert.this, "Provider disabled by the user. GPS turned off", Toast.LENGTH_LONG).show();

        }



        public void onProviderEnabled(String s) {

            Toast.makeText(PostAlert.this, "Provider enabled by the user. GPS turned on", Toast.LENGTH_LONG).show();

        }



    }


    protected void onResume() {
        super.onResume();

        Postclick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    applat=location.getLatitude();
                    applongi=location.getLongitude();
                }

                alertTopic = topic.getText().toString();
                if(TextUtils.isEmpty(alertTopic)) {
                    topic.setError("Add Topic");
                    return;
                }

                //Toast.makeText(getBaseContext(),alertTopic,Toast.LENGTH_SHORT).show();
                alertStory = desc.getText().toString();
                if(TextUtils.isEmpty(alertStory)) {
                    desc.setError("Add description");
                    return;
                }
                Utopic=URLEncoder.encode(alertTopic);
                UStory=URLEncoder.encode(alertStory);

                String serverURL = GlobalVar.Urlinitial+"post?topic=" + Utopic + "&story=" + UStory + "&email=" + GlobalVar.loginValue + "&lat="+applat+"&longi="+applongi;

                //Toast.makeText(getBaseContext(), serverURL, Toast.LENGTH_SHORT).show();
                new LongOperation().execute(serverURL);


            }
        });



    }

    private class LongOperation  extends AsyncTask<String, Void, Void> {
        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;




        protected void onPreExecute() {

            //Toast.makeText(getBaseContext(),"Processing...",Toast.LENGTH_SHORT).show();


        }


        protected Void doInBackground(String... urls) {
            try {


                HttpGet httpget = new HttpGet(urls[0]);


                ResponseHandler<String> responseHandler = new BasicResponseHandler();

                Content = Client.execute(httpget, responseHandler);



            } catch (ClientProtocolException e) {
                Error = e.getMessage();
                cancel(true);
            } catch (IOException e) {
                Error = e.getMessage();
                cancel(true);
            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            //Toast.makeText(getBaseContext(),Content,Toast.LENGTH_SHORT).show();
            if (Error != null) {

                Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_SHORT);


            } else {
                    Intent intent = new Intent(getApplicationContext(),Alertsdekho.class);
                    startActivity(intent);
            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_alert, menu);
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
