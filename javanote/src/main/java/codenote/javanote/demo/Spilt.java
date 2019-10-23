package src.main.java.demo;
/**
 * @author 凌浩,
 * @date 2019/7/23,
 * @time 9:27,
 */
public class Spilt {

    public static void main(String[] args) {
        String str = "1,2,";
        String[] arr = str.split(",");
        System.out.println(arr.toString());
    }

}
