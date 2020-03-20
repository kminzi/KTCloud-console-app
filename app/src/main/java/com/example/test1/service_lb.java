package com.example.test1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;

public class service_lb extends AppCompatActivity implements View.OnClickListener {

    private LBAdapter lbAdpater;
    private List<LBData> lData;
    private Button btn_zone;
    private EditText txt_zone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_lb);

//        //액션바 타이틀 변경하기
//        getSupportActionBar().setTitle("KT Cloud");
//        //액션바 배경색 변경
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF94D1CA));

        init();

        String []lbType = {"타입", "타입","타입"};
        String []lbOpt = {"옵션", "옵션","옵션"};
        String []name = {"lB이름1", "lB이름2", "lB이름3"};
        String []zonename = {"Zone","Zone","Zone"};
        String []ip = {"127.0.0.1","127.0.0.1","127.0.0.1"};
        String []port = {"80","80","80"};
        String []server = {"적용서버","적용서버","적용서버"};

        getData_service_lb(name,lbType, lbOpt, ip, port, server, zonename);

        btn_zone = (Button)findViewById(R.id.btn_lb_zone_search);
        btn_zone.setOnClickListener(this);
        txt_zone = (EditText)findViewById(R.id.txt_lb_zone_search);
        txt_zone.setFocusable(false);
        txt_zone.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btn_zone || v == txt_zone) {
            final CharSequence[] zoneItem = {"전체","KOR-Central A", "KOR-Central B", "KOR-Seoul M", "KOR-Seoul M2", "KOR-HA", "US-West"};

            AlertDialog.Builder oDialog = new AlertDialog.Builder(this);

            oDialog.setTitle("존(Zone) 위치 선택")
                    .setItems(zoneItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText tmp = (EditText)findViewById(R.id.txt_lb_zone_search);
                            tmp.setText(zoneItem[which]);
                        }
                    })
                    .setCancelable(true)
                    .show();
        }
    }

    private void init() {
        // recyclerView = server list
        RecyclerView recyclerView = findViewById(R.id.recyclerView_service_lb);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        lbAdpater = new LBAdapter();
        recyclerView.setAdapter(lbAdpater);
    }

    private void getData_service_lb(String[] name, String[] lbType, String[] lbOpt, String[] ip, String[] port, String[] server, String[] zonename) {
        // 임의의 데이터입니다.
        List<String> listName = Arrays.asList(name);
        List<String> listZone = Arrays.asList(zonename);
        List<String> listLBType = Arrays.asList(lbType);
        List<String> listLBOpt = Arrays.asList(lbOpt);
        List<String> listIp = Arrays.asList(ip);
        List<String> listPort = Arrays.asList(port);
        List<String> listServer = Arrays.asList(server);

        Integer [] tmp = new Integer[name.length];
        for(int i = 0; i < tmp.length; i++) {
            tmp[i] = R.drawable.lb;
        }

        List<Integer> listResId = Arrays.asList(tmp);

        for (int i = 0; i < listName.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            LBData lbData = new LBData();
            lbData.setName(listName.get(i));
            lbData.setLBOpt(listLBOpt.get(i));
            lbData.setResId(listResId.get(i));
            lbData.setZoneName(listZone.get(i));
            lbData.setIp(listIp.get(i));
            lbData.setPort(listPort.get(i));
            lbData.setLBType(listLBType.get(i));
            lbData.setServer(listServer.get(i));
            // 각 값이 들어간 data를 adapter에 추가합니다.
            lbAdpater.addItem(lbData);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        lbAdpater.notifyDataSetChanged();
    }

}
