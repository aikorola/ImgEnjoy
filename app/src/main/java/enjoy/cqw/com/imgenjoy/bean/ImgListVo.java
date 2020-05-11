package enjoy.cqw.com.imgenjoy.bean;

import java.io.Serializable;
import java.util.List;

public class ImgListVo implements Serializable {

    private MetaBean meta;
    private List<DataBean> data;

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class MetaBean implements Serializable {
        /**
         * current_page : 1
         * last_page : 2605
         * per_page : 24
         * total : 62510
         * query : nature
         * seed : null
         */

        private int current_page;
        private int last_page;
        private int per_page;
        private int total;
        private String query;
        private Object seed;

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public int getLast_page() {
            return last_page;
        }

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public Object getSeed() {
            return seed;
        }

        public void setSeed(Object seed) {
            this.seed = seed;
        }
    }

    public static class DataBean implements Serializable {
        /**
         * id : 2ejzo9
         * url : https://wallhaven.cc/w/2ejzo9
         * short_url : https://whvn.cc/2ejzo9
         * views : 149
         * favorites : 3
         * source :
         * purity : sfw
         * category : general
         * dimension_x : 2000
         * dimension_y : 1125
         * resolution : 2000x1125
         * ratio : 1.78
         * file_size : 783931
         * file_type : image/jpeg
         * created_at : 2019-11-18 10:25:26
         * colors : ["#424153","#999999","#cccccc","#abbcda","#000000"]
         * path : https://w.wallhaven.cc/full/2e/wallhaven-2ejzo9.jpg
         * thumbs : {"large":"https://th.wallhaven.cc/lg/2e/2ejzo9.jpg","original":"https://th.wallhaven.cc/orig/2e/2ejzo9.jpg","small":"https://th.wallhaven.cc/small/2e/2ejzo9.jpg"}
         */

        private String id;
        private String url;
        private String short_url;
        private int views;
        private int favorites;
        private String source;
        private String purity;
        private String category;
        private int dimension_x;
        private int dimension_y;
        private String resolution;
        private String ratio;
        private int file_size;
        private String file_type;
        private String created_at;
        private String path;
        private ThumbsBean thumbs;
        private List<String> colors;
        private boolean isLocal;

        public DataBean() {
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", url='" + url + '\'' +
                    ", short_url='" + short_url + '\'' +
                    ", views=" + views +
                    ", favorites=" + favorites +
                    ", source='" + source + '\'' +
                    ", purity='" + purity + '\'' +
                    ", category='" + category + '\'' +
                    ", dimension_x=" + dimension_x +
                    ", dimension_y=" + dimension_y +
                    ", resolution='" + resolution + '\'' +
                    ", ratio='" + ratio + '\'' +
                    ", file_size=" + file_size +
                    ", file_type='" + file_type + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", path='" + path + '\'' +
                    ", thumbs=" + thumbs +
                    ", colors=" + colors +
                    '}';
        }

        public boolean isLocal() {
            return isLocal;
        }

        public void setLocal(boolean local) {
            isLocal = local;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getShort_url() {
            return short_url;
        }

        public void setShort_url(String short_url) {
            this.short_url = short_url;
        }

        public int getViews() {
            return views;
        }

        public void setViews(int views) {
            this.views = views;
        }

        public int getFavorites() {
            return favorites;
        }

        public void setFavorites(int favorites) {
            this.favorites = favorites;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getPurity() {
            return purity;
        }

        public void setPurity(String purity) {
            this.purity = purity;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getDimension_x() {
            return dimension_x;
        }

        public void setDimension_x(int dimension_x) {
            this.dimension_x = dimension_x;
        }

        public int getDimension_y() {
            return dimension_y;
        }

        public void setDimension_y(int dimension_y) {
            this.dimension_y = dimension_y;
        }

        public String getResolution() {
            return resolution;
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
        }

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }

        public int getFile_size() {
            return file_size;
        }

        public void setFile_size(int file_size) {
            this.file_size = file_size;
        }

        public String getFile_type() {
            return file_type;
        }

        public void setFile_type(String file_type) {
            this.file_type = file_type;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public ThumbsBean getThumbs() {
            return thumbs;
        }

        public void setThumbs(ThumbsBean thumbs) {
            this.thumbs = thumbs;
        }

        public List<String> getColors() {
            return colors;
        }

        public void setColors(List<String> colors) {
            this.colors = colors;
        }

        public static class ThumbsBean implements Serializable {
            /**
             * large : https://th.wallhaven.cc/lg/2e/2ejzo9.jpg
             * original : https://th.wallhaven.cc/orig/2e/2ejzo9.jpg
             * small : https://th.wallhaven.cc/small/2e/2ejzo9.jpg
             */

            private String large;
            private String original;
            private String small;

            @Override
            public String toString() {
                return "ThumbsBean{" +
                        "large='" + large + '\'' +
                        ", original='" + original + '\'' +
                        ", small='" + small + '\'' +
                        '}';
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getOriginal() {
                return original;
            }

            public void setOriginal(String original) {
                this.original = original;
            }

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }
        }
    }
}
