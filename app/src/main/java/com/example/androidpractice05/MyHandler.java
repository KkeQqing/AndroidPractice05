package com.example.androidpractice05;

import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * 自定义 Handler，使用弱引用持有 Activity 或 ImageView，防止内存泄漏
 */
public class MyHandler extends Handler {
    // 定义消息类型常量
    public static final int MSG_UPDATE_IMAGE = 1;

    // 使用弱引用持有 ImageView，而不是持有整个 Activity，进一步降低风险
    // 如果持有 Activity，请用 WeakReference<CarouselActivity>
    private final WeakReference<ImageView> imageViewRef;

    public MyHandler(ImageView imageView) {
        this.imageViewRef = new WeakReference<>(imageView);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == MSG_UPDATE_IMAGE) {
            ImageView imageView = imageViewRef.get();
            if (imageView != null) {
                // 获取当前显示的图片索引（存储在 tag 中）
                Object tag = imageView.getTag();
                int currentIndex = (tag == null) ? 0 : (int) tag;

                // 图片资源 ID 数组
                int[] imageResIds = {
                        R.drawable.img1,
                        R.drawable.img2,
                        R.drawable.img3,
                        R.drawable.img4
                };

                // 计算下一张图片的索引（循环）
                int nextIndex = (currentIndex + 1) % imageResIds.length;

                // 更新图片
                imageView.setImageResource(imageResIds[nextIndex]);

                // 更新 tag 为新的索引
                imageView.setTag(nextIndex);
            }
        }
    }
}