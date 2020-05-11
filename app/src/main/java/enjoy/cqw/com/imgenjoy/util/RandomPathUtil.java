package enjoy.cqw.com.imgenjoy.util;

import java.util.ArrayList;
import java.util.List;

import enjoy.cqw.com.imgenjoy.config.HttpConfig;

/**
 * 获取随机的美图类别
 */
public class RandomPathUtil {
    private static List<String> resType;

    /**
     * 获取随机的资源类别
     *
     * @return res
     */
    public static String getRandomResPath() {
        resType = new ArrayList<>();
        resType.add(HttpConfig.FENG_JING);
        resType.add(HttpConfig.NV_SHENG);
        resType.add(HttpConfig.YING_SHI);
        resType.add(HttpConfig.ER_CI_YUAN);
        return resType.get((int) (Math.random() * (resType.size() - 1)));
    }
}
