package client;

public abstract class thread extends Thread {

    private boolean suspend = false;
    private int time;

    private String control = ""; // 只是需要一个对象而已，这个对象没有实际意义

    public thread(int time) {
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

    public boolean isSuspend() {
        return this.suspend;
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

    public static void main(String[] args) throws Exception {
        thread myThread = new thread(2000) {
            protected void runPersonelLogic() {
                System.out.println("myThead is running");
            }
        };
        myThread.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <1000; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("线程1");
                }
            }
        }).start();

        myThread.setSuspend(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <1000; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("线程2");
                }
            }
        }).start();
        Thread.sleep(6000);
        System.out.println("myThread has stopped");

        myThread.setSuspend(false);
        Thread.sleep(5000);
    }
}


