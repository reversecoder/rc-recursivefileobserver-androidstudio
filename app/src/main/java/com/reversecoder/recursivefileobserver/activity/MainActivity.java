package com.reversecoder.recursivefileobserver.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.reversecoder.recursivefileobserver.R;
import com.reversecoder.recursivefileobserver.service.FileObserverService;

import static com.reversecoder.recursivefileobserver.fileobserver.FileObserverConfig.FILE_OBSERVER_MASK;
import static com.reversecoder.recursivefileobserver.util.AllConstants.INTENT_FILTER_ACTIVITY_UPDATE;
import static com.reversecoder.recursivefileobserver.util.AllConstants.KEY_INTENT_EVENT;
import static com.reversecoder.recursivefileobserver.util.AllConstants.KEY_INTENT_PATH;

public class MainActivity extends AppCompatActivity {

    Intent intentOserverService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFileObserver();
    }

    private void initFileObserver() {

        // using service
        intentOserverService = new Intent(getBaseContext(), FileObserverService.class);
        intentOserverService.putExtra(FileObserverService.EXTRA_ACTION, FileObserverService.EXTRA_ACTION_START);
        intentOserverService.putExtra(FileObserverService.EXTRA_DIR_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
        intentOserverService.putExtra(FileObserverService.EXTRA_MASK, FILE_OBSERVER_MASK);
        startService(intentOserverService);

        //using single activity
        //        FileObserverManager.getInstance(MainActivity.this,Environment.getExternalStorageDirectory().getAbsolutePath()).setFileObserverListener(new FileObserverListener() {
//            @Override
//            public void onEvent(int event, String path) {
//                switch (event) {
//                    case FileObserver.CREATE:
//                        Log.d("FileObserverPOC: ", "onEvent: event = " + "File Created" + ", path = " + path);
//                        Toast.makeText(MainActivity.this, "onEvent: event = " + "File Created" + ", path = " + path, Toast.LENGTH_SHORT).show();
//                        break;
//                    case FileObserver.DELETE:
//                        Log.d("FileObserverPOC: ", "onEvent: event = " + "File Deleted" + ", path = " + path);
//                        Toast.makeText(MainActivity.this, "onEvent: event = " + "File Deleted" + ", path = " + path, Toast.LENGTH_SHORT).show();
//                        break;
//                    case FileObserver.DELETE_SELF:
//                        Log.d("FileObserverPOC: ", "onEvent: event = " + "File Deleted Self" + ", path = " + path);
//                        Toast.makeText(MainActivity.this, "onEvent: event = " + "File Deleted Self" + ", path = " + path, Toast.LENGTH_SHORT).show();
//                        break;
//                    case FileObserver.MODIFY:
//                        Log.d("FileObserverPOC: ", "onEvent: event = " + "File Modified" + ", path = " + path);
//                        Toast.makeText(MainActivity.this, "onEvent: event = " + "File Modified" + ", path = " + path, Toast.LENGTH_SHORT).show();
//                        break;
//                    case FileObserver.MOVED_FROM:
//                        Log.d("FileObserverPOC: ", "onEvent: event = " + "File Moved From" + ", path = " + path);
//                        Toast.makeText(MainActivity.this, "onEvent: event = " + "File Moved From" + ", path = " + path, Toast.LENGTH_SHORT).show();
//                        break;
//                    case FileObserver.MOVED_TO:
//                        Log.d("FileObserverPOC: ", "onEvent: event = " + "File Moved To" + ", path = " + path);
//                        Toast.makeText(MainActivity.this, "onEvent: event = " + "File Moved To" + ", path = " + path, Toast.LENGTH_SHORT).show();
//                        break;
//                    case FileObserver.MOVE_SELF:
//                        Log.d("FileObserverPOC: ", "onEvent: event = " + "File Moved Self" + ", path = " + path);
//                        Toast.makeText(MainActivity.this, "onEvent: event = " + "File Moved Self" + ", path = " + path, Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        }).startWatching();
    }

    private void updateUI(Intent intent) {
        int event = intent.getIntExtra(KEY_INTENT_EVENT, -1);
        String path = intent.getStringExtra(KEY_INTENT_PATH);

//        Toast.makeText(MainActivity.this, "Activity: " + "onEvent: event = " + event + ", path = " + path, Toast.LENGTH_SHORT).show();
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        try {
            registerReceiver(broadcastReceiver, new IntentFilter(INTENT_FILTER_ACTIVITY_UPDATE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
