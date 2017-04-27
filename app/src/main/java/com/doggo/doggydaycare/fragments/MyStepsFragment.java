package com.doggo.doggydaycare.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doggo.doggydaycare.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyStepsFragment extends Fragment
{
    public static final String TAG_MY_STEPS_FRAGMENT = "my_info_fragment";
    private LineChart mChart;

    public MyStepsFragment()
    {
        // Required empty public constructor
    }

    public static MyStepsFragment newInstance()
    {
        return new MyStepsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        Log.d("MyStepsFragment:", "Messages Fragment ON_CREATE_VIEW()");
        View view = inflater.inflate(R.layout.fragment_mysteps, container, false);

        mChart = (LineChart) view.findViewById(R.id.chart1);

        // no description text
        mChart.getDescription().setEnabled(true);
        mChart.getLegend().setEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(false);

        // set an alternative background color
        mChart.setBackgroundColor(Color.DKGRAY);
        mChart.setViewPortOffsets(0f, 0f, 0f, 0f);

        // add data

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setTextColor(Color.WHITE);
        l.setEnabled(true);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        // xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(0.1f); // one hour

        xAxis.setValueFormatter(new IAxisValueFormatter()
        {
            private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");

            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                //long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date((long)value*1000));
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        // leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);
        // leftAxis.setAxisMaximum(17000f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.rgb(255, 192, 56));

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        setData();

        mChart.invalidate();

        return view;
    }

    private void setData()
    {
        ArrayList<LineDataSet> lineDataSet = new ArrayList<LineDataSet>();
        ArrayList<Entry> data= new ArrayList<Entry>();
        data.add(new Entry(1487300560,0));
        data.add(new Entry(1487310560,100));
        data.add(new Entry(1487320560,200));
        data.add(new Entry(1487330560,300));
        data.add(new Entry(1487340560,1500));
        data.add(new Entry(1487350560,3500));
        data.add(new Entry(1487360560,6600));
        data.add(new Entry(1487370560,15000));

        LineDataSet   temp = new LineDataSet(data,"username" );

        temp.setAxisDependency(YAxis.AxisDependency.LEFT);

        temp.setLineWidth(1.5f);
        temp.setDrawCircles(false);
        temp.setDrawValues(false);
        temp.setFillAlpha(65);

        temp.setFillColor(Color.rgb(50, 117, 217));
        temp.setColor(Color.rgb(150, 117, 117));
        temp.setValueTextColor(Color.rgb(250, 17, 217));

        temp.setHighLightColor(Color.rgb(144, 217, 117));
        temp.setDrawCircleHole(false);
        lineDataSet.add(temp);
        // for(Entry entry:){
        // }

        LineData lineData=new LineData(lineDataSet.get(0));;

        lineData = new LineData(lineDataSet.get(0));

        lineData.setValueTextColor(Color.WHITE);

        lineData.setValueTextSize(9f);

        mChart.setData(lineData);
    }
}
