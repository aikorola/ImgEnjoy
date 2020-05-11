package enjoy.cqw.com.imgenjoy.activity.main_image_list;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.UpDownAnimatorAdapter;

import java.util.Random;

import enjoy.cqw.com.imgenjoy.R;
import enjoy.cqw.com.imgenjoy.adapter.ImageCardAdapter;
import enjoy.cqw.com.imgenjoy.bean.ImgListVo;
import enjoy.cqw.com.imgenjoy.custom_view.LoadingView;
import enjoy.cqw.com.imgenjoy.custom_view.ParallaxRecyclerView;

public class MainActivity extends AppCompatActivity implements MainContact.View, CardStackView.ItemExpendListener {

    private static final String TAG = MainActivity.class.getSimpleName() + "图片展示列表页面";
    private ParallaxRecyclerView mRecyclerView;
    private CardStackView mCardStackView;
    //    private ResAdapter mAdapter;
//    private TestStackAdapter mAdapter;
    private ImageCardAdapter mAdapter;
    private MainPresenter presenter;
    private LoadingView mLoadStateView;
    private int page;
    private String lastPage;

    private static String[] keys = {"landscape", "nature", "car", "sky", "search", "legs", "hero", "clouds", "natural", "ocean"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initEvent();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        presenter = new MainPresenter(MainActivity.this);
//        mLoadStateView = new LoadingView(this);
//        mRecyclerView = findViewById(R.id.recyclerView);
        mCardStackView = findViewById(R.id.cardStackView);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mAdapter = new ImageCardAdapter(MainActivity.this);
        mCardStackView.setAdapter(mAdapter);
        mCardStackView.setItemExpendListener(this);
//        mCardStackView.setAnimatorAdapter(new AllMoveDownAnimatorAdapter(mCardStackView));
        mCardStackView.setAnimatorAdapter(new UpDownAnimatorAdapter(mCardStackView));
//        mCardStackView.setAnimatorAdapter(new UpDownStackAnimatorAdapter(mCardStackView));

        int i = new Random().nextInt(keys.length - 1);
        presenter.getKeywordImgList(keys[i], page, new MainPresenter.DataResponseCallBack() {
            @Override
            public void onFailure(String errorMsg) {
                Log.e(TAG, "onFailure: 错误信息==>" + errorMsg);
            }

            @Override
            public void onResponse(Object obj) {
                ImgListVo vo = (ImgListVo) obj;
                mAdapter.updateData(vo.getData());
            }
        });

//        presenter.getRenderImgList(new MainPresenter.DataResponseCallBack() {
//            @Override
//            public void onFailure(Call call, Exception e, int errorCode) {
//
//            }
//
//            @Override
//            public void onResponse(List<NiceImgBean.ResultBean> niceBean) {
//                mAdapter = new ResAdapter(niceBean, MainActivity.this);
//                mRecyclerView.setAdapter(mAdapter);
//                if (mAdapter != null) {
//                    mAdapter.setOnItemClickListener(new ResAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View v, int position) {
//                            Toast.makeText(MainActivity.this, "当前点击的是：" + position, Toast.LENGTH_SHORT).show();
//                            // 点击item时将图片全屏，并提供相应功能
//                            startActivity(new Intent(MainActivity.this, ShowActivity.class));
//                        }
//                    });
//                }
//            }
//        });
    }

    /**
     * 初始化监听事件
     */
    private void initEvent() {
        mAdapter.setOnItemClickListener(new ImageCardAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int pos) {
                Toast.makeText(MainActivity.this, "itemClick==>" + v + "    pos: " + pos, Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.setOnItemLongClickListener(new ImageCardAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(View v, int pos) {
                Toast.makeText(MainActivity.this, "itemLongClick==>" + v + "    pos: " + pos, Toast.LENGTH_SHORT).show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mCardStackView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    //1代表底部,返回true表示没到底部,还可以滑
                    boolean b = mCardStackView.canScrollVertically(1);
                    if (!b) {
                        Toast.makeText(MainActivity.this, "到底了", Toast.LENGTH_SHORT).show();
                    }

                    //-1代表顶部,返回true表示没到顶,还可以滑
                    boolean b1 = mCardStackView.canScrollVertically(-1);
                    if (!b1) {
                        Toast.makeText(MainActivity.this, "到顶了", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /**
     * 判断卡片是否被点击，true就将卡片展开
     *
     * @param expend
     */
    @Override
    public void onItemExpend(boolean expend) {

    }
}
