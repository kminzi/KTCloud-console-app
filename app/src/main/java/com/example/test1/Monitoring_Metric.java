package com.example.test1;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Monitoring_Metric extends AppCompatActivity {
    private MetricAdapter adapter;
    APIcall_watch apIcall_watch = new APIcall_watch();
    final int[] server_num = new int[1];
    String statistic, cycle;
    final Handler handler = new Handler();
    static ArrayList<String> list_a = new ArrayList<String>();//메트릭 리스트 ArrayList
    ArrayList<String[]> list = new ArrayList<String[]>();//서버 정보를 받아올 ArrayList- name, id
    int period;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moni_watch_metric);

        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF94D1CA));

        RadioGroup rg = (RadioGroup) findViewById(R.id.rg_metric_opt);
        final RadioButton all = (RadioButton) findViewById(R.id.rbtn_metric_allServer);
        final RadioButton per = (RadioButton) findViewById(R.id.rbtn_metric_perServer);

        init();

        // 메트릭 그래프 옵션 선택

        final String[] serverName =  new String[100];
        final String[] opt = {"CPUUtilization", "MemoryTarget", "MemoryInternalFree", "DiskReadBytes", "DiksWriteBytes", "NetworkIn", "NetworkOut"};
        final String[] metricOpt = new String[serverName.length * 7];

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list = apIcall_watch.listServerAt("Seoul-M");//default - m존, running상태
                    server_num[0] = list.size();
                    for (int i = 0; i < server_num[0]; i++) serverName[i] = list.get(i)[0];

                    int tmp = 0;
                    for (int i = 0; i < server_num[0]; i++) {
                        for (int j  = 0; j < opt.length; j++) {
                            metricOpt[tmp++] = serverName[i] + " - " + opt[j];
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "조회할 서버가 없습니다", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (all.isChecked()) {
                    adapter.rmItem();
                    getData(opt, 7);
                } else if (per.isChecked()) {
                    adapter.rmItem();
                    getData(metricOpt, server_num[0]*7);
                } else {
                    adapter.rmItem();
                }
            }
        });
        menu_btn();
    }

    public void menu_btn() {
        // 메트릭 그래프 추가 옵션(주기, 통계 등) 선택
        final Button btn_status = (Button) findViewById(R.id.btn_moni_metric_status);
        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미
                getMenuInflater().inflate(R.menu.metric_status_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.sum:
                                btn_status.setText("합");
                                statistic = "Sum";
                                break;
                            case R.id.max:
                                btn_status.setText("최대");
                                statistic = "Maximum";
                                break;
                            case R.id.min:
                                btn_status.setText("최소");
                                statistic = "Minimum";
                                break;
                            case R.id.sample:
                                btn_status.setText("샘플 수");
                                statistic = "SampleCount";
                                break;
                            case R.id.avg:
                                btn_status.setText("평균");
                                statistic = "Average";
                                break;
                            default:
                                btn_status.setText("합");
                                statistic = "Sum";
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });


        final Button btn_period = (Button) findViewById(R.id.btn_moni_metric_period);
        btn_period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미

                getMenuInflater().inflate(R.menu.metric_period_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.m5:
                                btn_period.setText("5분");
                                cycle = "5";
                                break;
                            case R.id.m15:
                                btn_period.setText("15분");
                                cycle = "15";
                                break;
                            case R.id.m30:
                                btn_period.setText("30분");
                                cycle = "30";
                                break;
                            case R.id.h1:
                                btn_period.setText("1시간");
                                cycle = "60";
                                break;
                            case R.id.h6:
                                btn_period.setText("6시간");
                                cycle = "360";
                                break;
                            case R.id.h12:
                                btn_period.setText("12시간");
                                cycle = "720";
                                break;
                            case R.id.d1:
                                btn_period.setText("1일");
                                cycle = "1440";
                                break;
                            default:
                                btn_period.setText("5분");
                                cycle = "5";
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });


        final Button btn_cycle = (Button) findViewById(R.id.btn__moni_metric_cycle);
        btn_cycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미

                getMenuInflater().inflate(R.menu.metric_cycle_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.h1:
                                btn_cycle.setText("1시간");
                                period = 1;
                                break;
                            case R.id.h3:
                                btn_cycle.setText("3시간");
                                period = 3;
                                break;
                            case R.id.h6:
                                btn_cycle.setText("6시간");
                                period = 6;
                                break;
                            case R.id.h12:
                                btn_cycle.setText("12시간");
                                period = 12;
                                break;
                            case R.id.d1:
                                btn_cycle.setText("1일");
                                period = 24;
                                break;
                            case R.id.w1:
                                btn_cycle.setText("1주일");
                                period = 168;
                                break;
                            default:
                                btn_cycle.setText("1시간");
                                period = 1;
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }


    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.techcenter, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.web:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cloud.kt.com/portal/portal.notice.html?type="));//문의하기 웹으로 전환
                startActivity(intent);
                break;
            case R.id.tel:
                String num ="080-2580-005";
                Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));//자동 전화하기 화면으로 전환
                startActivity(intent2);
                break;
            default:
                break;
        }
        return true;
    }
    private void init() {
        // recyclerView = server list
        RecyclerView recyclerView = findViewById(R.id.recyclerView_topic_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MetricAdapter();
        recyclerView.setAdapter(adapter);

        //Average, Sum, SampleCount, Maximum, Minimum
        statistic = "Sum";
        //구간 설정(시간단위- 1, 3, 6, 12, 24, 168)
        period = 1;
//        apIcall_watch.setTime(period);

        //주기(분단위- 5, 15, 30, 60, 360, 720, 1440)
        cycle = "5";
    }

    private void getData(String [] opt, int num) {
        // 임의의 데이터입니다.
        List<String> listOpt = Arrays.asList(opt);

        CheckBox [] tmp = new CheckBox[num];
        Boolean [] tmp_b = new Boolean[num];

        for(int i = 0; i < tmp.length; i++) {
            tmp[i] = findViewById(R.id.cbtn_metric_opt);
            tmp_b[i] = false;
        }

        List<Boolean> listBool = Arrays.asList(tmp_b);
        List<CheckBox> listCheckBox = Arrays.asList(tmp);

        for (int i = 0; i < num; i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            MetricData maData = new MetricData();
            maData.setOpt(listOpt.get(i));
            maData.setCbtn(listCheckBox.get(i));
            maData.setChecked(listBool.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(maData);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }

    public static void getList(String s){
        list_a.add(s);
        System.out.println(list_a);
    }

    LineChart lineChart_metric;
    ArrayList<LineDataSet> lineDataSet_metric;
    ArrayList<String> labels_metric;
    int count, size;

    public void firstClicked(View v){
        count = 0;
        lineChart_metric = (LineChart) findViewById(R.id.chart_metric);
        lineChart_metric.invalidate();
        lineChart_metric.clear();

        apIcall_watch.setTime(period);//시간 설정
        apIcall_watch.setPeriod(cycle);
        apIcall_watch.setStatistics(statistic);

        size = list_a.size();

        for(int i=0;i<list_a.size();i++){
            if(list_a.get(i).contains("-")){

            }else{
                ShowAllClicked(list_a);
            }
        }
    }

    public void drawgraph(){
//        boolean n_success = true;
//        while(n_success){
//            if(count==size) n_success = false;
//        }
        try{
            TimeUnit.MILLISECONDS.sleep(10000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lineChart_metric.setData(new LineData(labels_metric,lineDataSet_metric));
    }

    /**
     * @brief 속성 선택 후 메트릭을 그리기 위한 함수 - 개별 서버 메트릭
     */
//    public void ShowSpecificClicked(View v) {
//        final String metricname="";
//        final String vmname=null, vmid = null;
//
//        final ArrayList<Entry> entries_metric = new ArrayList<>();
//        final LineDataSet dataset_metric = new LineDataSet(entries_metric, metricname);
//        final ArrayList<String> labels_metric = new ArrayList<String>();
//
//        new Thread(new Runnable() {
//            HashMap<String, String> list_metric = new HashMap<String, String>();
//            Set<String> xlist_metric = new LinkedHashSet<>();
//            @Override
//            public void run() {
//                ArrayList<String[]> list = new ArrayList<String[]>();//서버 정보를 받아올 ArrayList
//                try {
//                    apIcall_watch.showSpecificServerMetric(vmname, vmid, metricname);//메트릭 api호출
//                    list_metric = apIcall_watch.getInfo(metricname);//그래프 가져오기
//                    xlist_metric = list_metric.keySet();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (InvalidKeyException e) {
//                    e.printStackTrace();
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), "그래프를 그릴 수 없습니다", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }handler.post(new Runnable() {
//                    @Override
//                    public void run() {//UI접근
//                       String xarr_networkin[] = xlist_networkin.toArray(new String[xlist_networkin.size()]);
//                       for (int i = 0; i < 6; i++) {
//                            entries_networkin.add(new Entry(Float.parseFloat(list_networkin.get(xarr_networkin[i])), i));
//                            labels_networkin.add(xarr_networkin[i]);
//                        }
//
//                        LineData data_networkin = new LineData(labels_networkin, dataset_networkin);
//                        dataset_networkin.setColors(Collections.singletonList(0xFF94D1CA));
//                        dataset_networkin.setLineWidth(3.5f);
//                        dataset_networkin.setDrawCubic(true); //선 둥글게 만들기
//
//                        lineChart_networkin.setData(data_networkin);
//                        lineChart_networkin.animateY(2000);
//
//                    }
//                });
//            }
//        }).start();
//    }


    /**
     * @brief 속성 선택 후 메트릭을 그리기 위한 함수 - 모든 서버 메트릭
     */
    public void ShowAllClicked(final ArrayList<String> metricname) {//메트릭 이름 리스트
        final ArrayList<LineDataSet> lineDataSet_metric = new ArrayList<>();
        final ArrayList<String> labels_metric;
        final ArrayList<Entry> entries_metric = new ArrayList<>();
        final LineDataSet dataset_metric = new LineDataSet(entries_metric, metricname.get(1));
        labels_metric = new ArrayList<String>();

        new Thread(new Runnable() {
            HashMap<String, String> list_metric = new HashMap<String, String>();
            Set<String> xlist_metric = new LinkedHashSet<>();
            @Override
            public void run() {
                ArrayList<String[]> list = new ArrayList<String[]>();//서버 정보를 받아올 ArrayList
                try {
                    apIcall_watch.showMetric(metricname.get(1));
                    list_metric = apIcall_watch.getInfo(metricname.get(1));
                    xlist_metric = list_metric.keySet();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "그래프를 그릴 수 없습니다", Toast.LENGTH_LONG).show();
                        }
                    });
                }handler.post(new Runnable() {
                    @Override
                    public void run() {//UI접근
                        String xarr_metric[] = xlist_metric.toArray(new String[xlist_metric.size()]);
                        for (int i = 0; i < 6; i++) {
                            entries_metric.add(new Entry(Float.parseFloat(list_metric.get(xarr_metric[i])), i));
                            labels_metric.add(xarr_metric[i]);
                        }

                        dataset_metric.setColors(Collections.singletonList(0xFF94D1CA));
                        dataset_metric.setLineWidth(3.5f);
                        dataset_metric.setDrawCubic(true); //선 둥글게 만들기

                        lineDataSet_metric.add(dataset_metric);
                        count++;
                        lineChart_metric.animateY(2000);
                        lineChart_metric.setData(new LineData(labels_metric,lineDataSet_metric));
                    }
                });
            }
        }).start();
    }
    /**
     * 하단바의 Dashboard 버튼 클릭 처리 함수
     */
    public void DashboardClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(intent);
    }

    /**
     * 하단바의 service 버튼 클릭 처리 함수
     */
    public void ServiceClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), service_main.class);
        startActivity(intent);
    }

    /**
     * 하단바의 Monitoring 버튼 클릭 처리 함수
     */
    public void MonitoringClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), Monitoring.class);
        startActivity(intent);
    }

    /**
     * 하단바의 Payment 버튼 클릭 처리 함수
     */
    public void PaymentClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), Payment.class);
        startActivity(intent);
    }
}

