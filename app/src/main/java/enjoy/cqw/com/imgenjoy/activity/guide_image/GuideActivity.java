package enjoy.cqw.com.imgenjoy.activity.guide_image;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import enjoy.cqw.com.imgenjoy.R;
import enjoy.cqw.com.imgenjoy.activity.main_image_list.MainActivity2;
import enjoy.cqw.com.imgenjoy.bean.TranslationBean;
import enjoy.cqw.com.imgenjoy.config.Constants;
import enjoy.cqw.com.imgenjoy.custom_view.CustomProgress;
import enjoy.cqw.com.imgenjoy.custom_view.TitleCardView;
import enjoy.cqw.com.imgenjoy.util.HttpCallBack;
import enjoy.cqw.com.imgenjoy.util.HttpRequestUtil;
import enjoy.cqw.com.imgenjoy.util.wallpaper.ImageUtil;

public class GuideActivity extends AppCompatActivity {

    private TitleCardView mTclAnime, mTclNatural, mTclCar, mTclAnimal, mTclMovie, mTclGame, mTclHero, mTclSimplicity, mTclGirl, mTclOcean, mTclLike, mTclDownload;
    private LinearLayout mSearchView;
    private Button mBtnSearch;
    private EditText mKeyword;
    private InputMethodManager imm;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String KEY_ADMOB_SP_NAME = getString(R.string.key_admob_sp_name);
        final String KEY_IS_SHOW_ADMOB = getString(R.string.key_is_show_admob);

        initPermission();
        setContentView(R.layout.activity_guide);

