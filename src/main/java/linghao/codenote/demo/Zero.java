package linghao.codenote.demo;

import java.text.DecimalFormat;

/**
 * @author linghao,
 * @date 2019/12/2,
 * @time 5:50 下午
 */
public class Zero {
    public static void main(String[] args) {
        String lane ="12345";
        //int lane = 12335;
      //  String laneId = new DecimalFormat("00000").format(Integer.valueOf(lane));
        String laneId = "wds123124"+ String.format("%05d",Integer.valueOf(lane.substring(0,1)));
        System.out.println(laneId);

    }



}
