package enjoy.cqw.com.imgenjoy.custom_view;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import enjoy.cqw.com.imgenjoy.R;

/**
 * 自定义的dialog(底部出现的效果)
 */
public class CustomDialog extends Dialog {
    private TextView mLockScreen, mDesktop, mAll;
    private Context mContext;       // 或者用dialog提供的getContext()

    private LockScreenClick lockScreenClick;
    private DesktopClick desktopClick;
    private AllStyleClick allStyleClick;

    public CustomDialog(@NonNull Context context) {
//        super(context);
        // 设置自定义dialog的样式
        super(context, R.style.CustomDialogStyle);

        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initView();

        setCancelable(true);

        // 设置窗口样式
        setWindow();

        initEvent();
    }

    /**
     * 设置窗口样式
     */
    private void setWindow() {
        // todo 出现两个bug
        // 1. 解决自定义dialog布局没出现在屏幕底部的bug
        Window window = getWindow();    // 得到窗口
        WindowManager.LayoutParams params = window.getAttributes(); // 得到属性参数
        // 设置参数(和在xml中设置属性是一样的)
        params.width = WindowManager.LayoutParams.MATCH_PARENT; // 设置宽度为匹配父容器
//        int height = getWindow().getWindowManager().getDefaultDisplay().getHeight();    // 得到屏幕高度
//        params.height = height / 3;   // 设置dialog高度为屏幕的三分之一
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;   // 设置dialog高度为屏幕的三分之一
        params.gravity = Gravity.BOTTOM;    // 设置dialog在屏幕底部显示

        // 2. 解决dialog的样式bug------设置样式的代码见构造方法
        // todo dialog不能宽度不能占全，解决dialog默认(因为继承了dialog)有内边距的问题，也可以在xml中设置style时重写父类(dialog)的内边距
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setWindowAnimations(android.R.style.Animation_Dialog);
    }


    private void initView() {
        mLockScreen = (TextView) findViewById(R.id.lock_screen_wall_paper);
        mDesktop = (TextView) findViewById(R.id.desktop_wall_paper);
        mAll = (TextView) findViewById(R.id.all_wall_paper);
    }

    private void initEvent() {
        mLockScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lockScreenClick != null) {
                    lockScreenClick.onClick();
                }
            }
        });
        mDesktop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desktopClick != null) {
                    desktopClick.onClick();
                }
            }
        });
        mAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allStyleClick != null) {
                    allStyleClick.onClick();
                }
            }
        });
    }

    public void setLockScreenClickListener(LockScreenClick lockScreenListener) {
        this.lockScreenClick = lockScreenListener;
    }

    public void setDesktopClickListener(DesktopClick desktopClickListener) {
        this.desktopClick = desktopClickListener;
    }

    public void setAllStyleClickListener(AllStyleClick allStyleClickListener) {
        this.allStyleClick = allStyleClickListener;
    }

    public interface LockScreenClick {
        void onClick();
    }

    public interface DesktopClick {
        void onClick();
    }

    public interface AllStyleClick {
        void onClick();
    }
}

