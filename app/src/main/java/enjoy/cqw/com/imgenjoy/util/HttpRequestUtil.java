package enjoy.cqw.com.imgenjoy.util;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

public class HttpRequestUtil {

    public static void get(final String url, final HttpCallBack callBack) {
        new Thread(){
            @Override
            public void run() {
                OkHttpUtils
                        .get()
                        .url(url)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                callBack.onFailure("请求失败");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                callBack.onSuccess(response);
                            }
                        });
            }
        }.start();
    }

    public static void get(final Map<String, String> params, final String url, final HttpCallBack callBack) {
        new Thread(){
            @Override
            public void run() {
                OkHttpUtils
                        .get()
                        .url(url)
                        .params(params)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                callBack.onFailure("请求失败"+e.getMessage());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                callBack.onSuccess(response);
                            }

                            @Override
                            public void inProgress(float progress, long total, int id) {
                                super.inProgress(progress, total, id);
                                callBack.inProgress(progress);
                            }
                        });
            }
        }.start();
    }

    public static void post(Map<String, String> params, String url, final HttpCallBack callBack) {
        OkHttpUtils
                .post()
                .url(url)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onFailure("请求失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        callBack.onSuccess(response);
                    }
                });
    }
}
