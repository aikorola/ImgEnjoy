package enjoy.cqw.com.imgenjoy.custom_view;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import enjoy.cqw.com.imgenjoy.R;


public class CustomProgress extends Dialog {
    private static final String TAG = "CustomProgress";
    private Context mContext;
    private CustomProgress mDialog;
    private TextView mText;

    public CustomProgress(Context context) {
        super(context);
        this.mContext = context;
    }

    public CustomProgress(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    /**
     * 当窗口焦点改变时调用
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.e(TAG, "onWindowFocusChanged: ");
        // 帧动画
        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        // 获取ImageView上的动画背景
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        // 开始动画
        spinner.start();

//        ObjectAnimator anim = ObjectAnimator.ofInt(findViewById(R.id.spinnerImageView), "rotation", 0, 10000);
//        anim.setDuration(800);
//        anim.setInterpolator(new LinearInterpolator());
//        anim.setRepeatCount(ObjectAnimator.INFINITE);
//        anim.start();
    }

    /**
     * 给Dialog设置提示信息
     *
     * @param message
     */
    public void setMessage(CharSequence message) {
        if (mDialog != null && mDialog.isShowing()) {
            if (message != null && message.length() > 0) {
                mText.setText(message);
                mText.invalidate();
            }
        }
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param context        上下文
     * @param message        提示
     * @param cancelable     是否按返回键取消
     * @param cancelListener 按下返回键监�?
     * @return
     */
    public static CustomProgress show(Context context, CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
        CustomProgress dialog = new CustomProgress(context, R.style.Custom_Progress);
        dialog.setTitle("");
        dialog.setContentView(R.layout.custom_progress);
        if (message == null || message.length() == 0) {
            dialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) dialog.findViewById(R.id.message);
            txt.setText(message);
        }
        // 按返回键是否取消
        dialog.setCancelable(cancelable);
        // 监听返回键处�?
        dialog.setOnCancelListener(cancelListener);
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层�?�明�?
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
        return dialog;
    }


    /**
     * 弹出自定义ProgressDialog
     *
     * @param context        上下�?
     * @param message        提示
     * @param cancelable     是否按返回键取消
     * @param cancelListener 按下返回键监�?
     * @return
     */
    public static CustomProgress getCustomProgress(Context context, CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
        CustomProgress dialog = new CustomProgress(context, R.style.Custom_Progress);
        dialog.setTitle("");
        dialog.setContentView(R.layout.custom_progress);
        if (message == null || message.length() == 0) {
            dialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) dialog.findViewById(R.id.message);
            txt.setText(message);
        }
        // 按返回键是否取消
        dialog.setCancelable(cancelable);
        // 监听返回键处�?
        dialog.setOnCancelListener(cancelListener);
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层�?�明�?
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        return dialog;
    }

    public void showProgressDialog(String message, boolean isCancel) {
        mDialog = new CustomProgress(mContext, R.style.Custom_Progress);
        mDialog.setTitle("");
        mDialog.setContentView(R.layout.custom_progress);
        mText = mDialog.findViewById(R.id.message);
        if (message == null || message.length() == 0) {
            mDialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            mText.setText(message);
        }
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        // 设置背景层半透明
        lp.dimAmount = 0.2f;
        mDialog.setCancelable(isCancel);
        mDialog.getWindow().setAttributes(lp);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        mDialog.show();
    }

    public void closeProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
