package com.thoingthoing.videolive.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.thoingthoing.videolive.adapter.contract.liveContract;
import com.thoingthoing.videolive.model.stream_item;
import com.thoingthoing.videolive.ui.player.StreamPlayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class live_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements liveContract.View, liveContract.Model {

    private List<stream_item> item;
    private Context context;
    private int count = 0;

    public live_adapter(Context context) {
        this.context = context;
        item = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_stream_item, parent, false);
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
                .load(R.drawable.profile_default)
                .error(context.getResources().getColor(R.color.half_black))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder1.thumbnail);

        holder1.streamer.setText(item1.nickname);
        holder1.incomming.setText(String.valueOf(count) + "명 시청중");

    }

    // 방송정보 추가
    @Override
    public void addItem(stream_item e) {
        // 접속자 정보에서 접속중인 유저를 확인하여 인원 카운트
        item.add(e);
        // 접속자 인원 체크
        if (e.userlist != null) {
            count = e.userlist.size();
        }

    }

    //방송정보 삭제
    @Override
    public void removeItem(stream_item e) {
        for (int i = 0; i < item.size(); i++) {

            if (e.url.equals(item.get(i).url)) {
                item.remove(i);
            }
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public void notifyChanged() {
        notifyDataSetChanged();
    }

    private class MainViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.main_item_title)
        TextView title;
        @BindView(R.id.main_layer)
        LinearLayout layout;
        @BindView(R.id.main_item_thumbnail_image)
        ImageView thumbnail;
        @BindView(R.id.main_item_streamer)
        TextView streamer;
        @BindView(R.id.main_item_incomming)
        TextView incomming;

        private MainViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
