package com.thoingthoing.videolive.ui.streamerinfo.presenter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;

import com.thoingthoing.videolive.adapter.contract.vodContract;

public interface InfoContract {

    interface View{

        void setTitle(String title_msg);
        void setBookmarkCount(String count);
        void setNickname(String nick);
        void setRanking(String rank);
        void setNotice(String notice_msg);
        void setbookMarkbtnChange(boolean isChange);
        void visibleNoticeBtn();
        void noticeDialog(String message);
        void setProgress(boolean isRun);
        void setback_thumbnail(String image_url);
        void back_thumbnail(Uri uri);
        void profile_image(Uri uri);
        void setprofiel_image(String url);
    }

    interface Presenter{

        void setView(View v);
        void getContext(Context context);
        void deathView();

        void firebase(String key);

        // 북마크 클릭
        void bookmark();

        // 공지사항 수정
        void changeNotice();
        void sendNotice(String message);

        // 다시보기
        void setVodRecyclerview(RecyclerView recyclerview);

        // 사진 변경 버튼
        void changeImage(int dif, android.support.v4.app.FragmentManager manager);

        void setvodConstractView(vodContract.View vodView);
        void setvodConstractModel(vodContract.Model vodModel);
    }
}
