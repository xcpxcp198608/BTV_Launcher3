package com.px.common.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.px.common.animator.Zoom;

/**
 * base recycle adapter
 */

public abstract class BaseRecycleAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnItemFocusListener onItemFocusListener;
    private boolean zoom = true;

    /**
     * 设置item layout resource id
     */
    protected abstract int setLayoutId();

    /**
     * new 实际需要的view holder , 必须继承于RecyclerView.ViewHolder
     * @param view 通过item 的layout 创建的view
     * @return 实际需要的view holder
     */
    protected abstract VH createHolder(View view);

    /**
     * 绑定holder的view与数据
     * @param holder holder
     * @param position 当前的item位置
     */
    protected abstract void bindHolder(VH holder, int position);

    /**
     * 获取items的数量
     * @return 总items数量
     */
    public abstract int getItemCounts();

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(setLayoutId(), parent, false);
        return createHolder(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        bindHolder(holder, position);
        if(onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, position);
                }
            });
        }
        if(onItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(v, position);
                    return false;
                }
            });
        }
        if(onItemFocusListener != null){
            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(zoom) {
                        if (hasFocus) {
                            Zoom.zoomIn10to11(v);
                        } else {
                            Zoom.zoomIn11to10(v);
                        }
                    }
                    onItemFocusListener.onFocus(v, position, hasFocus);
                }
            });
        }
    }

    public boolean isZoom() {
        return zoom;
    }

    public void setZoom(boolean zoom) {
        this.zoom = zoom;
    }

    @Override
    public int getItemCount() {
        return getItemCounts();
    }

    interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    interface OnItemLongClickListener{
        void onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

    interface OnItemFocusListener{
        void onFocus(View view, int position, boolean hasFocus);
    }

    public void setOnItemFocusListener(OnItemFocusListener onItemFocusListener){
        this.onItemFocusListener = onItemFocusListener;
    }
}
