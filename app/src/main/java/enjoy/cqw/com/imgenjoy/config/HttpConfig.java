package enjoy.cqw.com.imgenjoy.config;

public class HttpConfig {
    /*
    风景随机图：http://pic.tsmp4.net/api/fengjing/img.php
    女神随机图：http://pic.tsmp4.net/api/nvsheng/img.php
    影视随机图：http://pic.tsmp4.net/api/yingshi/img.php
    二次元随机图：http://pic.tsmp4.net/api/erciyuan/img.php
     */

    public static final String ROOT_PATH = "http://pic.tsmp4.net/api/";

    public static final String ROOT_SUFFIX = "/img.php";

    public static final String FENG_JING = "fengjing";
    public static final String NV_SHENG = "nvsheng";
    public static final String YING_SHI = "yingshi";
    public static final String ER_CI_YUAN = "erciyuan";

    /*****************************************************NEW************************************************************************/
    /**
     * 美图推荐（post）
     * params1 page string
     * params2 count string
     * 不传params则随机返回20个
     */
    public static final String NICE_IMG = "https://api.apiopen.top/getImages";
    /*****************************************************NEW************************************************************************/

    /*****************************************************WALL_HAVEN*****************************************************************/
    // 搜索壁纸的ROOT_URL
    public static final String WALL_PAPER_ROOT_URL = "https://wallhaven.cc/api/v1/search";
    /*****************************************************WALL_HAVEN*****************************************************************/
}
