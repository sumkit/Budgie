package com.example.sunnysummer5.budgie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class HomeScreen extends AppCompatActivity {
    Button add;
    PieChart pieChart;
    TextView remaining,budget;
    double price=0.0;
    double[] pie = new double[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, Add.class);
                intent.putExtra("Budget", budget.getText().toString());
                intent.putExtra("Pie", pie);
                startActivity(intent);
            }
        });
        pieChart = (PieChart) findViewById(R.id.chart1);
        budget = (TextView)findViewById(R.id.budget);
        remaining = (TextView)findViewById(R.id.budgetRemaining);
        budget.setText("Budget: $" + getIntent().getStringExtra("Budget"));

        if(getIntent() != null) {
            if(getIntent().getStringExtra("Caller").equals("HomeScreen")) {
                double[] temp = getIntent().getExtras().getDoubleArray("Pie");
                pie = temp.clone();
                price = getIntent().getExtras().getDouble("Price");
            }
            else if(getIntent().getStringExtra("Caller").equals("MainActivity")) {
                budget.setText(getIntent().getStringExtra("Budget"));
            }
            else if(getIntent().getStringExtra("Caller").equals("Add")) {
                pie = getIntent().getDoubleArrayExtra("Pie").clone();
                budget.setText(getIntent().getStringExtra("Budget").toString());
            }
        }
        PieChart chart = (PieChart) findViewById(R.id.chart1);
        chart = configureChart(chart);
        chart = setData(chart, pie);
        chart.animateXY(1500, 1500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
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

    public PieChart configureChart(PieChart chart) {
        chart.setHoleColor(getResources().getColor(android.R.color.background_light));
        chart.setHoleRadius(50f);
        chart.setDescription("");
        chart.setTransparentCircleRadius(5f);
        //chart.setDrawYValues(true);
        chart.setDrawCenterText(true);
        chart.setCenterText("Your Spending");
        chart.setCenterTextSize(25f);
        chart.setDrawHoleEnabled(true);
        chart.setDrawSliceText(false);
        chart.setRotationAngle(0);
        chart.setDescriptionTextSize(16f);
        //chart.setDrawXValues(false);
        chart.setRotationEnabled(true);
        chart.setUsePercentValues(true);
        //chart.setCenterText("MPAndroidChart\nLibrary");
        chart.setCenterTextRadiusPercent(100f);

        //YAxis leftAxis = YAxis.getAxisLeft();
        //leftAxis.setValueFormatter(new PercentFormatter());
        return chart;
    }
    private PieChart setData(PieChart chart, double[] spending) {
        //YAxis.setValueFormatter(new PercentFormatter());
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        double food = spending[0];
        double clothing = spending[1];
        double entertainment = spending[2];
        double travel = spending[3];
        double misc = spending[4];
        yVals1.add(new Entry((float) food, 0));
        yVals1.add(new Entry((float) clothing, 1));
        yVals1.add(new Entry((float) entertainment, 2));
        yVals1.add(new Entry((float)travel, 3));
        yVals1.add(new Entry((float) misc, 4));
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Food");
        xVals.add("Clothing");
        xVals.add("Entertainment");
        xVals.add("Travel");
        xVals.add("Miscellaneous");

        PieDataSet set1 = new PieDataSet(yVals1, "");
        set1.setValueFormatter(new PercentFormatter());
        set1.setValueTextSize(20f);
        set1.setSliceSpace(10f);
        //XLabels x1 = chart.getXLabels();
        //x1.setTypeface
        //PieDataSet set2 = new PieDataSet(xVals, "");

        //set2.setValueTextSize(20f);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        //ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        set1.setColors(colors);
        PieData data = new PieData(xVals, set1);
        data.setValueTextSize(20f);
        //PieData data = new PieDataSet(xVals, set1);
        //data.setValueTextSize(20f);

        //PieData data = new PieData(xVals, set1);
        //colors.add(getResources().getColor(android.R.color.holo_green_light));
        //colors.add(getResources().getColor(android.R.color.holo_red_light));
        //colors.add(getResources().getColor(android.R.color.holo_blue_bright));
        //colors.add(getResources().getColor(android.R.color.holo_purple));
        //colors.add(getResources().getColor(android.R.color.holo_orange_dark));
        set1.setColors(colors);
        Legend legend = chart.getLegend();
        legend.setTextSize(10f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(15f);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        legend.setYEntrySpace(100f);
        legend.setWordWrapEnabled(true);
        //PieData data = new PieData(xVals, set1);
        chart.setData(data);
        chart.highlightValues(null);
        chart.invalidate();
        return chart;
    }
}