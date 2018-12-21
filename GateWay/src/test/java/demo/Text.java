package demo;

public class Text {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                model.getCurrent();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                model.freedCurrent();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                model.useCurrent();
            }
        }).start();
        System.out.println("完毕");
    }
}

