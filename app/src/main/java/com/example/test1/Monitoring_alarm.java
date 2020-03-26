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

public class Monitoring_alarm extends AppCompatActivity {private MoniAlarmAdapter maAdapter;
    private List<MoniAlarmData> maData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moni_watch_alarm);

        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF94D1CA));

        init();

        String[] state = {"안정", "알람발생", "안정"};
        String[] name = {"ala1", "ala2", "ala3"};
        String[] condi = {"메모리릭", "메모리릭", "메모리릭"};
        String[] onoff = {"활성", "비활성", "활성"};
        String[] act = {"이메일", "폰", "이메일"};
        String[] type = {"모든 서버 통합", "모든 서버 통합", "모든 서버 통합"};
        getData(name, state, condi, onoff,act, type);
        menu_btn();

        Button btn_status = (Button) findViewById(R.id.btn_moni_alarm_status_sel);
        String value = null;
        if (getIntent().getExtras().getString("btn_value") != null) {
            value = getIntent().getExtras().getString("btn_value");
        }
        else {
            value = "전체";
        }
        btn_status.setText(value);
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

        maAdapter = new MoniAlarmAdapter();
        recyclerView.setAdapter(maAdapter);
    }

    public void menu_btn() {
        // 메트릭 그래프 추가 옵션(주기, 통계 등) 선택
        final Button btn_status = (Button) findViewById(R.id.btn_moni_alarm_status_sel);
        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미

                getMenuInflater().inflate(R.menu.alarm_status_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.occ:
                                btn_status.setText("발생");
                                break;
                            case R.id.nor:
                                btn_status.setText("안정");
                                break;
                            case R.id.data:
                                btn_status.setText("데이터 부족");
                                break;
                            case R.id.all:
                                btn_status.setText("전체");
                                break;
                            default:
                                btn_status.setText("전체");
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }


    private void getData(String[] name, String[] state, String[] condi, String[] onoff, String[] act, String[] type) {
        // 임의의 데이터입니다.
        List<String> listName = Arrays.asList(name);
        List<String> listState = Arrays.asList(state);
        List<String> listCondi = Arrays.asList(condi);
        List<String> listOnoff = Arrays.asList(onoff);
        List<String> listAct = Arrays.asList(act);
        List<String> listType = Arrays.asList(type);

        Integer [] tmp = new Integer[name.length];
        for(int i = 0; i < tmp.length; i++) {
            tmp[i] = R.drawable.ic_notifications_black_24dp;
        }

        List<Integer> listResId = Arrays.asList(tmp);

        for (int i = 0; i < listName.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            MoniAlarmData data = new MoniAlarmData();
            data.setName(listName.get(i));
            data.setState(listState.get(i));
            data.setResId(listResId.get(i));
            data.setCondi(listCondi.get(i));
            data.setOnoff(listOnoff.get(i));
            data.setAct(listAct.get(i));
            data.setType(listType.get(i));

            // 각 값이 들어간 data를 maAdapter에 추가합니다.
            maAdapter.addItem(data);
        }

        // maAdapter의 값이 변경되었다는 것을 알려줍니다.
        maAdapter.notifyDataSetChanged();
    }
}

