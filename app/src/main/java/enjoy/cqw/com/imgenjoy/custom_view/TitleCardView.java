package enjoy.cqw.com.imgenjoy.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import enjoy.cqw.com.imgenjoy.R;

public class TitleCardView extends CardView {

    private final String mTitleText;
    private ImageView mRes;
    private TextView mDesc;

    public TitleCardView(@NonNull Context context) {
//        super(context);
        this(context, null);
    }

    public TitleCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, -1);
    }

    public TitleCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        initView(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleCardView);
        String titleText = ta.getString(R.styleable.TitleCardView_title_text);
        int titleSrc = ta.getResourceId(R.styleable.TitleCardView_title_src, R.mipmap.nature);

        mRes.setImageResource(titleSrc);
        mDesc.setText(titleText);
        this.mTitleText = titleText;

        ta.recycle();
    }

    public String getTitleText() {
        return mTitleText;
    }

//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getPointerCount() == 1) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_UP:
//                    mRes.setAlpha(1f);
//                    mDesc.setTextColor(Color.parseColor("#FFFFFF"));
//                    break;
//                case MotionEvent.ACTION_BUTTON_PRESS:
//                    mRes.setAlpha(0.7f);
//                    mDesc.setTextColor(Color.parseColor("#e14c96"));
//                    break;
//            }
//        }
//        return super.onTouchEvent(event);
//    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.title_cardview, null);
        this.addView(view);
        mRes = view.findViewById(R.id.iv_res);
        mDesc = view.findViewById(R.id.tv_desc);
    }

    public void setImageBitmap(Bitmap bmp) {
        mRes.setImageBitmap(bmp);
    }

    public void setTitleText(String text) {
        mDesc.setText(text);
    }
}
