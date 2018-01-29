package com.king.converter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.king.khcareer.base.KApplication;
import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.model.sql.player.MySQLHelper;
import com.king.khcareer.utils.DBExportor;
import com.king.mytennis.view.R;

import java.io.File;

public class ConvertActivity extends AppCompatActivity {
    private TextView tvStatus;

    private StringBuffer statusBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        tvStatus = findViewById(R.id.tv_status);

        MySQLHelper.initAppContext(this);
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusBuffer = new StringBuffer();
                tvStatus.setVisibility(View.VISIBLE);
                new ConvertManager(ConvertActivity.this, new ConvertManager.ConvertCallback() {

                    @Override
                    public void appendStatus(String message) {
                        statusBuffer.append(message).append("\n");
                        tvStatus.setText(statusBuffer.toString());
                    }

                    @Override
                    public void onSuccess() {

                        // 导出到指定目录
                        DBExportor.execute();
                        // 删除db
                        String dbPath = KApplication.getInstance().getFilesDir().getParent() + "/databases/khcareer.db";
                        File file = new File(dbPath);
                        if (file.exists()) {
                            file.delete();
                        }

                        statusBuffer.append("Convert finished, database has been exported to ").append(Configuration.EXPORT_DIR);
                        tvStatus.setText(statusBuffer.toString());
                    }

                    @Override
                    public void onFailed(String msg) {
                        statusBuffer.append("Convert failed：").append(msg);
                        tvStatus.setText(statusBuffer.toString());
                    }
                }).start();
            }
        });
    }
}
