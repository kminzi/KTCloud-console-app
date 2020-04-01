package com.example.test1;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DBHAgroupAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<DBHAgroupData> listHAgroupData = new ArrayList<>();
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    // 직전에 클릭됐던 Item의 position

    APIcall_DB api_db = new APIcall_DB();
    Handler handler = new Handler(Looper.getMainLooper());
    private int prePosition = -1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_db_hagroup, parent, false);
        return new MessageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageViewHolder messageViewHolder = ((MessageViewHolder) holder);
        messageViewHolder.onBind(listHAgroupData.get(position), position);
        Button prom = ((MessageViewHolder) holder).promotion;

        final String masterid = ((MessageViewHolder) holder).masterid;
        final String slaveid = ((MessageViewHolder) holder).slaveid;
        final String groupid = ((MessageViewHolder) holder).groupid;
        final String masterstatus = ((MessageViewHolder) holder).masterstatus;
        String slavestatus = ((MessageViewHolder) holder).slavestatus;
        final String[] sec_id = new String[1];

        prom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if(masterstatus.equals("secondary")) sec_id[0] =masterid;
                else sec_id[0] =slaveid;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String s = api_db.updateHaGroupSlaveCount(groupid,sec_id[0]);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(v.getContext(), "적용 성공", Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(v.getContext(), "적용 실패", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }).start();
            };
        });
    }

    @Override
    public int getItemCount() {
        return listHAgroupData.size();
    }

    void addItem(DBHAgroupData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listHAgroupData.add(data);
    }

    private class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // API로 받아올 값들
        private ImageView imageView;
        private TextView hagroupname;
        private TextView slavecount;
        private TextView zone;
        private TextView masterName;
        private TextView masterStatus;
        private TextView masterFebricStatue;
        private TextView slaveName;
        private TextView slaveStatus;
        private TextView slaveFebricStatue;

        public String masterid;
        public String slaveid;
        public String slavestatus;
        public String masterstatus;
        public String groupid;

        private DBHAgroupData dbHAgroupData;

        // API 여부와 관계없이 고정된 뷰들
        private ConstraintLayout item;
        public Button promotion;


        // 포지션
        private int position;

        public MessageViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.img_service_db_hagroup);
            hagroupname = view.findViewById(R.id.txt_db_service_hagroupname);
            slavecount = view.findViewById(R.id.txt_service_db_hagroup_slave_num);
            zone = view.findViewById(R.id.txt_service_db_hagroup_zone);
            masterName = view.findViewById(R.id.service_db_hagroup_table_master_name);
            masterStatus = view.findViewById(R.id.service_db_hagroup_table_master_state);
            masterFebricStatue = view.findViewById(R.id.service_db_hagroup_table_master_febricstatus);
            slaveName = view.findViewById(R.id.service_db_hagroup_table_slave1_name);
            slaveStatus = view.findViewById(R.id.service_db_hagroup_table_slave1_state);
            slaveFebricStatue = view.findViewById(R.id.service_db_hagroup_table_slave1_febricstatus);
            promotion = view.findViewById(R.id.service_db_hagroup_table_slave1_promotion);

            item = view.findViewById(R.id.lay_service_db_hagroup_item);
        }

        void onBind(DBHAgroupData data, int position) {
            this.dbHAgroupData = data;
            this.position = position;


            imageView.setImageResource(data.getResId());
            hagroupname.setText(data.getHagroupname());
            slavecount.setText(data.getSlavecount());
            zone.setText(data.getZone());
            masterName.setText(data.getMasterName());
            masterStatus.setText(data.getMasterStatus());
            masterFebricStatue.setText(data.getMasterFebricStatus());
            slaveName.setText(data.getSlaveName());
            slaveStatus.setText(data.getSlaveStatus());
            slaveFebricStatue.setText(data.getSlaveFebricStatus());

            groupid = data.getHagroupId();
            slaveid = data.getSlaveId();
            masterid = data.getMasterId();
            slavestatus = data.getSlaveStatus();
            masterstatus = data.getMasterStatus();

            changeVisibility(selectedItems.get(position));

            imageView.setOnClickListener(this);
            hagroupname.setOnClickListener(this);
            slavecount.setOnClickListener(this);
            zone.setOnClickListener(this);
            masterName.setOnClickListener(this);
            masterStatus.setOnClickListener(this);
            masterFebricStatue.setOnClickListener(this);
            slaveName.setOnClickListener(this);
            slaveStatus.setOnClickListener(this);
            slaveFebricStatue.setOnClickListener(this);

        }

        //list click해서 접고 펼치기
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.txt_db_service_hagroupname:
                    if (selectedItems.get(position)) {
                        // 펼쳐진 Item을 클릭 시
                        selectedItems.delete(position);
                    } else {
                        // 직전의 클릭됐던 Item의 클릭상태를 지움
                        selectedItems.delete(prePosition);
                        // 클릭한 Item의 position을 저장
                        selectedItems.put(position, true);
                    }
                    // 해당 포지션의 변화를 알림
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    // 클릭된 position 저장
                    prePosition = position;
                    break;
            }

        }

        /**
         * 클릭된 Item의 상태 변경
         * @param isExpanded Item을 펼칠 것인지 여부
         */
        private void changeVisibility(final boolean isExpanded) {
            // height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용
            int dpValue = 214;
            float d = mContext.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            // Animation이 실행되는 시간, n/1000초
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    int value = (int) animation.getAnimatedValue();

                    item.getLayoutParams().height = value;
                    item.requestLayout();
                    item.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });
            // Animation start
            va.start();
        }
    }
}