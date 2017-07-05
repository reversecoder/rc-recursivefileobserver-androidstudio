package com.reversecoder.recursivefileobserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.FileObserver;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.reversecoder.recursivefileobserver.fileobserver.FileObserverListener;
import com.reversecoder.recursivefileobserver.fileobserver.FileObserverManager;
import com.reversecoder.recursivefileobserver.sqlite.table.DeletedFileInfo;

import org.litepal.crud.DataSupport;

import java.util.List;

import static com.reversecoder.recursivefileobserver.fileobserver.FileObserverConfig.FILE_OBSERVER_MASK;
import static com.reversecoder.recursivefileobserver.util.AllConstants.INTENT_FILTER_ACTIVITY_UPDATE;
import static com.reversecoder.recursivefileobserver.util.AllConstants.KEY_INTENT_EVENT;
import static com.reversecoder.recursivefileobserver.util.AllConstants.KEY_INTENT_PATH;

/**
 * @author Md. Rashadul Alam
 */
public class FileObserverService extends Service implements FileObserverListener {

    Intent broadcastIntentActivityUpdate;

    public static final String EXTRA_ACTION = "action";
    public static final String EXTRA_DIR_PATH = "dirPath";
    public static final String EXTRA_MASK = "mask";

    public static final int EXTRA_ACTION_START = 0;
    public static final int EXTRA_ACTION_STOP = 1;
    private FileObserverManager mFileObserver = null;

    private final String TAG = FileObserverService.class.getSimpleName();
    private String mDirPath = null;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcastIntentActivityUpdate = new Intent(INTENT_FILTER_ACTIVITY_UPDATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int action = intent.getIntExtra(EXTRA_ACTION, -1);
            switch (action) {
                case EXTRA_ACTION_START: {
                    mDirPath = intent.getExtras().getString(EXTRA_DIR_PATH);

                    final int mask = intent.getIntExtra(EXTRA_MASK, FILE_OBSERVER_MASK);
                    Thread startThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handleStartFileObserver(mDirPath, mask);
                        }
                    });
                    startThread.start();
                    break;
                }
                case EXTRA_ACTION_STOP: {
                    Thread stopThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handleStopFileObserver();
                        }
                    });
                    stopThread.start();
                    break;
                }
                default: {
                    break;
                }
            }
        }
        return START_STICKY;
    }

    private void handleStopFileObserver() {
        if (mFileObserver != null) {
            Log.d(TAG, "handleStopFileObserver: stopping..");
            mFileObserver.stopWatching();
        }
    }

    private void handleStartFileObserver(String dirPath, int mask) {
        if (mFileObserver == null) {
            mFileObserver = FileObserverManager.getInstance(this, mDirPath);
            mFileObserver.setMask(mask);
            mFileObserver.setFileObserverListener(this);
            mFileObserver.startWatching();
            Log.d(TAG, "handleStartFileObserver: starting..");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void sendUpdateToActivity(int event, String path) {
        broadcastIntentActivityUpdate.putExtra(KEY_INTENT_EVENT, event);
        broadcastIntentActivityUpdate.putExtra(KEY_INTENT_PATH, path);
        sendBroadcast(broadcastIntentActivityUpdate);
    }

    @Override
    public void onEvent(int event, String path) {
        String eventStatus = getObserverPrintedMessage(event, path);
        Log.d(TAG, "Event catched: " + eventStatus);
//        Toast.makeText(this, eventStatus, Toast.LENGTH_SHORT).show();
        sendUpdateToActivity(event, path);
    }

    public static String getObserverPrintedMessage(int event, String path) {
        String message = "No message";
        switch (event) {
            case FileObserver.CREATE:
                message = "event = " + "File Created" + "\npath = " + path;
                break;
            case FileObserver.DELETE:
                message = "event = " + "File Deleted" + "\npath = " + path;
                String[] splittedPath = path.split("/");
                if (splittedPath.length > 0) {
                    String fileName = splittedPath[(splittedPath.length - 1)];
                    List<DeletedFileInfo> deletedfile = DataSupport.where("fileName = ?", fileName).find(DeletedFileInfo.class);
                    if (deletedfile.size() > 0 && deletedfile.get(0).getFileName().equalsIgnoreCase(fileName)) {
                    } else {
                        DeletedFileInfo deletedFileInfo = new DeletedFileInfo(fileName, path);
                        deletedFileInfo.save();
                    }
                }
                break;
            case FileObserver.DELETE_SELF:
                message = "event = " + "File Deleted Self" + "\npath = " + path;
                break;
            case FileObserver.MODIFY:
                message = "event = " + "File Modified" + "\npath = " + path;
                break;
            case FileObserver.MOVED_FROM:
                message = "event = " + "File Moved From" + "\npath = " + path;
                break;
            case FileObserver.MOVED_TO:
                message = "event = " + "File Moved To" + "path = " + path;
                break;
            case FileObserver.MOVE_SELF:
                message = "event = " + "File Moved Self" + "\npath = " + path;
                break;
        }

//        Log.d("RC_FILE_OBSERVER: ", message);
//        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

        return message;
    }
}
