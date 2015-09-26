package com.example.sunnysummer5.budgie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    TextView textView, dollarSign;
    EditText editText;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.budget_text);
        dollarSign = (TextView)findViewById(R.id.dollarSign);
        editText = (EditText)findViewById(R.id.editText);
        next = (Button)findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AQuery aq = new AQuery(getApplicationContext());
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("language","unk");
                hashMap.put("detectOrientation","true");
                String url="";
                aq.ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
                    @Override
                    public void callback(String _url, JSONArray json, AjaxStatus status) {
                        if(json != null) {
                            try {
                                System.out.println(json.get(0).toString());
                            } catch (JSONException e) {
                                System.out.println("error");
                                e.printStackTrace();
                            }
                        }
                    }
                });

                Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                intent.putExtra("Budget", editText.getText());
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
