package com.obd.reader.io;

/**
 * TODO put description
 */
public interface ObdProgressListener {

    void stateUpdate(final ObdCommandJob job);

}