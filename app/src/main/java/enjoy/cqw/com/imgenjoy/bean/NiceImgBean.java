package enjoy.cqw.com.imgenjoy.bean;

import java.util.List;

/**
 * 美图
 */
public class NiceImgBean {

    /**
     * code : 200
     * message : 成功!
     * result : [{"id":63,"time":"2018-09-18 17:52:39","img":"http://ww1.sinaimg.cn/large/0065oQSqly1frepsy47grj30qo0y97en.jpg"},{"id":93,"time":"2018-09-18 17:52:39","img":"http://7xi8d6.com1.z0.glb.clouddn.com/20171027114026_v8VFwP_joanne_722_27_10_2017_11_40_17_370.jpeg"},{"id":275,"time":"2018-09-18 17:52:40","img":"http://ww1.sinaimg.cn/large/610dc034gw1fa1vkn6nerj20u011gdjm.jpg"},{"id":581,"time":"2018-09-18 17:52:43","img":"http://ww1.sinaimg.cn/large/7a8aed7bgw1evdga4dimoj20qo0hsmzf.jpg"},{"id":664,"time":"2018-11-20 04:00:01","img":"https://ws1.sinaimg.cn/large/0065oQSqgy1fxd7vcz86nj30qo0ybqc1.jpg"},{"id":108,"time":"2018-09-18 17:52:39","img":"https://ws1.sinaimg.cn/large/610dc034ly1fjgfyxgwgnj20u00gvgmt.jpg"},{"id":366,"time":"2018-09-18 17:52:41","img":"http://ww2.sinaimg.cn/large/610dc034jw1f5qyx2wpohj20xc190tr7.jpg"},{"id":85,"time":"2018-09-18 17:52:39","img":"http://7xi8d6.com1.z0.glb.clouddn.com/20171116115656_vnsrab_Screenshot.jpeg"},{"id":624,"time":"2018-09-18 17:52:43","img":"http://ww2.sinaimg.cn/large/610dc034jw1etn2pltc7mj20f90mwmye.jpg"},{"id":105,"time":"2018-09-18 17:52:39","img":"https://ws1.sinaimg.cn/large/610dc034ly1fjqw4n86lhj20u00u01kx.jpg"},{"id":75,"time":"2018-09-18 17:52:39","img":"http://7xi8d6.com1.z0.glb.clouddn.com/20171219224721_wFH5PL_Screenshot.jpeg"},{"id":661,"time":"2018-10-16 04:00:00","img":"https://ws1.sinaimg.cn/large/0065oQSqly1fw8wzdua6rj30sg0yc7gp.jpg"},{"id":338,"time":"2018-09-18 17:52:41","img":"http://ww3.sinaimg.cn/large/610dc034jw1f71bezmt3tj20u00k0757.jpg"},{"id":532,"time":"2018-09-18 17:52:42","img":"http://ww3.sinaimg.cn/large/a3bcec5fjw1exukiyu2zoj20hs0qo0w9.jpg"},{"id":50,"time":"2018-09-18 17:52:39","img":"http://ww1.sinaimg.cn/large/0065oQSqly1frg40vozfnj30ku0qwq7s.jpg"},{"id":401,"time":"2018-09-18 17:52:41","img":"http://ww2.sinaimg.cn/large/610dc034jw1f454lcdekoj20dw0kumzj.jpg"},{"id":211,"time":"2018-09-18 17:52:40","img":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-06-tumblr_ombicf0KXi1vpybydo6_540.jpg"},{"id":214,"time":"2018-09-18 17:52:40","img":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-02-28-15057157_446684252387131_4267811446148038656_n.jpg"},{"id":17,"time":"2018-09-18 17:52:39","img":"http://ww1.sinaimg.cn/large/0065oQSqly1ft3fna1ef9j30s210skgd.jpg"},{"id":561,"time":"2018-09-18 17:52:42","img":"http://ww1.sinaimg.cn/large/7a8aed7bgw1ewc4irf4syj20go0ltdjk.jpg"}]
     */

    private int code;
    private String message;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NiceImgBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }

    public NiceImgBean() {
    }

    public NiceImgBean(int code, String message, List<ResultBean> result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 63
         * time : 2018-09-18 17:52:39
         * img : http://ww1.sinaimg.cn/large/0065oQSqly1frepsy47grj30qo0y97en.jpg
         */

        private int id;
        private String time;
        private String img;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public ResultBean() {
        }

        public ResultBean(int id, String time, String img) {
            this.id = id;
            this.time = time;
            this.img = img;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "id=" + id +
                    ", time='" + time + '\'' +
                    ", img='" + img + '\'' +
                    '}';
        }
    }
}
