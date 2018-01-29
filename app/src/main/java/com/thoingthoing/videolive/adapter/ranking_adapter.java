package com.thoingthoing.videolive.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.thoingthoing.video_live.R;
import com.thoingthoing.videolive.adapter.contract.rankingContract;
import com.thoingthoing.videolive.model.ranking_item;
import com.thoingthoing.videolive.ui.streamerinfo.StreamerInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class ranking_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements rankingContract.Model, rankingContract.View {

    private Context context;
    private List<ranking_item> item;

    public ranking_adapter(Context context) {
        this.context = context;
        item = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_item, parent, false);
        return new RankingHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        RankingHolder holder1 = (RankingHolder) holder;
        final ranking_item item1 = item.get(position);

        // 생방송 중일경우 표시
        if (item1.getOnoff() == 1) {
            holder1.onoff.setText("Live");
        } else {
            holder1.onoff.setText("");
        }

        // 1~3 등까지 빨강색
        if(position <3){
            holder1.rank.setTextColor(context.getResources().getColor(R.color.red));
        }

        holder1.rank.setText(String.valueOf(position + 1));
        holder1.email.setText(item1.getEmail());
        holder1.nickname.setText(item1.getNickname());


        Glide.with(context)
                .load(item1.getProfileimg())
                .error(R.drawable.profile_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder1.profile_img);

        holder1.next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StreamerInfo.class);
                intent.putExtra("key", item1.getUserkey());
                intent.putExtra("ranking", position+1);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public void addItem(ranking_item item1) {
        item.add(item1);
    }

    @Override
    public void notifyChanged() {
        notifyDataSetChanged();
    }

    private class RankingHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ranking_item_email)
        TextView email;
        @BindView(R.id.ranking_item_nickname)
        TextView nickname;
        @BindView(R.id.ranking_item_onoff)
        TextView onoff;
        @BindView(R.id.ranking_item_rank)
        TextView rank;
        @BindView(R.id.ranking_item_profile_img)
        CircleImageView profile_img;
        @BindView(R.id.ranking_item_next)
        ImageButton next_btn;

        private RankingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
