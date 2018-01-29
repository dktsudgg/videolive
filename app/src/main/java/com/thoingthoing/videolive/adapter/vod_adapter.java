package com.thoingthoing.videolive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.thoingthoing.video_live.R;
import com.thoingthoing.videolive.adapter.contract.vodContract;
import com.thoingthoing.videolive.model.vod_item;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class vod_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements vodContract.View, vodContract.Model {

    private List<vod_item> item;
    private Context context;

    public vod_adapter(Context context) {
        this.context = context;
        item = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_vod_item, parent, false);
        return new vodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        vodViewHolder holder1 = (vodViewHolder) holder;
        final vod_item item1 = item.get(position);

        holder1.title.setText(item1.getTitle());

        Glide.with(context) // 썸네일
                .load(item1.getThumbnail())
                .error(context.getResources().getColor(R.color.half_black))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder1.thumbnail);

        holder1.streamer.setText(item1.getNickname());

    }

    // 방송정보 추가
    @Override
    public void addItem(List<vod_item> e) {
        this.item = e;
    }


    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public void notifyChanged() {
        notifyDataSetChanged();
    }

    private class vodViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.info_vod_title)
        TextView title;
        @BindView(R.id.info_vod_layer)
        LinearLayout layout;
        @BindView(R.id.info_vod_thumbnail)
        ImageView thumbnail;
        @BindView(R.id.info_vod_nickname)
        TextView streamer;

        private vodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
