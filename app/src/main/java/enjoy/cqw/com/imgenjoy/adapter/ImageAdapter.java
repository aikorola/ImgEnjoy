package enjoy.cqw.com.imgenjoy.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import enjoy.cqw.com.imgenjoy.R;
import enjoy.cqw.com.imgenjoy.bean.ImgListVo;
import enjoy.cqw.com.imgenjoy.config.Constants;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ImgListVo.DataBean> data;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    private int ITEM_TYPE_CONTENT = 100000;
    private int ITEM_TYPE_FOOTER = 100001;

    public static final int LOADMORE_LOADING = 0X115;
    public static final int LOADMORE_LOADING_FAILURE = 0X116;
    public static final int LOADMORE_LOADING_EMPTY = 0X117;
    private int LOADMORE_STATE = LOADMORE_LOADING;

    private RecyclerView.ViewHolder h;  // 尾布局

    public ImageAdapter(Context mContext, List<ImgListVo.DataBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new ImgViewHolder(View.inflate(mContext, R.layout.image_card_list, null));
        if (viewType == ITEM_TYPE_FOOTER) {
            return new FootHolder(LayoutInflater.from(mContext).inflate(R.layout.item_img_foot, parent, false));
        } else if (viewType == ITEM_TYPE_CONTENT) {
            return new ImgViewHolder(LayoutInflater.from(mContext).inflate(R.layout.image_card_list, parent, false));
        }
        return null;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder h, @SuppressLint("RecyclerView") final int pos) {
        if (h instanceof FootHolder) {
            this.h = h;
        }
        if (h instanceof ImgViewHolder) {
            // 设置监听
            ((ImgViewHolder) h).mParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(v, data.get(pos), pos);
                    }
                }
            });
            ((ImgViewHolder) h).mParent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onLongClick(v, data.get(pos), pos);
                    }
                    return false;
                }
            });

            // 设置数据
            if (pos + 1 == getItemCount()) {
                return;
            }

            if (data.get(pos).isLocal()) {
                final ObjectAnimator anim = ObjectAnimator.ofInt(((ImgViewHolder) h).mImg, "ImageLevel", 0, 10000);
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        // 设置动画
                        ((ImgViewHolder) h).mImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        ((ImgViewHolder) h).mImg.setImageResource(R.drawable.rotate_progress);
                        anim.setDuration(800);
                        anim.setRepeatCount(ObjectAnimator.INFINITE);
                        anim.start();
                    }

                    @Override
                    protected Bitmap doInBackground(Void... voids) {
                        Log.i("TAG", "onBindViewHolder: " + data.get(pos).toString());
                        // 已下载的图片，压缩原图进行列表的预览
                        String imagePath = Constants.IMAGE_RES_PATH + data.get(pos).getId() + ".jpg";
                        Log.i("TAG", "onBindViewHolder_local: " + imagePath);
                        // 设置参数
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
                        BitmapFactory.decodeFile(imagePath, options);
                        int height = options.outHeight;
                        int width = options.outWidth;
                        int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
                        int minLen = Math.min(height, width); // 原图的最小边长
                        if (minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
                            float ratio = (float) minLen / 300.0f; // 计算像素压缩比例
                            inSampleSize = (int) ratio;
                        }
                        options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
                        options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
                        Bitmap bm = BitmapFactory.decodeFile(imagePath, options); // 解码文件
                        if (bm == null) return null;
                        Log.e("TAG", "size: " + bm.getByteCount() + " width: " + bm.getWidth() + " height:" + bm.getHeight()); // 输出图像数据
                        return bm;
                    }

                    @Override
                    protected void onPostExecute(Bitmap bmp) {
                        super.onPostExecute(bmp);
                        if (anim != null && anim.isRunning()) {
                            anim.cancel();
                        }
                        ((ImgViewHolder) h).mImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ((ImgViewHolder) h).mImg.setImageBitmap(bmp);
                    }
                }.execute();
            } else {
                // 设置动画
                ((ImgViewHolder) h).mImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                final ObjectAnimator anim = ObjectAnimator.ofInt(((ImgViewHolder) h).mImg, "ImageLevel", 0, 10000);
                anim.setDuration(800);
                anim.setRepeatCount(ObjectAnimator.INFINITE);
                anim.start();

                Glide.with(mContext)
                        .load(data.get(pos).getThumbs().getOriginal())
                        .placeholder(R.drawable.rotate_progress)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                anim.cancel();
                                ((ImgViewHolder) h).mImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                anim.cancel();
                                ((ImgViewHolder) h).mImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                return false;
                            }
                        })
                        .error(R.mipmap.loading_failure)
                        .into(((ImgViewHolder) h).mImg);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return ITEM_TYPE_FOOTER;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    /**
     * 更新数据
     *
     * @param data 数据
     */
    public void updateData(List<ImgListVo.DataBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    /**
     * 设置加载的状态
     */
    public void loadMoreState(int loadingState) {
        this.LOADMORE_STATE = loadingState;
        if (h == null) return;
        if (LOADMORE_STATE == LOADMORE_LOADING) {           // 正在加载
            ((FootHolder) h).mFootLoadMore.setText("玩命加载中...");
        }
        if (LOADMORE_STATE == LOADMORE_LOADING_FAILURE) {   // 加载失败
            ((FootHolder) h).mFootLoadMore.setText("加载失败了...");
        }
        if (LOADMORE_STATE == LOADMORE_LOADING_EMPTY) {   // 无数据
            ((FootHolder) h).mFootLoadMore.setText("我也是有底线的");
        }
    }

    static class ImgViewHolder extends RecyclerView.ViewHolder {

        ImageView mImg;
        LinearLayout mParent;

        ImgViewHolder(View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.item_card_stack_iv_img);
            mParent = itemView.findViewById(R.id.card_parent);
        }
    }

    static class FootHolder extends RecyclerView.ViewHolder {

        TextView mFootLoadMore;

        FootHolder(@NonNull View itemView) {
            super(itemView);
            mFootLoadMore = itemView.findViewById(R.id.foot_load_more);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onClick(View v, ImgListVo.DataBean data, int pos);
    }

    public interface OnItemLongClickListener {
        void onLongClick(View v, ImgListVo.DataBean data, int pos);
    }
}
