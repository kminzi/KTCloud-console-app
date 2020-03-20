package com.example.test1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;

public class service_db extends AppCompatActivity implements View.OnClickListener {

    private DBAdapter DBAdapter;
    private List<DBData> dbData;
    private Button btn_zone;
    private EditText txt_zone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_db);

//        //액션바 타이틀 변경하기
//        getSupportActionBar().setTitle("KT Cloud");
//        //액션바 배경색 변경
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF94D1CA));

        init();

        // (String[] state, String[] created, String[] name, String[] zoneName, String[] osname)

        String []state = {"사용중", "사용중","사용중"};
        String []created = {"1990-01-01","1990-01-01","1990-01-01"};
        String []name = {"DB1", "DB2","DB3"};
        String []zoneName = {"목동", "목동","목동"};
        String []dev = {"HA", "HA", "HA"};
        String []size = {"20GB", "25GB", "40GB"};
        String []DBstate = {"정상", "정상", "정상"};
        getData_service_db(state, created, name, zoneName, dev, size, DBstate);

        btn_zone = (Button)findViewById(R.id.btn_db_zone_search);
        btn_zone.setOnClickListener(this);

        txt_zone = (EditText)findViewById(R.id.txt_db_zone_search);
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
                            EditText tmp = (EditText)findViewById(R.id.txt_db_zone_search);
                            tmp.setText(zoneItem[which]);
                        }
                    })
                    .setCancelable(true)
                    .show();
        }

    }


    private void init() {
        // recyclerView = db list
        RecyclerView recyclerView = findViewById(R.id.recyclerView_service_db);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        DBAdapter = new DBAdapter();
        recyclerView.setAdapter(DBAdapter);
    }


    private void getData_service_db(String[] state, String[] created, String[] name, String[] zoneName, String[] dev, String[] size, String[] DBstate) {
        // 임의의 데이터입니다.
        List<String> listState = Arrays.asList(state);
        List<String> listCreated = Arrays.asList(created);
        List<String> listName = Arrays.asList(name);
        List<String> listZone = Arrays.asList(zoneName);
        List<String> listDev = Arrays.asList(dev);
        List<String> listSize = Arrays.asList(size);
        List<String> listDBstate = Arrays.asList(DBstate);

        Integer [] tmp = new Integer[state.length];
        for(int i = 0; i < tmp.length; i++) {
            tmp[i] = R.drawable.db;
        }

        List<Integer> listResId = Arrays.asList(tmp);

        for (int i = 0; i < listState.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            DBData dbData = new DBData();
            dbData.setName(listName.get(i));
            dbData.setState(listState.get(i));
            dbData.setResId(listResId.get(i));
            dbData.setZoneName(listZone.get(i));
            dbData.setDev(listDev.get(i));
            dbData.setSize(listSize.get(i));
            dbData.setCreated(listCreated.get(i));
            dbData.setDBstate(listDBstate.get(i));
            // 각 값이 들어간 data를 adapter에 추가합니다.
            DBAdapter.addItem(dbData);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        DBAdapter.notifyDataSetChanged();
    }

}
