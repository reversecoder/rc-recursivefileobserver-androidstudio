package com.reversecoder.recursivefileobserver.fileobserver;

/**
 * @author Md. Rashadul Alam
 */
public interface FileObserverListener {
    void onEvent(int event, String path);
}