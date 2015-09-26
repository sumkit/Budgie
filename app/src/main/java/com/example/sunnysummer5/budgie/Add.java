package com.example.sunnysummer5.budgie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Add extends AppCompatActivity {
    Button camera, add;
    CheckBox food, clothing, entertainment, travel, misc;
    EditText editText;
    double[] pie = new double[5];
    double[] temp = new double[5];

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    //public static final int MEDIA_TYPE_IMAGE = 1;
    private Bitmap bitmap;
    private String imageToText;
    private double price=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        for(Double i : pie) {
            i=0.0;
        }
        if(getIntent() != null) {
            pie = getIntent().getDoubleArrayExtra("Pie").clone();
        }
        editText = (EditText) findViewById(R.id.editText);
        camera = (Button) findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add.this, HomeScreen.class);
                intent.putExtra("Caller","Add");
                intent.putExtra("Pie", getIntent().getDoubleArrayExtra("Pie"));
                intent.putExtra("Budget", getIntent().getStringExtra("Budget").toString());
                if(editText.getText().toString().length() == 0) {
                    intent.putExtra("Price", price);
                    for(int i = 0; i < pie.length; i++) {
                        pie[i] += (temp[i]*price);
                    }
                }
                else {
                    intent.putExtra("Price", Double.parseDouble(editText.getText().toString()));
                    price = Double.parseDouble(editText.getText().toString());
                    for(int i = 0; i < pie.length; i++) {
                        pie[i] += (temp[i]*Double.parseDouble(editText.getText().toString()));
                    }
                }
                intent.putExtra("Spending", getIntent().getExtras().getDouble("Spending")-price);
                intent.putExtra("Pie", pie);
                startActivity(intent);
            }
        });

        food = (CheckBox) findViewById(R.id.food);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v);
            }
        });
        clothing = (CheckBox) findViewById(R.id.clothing);
        clothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v);
            }
        });
        entertainment = (CheckBox) findViewById(R.id.entertainment);
        entertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v);
            }
        });
        travel = (CheckBox) findViewById(R.id.travel);
        travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v);
            }
        });
        misc = (CheckBox) findViewById(R.id.misc);
        misc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v);
            }
        });
    }

    //what happens when click a checkbox
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked(); // Is the view now checked?

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.food:
                if (checked) {
                    temp[0]++;
                }
                else {
                    temp[0]--;
                }
                break;
            case R.id.clothing:
                if (checked) {
                    temp[1]++;
                }
                else {
                    temp[1]--;
                }
                break;
            case R.id.entertainment:
                if(checked) {
                    temp[2]++;
                }
                else {
                    temp[2]--;
                }
                break;
            case R.id.travel:
                if(checked) {
                    temp[3]++;
                }
                else {
                    temp[3]--;
                }
                break;
            case R.id.misc:
                if(checked) {
                    temp[4]++;
                }
                else {
                    temp[4]--;
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
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

    public double getTotal(String receipt) {
        //String pattern = "total(.*)(\\$)(\\d+\\.\\d\\d)";
        String pattern = "(.*?)(\\$)(\\d+\\.\\d\\d)(.*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(receipt);
        if (m.find()) {
            String result = m.group(3);
            double total = Double.parseDouble(result);
            return total;
        }
        else return 0.0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");

            new Thread() {
                @Override
                public void run() {
                    try
                    {
                        URL oracle = new URL("http://api.projectoxford.ai/vision/v1/ocr?language=unk&detectOrientation=true");
                        URLConnection yc = (HttpURLConnection) oracle.openConnection();


                        yc.setRequestProperty("Content-Type", "application/octet-stream");
                        yc.setRequestProperty("Ocp-Apim-Subscription-Key", "1baee771e6aa47998831f21b634a03ad");


                        yc.setDoOutput(true);
                        OutputStream out = new BufferedOutputStream(yc.getOutputStream());

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        out.write(byteArray);
                        out.close();

                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                yc.getInputStream()));
                        String inputLine;
                        String total = "";
                        while ((inputLine = in.readLine()) != null) {
                            System.out.println(inputLine);
                            total += inputLine;
                        }
                        in.close();
                        imageToText = total;
                        price = getTotal(total);
                        editText.setText(Double.toString(price));
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        System.out.println("ERROR");
                    }
                }
            }.start();
        }
        else {
            System.out.println("NOT CAMERA");
        }
    }
}