package linghao.codenote.demo;

/**
 * @author linghao,
 * @date 2020/1/2,
 * @time 2:54 下午
 */
public class Loop {

    public static void main(String[] args) {
        for (int i = 0; i < 12; i++) {

            if (i % 4 == 0) {
                System.out.println("bb" + i);
                continue;
            } else if (i % 2 == 0) {
                System.out.println("aa" + i);
                continue;
            } else {
                System.out.println("cc" + i);
            }
        }
    }
}
