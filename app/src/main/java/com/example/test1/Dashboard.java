package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    Handler handler = new Handler();
    APIcall_main API = (APIcall_main) getApplication();
    APIcall_server api_server = new APIcall_server();

    TextView server_num;
    private RecyclerAdapter adapter_s; // 서버용 어답터
    private RecyclerAdapter adapter_m; // 모니터링용 어답터
    private RecyclerAdapter adapter_a; // 알람용 어답터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        init();

        final String[] title_s = {"테스트 서버 이름 1", "테스트 서버 이름 2", "테스트 서버 이름 3", "테스트 서버 이름 3"};
        final String[] content_s = {"테스트 서버 내용 1", "테스트 서버 내용 2", "테스트 서버 내용 3", "테스트 서버 이름 3"};
        final String[] os_s = {"테스트 서버 내용 1", "테스트 서버 내용 2", "테스트 서버 내용 3", "테스트 서버 이름 3"};

        String[] title_m = {"테스트 모니터링 상황 1", "테스트 모니터링 상황 2", "테스트 모니터링 상황 3"};
        String[] content_m = {"테스트 모니터링 내용 1", "테스트 모니터링 내용 2", "테스트 모니터링 내용 3"};

        String[] title_a = {"[서버]", "[디스크]", "[디스크]"};
        String[] content_a = {"테스트 알람 내용 1", "테스트 알람 내용 2", "테스트 알람 내용 3"};

        server_num = (TextView) findViewById(R.id.txt_server_number);

        getData_m(title_m, content_m); // 모니터링용
        getData_a(title_a, content_a); // 모니터링용

        //서버 목록 가져오기
        new Thread(new Runnable() {
            ArrayList<String[]> list = new ArrayList<String[]>();//서버 정보를 받아올 ArrayList
            @Override
            public void run() {
                try {
                    API.setZone("Seoul-M");//default 값 설정
                    API.setState("all");
                    list = api_server.listServers();
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
                        server_num.setText(list.size() + "개 사용 중");
                        for (int i = 0; i < 4; i++) {
                            title_s[i] = list.get(i)[0];
                            content_s[i] = "스펙 : " + list.get(i)[1] + " 상태 : " + list.get(i)[2];
                            os_s[i] = list.get(i)[4];
                        }
                        getData_s(title_s, content_s, os_s);//
                    }
                });
            }
        }).start();

    }

    /**
     * 상단바 techcenter아이콘 클릭 처리 함수
     * @param v
     */
    @SuppressLint("RestrictedApi")
    public void TechcenterClicked(View v) {
        MenuBuilder menuBuilder =new MenuBuilder(this);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.techcenter, menuBuilder);
        Context wrapper = new ContextThemeWrapper(this, R.style.popupTheme1);
        MenuPopupHelper optionsMenu = new MenuPopupHelper(wrapper, menuBuilder, v);
        optionsMenu.setForceShowIcon(true);

        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
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

            @Override
            public void onMenuModeChange(MenuBuilder menu) {

            }
        });
        optionsMenu.show();
    }

    /**
     * 하단바의 Dashboard 버튼 클릭 처리 함수
     */
    public void DashboardClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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


    private void init() {
        // recyclerView = server list
        RecyclerView recyclerView = findViewById(R.id.recyclerView_server);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter_s = new RecyclerAdapter();
        recyclerView.setAdapter(adapter_s);

        // recyclerView2 = monitoring list
        RecyclerView recyclerView2 = findViewById(R.id.recyclerView_monitoring);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        adapter_m = new RecyclerAdapter();
        recyclerView2.setAdapter(adapter_m);

        // recyclerView3 = alarm list
        RecyclerView recyclerView3 = findViewById(R.id.recyclerView_alarm);

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
        recyclerView3.setLayoutManager(linearLayoutManager3);

        adapter_a = new RecyclerAdapter();
        recyclerView3.setAdapter(adapter_a);
    }


    private void getData_s(String[] title, String[] content, String[] os) {
        // 임의의 데이터입니다.
        List<String> listTitle = Arrays.asList(title);
        List<String> listContent = Arrays.asList(content);
        List<String> listOS = Arrays.asList(os);

        Integer[] tmp = new Integer[title.length];
        for (int i = 0; i < tmp.length; i++) {
            if (os[i].contains("centos"))
                tmp[i] = R.drawable.linux;
            else
                tmp[i] = R.drawable.window;
        }

        List<Integer> listResId = Arrays.asList(tmp);

        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setResId(listResId.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter_s.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter_s.notifyDataSetChanged();
    }

    private void getData_m(String[] title, String[] content) {
        // 임의의 데이터입니다.
        List<String> listTitle = Arrays.asList(title);
        List<String> listContent = Arrays.asList(content);

        Integer[] tmp = new Integer[title.length];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = R.drawable.monitoring;
        }

        List<Integer> listResId = Arrays.asList(tmp);

        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setResId(listResId.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter_m.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter_m.notifyDataSetChanged();
    }

    private void getData_a(String[] title, String[] content) {
        // 임의의 데이터입니다.
        List<String> listTitle = Arrays.asList(title);
        List<String> listContent = Arrays.asList(content);

        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter_a.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter_a.notifyDataSetChanged();
    }
}

