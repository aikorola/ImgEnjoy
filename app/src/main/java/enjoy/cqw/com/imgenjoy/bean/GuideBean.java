package enjoy.cqw.com.imgenjoy.bean;

public class GuideBean {
    private int background;
    private String desc;

    public GuideBean() {
    }

    public GuideBean(int background, String desc) {
        this.background = background;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "GuideBean{" +
                "background=" + background +
                ", desc='" + desc + '\'' +
                '}';
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
