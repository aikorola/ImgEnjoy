package enjoy.cqw.com.imgenjoy.activity.main_image_list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import enjoy.cqw.com.imgenjoy.R;
import enjoy.cqw.com.imgenjoy.activity.image_show_fun.ShowActivity;
import enjoy.cqw.com.imgenjoy.adapter.ImageAdapter;
import enjoy.cqw.com.imgenjoy.bean.ImgInfoDataBase;
import enjoy.cqw.com.imgenjoy.bean.ImgListVo;
import enjoy.cqw.com.imgenjoy.config.Constants;
import enjoy.cqw.com.imgenjoy.custom_view.CustomProgress;
import enjoy.cqw.com.imgenjoy.custom_view.StateView;

public class MainActivity2 extends AppCompatActivity implements MainContact.View {

    private static final String TAG = MainActivity2.class.getSimpleName() + "图片展示列表页面";
    public static final int OPERATION_CODE = 123;  // ShowActivity.class操作码
    private ImageAdapter mAdapter;
    private MainPresenter presenter;

    private RecyclerView mRecyclerView;
    private StateView mStateView;

    private List<ImgListVo.DataBean> data = new ArrayList<>();   // 存放所有的数据
    private int page = 1;           // 当前的页码
    private boolean isLoading;      // 是否正在加载
    private CustomProgress progress;
    private int pageCount = 20;     // 当前加载喜欢和下载的条数
    private boolean isEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initView();
        initData(true);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        presenter = new MainPresenter(MainActivity2.this);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mStateView = findViewById(R.id.stateView);
    }

    /**
     * 初始化数据
     */
    private void initData(final boolean isFirst) {
//        if (isFirst) {  // 第一次进，开全局菊花
//            progress = new CustomProgress(this);
//            progress.showProgressDialog("努力加载中...", true);
//        }
        isLoading = true;
        String keyword = getIntent().getStringExtra("keyword");
        switch (keyword) {
            case "like":
                // 去本地数据库中取到“喜欢”tag的NetPath和LocalPath
//                getLike(progress);
                getLike();
                break;
            case "download":
                // 去本地数据库中取到“下载”tag的NetPath（假如文件丢失，就需要）和LocalPath
//                Toast.makeText(MainActivity2.this, "download", Toast.LENGTH_SHORT).show();
//                getDownload(progress);
                getDownload();
                break;
            default:
                // 网络请求所有的图片
                presenter.getKeywordImgList(keyword, page, new MainPresenter.DataResponseCallBack() {
                    @Override
                    public void onFailure(String errorMsg) {
                        Log.e(TAG, "onFailure: 错误信息==>" + errorMsg);
                        if (progress != null) {
                            progress.setMessage("加载失败！");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progress.closeProgressDialog();
                                }
                            }, 800);
                        }
                        // 加载失败
                        mStateView.showError(new StateView.ClickListener() {
                            @Override
                            public void onClick(View view) {
                                progress = new CustomProgress(MainActivity2.this);
                                progress.showProgressDialog("正在加载中...", true);
                                initData(true);
                            }
                        });
                        isLoading = false;
                        if (mAdapter != null) {
                            mAdapter.loadMoreState(ImageAdapter.LOADMORE_LOADING_FAILURE);
                        }
//                        else if (progress != null || progress.isShowing()) {
//                            progress.setMessage("加载失败\n" + errorMsg);
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    progress.closeProgressDialog();
//                                }
//                            }, 800);
//                        }
                    }

                    @Override
                    public void onResponse(Object obj) {
                        Log.e(TAG, "onResponse: 请求成功    " + Thread.currentThread().getName());
                        if (progress != null) {
                            progress.setMessage("加载完成！");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progress.closeProgressDialog();
                                }
                            }, 800);
                        }
                        mStateView.hideView();
