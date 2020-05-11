package enjoy.cqw.com.imgenjoy.bean;

import org.litepal.crud.LitePalSupport;

public class ImgInfoDataBase extends LitePalSupport {
    private long id;
    private String resourceId;
    private String netPath;
    private String infoUrl;
    private String previewUrl;
    private boolean isLike;

    public ImgInfoDataBase() {
    }

    public ImgInfoDataBase(String resourceId, String netPath, String infoUrl, String previewUrl, boolean isLike) {
        this.resourceId = resourceId;
        this.netPath = netPath;
        this.infoUrl = infoUrl;
        this.previewUrl = previewUrl;
        this.isLike = isLike;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getNetPath() {
        return netPath;
    }

    public void setNetPath(String netPath) {
        this.netPath = netPath;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    @Override
    public String toString() {
        return "ImgInfoDataBase{" +
                "id=" + id +
                ", resourceId='" + resourceId + '\'' +
                ", netPath='" + netPath + '\'' +
                ", infoUrl='" + infoUrl + '\'' +
                ", previewUrl='" + previewUrl + '\'' +
                ", isLike=" + isLike +
                '}';
    }
}
