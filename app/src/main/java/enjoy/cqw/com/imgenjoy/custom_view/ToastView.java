package enjoy.cqw.com.imgenjoy.custom_view;

import android.widget.Toast;

import enjoy.cqw.com.imgenjoy.App;

public class ToastView {
    private volatile static Toast mToast;

    private ToastView() {

    }

    public static Toast showSystemToast(String message) {
        if (mToast != null) {
            synchronized (ToastView.class) {
                if (mToast == null) {
                    mToast = Toast.makeText(App.getContext(), message, Toast.LENGTH_SHORT);
                } else {
                    mToast.setText(message);
                }
                mToast.show();
            }
        }
        return mToast;
    }
}