//                        if (progress != null || progress.isShowing()) {
//                            progress.setMessage("加载完成");
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    progress.closeProgressDialog();
//                                }
//                            }, 800);
//                        }
                        if (obj == null) {
                            // 加载到底了
                            if (isFirst) {
                                mStateView.showEmpty();
                            }
                            isEnd = true;
                            mAdapter.loadMoreState(ImageAdapter.LOADMORE_LOADING_EMPTY);
                            return;
                        }
                        ImgListVo vo = (ImgListVo) obj;
                        if (data != null && vo.getData() != null) {
                            data.addAll(vo.getData());
                            Log.e(TAG, "onResponse: " + data.size());
                            if (!data.isEmpty()) {
                                if (data.size() < 24) {
                                    isEnd = true;
                                    if (mAdapter == null) {
                                        mAdapter = new ImageAdapter(MainActivity2.this, data);
                                        mRecyclerView.setAdapter(mAdapter);
                                    }
                                    mAdapter.loadMoreState(ImageAdapter.LOADMORE_LOADING_EMPTY);
                                }
                            }
                            isLoading = false;

                            // 设置适配器
                            if (isFirst) {
                                if (mAdapter == null) {
                                    mAdapter = new ImageAdapter(MainActivity2.this, data);
                                    mRecyclerView.setAdapter(mAdapter);
                                } else {
                                    mAdapter.notifyDataSetChanged();
                                }
                                initEvent();
                            } else {
                                // 直接设置数据
                                if (mAdapter != null) {
                                    mAdapter.updateData(data);
                                }
                            }
                            Log.e(TAG, "onResponse_after: " + data.size());
                        }
                    }
                });
                break;
        }
    }

    /**
     * 初始化监听事件
     */
    private void initEvent() {
        // 初始化RecyclerView的Item的点击事件
        mAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, ImgListVo.DataBean data, int pos) {
//                Toast.makeText(MainActivity2.this, "itemClick==>" + v + "    pos: " + pos, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity2.this, ShowActivity.class);
                intent.putExtra("image_data", data);
                intent.putExtra("pos", pos);
                startActivityForResult(intent, OPERATION_CODE);
            }
        });

        // 初始化RecyclerView的Item的长按事件
