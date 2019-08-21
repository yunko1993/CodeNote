package demo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author 凌浩,
 * @date 2019/7/25,
 * @time 15:15,
 */
public class JsonArr {
    public static void main(String[] args) {
        String str = " [{ 'taskID': '1', 'productID': '1' }, { 'taskID': '1', 'productID': '1' }, { 'taskID': '', 'productID': '' }, { 'taskID': '', 'productID': '' }]";
        JSONArray params = JSONArray.fromObject(str);
        for (int i = 0; i < params.size(); i++) {
            String info = params.getString(i);
            JSONObject pam = JSONObject.fromObject(info);
            String taskId = pam.getString("taskID");
            String productId = pam.getString("productID");
            System.out.println(taskId + "---" + productId);
        }
    }
}
