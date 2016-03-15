package org.mb.monitor;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dmitriy Dzhevaga on 15.03.2016.
 */
public class FileMonitor {
    private final File file;
    private final Runnable handler;
    private long lastModified;
    private boolean isScheduled;

    public FileMonitor(File file, Runnable handler) {
        this.file = file;
        this.lastModified = file.lastModified();
        this.handler = handler;
    }

    public void schedule(long delay) {
        if (isScheduled) {
            throw new IllegalStateException("Monitor is already scheduled");
        }
        Executors.newScheduledThreadPool(1, runnable -> new Thread(runnable, "FileMonitor")).
                scheduleWithFixedDelay(this::doMonitor, delay, delay, TimeUnit.SECONDS);
        isScheduled = true;
    }

    private void doMonitor() {
        if (checkModified()) {
            handler.run();
        }
    }

    private boolean checkModified() {
        long modified = file.lastModified();
        if (lastModified != modified) {
            lastModified = modified;
            return true;
        } else {
            return false;
        }
    }
}
