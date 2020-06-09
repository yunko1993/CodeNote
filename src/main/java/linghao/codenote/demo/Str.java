package linghao.codenote.demo;

import lombok.var;

/**
 * @author linghao,
 * @date 2019/11/26,
 * @time 7:28 下午
 */
public class Str {
    public static void main(String[] args) {
        String str = "22.11 Gib";

        System.out.println(Float.valueOf(str.substring(0, str.indexOf(" "))));

        //        String ss = str.substring(str.length()-1);
//
//        System.out.println(!ss.matches("^[0-9]*$"));
//        System.out.println(str.substring(0,str.length()-1));
    }
}
