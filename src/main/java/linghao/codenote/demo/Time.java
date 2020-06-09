package linghao.codenote.demo;

import java.util.Date;

/**
 * @author 凌浩,
 * @date 2019/10/23,
 * @time 14:44,
 */
public class Time {
    public static void main(String[] args) {

        System.out.println(new Date());

        System.out.println(new Date(System.currentTimeMillis() + 4 * 1000 * 60 * 60));

    }
}
