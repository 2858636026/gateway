package demo;

public class model {
    private static Integer current = 0;

    public static synchronized Integer getCurrent() {
        try {
            System.out.println("执行获取");
            Thread.sleep(5000);
            System.out.println("获取完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return model.current;
    }

    public static synchronized void freedCurrent() {
        try {
            System.out.println("执行释放");
            Thread.sleep(5000);
            System.out.println("释放完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        model.current--;
    }

    public static synchronized void useCurrent() {
        try {
            System.out.println("执行使用");
            Thread.sleep(5000);
            System.out.println("使用使用");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        model.current++;
    }
}
