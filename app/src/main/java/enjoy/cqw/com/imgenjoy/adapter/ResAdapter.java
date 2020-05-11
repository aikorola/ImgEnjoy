package enjoy.cqw.com.imgenjoy.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

public class ResAdapter extends RecyclerView.Adapter<ResAdapter.MyViewHolder> {
    private static final String TAG = ResAdapter.class.getSimpleName() + "图片列表的适配器";
    private List<ImgListVo.DataBean> mData;
    private Context mContext;
    // item的点击事件
    private OnItemClickListener onItemClickListener = null;

    public ResAdapter(List<ImgListVo.DataBean> mData, Context mContext) {
        this.mData = mData;
        Log.i(TAG, "ResAdapter: size==>"+mData.size());
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {
        return new MyViewHolder(View.inflate(mContext, R.layout.item_img, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int pos) {
        final ObjectAnimator anim = ObjectAnimator.ofInt(holder.iv_img, "ImageLevel", 0, 10000);
        anim.setDuration(800);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.start();
        Log.i(TAG, "onBindViewHolder: " + "   path==>" + mData.get(pos).getPath());
        Glide.with(mContext)
                .load(mData.get(pos).getPath())
                .placeholder(R.drawable.rotate_progress)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        anim.cancel();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        anim.cancel();
                        return false;
                    }
                })
                .error(R.mipmap.loading_failure)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.iv_img);

        // 实现点击效果
        //实现点击效果
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, pos);
                }
            }
        });
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        Glide.with(mContext).clear(holder.iv_img);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.item_iv_img);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}
