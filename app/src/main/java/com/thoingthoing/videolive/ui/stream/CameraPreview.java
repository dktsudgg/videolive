package com.thoingthoing.videolive.ui.stream;


import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.pedro.rtplibrary.rtsp.RtspCamera1;
import com.pedro.rtsp.utils.ConnectCheckerRtsp;
import com.thoingthoing.videolive.ui.stream.presenter.StreamContract;
import com.thoingthoing.videolive.ui.stream.presenter.StreamPresenter;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, ConnectCheckerRtsp {

    private RtspCamera1 rtspCamera1;
    private SurfaceHolder mHolder;
    private StreamPresenter presenter;
    private StreamContract.streamCallback callback;

    public CameraPreview(Context context, StreamPresenter presenter, StreamContract.streamCallback callback) {
        super(context);

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        rtspCamera1 = new RtspCamera1(this, this);

        this.presenter = presenter;
        presenter.RtspCamera(rtspCamera1);

        this.callback = callback;

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        presenter.rtspPreview(true);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        presenter.rtspPreview(false);
    }

    @Override
    public void onConnectionSuccessRtsp() {
        callback.successCallback();
    }

    @Override
    public void onConnectionFailedRtsp(String s) {
        callback.failCallback();
    }

    @Override
    public void onDisconnectRtsp() {

    }

    @Override
    public void onAuthErrorRtsp() {

    }

    @Override
    public void onAuthSuccessRtsp() {

    }


}




