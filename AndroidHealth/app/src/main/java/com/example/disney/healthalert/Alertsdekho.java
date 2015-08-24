package com.example.disney.healthalert;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class Alertsdekho extends AppCompatActivity {
public ListView myList;
    public  ArrayAdapter adapter;
    private Button AlertClick;
    public Location location, dlocation;
    private TextView Hidisplay;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 5;
    private static final long MINIMUM_TIME_BETWEEN_UPDATES =100;
    protected LocationManager locationManager;
    protected Button retrieveLocationButton;
    public  ArrayList<String> list = new ArrayList<String>();
    public String[] myArr;
    public String[] myStory,Username;
    public Double[] myLat,myLongi;
   public String serverURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alertsdekho);
       // Hidisplay=(TextView)findViewById(R.id.textView9);
        //Hidisplay.setText( GlobalVar.loginValue);

        AlertClick = ( Button )findViewById(R.id.button5) ;


       myList= (ListView) findViewById(R.id.listView);




         adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
       //myList.setAdapter(adapter);

        retrieveLocationButton = (Button) findViewById(R.id.button4);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());

        //retrieveLocationButton.setOnClickListener(new View.OnClickListener() {

            //@Override
            //public void onClick(View v) {
                showCurrentLocation();

                 serverURL = GlobalVar.Urlinitial+"refresh";
               // Toast.makeText(getBaseContext(), serverURL, Toast.LENGTH_LONG).show();
                new LongOperation().execute(serverURL);


           // }
       // });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent i = new Intent(getApplicationContext(), ShowAlert.class);
               i.putExtra("Topic",myArr[position]);
                i.putExtra("Desc",myStory[position]);
                i.putExtra("Username",Username[position]);
                startActivity(i);

            }
        });

    }

    private class LongOperation  extends AsyncTask<String, Void, Void> {
        private final HttpClient Client = new DefaultHttpClient();

        private String Content;
        private String Error = null;




        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //UI Element
            //Toast.makeText(getBaseContext(),"Processing...",Toast.LENGTH_SHORT).show();


        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            try {

                // Call long running operations here (perform background computation)
                // NOTE: Don't call UI Element here.

                // Server url call by GET method
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


            }
            else {


                Toast.makeText(getBaseContext(),"Updating List",Toast.LENGTH_SHORT).show();
               try {
                   JSONArray arr = new JSONArray(Content);
                   Double chklat,chklongi;
                   //Toast.makeText(getBaseContext(),Content.toString(),Toast.LENGTH_SHORT).show();

                   int j=0;
                   dlocation=new Location("dlocation");
                  myArr = new String[arr.length()];
                    myStory = new String[arr.length()];
                   myLat = new Double[arr.length()];
                   myLongi = new Double[arr.length()];
                   Username=new String[arr.length()];
                  // Toast.makeText(getBaseContext(),"length"+arr.length(),Toast.LENGTH_SHORT).show();
                   for (int i = 0; i < arr.length(); i++) {
                       JSONObject obj = arr.getJSONObject(i);
                      if(obj!=null) {
                           chklat = obj.getDouble("Lat");
                           chklongi = obj.getDouble("Longi");
                           dlocation.setLatitude(chklat);
                           dlocation.setLongitude(chklongi);
                          //Toast.makeText(getBaseContext(),"dist"+location.distanceTo(dlocation),Toast.LENGTH_SHORT).show();
                           if ((location != null) && (location.distanceTo(dlocation) < 40000)) {
                               myArr[j] = obj.getString("Topic");
                               myStory[j] = obj.getString("Story");
                               myLat[j] = obj.getDouble("Lat");
                               myLongi[j] = obj.getDouble("Longi");
                               Username[j]=obj.getString("Email");
                               j++;
                           } else {
                               continue;
                           }
                          // Toast.makeText(getBaseContext(),myStory[j],Toast.LENGTH_SHORT).show();
                       }

                   }

                   for ( int i = 0; i < j; ++i) {


                       list.add(0,myArr[i]);
                  }
                   myList.setAdapter(adapter);


               }
               catch (Exception e)
               {
                   Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();
               }
                //Intent intent = new Intent(getApplicationContext(),Alertsdekho.class);
                //startActivity(intent);


            }
        }

    }

    protected void showCurrentLocation() {



       location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);



        if (location != null) {

            String message = String.format(" \n Longitude: %1$s \n Latitude: %2$s", location.getLongitude(), location.getLatitude());

            Toast.makeText(Alertsdekho.this, message, Toast.LENGTH_SHORT).show();

        }

    }

    private class MyLocationListener implements LocationListener {



        public void onLocationChanged(Location location) {

            String message = String.format(" service \n Longitude: %1$s \n Latitude: %2$s", location.getLongitude(), location.getLatitude());

            Toast.makeText(Alertsdekho.this, message, Toast.LENGTH_SHORT).show();

        }



        public void onStatusChanged(String s, int i, Bundle b) {

            Toast.makeText(Alertsdekho.this, "Provider status changed", Toast.LENGTH_LONG).show();

        }



        public void onProviderDisabled(String s) {

            Toast.makeText(Alertsdekho.this, "Provider disabled by the user. GPS turned off", Toast.LENGTH_LONG).show();

        }



        public void onProviderEnabled(String s) {

            Toast.makeText(Alertsdekho.this, "Provider enabled by the user. GPS turned on", Toast.LENGTH_LONG).show();

        }



    }



    protected void onResume() {
        super.onResume();
        AlertClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), PostAlert.class);
                startActivity(intent);

            }
        });
        retrieveLocationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                showCurrentLocation();
                new LongOperation().execute(serverURL);


            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_alertsdekho, menu);
        //return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        /*switch (item.getItemId()) {
            case R.id.action_compose:
            {Intent intent = new Intent(getApplicationContext(),Myself.class);
                startActivity(intent);
                return true;}*/


                return super.onOptionsItemSelected(item);


        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
         //   return true;
       // }

        //return super.onOptionsItemSelected(item);
    }
}
