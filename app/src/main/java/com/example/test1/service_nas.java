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

public class service_nas extends AppCompatActivity implements View.OnClickListener{

    private NASAdapter nsAdapter;
    private List<NASData> nData;
    private Button btn_zone;
    private EditText txt_zone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_nas);

//        //액션바 타이틀 변경하기
//        getSupportActionBar().setTitle("KT Cloud");
//        //액션바 배경색 변경
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF94D1CA));

        init();

        String []name = {"NAS1", "NAS2","NAS3"};
        String []zonename = {"목동1센터", "목동1센터","목동1센터"};
        String []curSize = {"20GB", "20GB","20GB"};
        String []tarSize = {"20GB", "20GB","20GB"};
        String []protocol = {"HTTP", "HTTP","HTTP"};
        getData_service_nas(name,zonename,curSize,tarSize,protocol);

        btn_zone = (Button)findViewById(R.id.btn_nas_zone_search);
        btn_zone.setOnClickListener(this);
        txt_zone = (EditText)findViewById(R.id.txt_nas_zone_search);
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
                            EditText tmp = (EditText)findViewById(R.id.txt_nas_zone_search);
                            tmp.setText(zoneItem[which]);
                        }
                    })
                    .setCancelable(true)
                    .show();
        }
    }
    private void init() {
        // recyclerView = server list
        RecyclerView recyclerView = findViewById(R.id.recyclerView_service_nas);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        nsAdapter = new NASAdapter();
        recyclerView.setAdapter(nsAdapter);
    }

    private void getData_service_nas(String[] name, String[] zonename, String[] curSize, String[] tarSize, String[] protocol){
        // 임의의 데이터입니다.
        List<String> listName = Arrays.asList(name);
        List<String> listZone = Arrays.asList(zonename);
        List<String> listCurSize = Arrays.asList(curSize);
        List<String> listTarSize = Arrays.asList(tarSize);
        List<String> listProtocol = Arrays.asList(protocol);

        Integer [] tmp = new Integer[name.length];

        for(int i = 0; i < tmp.length; i++) {
            tmp[i] = R.drawable.nas;
        }

        List<Integer> listResId = Arrays.asList(tmp);

        for (int i = 0; i < listName.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            NASData nData = new NASData();
            nData.setName(listName.get(i));
            nData.setResId(listResId.get(i));
            nData.setZoneName(listZone.get(i));
            nData.setCurSize(listCurSize.get(i));
            nData.setTarSize(listTarSize.get(i));
            nData.setProtocol(listProtocol.get(i));
            // 각 값이 들어간 data를 adapter에 추가합니다.
            nsAdapter.addItem(nData);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        nsAdapter.notifyDataSetChanged();
    }



}
