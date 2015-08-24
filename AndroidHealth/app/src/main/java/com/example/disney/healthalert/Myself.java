package com.example.disney.healthalert;

import android.content.Intent;
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

public class Myself extends AppCompatActivity {
    private Button saveClick;
    private EditText Memail,Mpass,Mname,Mradius;
    private String Semail,Spass,Sname,Sage,Sgender,Sradius,Sadd;
    private TextView erMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself);
        saveClick=(Button)findViewById(R.id.button6);
        Mpass=(EditText)findViewById(R.id.editText11);
       // Mradius=(EditText)findViewById(R.id.editText10);
        Memail=(EditText)findViewById(R.id.editText9);
        Mname=(EditText)findViewById(R.id.editText5);
        erMsg=(TextView)findViewById(R.id.textView8);


    }

    protected void onResume() {
        super.onResume();
        saveClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Sname = URLEncoder.encode(Mname.getText().toString());
                Semail = Memail.getText().toString();
                if (Semail.contains(" ")) {
                    Memail.setError("");
                    erMsg.setText("No space allowed");
                    return;

                }
                if (Semail.length() < 5)
                {  Memail.setError("");
                    erMsg.setText("Enter atleast 5 characters");
                }
               // Sradius = Mradius.getText().toString();
                Spass = URLEncoder.encode(Mpass.getText().toString());
                if (Spass.length() < 5)
                {
                    Mpass.setError("");
                    erMsg.setText("Enter atleast 5 characters");
                }
               // Toast.makeText(getBaseContext(),Spass,Toast.LENGTH_SHORT).show();




                String serverURL = GlobalVar.Urlinitial+"signup?Name=" + Sname  + "&email=" + Semail + "&pass=" + Spass;
               // Toast.makeText(getBaseContext(), serverURL, Toast.LENGTH_LONG).show();
                new LongOperation().execute(serverURL);


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
            Toast.makeText(getBaseContext(),"Processing...",Toast.LENGTH_SHORT).show();


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
            // NOTE: You can call UI Element here.

            // Close progress dialog

           // {//Toast.makeText(getBaseContext(),Content,Toast.LENGTH_SHORT).show();}
            if (Error != null) {

                Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_SHORT);


            } else {
                if(Content.equals("0"))
                {
                    erMsg.setText("Username already exists");
                    Memail.setError("");}

                else
                {Toast.makeText(getBaseContext(),"Saved Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Alertsdekho.class);
                    startActivity(intent);}
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_myself, menu);
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
