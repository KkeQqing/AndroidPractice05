package com.example.androidpractice05;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.Random;

public class HandlerThreadActivity extends AppCompatActivity {

    private ImageView ivImage;
    private Button btThread;

    // 图片资源数组 (请确保你的 drawable 里有这些名字的图片，或者改成 img1, img2...)
    // 这里为了演示，假设你有 R.drawable.img1 到 R.drawable.img8
    private final int[] imageResIds = {
            R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4,
            R.drawable.img5, R.drawable.img6, R.drawable.img7, R.drawable.img8
    };

    // 用于保存随机选中的图片索引
    private int mChoiceImgIndex = 0;

    // 消息常量
    private static final int CHANGE_ID = 0; // 子线程收到此消息：生成随机数
    private static final int SHOW_IMAGE = 1; // 主线程收到此消息：更新 UI

    // 1. 主线程 Handler (用于更新 UI)
    // 使用静态内部类 + WeakReference 防止内存泄漏
    static class MainHandler extends Handler {
        private final WeakReference<HandlerThreadActivity> activityRef;

        public MainHandler(HandlerThreadActivity activity) {
            super(Looper.getMainLooper()); // 显式绑定主线程 Looper
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            HandlerThreadActivity activity = activityRef.get();
            if (activity != null) {
                if (msg.what == SHOW_IMAGE) {
                    // 更新 ImageView
                    activity.ivImage.setImageResource(activity.imageResIds[activity.mChoiceImgIndex]);
                    Toast.makeText(activity, "主线程已更新图片：第 " + (activity.mChoiceImgIndex + 1) + " 张", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private MainHandler mainHandler;

    // 2. 子线程类
    class MyThread extends Thread {
        private Handler subHandler; // 子线程自己的 Handler

        @Override
        public void run() {
            // 【关键】子线程必须初始化 Looper 才能创建 Handler
            Looper.prepare();

            subHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == CHANGE_ID) {
                        // --- 子线程工作区：耗时操作或逻辑计算 ---
                        Random random = new Random();
                        mChoiceImgIndex = random.nextInt(imageResIds.length);

                        Toast.makeText(HandlerThreadActivity.this,
                                "子线程生成了索引：" + mChoiceImgIndex,
                                Toast.LENGTH_SHORT).show();

                        // --- 通知主线程更新 UI ---
                        // 注意：这里不能直接改 ImageView，必须发消息给 mainHandler
                        mainHandler.sendEmptyMessage(SHOW_IMAGE);
                    }
                }
            };

            // 【关键】启动消息循环，否则 Handler 收不到消息
            Looper.loop();
        }

        // 提供一个方法让外部能向子线程发消息
        public void triggerRandom() {
            if (subHandler != null) {
                subHandler.sendEmptyMessage(CHANGE_ID);
            }
        }
    }

    private MyThread myThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_thread);

        // 初始化控件
        ivImage = findViewById(R.id.iv_image);
        btThread = findViewById(R.id.bt_thread);

        // 初始化主线程 Handler
        mainHandler = new MainHandler(this);

        // 初始化并启动子线程
        myThread = new MyThread();
        myThread.start();

        // 设置子线程按钮的点击监听
        btThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击后，通知子线程去干活
                myThread.triggerRandom();
            }
        });

        // 默认先显示第一张图
        ivImage.setImageResource(imageResIds[0]);
    }

    /**
     * 按钮 1 的回调函数：直接在 UI 主线程随机加载图片
     * (对应 XML 中的 android:onClick="showImageByUiThread")
     */
    public void showImageByUiThread(View view) {
        Random random = new Random();
        mChoiceImgIndex = random.nextInt(imageResIds.length);

        // 直接在主线程更新 UI (这是允许的，因为就在主线程)
        ivImage.setImageResource(imageResIds[mChoiceImgIndex]);

        Toast.makeText(this, "UI 主线程直接加载：第 " + (mChoiceImgIndex + 1) + " 张", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理资源，防止泄漏
        if (myThread != null) {
            // 实际开发中可能需要更优雅的方式停止 Looper，这里简单处理
            myThread.interrupt();
        }
        if (mainHandler != null) {
            mainHandler.removeCallbacksAndMessages(null);
        }
    }
}