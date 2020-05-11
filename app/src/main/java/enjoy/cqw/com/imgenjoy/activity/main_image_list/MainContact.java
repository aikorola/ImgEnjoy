package enjoy.cqw.com.imgenjoy.activity.main_image_list;

public interface MainContact {
    interface View {
        // 对View的操作
    }

    interface Presenter {
        // 获取列表
        void getRenderImgList(MainPresenter.DataResponseCallBack callBack);

        // 获取指定关键词的图片集合
        void getKeywordImgList(String keyword, int page, MainPresenter.DataResponseCallBack callBack);
    }
}
