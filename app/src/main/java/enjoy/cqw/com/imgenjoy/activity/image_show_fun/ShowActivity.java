package enjoy.cqw.com.imgenjoy.activity.image_show_fun;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import enjoy.cqw.com.imgenjoy.R;
import enjoy.cqw.com.imgenjoy.activity.WebViewActivity;
import enjoy.cqw.com.imgenjoy.activity.main_image_list.MainActivity2;
import enjoy.cqw.com.imgenjoy.bean.ImgInfoDataBase;
import enjoy.cqw.com.imgenjoy.bean.ImgListVo;
import enjoy.cqw.com.imgenjoy.config.Constants;
import enjoy.cqw.com.imgenjoy.custom_view.CustomProgress;
import enjoy.cqw.com.imgenjoy.util.FileUtil;
import enjoy.cqw.com.imgenjoy.util.HttpRequestImpl;
import enjoy.cqw.com.imgenjoy.util.ShareAndConvertUtil;
import enjoy.cqw.com.imgenjoy.util.wallpaper.SetWallpaper;

public class ShowActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ShowActivity.class.getSimpleName() + "图片显示页面";

    private final String APP_AUTHORITY = "cc.shinichi.wallpaperdemo.fileprovider";
    private ImageView mIvBack, mIvLike, mIvMore, mIvShare, mIvDownload, mIvWallpaper, mIvDelete;
    private PhotoView mShowInfo;
    private RelativeLayout mHeaderLayout, mFooterLayout;
    private ImgListVo.DataBean data;
    private boolean isOk, isLike, isDownload, isLoadingError;
    private Drawable mShareRes;
    private volatile String imageFilesPath;  // 下载好的图片的路径
    private String mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        initView();
        initData();
    }

    private void initView() {
        mShowInfo = findViewById(R.id.show_info);
        // 启用图片缩放功能
        mShowInfo.enable();
        // 禁用图片缩放功能 (默认为禁用，会跟普通的ImageView一样，缩放功能需手动调用enable()启用)
//        mShowInfo.disenable();
        // 获取图片信息
        Info info = mShowInfo.getInfo();
//        Log.e(TAG, "initView: " + info);
        // 从普通的ImageView中获取Info
//        Info info = PhotoView.getImageViewInfo(ImageView);
        // 从一张图片信息变化到现在的图片，用于图片点击后放大浏览，具体使用可以参照demo的使用
        mShowInfo.animaFrom(info);
        // 从现在的图片变化到所给定的图片信息，用于图片放大后点击缩小到原来的位置，具体使用可以参照demo的使用
        mShowInfo.animaTo(info, new Runnable() {
            @Override
            public void run() {
                //动画完成监听
            }
        });
        // 获取/设置 动画持续时间
        mShowInfo.setAnimaDuring(800);
        int d = mShowInfo.getAnimaDuring();
        // 获取/设置 最大缩放倍数
        mShowInfo.setMaxScale(3f);
        float maxScale = mShowInfo.getMaxScale();
        // 设置动画的插入器
        mShowInfo.setInterpolator(new FastOutSlowInInterpolator());

        mIvBack = findViewById(R.id.iv_back);
        mIvDelete = findViewById(R.id.iv_delete);
        mIvLike = findViewById(R.id.iv_like);
        mIvMore = findViewById(R.id.iv_more);
        mIvShare = findViewById(R.id.iv_share);
        mIvDownload = findViewById(R.id.iv_download);
        mIvWallpaper = findViewById(R.id.iv_wallpaper);
        mHeaderLayout = findViewById(R.id.rl_header);
        mFooterLayout = findViewById(R.id.rl_footer);

        mShowInfo.setOnClickListener(this);

        mIvBack.setOnClickListener(this);
        mIvDelete.setOnClickListener(this);
        mIvLike.setOnClickListener(this);
        mIvMore.setOnClickListener(this);
        mIvShare.setOnClickListener(this);
        mIvDownload.setOnClickListener(this);
        mIvWallpaper.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    private void initData() {
        LitePal.getDatabase();      // 开启数据库
        Intent intent = getIntent();
        data = (ImgListVo.DataBean) intent.getSerializableExtra("image_data");
        int pos = intent.getIntExtra("pos", -1);

        setLikeState(); // 设置喜欢的状态

        // 如果本地有下载好的数据则不再网络请求
        mFilePath = Constants.IMAGE_RES_PATH + data.getId() + ".jpg";
        Log.i(TAG, "localFilePath==>" + mFilePath + "  fileIsExist==" + FileUtil.checkFileExist(mFilePath));
        if (FileUtil.checkFileExist(mFilePath)) {
            mIvDelete.setVisibility(View.VISIBLE);
            imageFilesPath = mFilePath;
            isOk = true;
            mShowInfo.setImageBitmap(BitmapFactory.decodeFile(mFilePath));
            return;
        }
        mIvDelete.setVisibility(View.GONE);

        // 本地为下载，开启网络请求
        final ObjectAnimator anim = ObjectAnimator.ofInt(mShowInfo, "ImageLevel", 0, 10000);
        anim.setDuration(800);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.start();
        Glide.with(this)
                .load(data.getPath())
                .placeholder(R.drawable.rotate_progress)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        isOk = false;
                        isLoadingError = true;
                        anim.cancel();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        isOk = true;
                        anim.cancel();

                        mShareRes = resource;
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.mipmap.error)
                .into(mShowInfo);
    }

    /**
     * 设置喜欢的状态
     */
    private void setLikeState() {
        List<ImgInfoDataBase> imgInfo = queryImgInfo();
        if (imgInfo == null || imgInfo.isEmpty()) return;
        for (ImgInfoDataBase i : imgInfo) {
            Log.i("", "initView: " + i.toString());
            if (i.getResourceId().equals(data.getId())) {
                if (i.isLike()) {
                    isLike = true;
                    mIvLike.setImageResource(R.mipmap.like_check);
                } else {
                    isLike = false;
                    mIvLike.setImageResource(R.mipmap.like_normal);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_info:
                Log.i(TAG, "onClick: " + isLoadingError);
                if (isLoadingError) {   // 加载失败，点击可重新加载
                    initData();
                }
                if (mHeaderLayout.getVisibility() == View.VISIBLE && mFooterLayout.getVisibility() == View.VISIBLE) {
                    mHeaderLayout.setVisibility(View.GONE);
                    mFooterLayout.setVisibility(View.GONE);
                    mShowInfo.setBackgroundColor(Color.BLACK);
                    // 全屏当前Activity
//                    setFullScreen();
                } else {
                    mHeaderLayout.setVisibility(View.VISIBLE);
                    mFooterLayout.setVisibility(View.VISIBLE);
                    mShowInfo.setBackgroundColor(Color.WHITE);
                    // 退出全屏
//                    exitFullScreen();
                }
                break;
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.iv_delete:
                // 删除本地图片
                deleteLocalPhoto();
                break;
            case R.id.iv_like:
                // 喜欢/取消喜欢
                setLike();
                break;
            case R.id.iv_more:
                // 图片详情
                startImageInfo();
                break;
            case R.id.iv_share:
                if (mShareRes != null) {
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), ((BitmapDrawable) mShareRes).getBitmap(), null, null));
//                    Log.i(TAG, "onClick: " + uri);    onClick: content://media/external/images/media/2119214
                    ShareAndConvertUtil.shareImgToAll(ShowActivity.this, data.getUrl(), uri);
                } else {
                    Toast.makeText(ShowActivity.this, "图片还没加载好，请稍等！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_download:
                // 下载图片保存到本地以及数据库
                downloadPhoto();
                break;
            case R.id.iv_wallpaper:
                // 设置壁纸
                setWallPaper();
                break;
            default:
                break;
        }
    }

    /**
     * 删除本地图片
     */
    private void deleteLocalPhoto() {
        if (mFilePath != null) {
            File file = new File(mFilePath);
            boolean isFileExist = file.exists();
            if (isFileExist) {
                // 如果在本地---->删除
                boolean isDelete = file.delete();
                if (isDelete) {
                    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                    Intent intentData = new Intent();
                    intentData.putExtra("operation", "delete");
                    setResult(MainActivity2.OPERATION_CODE, intentData);
                    this.finish();
                } else {
                    Toast.makeText(this, "删除失败，请检查你的文件管理器！", Toast.LENGTH_SHORT).show();
                }
            } else {
                // 如果不在本地---->隐藏删除图标
                mIvDelete.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, "还没加载好哦！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 图片详情页
     */
    private void startImageInfo() {
        String url = data.getUrl();
        if (!TextUtils.isEmpty(url)) {
            Log.i(TAG, "startImageInfo: image_info_url==>" + url);

            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("more_url", url);
            startActivity(intent);
        } else {
            Toast.makeText(this, "等我加载完哦！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 喜欢/取消喜欢
     */
    private void setLike() {
        if (isLike) {
            ImgInfoDataBase like = new ImgInfoDataBase(data.getId(), data.getPath(), data.getUrl(), data.getThumbs().getOriginal(), false);
            like.saveOrUpdate("resourceId = ?", data.getId());


//            int delete = LitePal.delete(ImgInfoDataBase.class, mResourceId);// mResourceId是自增的ID
//            if (delete < 1) {
//                Toast.makeText(this, "取消喜欢失败！" + delete, Toast.LENGTH_SHORT).show();
//                return;
//            }

            isLike = false;
            mIvLike.setImageResource(R.mipmap.like_normal);

        } else {
            ImgInfoDataBase like = new ImgInfoDataBase(data.getId(), data.getPath(), data.getUrl(), data.getThumbs().getOriginal(), true);
            like.saveOrUpdate("resourceId = ?", data.getId());

//            int update = like.update(mResourceId);
//            if (update < 1) {
//                Toast.makeText(this, "喜欢失败！" + update, Toast.LENGTH_SHORT).show();
//                return;
//            }

            isLike = true;
            mIvLike.setImageResource(R.mipmap.like_check);
        }
    }

    /**
     * k
     * 从本地的数据库拿到已下载图片的想关信息
     */
    private List<ImgInfoDataBase> queryImgInfo() {
        return LitePal.findAll(ImgInfoDataBase.class);
    }

    /**
     * 选择设置壁纸的样式
     */
    private void setWallPaper() {
        if (isOk) {
            if (!TextUtils.isEmpty(imageFilesPath)) {
                // 调用系统的设置壁纸和联系人头像的方法，全兼容
                Toast.makeText(ShowActivity.this, "正在准备中...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra("mimeType", "image/*");
                Uri uri = null;
                try {
                    uri = Uri.parse(MediaStore.Images.Media
                            .insertImage(getContentResolver(),
                                    imageFilesPath, null, null));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                intent.setData(uri);
                startActivityForResult(intent, 1);
            } else {
                downloadPhoto();
                SetWallpaper.setWallpaper(ShowActivity.this, imageFilesPath, APP_AUTHORITY);
            }
        } else {
            Toast.makeText(ShowActivity.this, "图片没有加载好哦", Toast.LENGTH_SHORT).show();
        }
    }

//    private void setWallPaper() {
//        if (!TextUtils.isEmpty(imageFilesPath)) {
//            WallpaperManager mWallManager = WallpaperManager.getInstance(this);
//            try {
//                Bitmap bitmap = BitmapFactory.decodeFile(imageFilesPath);
//                mWallManager.setBitmap(bitmap);
//                Toast.makeText(this, "壁纸设置成功", Toast.LENGTH_SHORT)
//                        .show();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            downloadPhoto();
//        }
//    }
//
//    private void setLockWallPaper() {
//        if (!TextUtils.isEmpty(imageFilesPath)) {
//            try {
//                WallpaperManager mWallManager = WallpaperManager.getInstance(this);
//                Class class1 = mWallManager.getClass();//获取类名
//                Method setWallPaperMethod = class1.getMethod("setBitmapToLockWallpaper", Bitmap.class);//获取设置锁屏壁纸的函数
//                setWallPaperMethod.invoke(mWallManager, BitmapFactory.decodeFile(imageFilesPath));//调用锁屏壁纸的函数，并指定壁纸的路径imageFilesPath
//                Toast.makeText(this, "锁屏壁纸设置成功", Toast.LENGTH_SHORT).show();
//            } catch (Throwable e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        } else {
//            downloadPhoto();
//        }
//    }

    /**
     * 下载图片保存到本地
     */
    @SuppressLint("StaticFieldLeak")
    private void downloadPhoto() {
        final String filePath = Constants.IMAGE_RES_PATH + data.getId() + ".jpg";
        if (new File(filePath).exists()) {
            isDownload = true;
            imageFilesPath = filePath;
            Toast.makeText(this, "文件已存在，保存在：" + filePath, Toast.LENGTH_LONG).show();
            return;
        }
        final CustomProgress downloadProgress = new CustomProgress(this);
        downloadProgress.showProgressDialog("正在下载...", true);
        new Thread() {
            @Override
            public void run() {
                new HttpRequestImpl().httpDownloadFile(data.getPath(), filePath, new HttpRequestImpl.HttpFileCallBack() {
                    @Override
                    public void onResponse(final File file, int successCode) {
                        isDownload = true;
                        imageFilesPath = file.getAbsolutePath();
                        Log.i(TAG, "onResponse: " + imageFilesPath + "      " + file.getAbsolutePath());

                        //发送广播通知系统图库刷新数据
                        Uri uri = Uri.fromFile(file);
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                        Toast.makeText(ShowActivity.this, "文件下载成功，保存在：" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                downloadProgress.closeProgressDialog();
                            }
                        }, 800);
                    }

                    @Override
                    public void onError(Exception e, int errorCode) {
                        downloadProgress.setMessage("下载失败");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                downloadProgress.closeProgressDialog();
                            }
                        }, 800);
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        int currentProgress = (int) (progress * 100);
                        downloadProgress.setMessage("已下载：" + currentProgress + "%");
                    }
                });
            }
        }.start();
        downloadProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadProgress.setMessage("see you");
                downloadProgress.dismiss();
            }
        });

    }

    /**
     * 全屏显示
     */
    private void setFullScreen() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); // Activity全屏显示，且状态栏被覆盖掉
    }

    /**
     * 退出全屏
     */
    private void exitFullScreen() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(params);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); // Activity全屏显示，但是状态栏不会被覆盖掉，而是正常显示，只是Activity顶端布局会被覆盖住
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
