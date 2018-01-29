package com.thoingthoing.videolive.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thoingthoing.video_live.R;
import com.thoingthoing.videolive.model.chat_item;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Chat_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<chat_item> item;

    public Chat_Adapter(List<chat_item> item) {
        this.item = item;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.livechat_item, parent, false);
        return new LiveChatHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LiveChatHolder holder1 = (LiveChatHolder) holder;

        chat_item item1 = item.get(position);
        holder1.message.setText(item1.getChat_message());
        holder1.nickname.setText(item1.getChat_nickname());
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    // 채팅 메시지 추가
    public void addItem(chat_item e){
        item.add(e);
        notifyDataSetChanged();
    }

    private class LiveChatHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.livechat_item_message)
        TextView message;
        @BindView(R.id.livechat_item_nickname)
        TextView nickname;

        private LiveChatHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
