package com.thoingthoing.videolive.ui.streamerinfo.presenter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thoingthoing.video_live.R;
import com.thoingthoing.videolive.adapter.contract.vodContract;
import com.thoingthoing.videolive.adapter.vod_adapter;
import com.thoingthoing.videolive.model.Data;
import com.thoingthoing.videolive.model.UserData;
import com.thoingthoing.videolive.model.vod_item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;

public class InfoPresenter implements InfoContract.Presenter {

    private Context context;
    private InfoContract.View v;
    private FirebaseDatabase firebaseDatabase;
    private String key, notice_message, nick;
    private int bookmarkCount;
    private vodContract.View vodView;
    private vodContract.Model vodModel;

    //  북마크가 체크되어있는지
    private boolean isBookmarkChecked = false;

    @Override
    public void setView(InfoContract.View v) {
        this.v = v;
    }

    @Override
    public void getContext(Context context) {
        this.context = context;
    }

    @Override
    public void deathView() {
        v = null;
    }

    // Firebase에서 정보를 받아와 출력
    @Override
    public void firebase(final String key) {
        this.key = key;

        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseDatabase.getReference("user").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                nick = dataSnapshot.getValue(UserData.class).getNickname();
                int count = dataSnapshot.getValue(UserData.class).getBookmarkcount();
                notice_message = dataSnapshot.getValue(UserData.class).getNotice_message();
                String back_thumbnail_url = dataSnapshot.getValue(UserData.class).getBack_thumbnail();
                String profile_image_url = dataSnapshot.getValue(UserData.class).getProfile_url();
                v.setNickname(nick);
                v.setTitle(nick + context.getString(R.string.info_title));
                v.setBookmarkCount("구독자 " + String.valueOf(count) + "명");
                v.setNotice(notice_message);
                v.setprofiel_image(profile_image_url);
                v.setback_thumbnail(back_thumbnail_url);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("firebaseLoadError", String.valueOf(databaseError));
            }
        });

        // 로그인되어있을경우
        if (Data.uid != null) {

            // 내 방송국일경우 공지 수정버튼 보이기
            if (Data.uid.equals(key)) {
                v.visibleNoticeBtn();
            }

            myBookMark();
        }

    }

    // 북마크 추가 & 제거
    @Override
    public void bookmark() {

        // 북마크 해시맵 형식
        final HashMap<String, Object> hash = new HashMap<>();
        hash.put("bookmark", key);

        // 본인 게시판이 아닐경우
        if (!key.equals(Data.uid)) {

            // 북마크가 추가되어있을경우
            if (isBookmarkChecked) {

                firebaseDatabase.getReference("user").child(Data.uid).child("bookmark").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot node : dataSnapshot.getChildren()) {
                            // 나의 북마크 목록 조회

                            String k = String.valueOf(node.child("bookmark").getValue());
                            if (key.equals(k)) {

                                String childKey = node.getKey();
                                firebaseDatabase.getReference("user").child(Data.uid).child("bookmark").child(childKey).setValue(null);
                                bookmarkCount--;

                                hash.clear();
                                hash.put("bookmarkcount", bookmarkCount);

                                firebaseDatabase.getReference("user").child(Data.uid).updateChildren(hash);
                                v.setbookMarkbtnChange(false);
                                isBookmarkChecked = false;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                // 아닐경우 북마크 추가
            } else {
                firebaseDatabase.getReference("user").child(Data.uid).child("bookmark").push().setValue(hash);

                int count = bookmarkCount + 1;

                hash.clear();
                hash.put("bookmarkcount", count);
                firebaseDatabase.getReference("user").child(Data.uid).updateChildren(hash);

                isBookmarkChecked = true;
            }
        }
    }

    // 한줄 공지 다이얼로그
    @Override
    public void changeNotice() {
        v.noticeDialog(notice_message);
    }

    // 서버에 공지사항 업데이트
    @Override
    public void sendNotice(final String message) {

        v.setProgress(true);

        HashMap<String, Object> hash = new HashMap<>();
        hash.put("notice_message", message);
        firebaseDatabase.getReference("user").child(Data.uid).updateChildren(hash).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                v.setProgress(false);
                v.setNotice(message);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                v.setProgress(false);
            }
        });
    }

    // 다시보기에 아이템 추가
    @Override
    public void setVodRecyclerview(RecyclerView recyclerview) {
        vod_adapter adapter = new vod_adapter(context);
        recyclerview.setAdapter(adapter);

        setvodConstractModel(adapter);
        setvodConstractView(adapter);

        List<vod_item> item = new ArrayList<>();

        vodModel.addItem(item);
        vodView.notifyChanged();
    }

    @Override
    public void changeImage(final int dif, android.support.v4.app.FragmentManager manager) {

        TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(context)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        sendImagetoFirebaseStorage(uri, dif);
                    }
                })
                .create();

        bottomSheetDialogFragment.show(manager);

    }

    private void sendImagetoFirebaseStorage(final Uri uri, final int dif) {

        // 프로그레스 실행
        v.setProgress(true);

        // 파이어베이스 스토리지 생성
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // 파일명 생성
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
        Date now = new Date();
        String filename = formatter.format(now) + ".png";

        // 경로
        String path;
        if(dif == 0){
            path = "profile/";
        }else{
            path = "thumbnail/";
        }

        // 업로드
        StorageReference storageRef = storage.getReferenceFromUrl(context.getString(R.string.default_path)).child("images/" + path + filename);
        storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                v.setProgress(false);
                // 파일 경로
                String url = String.valueOf(taskSnapshot.getDownloadUrl());

                HashMap<String, Object> hash = new HashMap<>();

                if (dif == 0) {
                    v.profile_image(uri);
                    hash.put("profile_url", url);
                } else {
                    v.back_thumbnail(uri);
                    hash.put("back_thumbnail", url);
                }
                // db update
                firebaseDatabase.getReference("user").child(Data.uid).updateChildren(hash);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                v.setProgress(false);
            }
        });


    }

    // vod adapter 연결
    @Override
    public void setvodConstractView(vodContract.View vodView) {
        this.vodView = vodView;
    }

    @Override
    public void setvodConstractModel(vodContract.Model vodModel) {
        this.vodModel = vodModel;
    }

    // 로그인 했을경우, 내가 추가한 북마크 체크하여 버튼 이미지 설정
    private void myBookMark() {
        firebaseDatabase.getReference("user").child(Data.uid).child("bookmark").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String info_marker = String.valueOf(dataSnapshot.child("bookmark").getValue());

                // 해당 방송국을 북마크 했거나 내가 방송국 주인일 경우
                if (key.equals(info_marker) || Data.uid.equals(info_marker)) {
                    v.setbookMarkbtnChange(true);
                    isBookmarkChecked = true;

                }
                bookmarkCount++;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
