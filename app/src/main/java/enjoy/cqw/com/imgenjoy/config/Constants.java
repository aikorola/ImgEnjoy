package enjoy.cqw.com.imgenjoy.config;

import android.os.Environment;

public class Constants {
    // 根路径
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory() + "/ImgEnjoy/";

    // 图片保存路径
    public static final String IMAGE_RES_PATH = ROOT_PATH + "image_download/";

    // 自动检测语言进行翻译地址
    public static final String TRANSLATION_URL = "http://fanyi.youdao.com/translate";

    // 开启18模式
//    public static boolean ON_WICKED = false;
}
