package com.example.test1;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class CustomDialog{

    private Context context;

    public CustomDialog(@NonNull Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final String auto_topic) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        //dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);


        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.messaging_popup);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dlg.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = dlg.getWindow();
        window.setAttributes(lp);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText topic = (EditText) dlg.findViewById(R.id.txt_msg_send_topic);
        final EditText title = (EditText) dlg.findViewById(R.id.txt_msg_send_title);
        final EditText content = (EditText) dlg.findViewById(R.id.txt_msg_send_content);
        final Button okButton = (Button) dlg.findViewById(R.id.btn_msg_send_confirm);
        final Button cancelButton = (Button) dlg.findViewById(R.id.btn_msg_send_cancle);

        topic.setText(auto_topic);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 메시지 발행 API

                dlg.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 종료
                dlg.dismiss();
            }
        });


    }
}