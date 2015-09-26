package com.example.sunnysummer5.budgie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    TextView textView, dollarSign;
    EditText editText;
    Button next;
    static final String URL = "http://api.projectoxford.ai/vision/v1/ocr?language=unk&detectOrientation =true";
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AQUtility.debug(true);
        textView = (TextView)findViewById(R.id.income_text);
        dollarSign = (TextView)findViewById(R.id.dollarSign);
        editText = (EditText)findViewById(R.id.editText);
//        wv = (WebView)findViewById(R.id.webview);
//        wv.getSettings().setJavaScriptEnabled(true);
//        wv.loadUrl("http://www.google.com");
        next = (Button)findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AQuery aQuery = new AQuery(getApplicationContext());
                AjaxCallback acb = new AjaxCallback<JSONArray>(){
                @Override
                public void callback(String _url, JSONArray json, AjaxStatus status) {
                    if (json != null) {
                        try {
                            System.out.println(json.get(0).toString());
                        } catch (JSONException e) {
                            System.out.println("error");
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("code: "+status.getCode());
                        System.out.println("error: "+status.getError());
                        System.out.println("msg: "+status.getMessage());
                        System.out.println("null json");
                    }
                }
            };
                //byte[] temp = getBytesFromBitmap("@mipmap/ic_launcher");
                JSONObject jso = new JSONObject();
                StringEntity se = null;
                try {
                    jso.put("Url","http://www.antigrain.com/research/font_rasterization/msword_text_rendering.png");
                    se = new StringEntity(jso.toString(), HTTP.UTF_8);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                HashMap<String,Object> params = new HashMap<String, Object>();
                params.put(AQuery.POST_ENTITY, se);
                //params.put("Content-Type","application/x-www-form-urlencoded");
                //params.put("Ocp-Apim-Subscription-Key","1baee771e6aa47998831f21b634a03ad");
                //params.put("source","http://www.antigrain.com/research/font_rasterization/msword_text_rendering.png");
                //params.put("source", temp);
                acb.header("Ocp-Apim-Subscription-Key", "1baee771e6aa47998831f21b634a03ad");
                //acb.headers(params);

//                params.put("language","unk");
//                params.put("detectOrientation","true");
                aQuery.ajax(URL, params, JSONArray.class, acb);


                Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                intent.putExtra("Budget", editText.getText().toString());
                startActivity(intent);
            }
        });

    }
    public byte[] getBytesFromBitmap(String fileName) {
        File imageFile = new File("");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imageFile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("COULD NOT FIND FILE");
            e.printStackTrace();
        }

        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
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
