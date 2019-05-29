package com.sdbi.delightfulmusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.text.DecimalFormat;

public class MusicService extends Service {
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private IBinder mBinder = new LocalBdiner();
    private DecimalFormat df = new DecimalFormat("#00");

    public class LocalBdiner extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void playSong(String path, int position) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
            int currentPosition = position;
            MusicInfo currentMusic = MainActivity.Musiclist
                    .get(currentPosition);
            Intent in = new Intent();
            in.setAction("PlayingMusic");
            in.putExtra("name", currentMusic.getName());
            in.putExtra("artist", currentMusic.getArtist());
            in.putExtra("currentPosition", currentPosition);
            sendBroadcast(in);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获得当前播放进度
    public int getCurrentProgress() {
        return mediaPlayer.getCurrentPosition();
    }

    // 获得当前播放音频时长，单位为毫秒
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    // 跳转到当前进度，单位为毫秒
    public void seekTo(int millis) {
        mediaPlayer.seekTo(millis);
    }

    // 返回音频总时长，单位为秒
    public String getMusicTotalTime() {

        int musicTime = getDuration() / 1000;

        return df.format(musicTime / 60) + ":" + df.format(musicTime % 60);

    }

    // 返回当前播放进度，单位为秒
    public String getMusicprogressTime() {

        int musicTime = getCurrentProgress() / 1000;

        return df.format(musicTime / 60) + ":" + df.format(musicTime % 60);

    }

    // 播放暂停
    public void pause() {
        mediaPlayer.pause();
    }

    // 恢复播放
    public void resumeplay() {
        mediaPlayer.start();
    }

    // 返回当前播放器是否播放
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }
}
