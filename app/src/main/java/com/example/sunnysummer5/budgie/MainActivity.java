package com.example.sunnysummer5.budgie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.util.AQUtility;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    TextView textView, dollarSign;
    EditText editText;
    Button next;
    static final String URL = "http://api.projectoxford.ai/vision/v1/ocr?language=unk&detectOrientation =true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AQUtility.debug(true);
        textView = (TextView)findViewById(R.id.income_text);
        dollarSign = (TextView)findViewById(R.id.dollarSign);
        editText = (EditText)findViewById(R.id.editText);
        next = (Button)findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                public void run() {
                        try

                        {
                            URL oracle = new URL("http://api.projectoxford.ai/vision/v1/ocr?language=unk&detectOrientation=true");
                            URLConnection yc = (HttpURLConnection) oracle.openConnection();


                            yc.setRequestProperty("Content-Type", "application/json");
                            yc.setRequestProperty("Ocp-Apim-Subscription-Key", "1baee771e6aa47998831f21b634a03ad");


                            yc.setDoOutput(true);
                            OutputStream out = new BufferedOutputStream(yc.getOutputStream());

//                            BufferedImage image = ImageIO.read(new File("/home/dipper/Downloads/domo1.jpg"));
//                            ByteArrayOutputStream out_stream = new ByteArrayOutputStream();
//                            ImageIO.write( image, "jpg", out_stream );
                            //byte[] byte_image = out_stream.toByteArray();
                            byte[] byte_image = getBytes("/Macintosh HD/Users/sunnysummer5/Desktop/budgieLOGO.png");
                            out.write(byte_image);

                            out.close();

                            BufferedReader in = new BufferedReader(new InputStreamReader(
                                    yc.getInputStream()));
                            String inputLine;
                            while ((inputLine = in.readLine()) != null)
                                System.out.println(inputLine);
                            in.close();
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            System.out.println("ERROR");
                        }
                    }
                }.start();

                Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                intent.putExtra("Budget", editText.getText().toString());
                startActivity(intent);
            }
        });

    }
    public byte[] getBytes(String fileName) {
        File imageFile = new File(fileName);
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