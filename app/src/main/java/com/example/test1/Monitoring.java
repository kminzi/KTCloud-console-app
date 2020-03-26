package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
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
                        lineChart.animateY(2000);

                        LineData data2 = new LineData(labels2, dataset2);
                        dataset2.setColors(Collections.singletonList(0xFF94D1CA));
                        dataset2.setDrawCubic(true); //선 둥글게 만들기

                        lineChart2.setData(data2);
                        lineChart2.animateY(2000);
                    }
                });
            }
        }).start();

        // 메트릭 이동
        Button b = (Button)findViewById(R.id.btn_moni_main_metric);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        Monitoring_Metric.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        // 알람 이동
        Button al = (Button)findViewById(R.id.btn_moni_main_alarm);
        al.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        Monitoring_alarm.class); // 다음 넘어갈 클래스 지정
                intent.putExtra("btn_value", "전체");
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        // 메시지 이동
        msg_btn();

        // 알람 상황별 이동
        alarm_btn();
    }

//    //액션버튼 메뉴 액션바에 집어 넣기
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }

    public void msg_btn() {
        Button msg = (Button)findViewById(R.id.btn_moni_main_msg);
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        Monitoring_Messaging.class); // 다음 넘어갈 클래스 지정
                intent.putExtra("btn_value", "전체");
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        Button tmsg = (Button)findViewById(R.id.tbtn_moni_main_msg);
        tmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        Monitoring_Messaging.class); // 다음 넘어갈 클래스 지정
                intent.putExtra("btn_value", "전체");
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });
    }

    public void alarm_btn() {
        Button occ = (Button)findViewById(R.id.btn_moni_alarm_occ);
        occ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        Monitoring_alarm.class); // 다음 넘어갈 클래스 지정
                intent.putExtra("btn_value", "발생");
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        Button data = (Button)findViewById(R.id.btn_moni_alarm_data);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        Monitoring_alarm.class); // 다음 넘어갈 클래스 지정
                intent.putExtra("btn_value", "데이터 부족");
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        Button nor = (Button)findViewById(R.id.btn_moni_alarm_normal);
        nor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        Monitoring_alarm.class); // 다음 넘어갈 클래스 지정
                intent.putExtra("btn_value", "안정");
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        Button met = (Button)findViewById(R.id.btn_moni_alarm_metric);
        met.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        Monitoring_alarm.class); // 다음 넘어갈 클래스 지정
                intent.putExtra("btn_value", "전체");
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });
    }


}
