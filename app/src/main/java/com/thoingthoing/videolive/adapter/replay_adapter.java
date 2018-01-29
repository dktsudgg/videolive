package com.thoingthoing.videolive.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.thoingthoing.video_live.R;
import com.thoingthoing.videolive.adapter.contract.replayContract;
import com.thoingthoing.videolive.model.stream_item;
import com.thoingthoing.videolive.ui.player.StreamPlayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class replay_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements replayContract.View, replayContract.Model {

    private List<stream_item> item;
    private Context context;

    public replay_adapter(Context context) {
        this.context = context;
        item = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_replay_item, parent, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MainViewHolder holder1 = (MainViewHolder) holder;
        final stream_item item1 = item.get(position);

        holder1.title.setText(item1.title);

        holder1.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 클릭시 플레이어 이동
                Intent intent = new Intent(context, StreamPlayer.class);
                intent.putExtra("url", item1.url);
                intent.putExtra("uid", String.valueOf(item1.uid));
                intent.putExtra("title", item1.title);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        Glide.with(context) // 썸네일
                .load(item1.thumbnail)
                .error(context.getResources().getColor(R.color.half_black))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder1.thumbnail);

    }


    // 방송정보 추가
    @Override
    public void addItem(List<stream_item> e) {
        for (int i = 0; i < e.size(); i++) {
            item.add(e.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    // 리스트 갱신
    @Override
    public void notifyAdapter() {
        notifyDataSetChanged();
    }


     private class MainViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.replay_item_title)
        TextView title;
        @BindView(R.id.replay_layer)
        FrameLayout layout;
        @BindView(R.id.replay_item_thumbnail_image)
        ImageView thumbnail;

        private MainViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
