package demo;

/**
 * @author 凌浩,
 * @date 2019/8/19,
 * @time 17:18,
 */
public class StringFormat {
    public static void main(String[] args) {
        String point = "1";
        Long max = null;
        max = 10027L;
        String menuNo = max+String.format("%04d",1);
        Long menuNo2=max+1;
        String munuNo3 = menuNo2.toString();
      //  System.out.println(menuNo);
        System.out.println(munuNo3);
    }

}
