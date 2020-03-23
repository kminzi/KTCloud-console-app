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

public class service_autoscaling extends AppCompatActivity implements View.OnClickListener {

    private AutoScalingAdapter atAdapter;
    private List<AutoscalingData> atData;
    private Button btn_zone;
    private EditText txt_zone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_autoscaling);


            //액션바 배경색 변경
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF94D1CA));

            init();

            String []state = {"시작", "시작","시작"};
            String []name = {"오토스케일링1", "오토스케일링2","오토스케일링3"};
            String []zonename = {"목동1센터", "목동1센터","목동1센터"};
            String []curVM = {"2개", "2개","2개"};
            String []tarVM = {"3개", "3개","3개"};
            String []minVM = {"1개", "1개","1개"};
            String []maxVM = {"4개", "4개","4개"};
            getData_service_at(state, name, zonename, curVM, tarVM, minVM, maxVM);

            btn_zone = (Button)findViewById(R.id.btn_auto_zone_search);
            btn_zone.setOnClickListener(this);

            txt_zone = (EditText)findViewById(R.id.txt_auto_zone_search);
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
                                txt_zone = (EditText)findViewById(R.id.txt_auto_zone_search);
                                txt_zone.setText(zoneItem[which]);
                            }
                        })
                        .setCancelable(true)
                        .show();
            }

        }


        private void init() {
            // recyclerView = server list
            RecyclerView recyclerView = findViewById(R.id.recyclerView_service_auto);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);

            atAdapter = new AutoScalingAdapter();
            recyclerView.setAdapter(atAdapter);
        }


        private void getData_service_at(String[] state, String[] name, String[] zonename, String[] curVM, String[] tarVM, String[] minVM, String[] maxVM) {
            // 임의의 데이터입니다.
            List<String> listState = Arrays.asList(state);
            List<String> listName = Arrays.asList(name);
            List<String> listZone = Arrays.asList(zonename);
            List<String> listCurVM = Arrays.asList(curVM);
            List<String> listTarVM = Arrays.asList(tarVM);
            List<String> listMinVM = Arrays.asList(minVM);
            List<String> listMaxVM = Arrays.asList(maxVM);

            Integer [] tmp = new Integer[state.length];

            for(int i = 0; i < tmp.length; i++) {
                tmp[i] = R.drawable.autoscaling;
            }

            List<Integer> listResId = Arrays.asList(tmp);

            for (int i = 0; i < listState.size(); i++) {
                // 각 List의 값들을 data 객체에 set 해줍니다.
                AutoscalingData atData = new AutoscalingData();
                atData.setName(listName.get(i));
                atData.setState(listState.get(i));
                atData.setResId(listResId.get(i));
                atData.setZoneName(listZone.get(i));
                atData.setCurVM(listCurVM.get(i));
                atData.setTarVM(listTarVM.get(i));
                atData.setMinVm(listMinVM.get(i));
                atData.setMaxVm(listMaxVM.get(i));
                // 각 값이 들어간 data를 adapter에 추가합니다.
                atAdapter.addItem(atData);
            }

            // adapter의 값이 변경되었다는 것을 알려줍니다.
            atAdapter.notifyDataSetChanged();
        }
}
