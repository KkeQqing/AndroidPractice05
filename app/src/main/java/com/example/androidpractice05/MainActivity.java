package com.example.androidpractice05;

import static android.app.ProgressDialog.show;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tv1,tv2,tv3;
    private Button btSetScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable( this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tv1=(TextView) findViewById(R.id.textview1);
        tv2=(TextView) findViewById(R.id.textview2);
        tv3=(TextView) findViewById(R.id.textview3);
        btSetScreen=(Button) findViewById(R.id.btScreen);

        btSetScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration cfg = getResources().getConfiguration();
                if (cfg.orientation == Configuration.ORIENTATION_LANDSCAPE) // 横屏
                    MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                else
                    MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });
    }

    public void getMessage(View v){
        Configuration cfg = getResources().getConfiguration();
        String screen=cfg.orientation==Configuration.ORIENTATION_LANDSCAPE?"横屏":"竖屏";
        String touchStatus=cfg.touchscreen==Configuration.TOUCHSCREEN_NOTOUCH?"无触摸屏":"支持触摸屏";
        String mncCode="运行商代码"+cfg.mnc;
        tv1.setText(screen);
        tv2.setText(touchStatus);
        tv3.setText(mncCode);
    }

    @Override
    public void onConfigurationChanged(Configuration cfg) {
        super.onConfigurationChanged(cfg);
        String screen = cfg.orientation ==
                Configuration.ORIENTATION_LANDSCAPE ? "横屏" : "竖屏";
        Toast.makeText( MainActivity.this,
                 "屏幕方向更改为" + screen,
                Toast.LENGTH_LONG).show();
    }
}