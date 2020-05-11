package enjoy.cqw.com.imgenjoy.activity.main_image_list;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import enjoy.cqw.com.imgenjoy.bean.ImgListVo;
import enjoy.cqw.com.imgenjoy.bean.NiceImgBean;
import enjoy.cqw.com.imgenjoy.config.HttpConfig;
import enjoy.cqw.com.imgenjoy.util.HttpCallBack;
import enjoy.cqw.com.imgenjoy.util.HttpRequestUtil;

public class MainPresenter implements MainContact.Presenter {
    private static final String TAG = MainPresenter.class.getSimpleName() + "_图片列表类的执行类";

    private MainContact.View mView;

    public MainPresenter(MainContact.View mView) {
        this.mView = mView;
    }

    /**
     * 获得渲染图片列表的资源
     */
    @Override
    public void getRenderImgList(final DataResponseCallBack callBack) {
        String url = HttpConfig.NICE_IMG;
        HttpRequestUtil.get(url, new HttpCallBack() {
            @Override
            public void onFailure(String errorMsg) {
                Log.e(TAG, "    errorMsg: " + errorMsg);
                // 关闭菊花
                // 提示错误
                callBack.onFailure(errorMsg);
            }

            @Override
            public void onSuccess(String response) {
                Log.i(TAG, "onSuccess: " + "    response==>" + response);
                // 关闭菊花
                // 展示图片

                // 1. parse json to JavaBean
                try {
                    String code = new JSONObject().getString("code");
                    String message = new JSONObject().getString("message");
                    Log.e(TAG, "responseCode==>" + code + "  message==>" + message);
                    if (!code.equals(200) || !message.equals("成功！")) {
                        Log.e(TAG, "API接口出现问题");
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "JSONException==>" + e.getMessage());
                }
                NiceImgBean niceImgBean = new Gson().fromJson(response, NiceImgBean.class);
                if (niceImgBean.getResult() == null && niceImgBean.getResult().size() <= 0) {
                    Log.e(TAG, "接口数据集合为空");
                    return;
                }
                callBack.onResponse(niceImgBean.getResult());
            }

            @Override
            public void inProgress(float progress) {
                // 保持显示菊花
                // 提示进度
            }
        });
    }

    @Override
    public void getKeywordImgList(String keyword, int page, final DataResponseCallBack callBack) {
        Map<String, String> params = new HashMap<>();
        // 模糊搜索
        params.put("q", keyword);
        // 加载的页码    每页24条data
        params.put("page", page + "");
        // 数据随机
        params.put("sorting", "random");
//        if (Constants.ON_WICKED) {
//            params.put("purity", "sfw");
//        }
        params.put("apikey", "X2jFCjkqVwOOfBJrscEanpiHqeBjIfae");
        HttpRequestUtil.get(params, HttpConfig.WALL_PAPER_ROOT_URL, new HttpCallBack() {
            @Override
            public void onFailure(String errorMsg) {
//                mView.showLoadingFailure();
                callBack.onFailure(errorMsg);
            }

            @Override
            public void onSuccess(String response) {
                Log.i(TAG, "onSuccess: " + response);
                if (TextUtils.isEmpty(response) || response.equals("null") || response.equals("")) {
//                    mView.showLoadingFailure();
                    callBack.onFailure("服务器宕机");
                    return;
                }
//                mView.closeLoading();
                ImgListVo data = (ImgListVo) parseJson(response, ImgListVo.class);
                if (data != null && data.getData().size() > 0) {
                    callBack.onResponse(data);
                } else {
                    callBack.onFailure("JavaBean转换失败");
                }
            }

            @Override
            public void inProgress(float progress) {
//                mView.showLoading();
                int currProgress = (int) (progress * 100);
//                Log.e(TAG, "inProgress: " + currProgress);
            }
        });
    }

    /**
     * 解析json
     */
    private Object parseJson(String jsonStr, Class clz) {
        return new Gson().fromJson(jsonStr, clz);
    }

    interface DataResponseCallBack {
        void onFailure(String errorMsg);

        void onResponse(Object obj);
    }
}
