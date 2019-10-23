package src.main.java.demo;
import java.io.Serializable;

/**
 * @author 凌浩,
 * @date 2019/5/17,
 * @time 11:11,
 */
public class NewObject implements  Cloneable, Serializable {

    private static final long serialVersionUID = -6796298979610090690L;

    void message(){
        System.out.println("love you three thousand times");
    }
    public static void main(String[] args) throws Exception {
        NewObject demo1 = new NewObject();

        NewObject demo2 =  NewObject.class.newInstance();

        NewObject demo3 = (NewObject) Class.forName("demo.NewObject").newInstance();

        NewObject demo4 = (NewObject) demo1.clone();

        NewObject demo5= NewObject.class.getConstructor().newInstance();

        demo5.message();

    }
}
