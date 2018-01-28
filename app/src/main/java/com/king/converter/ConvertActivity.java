package com.king.converter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.king.khcareer.model.sql.player.MySQLHelper;
import com.king.mytennis.view.R;

public class ConvertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        MySQLHelper.initAppContext(this);
        final ConvertManager manager = new ConvertManager(this);
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.start();
            }
        });
    }
}
