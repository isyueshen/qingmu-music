package com.luna1970.qingmumusic.activity;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.application.MusicApplication;
import com.luna1970.qingmumusic.entity.Music;
import com.luna1970.qingmumusic.service.MusicPlayService2;
import com.luna1970.qingmumusic.util.GlobalMusicPlayControllerConst;
import com.luna1970.qingmumusic.util.ToastUtils;

import java.io.FileNotFoundException;

import static com.luna1970.qingmumusic.application.MusicApplication.currentPosition;
import static com.luna1970.qingmumusic.application.MusicApplication.prevPosition;

public class MusicListActivity extends BaseActivity {
    private static final String TAG = "MusicListActivity";
    
    private ImageView largeAlbumPic;
    private ImageButton playMode;
    private ImageButton playAll;
    private TextView totalCountTV;
    private ListView listView;
    
    private SeekBar seekBar;
    private ImageView miniAlbumPic;
    private TextView musicTitleTV;
    private TextView musicArtistTV;
    private ImageButton playNextIV;
    private ImageButton playOrPauseIV;
    private ImageButton playPrevIV;
    
    private Intent intent;

    private Cursor cursor;

    private BroadcastReceiver broadcastReceiver;

    private int playModeIndex;
    private String[] playModeActionContainer;
    private int[] playModeResource;
    private int[] playModeDescriptionResource;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        setViews();
        getMusicList();
        setAdapter();
        initPlayMode();
        setListeners();
        setReceiver();
    }

    private void setViews() {
        // screen top
        largeAlbumPic = (ImageView) findViewById(R.id.largeAlbumPic);
        playMode = (ImageButton) findViewById(R.id.playMode);

        // screen medium
        playAll = (ImageButton) findViewById(R.id.playAll);
        totalCountTV = (TextView) findViewById(R.id.totalCountTV);
        listView = (ListView) findViewById(R.id.musicListView);

        // screen bottom and above play control bar
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        // bottom left
        miniAlbumPic = (ImageView) findViewById(R.id.miniAlbumPic);
        musicTitleTV = (TextView) findViewById(R.id.music_title_tv);
        musicArtistTV = (TextView) findViewById(R.id.musicArtistTV);

        // bottom right
        playOrPauseIV = (ImageButton) findViewById(R.id.playOrPause);
        playNextIV = (ImageButton) findViewById(R.id.playNext);
//        playPrevIV = (ImageButton) findViewById(R.id.playPrev);
    }

    private void setAdapter() {
        // how to custom cursor adapter data?
//        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.music_list_item, cursor, new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST},
//                new int[]{R.id.musicTitleTV, R.id.musicAlbumTV, R.id.musicInfoTV}, 0);
//        listView.setAdapter(simpleCursorAdapter);
//        totalCountTV.setText(new StringBuilder("(共").append(MusicApplication.musicLists.size()).append("首)"));
//        MusicListAdapter musicListAdapter = new MusicListAdapter(this, R.layout.music_list_item, MusicApplication.musicLists);
//        listView.setAdapter(musicListAdapter);
    }

    private void setListeners() {
        // Set play or pause button OnclickListener
        playOrPauseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.putExtra("seekBarProgress", seekBar.getProgress());
                intent.setAction(GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_OR_PAUSE);
                sendBroadcast(intent);
            }
        });
        playNextIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setAction(GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_NEXT);
                sendBroadcast(intent);
            }
        });
        playPrevIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setAction(GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_PREV);
                sendBroadcast(intent);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int currentFirstVisiblePosition = listView.getFirstVisiblePosition();
                int currentLastVisiblePosition = listView.getLastVisiblePosition();
                // refresh musicIndexTV and playingTrumpet widget view if and only if prePosition is not equal position
                if(prevPosition != position) {
                    // refresh prePosition listView item state if and only if it's on visible situation
                    if (prevPosition >= currentFirstVisiblePosition && prevPosition <= currentLastVisiblePosition) {
                        View viewd = listView.getChildAt(prevPosition-currentFirstVisiblePosition);
                        ((View) viewd.getTag(R.id.playingTrumpet)).setVisibility(View.GONE);
                        ((View) viewd.getTag(R.id.musicIndexTV)).setVisibility(View.VISIBLE);
//                    Log.i(TAG, "initMusicInfo: " + viewd);
                    }
                    ((View) view.getTag(R.id.playingTrumpet)).setVisibility(View.VISIBLE);
                    ((View) view.getTag(R.id.musicIndexTV)).setVisibility(View.GONE);
                }
                intent = new Intent();
                intent.putExtra("position", position);
                intent.setAction(GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_SPECIFIC);
                sendBroadcast(intent);
            }
        });
        playMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playModeIndex = ++playModeIndex>= playModeActionContainer.length ? 0 : playModeIndex;
                playMode.setImageBitmap(BitmapFactory.decodeResource(getResources(), playModeResource[playModeIndex]));
                intent = new Intent();
                intent.setAction(playModeActionContainer[playModeIndex]);
                ToastUtils.makeText(MusicListActivity.this, playModeDescriptionResource[playModeIndex], Toast.LENGTH_LONG).show();
                sendBroadcast(intent);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                intent = new Intent();
                intent.setAction(GlobalMusicPlayControllerConst.ACTION_ACTIVITY_SEEK_BAR_PROGRESS_CHANGED);
                Log.i(TAG, "onStopTrackingTouch: " + seekBar.getProgress());
                intent.putExtra(GlobalMusicPlayControllerConst.ACTION_ACTIVITY_SEEK_BAR_PROGRESS_CHANGED, seekBar.getProgress());
                sendBroadcast(intent);
            }
        });
        playAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.putExtra("position", 0);
                intent.setAction(GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_SPECIFIC);
                sendBroadcast(intent);
            }
        });
    }

    private void setReceiver() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_SERVICE_PLAYING);
        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_SERVICE_PAUSE);
        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_SERVICE_PLAY_CONTINUE);
        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_SERVICE_UPDATE_SEEK_BAR_PROGRESS);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "onReceive: " + intent.getAction());
                switch (intent.getAction()) {
                    case GlobalMusicPlayControllerConst.ACTION_SERVICE_PLAYING:
                        Log.i(TAG, "position:  " + currentPosition);
                        MusicApplication.isPlaying = true;
                        initMusicInfo();
                        break;
                    case GlobalMusicPlayControllerConst.ACTION_SERVICE_PLAY_CONTINUE:
                        playOrPauseIV.setImageResource(R.drawable.pause);
                        MusicApplication.isPlaying = true;
                        break;
                    case GlobalMusicPlayControllerConst.ACTION_SERVICE_PAUSE:
                        playOrPauseIV.setImageResource(R.drawable.play);
                        MusicApplication.isPlaying = false;
                        break;
                    case GlobalMusicPlayControllerConst.ACTION_SERVICE_UPDATE_SEEK_BAR_PROGRESS:
                        int currentPosition = intent.getIntExtra(GlobalMusicPlayControllerConst.ACTION_SERVICE_UPDATE_SEEK_BAR_PROGRESS, 0);
//                        Log.i(TAG, "onReceive: " + currentPosition + " " + MusicApplication.musicLists.get(position).getDuration());
                        seekBar.setProgress(currentPosition);
                        break;
                }
                if (!MusicApplication.isPlaying) {
                    seekBar.setVisibility(View.GONE);
                } else {
                    seekBar.setVisibility(View.VISIBLE);
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void initMusicInfo() {
        int currentFirstVisiblePosition = listView.getFirstVisiblePosition();
        int currentLastVisiblePosition = listView.getLastVisiblePosition();
//        Log.i(TAG, "currentFirstVisiblePosition: " + currentFirstVisiblePosition);
        if (MusicApplication.isPlaying) {
            // setting play button style
            playOrPauseIV.setImageResource(R.drawable.pause);

            // refresh musicIndexTV and playingTrumpet widget view if and only if prePosition is not equal position
            if(prevPosition != currentPosition) {
                // refresh prePosition listView item state if and only if it's on visible situation
                if (prevPosition >= currentFirstVisiblePosition && prevPosition <= currentLastVisiblePosition) {
                    View view = listView.getChildAt(prevPosition-currentFirstVisiblePosition);
                    ((View) view.getTag(R.id.playingTrumpet)).setVisibility(View.GONE);
                    ((View) view.getTag(R.id.musicIndexTV)).setVisibility(View.VISIBLE);
//                    Log.i(TAG, "initMusicInfo: " + view);
                }
                // refresh position listView item state if and only if it's on visible situation
                if (currentPosition >= currentFirstVisiblePosition && currentPosition <= currentLastVisiblePosition) {
                    View view = listView.getChildAt(currentPosition -currentFirstVisiblePosition);
                    ((View) view.getTag(R.id.playingTrumpet)).setVisibility(View.VISIBLE);
                    ((View) view.getTag(R.id.musicIndexTV)).setVisibility(View.GONE);
                }
            }
        }

        // setting smooth scroll to current play position, listView will be go straight immediately
        // if current position minus will play position out of range
        if (Math.abs(currentPosition - currentFirstVisiblePosition) > 10) {
            listView.setSelection(currentPosition);
        } else {
            listView.smoothScrollToPosition(currentPosition);
        }

        // setting screen bottom bar music info
        Music music = getMusicByPosition(currentPosition);
        if (music != null) {
            musicTitleTV.setText(music.getTitle());
            musicArtistTV.setText(music.getArtist());
//            seekBar.setMax((int) MusicApplication.musicLists.get(position).getDuration());

            // setting album art
            long id = music.getAlbumID();
            if (id!=-1) {
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), id)));
                } catch (FileNotFoundException e) {
                    // setting default bitmap if album art not found
                    bitmap = BitmapFactory.decodeStream(getResources().openRawResource(R.raw.avatar));
                    e.printStackTrace();
                }
                miniAlbumPic.setImageBitmap(bitmap);
                largeAlbumPic.setImageBitmap(bitmap);
            }
        }

    }

    private void initPlayMode() {
        playModeIndex = 0;
        playModeActionContainer = new String[]{GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ALL,
                GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ONCE,
                GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_SHUFFLE,
                GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_ORDER};
        playModeResource = new int[]{R.drawable.all_repeat,
                R.drawable.repeat_once,
                R.drawable.shuffle,
                R.drawable.order};
        playModeDescriptionResource = new int[]{R.string.all_repeat, R.string.repeat_once, R.string.shuffle, R.string.order};
    }


    private Cursor getMusicList() {
        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        return cursor;
    }

    private Music getMusicByPosition(int position) {
        Music music = null;
        Log.i(TAG, "getMusicByPosition: " + position);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToPosition(position);
            music = new Music();
            music.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            music.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
            music.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            music.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            music.setAlbumArt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_KEY)));
            music.setAlbumID(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            music.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            music.setData(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            music.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
        }
        return music;
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: " + currentPosition);
        if (!MusicApplication.isPlaying) {
//            seekBar.setVisibility(View.GONE);
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            MusicApplication.currentPosition = sharedPreferences.getInt("position", 0);
            initMusicInfo();
            seekBar.setProgress(sharedPreferences.getInt("seekBarProgress", 0));
        } else {
            initMusicInfo();
        }
        registerReceiver(broadcastReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(broadcastReceiver);

        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        unregisterReceiver(broadcastReceiver);
        if (!MusicApplication.isPlaying) {
            intent = new Intent(this, MusicPlayService2.class);
            stopService(intent);
        }
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("position", currentPosition);
        editor.putInt("seekBarProgress", seekBar.getProgress());
        editor.apply();
        Log.d(TAG, "onDestroy() ");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, R.string.exit);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
//                onDestroy();
//                Process.killProcess(Process.myPid());
                finish();
                MusicApplication.isPlaying = false;
                intent = new Intent(this, MusicPlayService2.class);
                stopService(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
