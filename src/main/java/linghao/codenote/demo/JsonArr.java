package linghao.codenote.demo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author 凌浩,
 * @date 2019/7/25,
 * @time 15:15,
 */
public class JsonArr {
    public static void main(String[] args) {
       String str = "[{'imageId':'f7b62e2e24c3','repository':'swr.cn-north-4.myhuaweicloud.com/common-image/x86-sentinel','tag':'1.0.0'}," +
//               "{imageId:231ba0f1ba32,'repository':swr.cn-north-4.myhuaweicloud.com/common-image/x86-etcdfs,'tag':1.0.0}," +
//               "{imageId:83cd1ff4fbe3,'repository':swr.cn-north-4.myhuaweicloud.com/common-image/x86-sersync,'tag':1.0.0}," +
//               "{imageId:fd7c4311ab12,'repository':swr.cn-north-4.myhuaweicloud.com/common-image/x86-nginx,'tag':1.0.0}," +
//               "{imageId:dc2184e50e97,'repository':swr.cn-north-4.myhuaweicloud.com/common-image/x86-redis,'tag':1.0.0}," +
//               "{imageId:30f534fe9517,'repository':swr.cn-north-4.myhuaweicloud.com/common-image/x86-mysql,'tag':1.0.0}," +
//               "{imageId:66dbd2d9fbb1,'repository':swr.cn-north-4.myhuaweicloud.com/common-image/x86-keepalived,'tag':1.0.0}," +
               "{'imageId':'350b164e7ae1','repository':'swr.cn-north-4.myhuaweicloud.com/huawei-ief-internal-app/pause_x86_64','tag':'latest'}]";
        // String str = " [{ 'taskID': '1', 'productID': '1' }, { 'taskID': '1', 'productID': '1' }, { 'taskID': '', 'productID': '' }, { 'taskID': '', 'productID': '' }]";
        JSONArray params = JSONArray.fromObject(str);
        for (int i = 0; i < params.size(); i++) {
            String info = params.getString(i);
            JSONObject pam = JSONObject.fromObject(info);
            String imageId = pam.getString("imageId");
          //  String productId = pam.getString("productID");
            System.out.println( "imageId为---" + imageId);
        }
    }
}
