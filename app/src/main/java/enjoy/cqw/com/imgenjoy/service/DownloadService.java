package enjoy.cqw.com.imgenjoy.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class DownloadService extends Service {

    private String mImageFilesPath;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        final ImgListVo.DataBean data = (ImgListVo.DataBean) intent.getSerializableExtra("data");
//
//        final String filePath = Constants.IMAGE_RES_PATH + data.getId() + ".jpg";
//        if (new File(filePath).exists()) {
//            Toast.makeText(this, "文件已存在，保存在：" + filePath, Toast.LENGTH_LONG).show();
//            return null;
//        }
//        Toast.makeText(this, "正在下载...", Toast.LENGTH_LONG).show();
//        new Thread() {
//            @Override
//            public void run() {
//                new HttpRequestImpl().httpDownloadFile(data.getPath(), filePath, new HttpRequestImpl.HttpFileCallBack() {
//                    @Override
//                    public void onResponse(File file, int successCode) {
//                        mImageFilesPath = file.getAbsolutePath();
//                        // 保存相关信息到数据库
////                        LitePal.getDatabase();
////                        ImgInfoDataBase base = new ImgInfoDataBase(data.getId(), data.getPath(), file.getAbsolutePath(), data.getUrl(), false);
////                        base.saveThrows();
//
//
//                        //发送广播通知系统图库刷新数据
//                        Uri uri = Uri.fromFile(file);
//                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
//                        Toast.makeText(DownloadService.this, "文件下载成功，保存在：" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onError(Call call, Exception e, int errorCode) {
//                        Toast.makeText(DownloadService.this, "下载失败", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void inProgress(float progress, long total, int id) {
//                        int currentProgress = (int) (progress * 100);
//                    }
//                });
//            }
//        }.start();
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public String getImageFilePath() {
            return mImageFilesPath;
        }
    }
}