        SharedPreferences sp = getSharedPreferences(KEY_ADMOB_SP_NAME, MODE_PRIVATE);
        boolean isShowAd = sp.getBoolean(KEY_IS_SHOW_ADMOB, false);
        if (isShowAd) {
            initAdMob();
        }else {
            // tip
            Toast.makeText(this,"如果您当前所在地属于被墙地区，请开启VPN，否则浏览速度较慢！",Toast.LENGTH_LONG).show();

            // 第一次进来让用户看不见广告
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(KEY_IS_SHOW_ADMOB, true);
            editor.apply();
        }
        initView();
        initEvent();
    }

    private void initAdMob() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("8EFBB638C6F3CEEF6709E66874830558").build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            private static final String TAG = "横幅广告单元";

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i(TAG, "onAdLoaded: ");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                /*
                    ERROR_CODE_INTERNAL_ERROR - 内部出现问题；例如，收到广告服务器的无效响应。  0
                    ERROR_CODE_INVALID_REQUEST - 广告请求无效；例如，广告单元 ID 不正确。      1
                    ERROR_CODE_NETWORK_ERROR - 由于网络连接问题，广告请求失败。                2
                    ERROR_CODE_NO_FILL - 广告请求成功，但由于缺少广告资源，未返回广告。         3
                */
                // Code to be executed when an ad request fails.
                Log.i(TAG, "onAdFailedToLoad: errorCode==>" + errorCode);
                switch (errorCode) {
                    case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                        Log.i(TAG, "onAdFailedToLoad: 内部出现问题；例如，收到广告服务器的无效响应。");
                        break;
                    case AdRequest.ERROR_CODE_INVALID_REQUEST:
                        Log.i(TAG, "onAdFailedToLoad: 广告请求无效；例如，广告单元 ID 不正确。");
                        break;
                    case AdRequest.ERROR_CODE_NETWORK_ERROR:
                        Log.i(TAG, "onAdFailedToLoad: 由于网络连接问题，广告请求失败。");
                        break;
                    case AdRequest.ERROR_CODE_NO_FILL:
                        Log.i(TAG, "onAdFailedToLoad: 广告请求成功，但由于缺少广告资源，未返回广告。");
                        break;
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.i(TAG, "onAdOpened: ");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.i(TAG, "onAdClicked: ");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i(TAG, "onAdLeftApplication: ");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.i(TAG, "onAdClosed: ");
            }
        });
    }

    private void initPermission() {
        String isSD = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, isSD) != PackageManager.PERMISSION_GRANTED) {
                // 申请权限
                ActivityCompat.requestPermissions(this, new String[]{isSD}, 1);
            } else {
                // 已授权
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                // 已授权
            } else {
                Toast.makeText(getApplicationContext(), "请给予存储权限，否则将无法使用程序！", Toast.LENGTH_LONG).show();
                initPermission();
            }
        }
    }

    private void initView() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mTclAnime = findViewById(R.id.tcl_anime);
        mTclNatural = findViewById(R.id.tcl_natural);
        mTclCar = findViewById(R.id.tcl_car);
        mTclGame = findViewById(R.id.tcl_game);
        mTclAnimal = findViewById(R.id.tcl_animal);
        mTclMovie = findViewById(R.id.tcl_movie);
        mTclSimplicity = findViewById(R.id.tcl_simplicity);
        mTclHero = findViewById(R.id.tcl_hero);
        mTclGirl = findViewById(R.id.tcl_girl);
        mTclOcean = findViewById(R.id.tcl_ocean);
        mTclLike = findViewById(R.id.tcl_like);
        mTclDownload = findViewById(R.id.tcl_download);
        mSearchView = findViewById(R.id.search_view);
        mBtnSearch = findViewById(R.id.search_button);
        mKeyword = findViewById(R.id.search_keyword);

        mTclAnime.setImageBitmap(ImageUtil.compressBitmap(this, R.mipmap.anime));
        mTclNatural.setImageBitmap(ImageUtil.compressBitmap(this, R.mipmap.nature));
        mTclCar.setImageBitmap(ImageUtil.compressBitmap(this, R.mipmap.car));
        mTclGame.setImageBitmap(ImageUtil.compressBitmap(this, R.mipmap.game));
        mTclAnimal.setImageBitmap(ImageUtil.compressBitmap(this, R.mipmap.animal));
        mTclMovie.setImageBitmap(ImageUtil.compressBitmap(this, R.mipmap.movie));
        mTclSimplicity.setImageBitmap(ImageUtil.compressBitmap(this, R.mipmap.simplicity));
        mTclGirl.setImageBitmap(ImageUtil.compressBitmap(this, R.mipmap.legs));
        mTclHero.setImageBitmap(ImageUtil.compressBitmap(this, R.mipmap.hero));
        mTclOcean.setImageBitmap(ImageUtil.compressBitmap(this, R.mipmap.ocean));
        mTclLike.setImageBitmap(ImageUtil.compressBitmap(this, R.mipmap.like));
        mTclDownload.setImageBitmap(ImageUtil.compressBitmap(this, R.mipmap.download_wall));
    }

    private void initEvent() {
        mTclAnime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConfig("anime");
            }
        });
        mTclNatural.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConfig("natural");
            }
        });
        mTclCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConfig("car");
            }
        });
        mTclGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConfig("game");
            }
        });
        mTclAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConfig("animal");
            }
        });
        mTclMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConfig("movie");
            }
        });
        mTclSimplicity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConfig("simplicity");
            }
        });
        mTclGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConfig("girl");
            }
        });
        mTclHero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConfig("hero");
            }
        });
        mTclOcean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConfig("ocean");
            }
        });
        mTclLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConfig("like");
            }
        });
        mTclDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConfig("download");
            }
        });
        // 监听软键盘状态
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect rect = new Rect();
                        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                        int rootInvisibleHeight = getWindow().getDecorView().getRootView().getHeight() - rect.bottom;
                        if (rootInvisibleHeight <= 100) {
                            //软键盘隐藏啦
                            mBtnSearch.setVisibility(View.VISIBLE);
                        } else {
                            //软键盘弹出啦
                            mBtnSearch.setVisibility(View.INVISIBLE);
                        }

                    }
                });
    }

    /**
     * 搜索按钮的监听
     */
    public void onSearch(View view) {
        if (mSearchView.getVisibility() == View.VISIBLE) {
            mSearchView.setVisibility(View.GONE);
            return;
        }
        mSearchView.setVisibility(View.VISIBLE);
        mKeyword.setFocusable(true);
        mKeyword.setFocusableInTouchMode(true);
        if (imm != null) {
            imm.showSoftInput(mKeyword, 0);
        }
    }

    /**
     * 取消按钮
     */
    public void onCancel(View view) {
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        mSearchView.setVisibility(View.GONE);
        mKeyword.setText("");
    }

    /**
     * 搜索按钮
     */
    public void onConfirm(View view) {
        String keyword = mKeyword.getText().toString();
        if (!"".equals(keyword)) {
            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
            final CustomProgress searchPro = new CustomProgress(this);
            searchPro.showProgressDialog("玩命搜索中...", false);
            // 1. start translation
            Map<String, String> params = new HashMap<>();
            params.put("doctype", "json");
            params.put("type", "AUTO");
            params.put("i", keyword);
            HttpRequestUtil.get(params, Constants.TRANSLATION_URL, new HttpCallBack() {
                @Override
                public void onFailure(String errorMsg) {
                    searchPro.closeProgressDialog();
                    Toast.makeText(GuideActivity.this, "系统繁忙，请稍后再试！", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String response) {
                    searchPro.closeProgressDialog();
                    // 1.1 parse json
                    TranslationBean translationBean = new Gson().fromJson(response, TranslationBean.class);
                    TranslationBean.TranslateResultBean bean = translationBean.getTranslateResult().get(0).get(0);
                    String target = bean.getTgt();
                    if (target != null) {
                        // 2. start search
                        Intent intent = new Intent(GuideActivity.this, MainActivity2.class);
                        intent.putExtra("keyword", target);
                        startActivity(intent);
                    } else {
                        Toast.makeText(GuideActivity.this, "系统繁忙，请稍后再试！", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void inProgress(float progress) {

                }
            });
        } else {
            Toast.makeText(this, "什么都没有找到啊QAQ", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 跳转的配置
     */
    private void startConfig(String keyword) {
        if (mSearchView.getVisibility() == View.VISIBLE) {
            mSearchView.setVisibility(View.GONE);
            return;
        }
        Intent intent = new Intent(GuideActivity.this, MainActivity2.class);
        intent.putExtra("keyword", keyword);
        startActivity(intent);
    }

    /**
     * 编辑模式
     */
    private void inEdit() {

    }
}
