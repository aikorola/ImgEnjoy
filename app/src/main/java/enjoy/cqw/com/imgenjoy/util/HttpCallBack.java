package enjoy.cqw.com.imgenjoy.util;

public interface HttpCallBack {
    void onFailure(String errorMsg);
    void onSuccess(String response);
    void inProgress(float progress);
}
