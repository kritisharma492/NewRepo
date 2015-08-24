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


public class MainActivity extends AppCompatActivity {
    private Button btnClick;
    private Button signUp;
    private EditText enterId;
    private EditText enterPass;
    private TextView errorMsg;
   // public String loginPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        btnClick = ( Button )findViewById(R.id.button) ;
        enterId = (EditText)findViewById(R.id.editText);
        enterPass=(EditText)findViewById(R.id.editText2);
        signUp=(Button)findViewById(R.id.button2);
        errorMsg=(TextView)findViewById(R.id.textView7);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    protected void onResume() {
        super.onResume();

        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Myself.class);
                startActivity(intent);

            }
        });











        btnClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                GlobalVar.loginValue = enterId.getText().toString();
                if(TextUtils.isEmpty(GlobalVar.loginValue)) {
                    enterId.setError("");
                    errorMsg.setText("Empty Username field");
                    return;
                }
                if (GlobalVar.loginValue.contains(" ")) {
                    enterId.setError("");
                    errorMsg.setText("No space allowed");
                    return;

                }
                GlobalVar.loginPass = enterPass.getText().toString();
                if(TextUtils.isEmpty(GlobalVar.loginPass)) {
                    enterPass.setError("");
                    errorMsg.setText("Empty Password field");
                    return;
                }
                if (GlobalVar.loginValue.length() < 5)
                {
                    enterPass.setError("");
                    errorMsg.setText("Enter atleast 5 characters");
                }


                // Create http client object to send request to server
                // Create URL string



                String serverURL=GlobalVar.Urlinitial+"login?email="+GlobalVar.loginValue+"&pass="+GlobalVar.loginPass;

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
            // NOTE: You can call UI Element here.

            // Close progress dialog


            if (Error != null) {

                Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_SHORT);


            } else {

                    if(Content.equals("0"))
                    {errorMsg.setText("Invalid Credentials");}
                   else
                    {Toast.makeText(getBaseContext(),"Welcome  "+GlobalVar.loginValue,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),Alertsdekho.class);
                        startActivity(intent);}
            }
        }

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
