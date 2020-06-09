package linghao.codenote.demo;

import java.util.Arrays;

/**
 * @author 凌浩,
 * @date 2019/7/23,
 * @time 9:27,
 */
public class Spilt {

    public static void main(String[] args) {
        String str = "1,2,";
        String str2 = "1|2|3|4";
        String[] arr = str2.split("[|]");
        if (Arrays.asList(arr).contains("0") || Arrays.asList(arr).contains("2")) {
            System.out.println("true");
        }
        System.out.println(Arrays.stream(arr));
    }

}
