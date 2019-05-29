package com.sdbi.delightfulmusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PlayingMusicInfo extends BroadcastReceiver {
    String name;
    String artist;
    int currentPosition;
    public static PlayingMusicInfo playingMusicInfo = new PlayingMusicInfo();

    public PlayingMusicInfo() {
    }

    public PlayingMusicInfo(String name, String artist, int currentPosition) {
        this.name = name;
        this.artist = artist;
        this.currentPosition = currentPosition;
    }

    //接收到音乐服务Service发来的广播，记录当前播放音乐的信息
    @Override
    public void onReceive(Context context, Intent intent) {
        playingMusicInfo = new PlayingMusicInfo(
                intent.getStringExtra("name"),
                intent.getStringExtra("artist"),
                intent.getIntExtra("currentPosition", 0)
        );

    }

    public String getName() {
        return this.name;
    }

    public String getArtist() {
        return this.artist;
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }

    public void setName(String name) {
        this.name = name;
    }


}
