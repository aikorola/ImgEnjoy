package enjoy.cqw.com.imgenjoy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import enjoy.cqw.com.imgenjoy.R;
import enjoy.cqw.com.imgenjoy.bean.GuideBean;
import enjoy.cqw.com.imgenjoy.custom_view.TitleCardView;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.GuideViewHolder> {
    private Context mContext;
    private List<GuideBean> mDatas;
    private ClickItemListener mListener;

    public GuideAdapter(Context mContext, List<GuideBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public GuideViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {
        return new GuideViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_guide_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final GuideViewHolder holder, final int pos) {
//        holder.mTitleView.setImageBitmap(ImageUtil.compressBitmap(mContext, mDatas.get(pos).getBackground()));
        new Thread() {
            @Override
            public void run() {
                holder.mTitleView.post(new Runnable() {
                    @Override
                    public void run() {
//                        holder.mTitleView.setImageBitmap(ImageUtil.compressBitmap(mContext, mDatas.get(pos).getBackground()));
                    }
                });
            }
        }.start();
        holder.mTitleView.setTitleText(mDatas.get(pos).getDesc());

        holder.mTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(pos, holder.mTitleView, mDatas);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setOnItemClickListener(ClickItemListener listener) {
        this.mListener = listener;
    }

    public interface ClickItemListener {
        void onItemClick(int pos, View view, List<GuideBean> datas);
    }

    class GuideViewHolder extends RecyclerView.ViewHolder {
        private TitleCardView mTitleView;

        public GuideViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitleView = itemView.findViewById(R.id.item_guide);
        }
    }
}
