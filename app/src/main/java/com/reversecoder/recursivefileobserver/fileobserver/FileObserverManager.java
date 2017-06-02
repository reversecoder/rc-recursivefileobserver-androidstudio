package com.reversecoder.recursivefileobserver.fileobserver;

import android.content.Context;
import android.os.FileObserver;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import static com.reversecoder.recursivefileobserver.service.FileObserverService.getObserverPrintedMessage;

/**
 * @author Md. Rashadul Alam
 */
public class FileObserverManager extends RecursiveFileObserver {

    String aboslutePath = "path to your directory";
    private FileObserverListener mListener = null;
    private final Handler handler;
    private Context mContext;
    private static volatile FileObserverManager sInstance = null;
    private final String TAG = "FileObserverManager";

    public static FileObserverManager getInstance(Context context, String path) {
        if (sInstance == null) {
            synchronized (FileObserverManager.class) {
                if (sInstance == null) {
                    sInstance = new FileObserverManager(context, path);
                }
            }
        }
        return sInstance;
    }

    private FileObserverManager(Context context, String path) {
        super(path);
        aboslutePath = path;
        mContext = context;
        handler = new Handler(context.getMainLooper());
    }

    public FileObserverManager setFileObserverListener(FileObserverListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    public void onEvent(final int event, final String path) {
        handler.post(new Runnable() {
            public void run() {
                if (mListener != null) {
//                    Log.d(TAG, "onEvent: event = " + event + ", path = " + path);
                    mListener.onEvent(event, path);
                    getObserverPrintedMessage(event, path);
                }
            }
        });
    }
}