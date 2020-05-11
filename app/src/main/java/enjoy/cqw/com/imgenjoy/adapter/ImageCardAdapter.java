package enjoy.cqw.com.imgenjoy.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;

import enjoy.cqw.com.imgenjoy.R;
import enjoy.cqw.com.imgenjoy.bean.ImgListVo;

public class ImageCardAdapter extends StackAdapter<ImgListVo.DataBean> {
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public ImageCardAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void bindView(ImgListVo.DataBean data, final int position, CardStackView.ViewHolder holder) {
        ImageCardViewHolder h = (ImageCardViewHolder) holder;
        h.mDesc.setText(String.valueOf(position));
        final ObjectAnimator anim = ObjectAnimator.ofInt(h.mImg, "ImageLevel", 0, 10000);
        anim.setDuration(800);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.start();
        Glide.with(mContext)
                .load(data.getPath())
                .placeholder(R.drawable.rotate_progress)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
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
                .into(h.mImg);

        h.mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(v, position);
            }
        });
        h.mParent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClickListener.onLongClick(v, position);
                return false;
            }
        });
    }

    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        return new ImageCardViewHolder(getLayoutInflater().inflate(R.layout.image_card_list, parent, false));
    }

    static class ImageCardViewHolder extends CardStackView.ViewHolder {
        TextView mDesc;
        ImageView mImg;
        LinearLayout mContainer;
        LinearLayout mParent;

        public ImageCardViewHolder(View view) {
            super(view);
            mDesc = view.findViewById(R.id.item_card_stack_tv_desc);
            mImg = view.findViewById(R.id.item_card_stack_iv_img);
            mContainer = view.findViewById(R.id.item_card_stack_ll_container);
            mParent = view.findViewById(R.id.card_parent);
        }

        @Override
        public void onItemExpand(boolean b) {
            mContainer.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        @Override
        protected void onAnimationStateChange(int state, boolean willBeSelect) {
            super.onAnimationStateChange(state, willBeSelect);
            if (state == CardStackView.ANIMATION_STATE_START && willBeSelect) {
                onItemExpand(true);
            }
            if (state == CardStackView.ANIMATION_STATE_END && !willBeSelect) {
                onItemExpand(false);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onClick(View v, int pos);
    }

    public interface OnItemLongClickListener {
        void onLongClick(View v, int pos);
    }
}
