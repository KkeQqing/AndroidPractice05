package com.example.androidpractice05;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class CarouselActivity extends AppCompatActivity {

    private ImageView ivCarousel;
    private MyHandler myHandler;
    private Timer timer;
    private TimerTask timerTask;

    // 标记当前是第几张图，初始为 -1，因为第一次会切换到 0
    private int currentImageIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔥 添加这行代码：在日志和弹窗中证明“我真的是 CarouselActivity”
        android.util.Log.d("DEBUG_TAG", "✅ 成功进入 CarouselActivity！");
        android.widget.Toast.makeText(this, "✅ 我是轮播页 CarouselActivity", android.widget.Toast.LENGTH_LONG).show();


        setContentView(R.layout.activity_carousel);

        // 1. 初始化控件
        ivCarousel = findViewById(R.id.iv_carousel);
        // 初始化时先设置第一张图，并将索引 0 存入 tag
        ivCarousel.setImageResource(R.drawable.img1);
        ivCarousel.setTag(0);

        // 2. 初始化自定义 Handler (传入 ImageView)
        myHandler = new MyHandler(ivCarousel);

        // 3. 启动定时器
        startTimer();
    }

    private void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                // TimerTask 运行在子线程，不能直接操作 UI
                // 发送消息给 Handler，由 Handler 在主线程更新 UI
                myHandler.sendEmptyMessage(MyHandler.MSG_UPDATE_IMAGE);
            }
        };

        // schedule 参数说明：
        // timerTask: 任务
        // 1000: 延迟 1000 毫秒 (1秒) 后开始执行第一次
        // 1000: 每隔 1000 毫秒 (1秒) 执行一次
        timer.schedule(timerTask, 1000, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 【关键优化】在 Activity 销毁时，必须停止定时器，否则会导致内存泄漏或崩溃
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        // 移除 Handler 中所有待处理的消息
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
    }
}