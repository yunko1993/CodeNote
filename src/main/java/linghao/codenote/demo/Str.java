package linghao.codenote.demo;

/**
 * @author linghao,
 * @date 2019/11/26,
 * @time 7:28 下午
 */
public class Str {
    public static void main(String[] args) {
        String str ="K211+550（苏皖）";
        System.out.println(str.split("（").length);


        //        String ss = str.substring(str.length()-1);
//
//        System.out.println(!ss.matches("^[0-9]*$"));
//        System.out.println(str.substring(0,str.length()-1));
    }
}
