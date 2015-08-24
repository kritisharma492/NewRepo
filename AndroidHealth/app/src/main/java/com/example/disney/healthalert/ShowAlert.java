package com.example.disney.healthalert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class ShowAlert extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alert);
        EditText textv;
        EditText textd;
        TextView tv1,tv2;

        textv = (EditText)findViewById(R.id.editText12);
        textd = (EditText)findViewById(R.id.editText13);
        tv1=(TextView)findViewById(R.id.textView10);
       // tv2=(TextView)findViewById(R.id.textView11);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String j =(String) b.get("Topic");
            textv.setText(j);
            String d =(String) b.get("Desc");
            textd.setText(d);
            String e =(String) b.get("Username");
            tv1.setText(e);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_alert, menu);
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
