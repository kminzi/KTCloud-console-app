package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Monitoring extends AppCompatActivity {

    APIcall_main API = (APIcall_main) getApplication();
    APIcall_watch apIcall_watch = new APIcall_watch();

    ArrayList<Integer> jsonList = new ArrayList<>(); // ArrayList 선언
    ArrayList<String> labelList = new ArrayList<>(); // ArrayList 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitoring);
        final Handler handler = new Handler();

        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF94D1CA));

        final LineChart lineChart = (LineChart) findViewById(R.id.chart);
        final LineChart lineChart2 = (LineChart) findViewById(R.id.chart2);

        final ArrayList<Entry> entries = new ArrayList<>();
        final LineDataSet dataset = new LineDataSet(entries, "# of Calls");
        final ArrayList<String> labels = new ArrayList<String>();

        final ArrayList<Entry> entries2 = new ArrayList<>();
        final LineDataSet dataset2 = new LineDataSet(entries2, "# of Calls");
        final ArrayList<String> labels2 = new ArrayList<String>();

        String xarr[], xarr2[];

        new Thread(new Runnable() {
            HashMap<String, String> list = new HashMap<String, String>();
            Set<String> xlist = new LinkedHashSet<>();
            Iterator<String> iter = xlist.iterator();

            HashMap<String, String> list2 = new HashMap<String, String>();
            Set<String> xlist2 = new LinkedHashSet<>();
            Iterator<String> iter2 = xlist2.iterator();

            @Override
            public void run() {
                try {
                    apIcall_watch.showMetric("NetworkIn");
                    list = apIcall_watch.getInfo("NetworkIn");
                    xlist = list.keySet();

                    apIcall_watch.showMetric("NetworkOut");
                    list2 = apIcall_watch.getInfo("NetworkOut");
                    xlist2 = list2.keySet();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {//UI접근
                        String xarr[] = xlist.toArray(new String[xlist.size()]);
                        String xarr2[] = xlist2.toArray(new String[xlist2.size()]);
                        for (int i = 0; i < 6; i++) {
                            entries.add(new Entry(Float.parseFloat(list.get(xarr[i])), i));
                            labels.add(xarr[i]);
                            entries2.add(new Entry(Float.parseFloat(list2.get(xarr2[i])), i));
                            labels2.add(xarr2[i]);
                        }

                        LineData data = new LineData(labels, dataset);
                        dataset.setColors(Collections.singletonList(0xFF94D1CA));
                        dataset.setDrawCubic(true); //선 둥글게 만들기

                        lineChart.setData(data);
                        lineChart.animateY(3000);

                        LineData data2 = new LineData(labels2, dataset2);
                        dataset2.setColors(Collections.singletonList(0xFF94D1CA));
                        dataset2.setDrawCubic(true); //선 둥글게 만들기

                        lineChart2.setData(data);
                        lineChart2.animateY(2000);
                    }
                });
            }
        }).start();

    }


}
