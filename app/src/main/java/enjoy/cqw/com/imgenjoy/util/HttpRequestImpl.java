package enjoy.cqw.com.imgenjoy.util;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.Map;

import okhttp3.Call;

public class HttpRequestImpl {
    private static final String TAG = "HttpRequestImpl网络请求";

    public void httpPost(String url, final HttpCallBack httpCallBack) {
//        LogUtils.d(TAG, "URL:" + url);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        httpCallBack.onError(call, e, i);
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        httpCallBack.onResponse(s, i);
                    }
                });
    }

    public void multiParamHttpPost(Map<String, String> params, String url, final HttpCallBack httpCallBack) {
//        LogUtils.d(TAG, "URL:" + url);
//        final OkHttpUtils okHttpUtils = new OkHttpUtils(null);

        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                httpCallBack.onError(call, e, i);
            }

            @Override
            public void onResponse(String s, int i) {
                httpCallBack.onResponse(s, i);
//                okHttpUtils.cancelTag(this);
            }
        });
    }

    public void multiParamHttpPost(Map<String, String> params, String url, long time, final HttpCallBack httpCallBack) {
//        LogUtils.d(TAG, "URL:" + url);

        OkHttpUtils.post().url(url).params(params).build().connTimeOut(time).readTimeOut(time).writeTimeOut(time).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                httpCallBack.onError(call, e, i);
            }

            @Override
            public void onResponse(String s, int i) {
                httpCallBack.onResponse(s, i);
            }
        });
    }

    public void httpGet(String url, final HttpCallBack httpCallBack) {
//        LogUtils.d(TAG, "URL:" + url);
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        httpCallBack.onError(call, e, i);
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        httpCallBack.onResponse(s, i);
                    }
                });
    }

    public void httpGet(String url, int timeOut, final HttpCallBack httpCallBack) {
//        LogUtils.d(TAG, "URL:" + url);
        OkHttpUtils.get()
                .url(url)
                .build()
                .connTimeOut(timeOut)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        httpCallBack.onError(call, e, i);
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        httpCallBack.onResponse(s, i);
                    }
                });
    }

    public void httpUploadFile(String token, String url, File file, final HttpCallBack httpCallBack) {
//        LogUtils.d(TAG, "URL:" + url);
        OkHttpUtils.post()
                .url(url + "?access_token=" + token)
//                .url(url + "?mine=com_upload_20181105")
//                .url(url)
                .addFile("fileImg", file.getName(), file)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        httpCallBack.onError(call, e, i);
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        httpCallBack.onResponse(s, i);
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        httpCallBack.inProgress(progress, total, id);
//                        LogUtils.d(TAG, "file upload progress:" + progress);
                    }
                });
    }

    public void httpUploadFile(Map<String, String> params, String url, File file, final HttpCallBack httpCallBack) {
//        LogUtils.d(TAG, "URL:" + url);
        OkHttpUtils.post()
                .addFile("fileImg", file.getName(), file)
                .params(params)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        httpCallBack.onError(call, e, i);
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        httpCallBack.onResponse(s, i);
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
//                        LogUtils.d(TAG, "file upload progress:" + progress);
                    }
                });
    }

    /**
     * 下载文件  调用execute(callback)进行执行，传入callback则代表是异步。如果单纯的execute()则代表同步的方法调用
     *
     * @param downFileUrl      下载链接
     * @param saveFileRootPath 保存路径
     * @param httpCallBack     监听
     */
    public void httpDownloadFile(String downFileUrl, String saveFileRootPath, final HttpFileCallBack httpCallBack) {
        if (httpCallBack == null) {
            return;
        }
        if (TextUtils.isEmpty(downFileUrl)) {
            return;
        }
        // 检查内存卡是否正常挂载
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return;
        }
        String destFileDir = saveFileRootPath.substring(0, saveFileRootPath.lastIndexOf("/") + 1);
        String destFileName = saveFileRootPath.substring(saveFileRootPath.lastIndexOf("/") + 1, saveFileRootPath.length());
//        Log.e(TAG, "httpDownloadFile_save: " + saveFileRootPath);
//        Log.e(TAG, "httpDownloadFile_dir: " + destFileDir);
//        Log.e(TAG, "httpDownloadFile_dest: " + destFileName);
        OkHttpUtils
                .get()
                .url(downFileUrl)
                .build()
                .execute(new FileCallBack(destFileDir, destFileName) {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        httpCallBack.inProgress(progress, total, id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int errorcode) {
                        Log.e(TAG, "onError: cause==>" + e.getCause() + "   exception==>" + e.getMessage());
                        httpCallBack.onError(e, errorcode);
                    }

                    @Override
                    public void onResponse(File file, int successCode) {
                        Log.e(TAG, "onResponse: " + file.getAbsolutePath());
                        httpCallBack.onResponse(file, successCode);
                    }
                });
    }

    public interface HttpCallBack {
        void onResponse(String result, int successCode);

        void onError(Call call, Exception e, int errorCode);

        void inProgress(float progress, long total, int id);
    }

    public interface HttpFileCallBack {
        void onResponse(File file, int successCode);

        void onError(Exception e, int errorCode);

        void inProgress(float progress, long total, int id);
    }
}
