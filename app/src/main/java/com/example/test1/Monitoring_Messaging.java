package com.example.test1;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class Monitoring_Messaging extends AppCompatActivity {
    private TopicAdapter tpAdapter;
    private List<TopicData> tpData;

    protected void onCreate(Bundle savedInstanceState) {
        //액션바 타이틀 변경하기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moni_messaging);

        getSupportActionBar().setTitle("KT Cloud");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF94D1CA));

        init();

        String[] name = {"topic1", "topic2", "topic3"};
        String[] pub = {"pub1", "pub2", "pub3"};
        String[] pro = {"1.1", "1.1", "1.1"};
        String[] con = {"설명1", "설명2", "설명3"};
        String[] rec = {"리시버1", "리시버2", "리시버3"};
        getData(name, pub, pro, con, rec);

    }

//    //액션버튼 메뉴 액션바에 집어 넣기
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }

    private void init() {
        // recyclerView = server list
        RecyclerView recyclerView = findViewById(R.id.recyclerView_topic_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        tpAdapter = new TopicAdapter();
        recyclerView.setAdapter(tpAdapter);
    }


    private void getData(String[] name, String[] pub, String[] pro, String[] con, String[] rec) {
        // 임의의 데이터입니다.
        List<String> listName = Arrays.asList(name);
        List<String> listPub = Arrays.asList(pub);
        List<String> listPro = Arrays.asList(pro);
        List<String> listCon = Arrays.asList(con);
        List<String> listRec = Arrays.asList(rec);

        Integer [] tmp = new Integer[name.length];
        for(int i = 0; i < tmp.length; i++) {
            tmp[i] = R.drawable.alarm;
        }

        List<Integer> listResId = Arrays.asList(tmp);

        for (int i = 0; i < listName.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            TopicData data = new TopicData();
            data.setName(listName.get(i));
            data.setPub(listPub.get(i));
            data.setResId(listResId.get(i));
            data.setProtocol(listPro.get(i));
            data.setContent(listCon.get(i));
            data.setReceiver(listRec.get(i));

            // 각 값이 들어간 data를 tpAdapter에 추가합니다.
            tpAdapter.addItem(data);
        }

        // tpAdapter의 값이 변경되었다는 것을 알려줍니다.
        tpAdapter.notifyDataSetChanged();
    }
}
