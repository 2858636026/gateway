import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassDemo3 {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // 要获取print(int ,int )方法 1.要获取一个方法就是获取类的信息，获取类的信息首先要获取类的类类型
        Class pClass = Class.forName("MyClass");
        Method printMessage = pClass.getMethod("printMessage");
        printMessage.invoke(String.class);
    }


}
