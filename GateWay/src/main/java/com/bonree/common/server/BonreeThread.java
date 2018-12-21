package com.bonree.common.server;

public abstract class BonreeThread extends Thread {
    
    private boolean suspend = false;
    private int time;
    private String control = ""; // 只是需要一个对象而已，这个对象没有实际意义

    public BonreeThread(int time) {
        this.time = time;
    }

    public void setSuspend(boolean suspend) {
        if (!suspend) {
            synchronized (control) {
                control.notifyAll();
            }
        }
        this.suspend = suspend;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (control) {
                if (suspend) {
                    try {
                        control.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            this.runPersonelLogic();
        }
    }

    protected abstract void runPersonelLogic();
}


