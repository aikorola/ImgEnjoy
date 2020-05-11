package enjoy.cqw.com.imgenjoy.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import enjoy.cqw.com.imgenjoy.R;

public class StateView extends LinearLayout {

    private static final String TAG = "StateView";
    private View mView;
    private ImageView mIvState;
    private TextView mTvState;
    private Button mBtnState;

    private Context mContext;
    private RotateAnimation mRotateAnim;

    public StateView(Context context) {
//        super(context);
        this(context, null);
        this.mContext = context;
    }

    public StateView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, -1);
        this.mContext = context;
    }

    public StateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        setClickable(true);
        initView();
    }

    private void initView() {
        mView = View.inflate(mContext, R.layout.state_view, null);
        addView(mView);
        mIvState = mView.findViewById(R.id.iv_state);
        mTvState = mView.findViewById(R.id.tv_state);
        mBtnState = mView.findViewById(R.id.btn_state);

        showLoading();
    }

    /**
     * 隐藏整个布局
     */
    public void hideView() {
        setVisibility(GONE);
    }

    public void showLoading() {
        mView.setVisibility(VISIBLE);
        mBtnState.setVisibility(GONE);
        mTvState.setVisibility(VISIBLE);
        mIvState.setVisibility(VISIBLE);

        mTvState.setText("正在加载中...");

//        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.anim_rotate);
        mRotateAnim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnim.setDuration(800);
        mRotateAnim.setRepeatMode(Animation.RESTART);
        mRotateAnim.setRepeatCount(Animation.INFINITE);
        mRotateAnim.setInterpolator(new FastOutSlowInInterpolator());
        mIvState.startAnimation(mRotateAnim);
    }

    public void showEmpty() {
        if (mRotateAnim != null) {
            mRotateAnim.cancel();
        }
        mView.setVisibility(VISIBLE);
        mBtnState.setVisibility(GONE);
        mTvState.setVisibility(VISIBLE);
        mIvState.setVisibility(VISIBLE);

        mIvState.setImageResource(R.mipmap.empty);
        mTvState.setText("什么都没找到哦QAQ");
    }

    public void showError(final ClickListener listener) {
        if (mRotateAnim != null) {
            mRotateAnim.cancel();
        }
        mView.setVisibility(VISIBLE);
        mBtnState.setVisibility(VISIBLE);
        mTvState.setVisibility(VISIBLE);
        mIvState.setVisibility(VISIBLE);

        mIvState.setImageResource(R.mipmap.error);
        mTvState.setText("加载失败");
        mBtnState.setClickable(true);
        mBtnState.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
    }

    public interface ClickListener {
        void onClick(View view);
    }
}
