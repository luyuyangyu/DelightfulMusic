package com.sdbi.delightfulmusic;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    public static ArrayList<MusicInfo> Musiclist;
    ListView listView;
    MusicAdapter adapter;
    MusicService musicService;
    String path;
    SeekBar seekBar;
    ImageView btiv1;
    ImageView btiv2;
    ImageView btiv3;
    ImageView btiv4;
    ImageView btiv5;
    TextView mmbottv1;
    TextView mmbottv2;
    TextView mmbottv3;
    int currentPosition;

    //Handler对象用于实时更新UI界面
    final private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    //控制SeekBar进度
                    seekBar.setProgress(msg.arg1);
                    Bundle bundle = msg.getData();
                    //显示歌曲名、歌手名
                    //mmbottv1.setText(PlayingMusicInfo.playingMusicInfo.getArtist());
                    //mmbottv2.setText(PlayingMusicInfo.playingMusicInfo.getName());
                    //显示播放时长和总时长
                    mmbottv3.setText(bundle.getString("process") + " - "
                            + bundle.getString("duration"));
                    btiv2.setBackgroundResource(R.drawable.music_pause_button_48);
                    break;
                case 2:
                    seekBar.setProgress(0);
                    btiv2.setBackgroundResource(R.drawable.music_play_button_48px);
            }
        }
    };
    //构建ServiceConnection对象，用以绑定音乐服务
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.LocalBdiner) service).getService();

            new Thread(new Runnable() {
                //开启新线程监听播放进度
                @Override
                public void run() {
                    while (true) {
                        if (musicService.isPlaying()) {
                            if (musicService.getDuration() != 0) {
                                //将播放进度转化为SeekBar进度百分数
                                int progress = musicService
                                        .getCurrentProgress()
                                        * 100
                                        / musicService.getDuration();
                                Message msg = new Message();
                                msg.what = 1;
                                msg.arg1 = progress;
                                Bundle bundle = new Bundle();
                                //获取当前播放时长和总时长
                                bundle.putString("duration",
                                        musicService.getMusicTotalTime());
                                bundle.putString("process",
                                        musicService.getMusicprogressTime());
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                        } else {
                            //未播放状态
                            if (musicService.getCurrentProgress() == 0) {
                                Message msg = new Message();
                                msg.what = 2;
                                msg.arg1 = 0;
                                handler.sendMessage(msg);
                            }
                        }

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }).start();

        }


        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);


        //调用获取本地音乐方法
        ScanMusic();
        //绑定服务
        final Intent serviceIntent = new Intent(this, MusicService.class);
        getApplicationContext().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        //进度条
        seekBar = findViewById(R.id.mmpb);
        //绑定Adapter
        listView = findViewById(R.id.mmlv);
        adapter = new MusicAdapter(this, Musiclist);
        listView.setAdapter(adapter);

        mmbottv1 = findViewById(R.id.mmbottv1);
        mmbottv2 = findViewById(R.id.mmbottv2);
        mmbottv3 = findViewById(R.id.mmbottv3);
        btiv1 = findViewById(R.id.mmbotiv1);
        btiv2 = findViewById(R.id.mmbotiv2);
        btiv3 = findViewById(R.id.mmbotiv3);
        btiv4 = findViewById(R.id.mmbotiv4);
        btiv5 = findViewById(R.id.mmbotiv5);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        //导航按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu_48);
        }


        btiv2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (musicService.isPlaying()) {
                    musicService.pause();
                    btiv2.setBackgroundResource(R.drawable.music_play_button_48px);
                } else {
                    musicService.resumeplay();
                    btiv2.setBackgroundResource(R.drawable.music_pause_button_48);
                }
            }
        });

        btiv3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (currentPosition == MainActivity.Musiclist.size() - 1) {
                    currentPosition = 0;
                } else {
                    currentPosition++;
                }

                getCurrentMusic(currentPosition);
            }
        });

        btiv5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (currentPosition == MainActivity.Musiclist.size() - 1) {
                    currentPosition = 0;
                } else {
                    currentPosition++;
                }

                getCurrentMusic(currentPosition);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getCurrentMusic(position);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
            }
        });



        //侧滑栏menuItem
        //navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_call:
                        //Intent intent = new Intent(MainActivity.this,Call.class);
                        //startActivity(intent);
                        break;
                    case R.id.nav_friends:
                        //Intent intent2 = new Intent(MainActivity.this,Friends.class);
                        //startActivity(intent2);
                        break;
                    case R.id.nav_mail:
                        //Intent intent3 = new Intent(MainActivity.this,Mail.class);
                        //startActivity(intent3);
                        break;
                    case R.id.location:
                        //Intent intent4 = new Intent(MainActivity.this,Location.class);
                        //startActivity(intent4);
                        break;

                    default:
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    //加载toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;


    }

    //顶部标题栏单击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.delete:
                Toast.makeText(this, "You Clicked Delete", Toast.LENGTH_LONG).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked Setting", Toast.LENGTH_LONG).show();
                break;
            default:
        }
        return true;
    }

    //从媒体库读多媒体文件
    private void ScanMusic() {
        Musiclist = new ArrayList<MusicInfo>();
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.DATA}, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.setName(cursor.getString(
                    cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            musicInfo.setArtist(cursor.getString(
                    cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            musicInfo.setPath(cursor.getString(
                    cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            Musiclist.add(musicInfo);


        }
    }

    //播放当前音乐
    private void getCurrentMusic(int position) {
        path = Musiclist.get(position).getPath();
        //调用service中当前播放的方法
        musicService.playSong(path, position);
        btiv2.setBackgroundResource(R.drawable.music_pause_button_48);
        mmbottv1.setText(Musiclist.get(position).getArtist());
        mmbottv2.setText(Musiclist.get(position).getName());
        currentPosition = position;
    }


}
