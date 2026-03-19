事件处理-Configuration与Handler（消息传递）
操作目录：
1设置手机横屏
2 Handle使用
3 Handle、Loop、MessageQueue

1 设置手机横屏
要求：
（1）显示手机一些信息
（2）设置屏幕横屏竖屏
2 Handle使用--实现一个简单的图片轮播效果
要求：
（1）Activity 中通过 Timer（定时器）+ Handler（消息处理）的组合实现：
（2）从第 1 秒开始，每隔 1 秒触发一次图片切换；循环展示预设的 4 张图片；
（3）采用「弱引用 + 自定义 Handler」的写法，避免 Handler 持有 Activity 强引用导致的内存泄漏。
3 Handle、Looper、MessageQueue
要求：
（1）在UI主线程中随机加载图片，更新UI（容易）
（2）在窗体中建立线程，使用Handle随机加载图片，更新UI
