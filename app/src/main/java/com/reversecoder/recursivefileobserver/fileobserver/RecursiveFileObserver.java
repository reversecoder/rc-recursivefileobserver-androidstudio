package com.reversecoder.recursivefileobserver.fileobserver;

import android.os.FileObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.reversecoder.recursivefileobserver.fileobserver.FileObserverConfig.FILE_OBSERVER_MASK;

/**
 * FileObserver with recursive path watch support
 *
 * @author Md. Rashadul Alam
 */
public abstract class RecursiveFileObserver extends FileObserver {
    private List<SingleObserver> _observers;
    private String _path;
    private int _mask;

    /**
     * Class constructor
     *
     * @param path The file or directory to monitor
     */
    public RecursiveFileObserver(final String path) {
        this(path, FILE_OBSERVER_MASK);
    }

    /**
     * Class constructor
     *
     * @param path The file or directory to monitor
     * @param mask The event or events (added together) to watch for
     */
    public RecursiveFileObserver(final String path, final int mask) {
        super(path, mask);
        this._path = path;
        this._mask = mask;
    }

    public void setMask(int mask) {
        this._mask = mask;
    }

    /**
     * @see android.os.FileObserver#startWatching()
     */
    @Override
    public void startWatching() {
        if (this._observers != null) {
            return;
        }

        this._observers = new ArrayList<>();
        final Stack<String> pathStack = new Stack<>();
        pathStack.push(this._path);

        while (pathStack.isEmpty() == false) {
            final String parentPath = pathStack.pop();
            final SingleObserver newObserver = new SingleObserver(parentPath, this._mask);
            this._observers.add(newObserver);

            final File path = new File(parentPath);
            final File[] fileList = path.listFiles();

            if (fileList != null && fileList.length > 0) {
                for (File fileItem : fileList) {
                    if ((fileItem.isDirectory() == true) && (fileItem.getName().equals(".") == false) && (fileItem.getName().equals("..") == false)) {
                        pathStack.push(fileItem.getPath());
                    }
                }
            }
        }

        for (SingleObserver observerItem : this._observers) {
            observerItem.startWatching();
        }
    }

    /**
     * @see android.os.FileObserver#stopWatching()
     */
    @Override
    public void stopWatching() {
        if (this._observers == null) {
            return;
        }

        for (SingleObserver observerItem : this._observers) {
            observerItem.stopWatching();
        }

        this._observers.clear();
        this._observers = null;
    }

    /**
     * Monitor a single directory and dispatch all the events to its parent
     */
    private class SingleObserver extends FileObserver {
        private final String _watchPath;

        public SingleObserver(final String path, final int mask) {
            super(path, mask);
            this._watchPath = path;
        }

        @Override
        public void onEvent(final int event, final String path) {
            RecursiveFileObserver.this.onEvent(event, this._watchPath + "/" + path);
        }
    }
}