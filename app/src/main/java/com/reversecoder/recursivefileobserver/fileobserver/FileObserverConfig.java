package com.reversecoder.recursivefileobserver.fileobserver;

import android.os.FileObserver;

/**
 * @author Md. Rashadul Alam
 */
public class FileObserverConfig {

    public static final int FILE_OBSERVER_MASK = (FileObserver.CREATE |
            FileObserver.DELETE |
            FileObserver.DELETE_SELF | // include directory itself
            FileObserver.MODIFY |
            FileObserver.MOVED_FROM |
            FileObserver.MOVED_TO |
            FileObserver.MOVE_SELF);
}
