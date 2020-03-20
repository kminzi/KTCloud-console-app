package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class service_server extends AppCompatActivity {
    public static Context mContext;
    Handler handler = new Handler();
    private ServerAdapter svAdpater;
    private List<ServerData> sData;

    APIcall_main API = (APIcall_main) getApplication();
    APIcall_server api_server = new APIcall_server();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_server);

//        //액션바 타이틀 변경하기
//        getSupportActionBar().setTitle("KT Cloud");
//        //액션바 배경색 변경
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF94D1CA));

        init();
        mContext = this;

        final String[] state = {"시작", "시작", "시작", "시작"};
        final String[] created = {"테스트 서버 내용 1", "테스트 서버 내용 2", "테스트 서버 내용 3", "시작"};
        final String[] name = {"테스트 서버 내용 1", "테스트 서버 내용 2", "테스트 서버 내용 3", "시작"};
        final String[] zonename = {"테스트 서버 내용 1", "테스트 서버 내용 2", "테스트 서버 내용 3", "시작"};
        final String[] osname = {"테스트 서버 내용 1", "테스트 서버 내용 2", "테스트 서버 내용 3", "시작"};

        //사용자가 입력한 위치, 상태에 따른 서버 목록 가져오기
        new Thread(new Runnable() {
            ArrayList<String[]> list = new ArrayList<String[]>();//서버 정보를 받아올 ArrayList
            @Override
            public void run() {
                try {
                    API.setZone("Seoul-M");//default 값 설정 - UI변경되고 수정해야 함
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
                        for (int i = 0; i < 4; i++) {
                            state[i] = list.get(i)[2];
                            created[i] = list.get(i)[3] ;
                            name[i] = list.get(i)[0];
                            zonename[i] = API.getZone();
                            osname[i] = list.get(i)[4];
                        }
                        getData_service_s(state, created, name, zonename, osname);
                    }
                });
            }
        }).start();
    }

    private void init() {
        // recyclerView = server list
        RecyclerView recyclerView = findViewById(R.id.recyclerView_service_server);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        svAdpater = new ServerAdapter();
        recyclerView.setAdapter(svAdpater);
    }

    private void getData_service_s(String[] state, String[] created, String[] name, String[] zonename, String[] osname) {
        // 임의의 데이터입니다.
        List<String> listState = Arrays.asList(state);
        List<String> listCreated = Arrays.asList(created);
        List<String> listName = Arrays.asList(name);
        List<String> listZone = Arrays.asList(zonename);
        List<String> listOsname = Arrays.asList(osname);

        Integer[] tmp = new Integer[state.length];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = R.drawable.kt_cloud;
        }

        List<Integer> listResId = Arrays.asList(tmp);

        for (int i = 0; i < listState.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            ServerData sData = new ServerData();
            sData.setName(listName.get(i));
            sData.setState(listState.get(i));
            sData.setResId(listResId.get(i));
            sData.setZonename(listZone.get(i));
            sData.setOsname(listOsname.get(i));
            sData.setCreated(listCreated.get(i));
            // 각 값이 들어간 data를 adapter에 추가합니다.
            svAdpater.addItem(sData);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        svAdpater.notifyDataSetChanged();
    }


    public void onButtonClicked_home(View v) {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(intent);
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