//        mAdapter.setOnItemLongClickListener(new ImageAdapter.OnItemLongClickListener() {
//            @Override
//            public void onLongClick(View v, ImgListVo.DataBean data, int pos) {
////                Toast.makeText(MainActivity2.this, "itemLongClick==>" + v + "    pos: " + pos, Toast.LENGTH_SHORT).show();
//                if (Constants.ON_WICKED) {
//                    Constants.ON_WICKED = false;
//                    Toast.makeText(MainActivity2.this, "已关闭18模式，绅士再见！", Toast.LENGTH_SHORT).show();
//                } else {
//                    Constants.ON_WICKED = true;
//                    Toast.makeText(MainActivity2.this, "已开启18模式，隐藏这么深，还是被你发现啦！", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                newState
//                        SCROLL_STATE_IDLE = 0;      // 空闲
//                        SCROLL_STATE_DRAGGING = 1;  // 正在被外部拖拽,一般为用户正在用手指滚动
//                        SCROLL_STATE_SETTLING = 2;  // 自动滚动开始
                //1代表底部,返回true表示没到底部,还可以滑
                boolean b = mRecyclerView.canScrollVertically(1);
                if (!b && newState == 0 && !isLoading) {  // 如果到底了并且当前未开始加载、停止滑动的状态下
//                    Toast.makeText(MainActivity2.this, "到底了", Toast.LENGTH_SHORT).show();
                    // 继续加载
                    page += 1;
                    if (mAdapter != null) {
                        mAdapter.loadMoreState(ImageAdapter.LOADMORE_LOADING);
                    }
                    if (isEnd) {
                        if (mAdapter != null) {
                            mAdapter.loadMoreState(ImageAdapter.LOADMORE_LOADING_EMPTY);
                        }
                    } else {
                        initData(false);
                    }
                }

                //-1代表顶部,返回true表示没到顶,还可以滑
                boolean b1 = mRecyclerView.canScrollVertically(-1);
                if (!b1 && newState == 0) {
//                    Toast.makeText(MainActivity2.this, "到顶了", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 从本地持久化文件中取到“喜欢”的资源
     */
//    private void getLike(final CustomProgress progress) {
    private void getLike() {
        LitePal.getDatabase();
        List<ImgInfoDataBase> all = LitePal.where("id > 0").limit(pageCount).find(ImgInfoDataBase.class);
        Log.e(TAG, "getLike_before: " + all.size());
        if (all.isEmpty()) {
            mStateView.showEmpty();
//            if (progress != null) {
//                progress.closeProgressDialog();
//            }
        } else {
            for (ImgInfoDataBase info : all) {
                if (!info.isLike()) continue;
                ImgListVo.DataBean dataBean = new ImgListVo.DataBean();
                dataBean.setId(info.getResourceId());
                dataBean.setPath(info.getNetPath());
                dataBean.setUrl(info.getInfoUrl());
                ImgListVo.DataBean.ThumbsBean thumbs = new ImgListVo.DataBean.ThumbsBean();
                thumbs.setOriginal(info.getPreviewUrl());
                dataBean.setThumbs(thumbs);
                data.add(dataBean);
            }
            if (mAdapter == null) {
                mAdapter = new ImageAdapter(this, data);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
            Log.e(TAG, "getLike_after: " + data.size() + "        " + pageCount);
            mStateView.hideView();
//            if (progress != null) {
//                progress.setMessage("加载完成");
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        progress.closeProgressDialog();
//                    }
//                }, 800);
//            }
            if (data.size() < pageCount) {
                // 加载到底了
                isEnd = true;
                if (mAdapter != null) {
                    mAdapter.loadMoreState(ImageAdapter.LOADMORE_LOADING_EMPTY);
                }
            }
            pageCount += 20;
            isLoading = false;
            initEvent();
        }
    }

    /**
     * 从本地持久化文件中取到“下载”的资源
     */
    @SuppressLint("StaticFieldLeak")
//    private void getDownload(final CustomProgress progress) {
    private void getDownload() {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                progress.setMessage("正在加载...");
            }

            @Override
            protected List<String> doInBackground(Void... voids) {
                String imageResPath = Constants.IMAGE_RES_PATH;
                File[] files = new File(imageResPath).listFiles();
                if (files != null) {
                    List<String> res = new ArrayList<>();
                    for (int i = files.length - 1; i >= 0; i--) {
                        String path = files[i].getAbsolutePath();
                        res.add(path);
                    }
//                    for (File file : files) {
//                        String path = file.getAbsolutePath();
//                        res.add(path);
//                    }
                    return res;
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<String> s) {
                super.onPostExecute(s);
                if (s == null) {
                    Log.e(TAG, "getDownload: 空文件夹");
                    mStateView.showEmpty();
//                    if (progress != null) {
//                        mStateView.showEmpty();
//                        progress.setMessage("居然什么都没有哦！");
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                progress.closeProgressDialog();
//                            }
//                        }, 800);
//                    }
                } else {
                    for (String str : s) {
                        // str ==> /storage/emulated/0/ImgEnjoy/image_download/kwvzmq.jpg
                        str = str.substring(str.lastIndexOf("/") + 1, str.indexOf("."));  // kwvzmq

                        ImgListVo.DataBean b = new ImgListVo.DataBean();
                        b.setId(str);
                        b.setPath("https://w.wallhaven.cc/full/2e/wallhaven-" + str);
                        b.setUrl("https://wallhaven.cc/w/" + str);

                        ImgListVo.DataBean.ThumbsBean thumbs = new ImgListVo.DataBean.ThumbsBean();
                        String tag = str.substring(0, 2);
                        thumbs.setOriginal("https://th.wallhaven.cc/orig/" + tag + "/" + str);
                        b.setThumbs(thumbs);
                        b.setLocal(true);

                        data.add(b);

                        Log.e(TAG, "onPostExecute: " + b.toString());
                    }

                    if (mAdapter == null) {
                        mAdapter = new ImageAdapter(MainActivity2.this, data);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    mAdapter.notifyDataSetChanged();

                    isLoading = false;
                    isEnd = true;
                    mAdapter.loadMoreState(ImageAdapter.LOADMORE_LOADING_EMPTY);
                    initEvent();
                }
                mStateView.hideView();
//                if (progress != null) {
//                    progress.setMessage("加载完成");
//                    mStateView.hideView();
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            progress.closeProgressDialog();
//                        }
//                    }, 800);
//                }
            }
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPERATION_CODE) {
            if (data != null) {
                String operation = data.getStringExtra("operation");
                if (operation.equals("delete")) {
                    // 刷新页面
                    initData(false);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (data != null) {
            data.clear();
            data = null;
        }
        page = 1;
        pageCount = 20;
        isEnd = false;
    }
}
