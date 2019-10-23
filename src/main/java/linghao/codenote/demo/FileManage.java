package linghao.codenote.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author 凌浩,
 * @date 2019/9/2,
 * @time 16:55,
 */
public class FileManage {
    public static void main(String[] args) {
        File file =new File("D:\\file");
        if(!file.exists()){
            file.mkdir();
        }
        try{//异常处理
            //如果Qiju_Li文件夹下没有Qiju_Li.txt就会创建该文件
            BufferedWriter bw=new BufferedWriter(new FileWriter("D:\\file\\11.txt"));
            bw.write("Hello I/O!");//在创建好的文件中写入"Hello I/O"
            bw.close();//一定要关闭文件
        }catch(IOException e){
            e.printStackTrace();
        }


    }
}
