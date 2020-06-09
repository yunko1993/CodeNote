package linghao.codenote.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 凌浩,
 * @date 2019/6/25,
 * @time 15:49,
 */
public class TestTureFalse {
    public static void main(String[] args) {
        boolean a = true;
        boolean b = true;
        boolean c = true;
        boolean d = true;
        boolean e = true;
        Integer gantryStatus = 0;
        if (a && b && c && d && e) {
            gantryStatus = 4;
        }
        ;


        List<String> flags = new ArrayList<>();
        flags.add(String.valueOf(a));
        flags.add(String.valueOf(b));

        flags.add(String.valueOf(c));

        flags.add(String.valueOf(d));

        flags.add(String.valueOf(e));
        if (flags.contains("false")) {
            if (flags.contains("true")) {
                gantryStatus = 2;
            } else {
                gantryStatus = 1;
            }
        }
        System.out.println("结果是：" + gantryStatus);
    }
}
