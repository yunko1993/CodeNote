package linghao.codenote.excel.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import linghao.codenote.demo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 凌浩,
 * @date 2019/10/30,
 * @time 9:39,
 */
@RestController
@RequestMapping("/excel")
public class FreeMarkerExcel {

    @Autowired
    private Configuration freemarkerConf;

    @RequestMapping("/export")
    public void Export(HttpServletResponse response) throws IOException, TemplateException {
        List<Student> students = new ArrayList<>();
        students.add(new Student("abc", 12));
        students.add(new Student("bcd", 20));
        students.add(new Student("cde", 17));
        students.add(new Student("efg", 15));
        students.add(new Student("def", 25));
        Map<String, Object> root = new HashMap<>();
        root.put("list",students);
        String fileName = "学生导出.xls";
        Template template;
        template=freemarkerConf.getTemplate("freemarker.ftl");
        //获取系统所在目录
        String userDir = System.getProperties().getProperty("user.dir");
        userDir = userDir + File.separator + fileName;
        File show = new File(userDir);
        show.setWritable(true,false);
        Writer out = new OutputStreamWriter(new FileOutputStream(show), "UTF-8");
        //渲染
        template.process(root, out);
        out.close();
        download(userDir,response,fileName);
        show.delete();
    }

    /**
     * 下载
     * @param filePath
     * @param response
     * @param fname
     * @throws IOException
     */
    public void download(String filePath, HttpServletResponse response, String fname) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setHeader("Pragma", "No-Cache");
        response.setHeader("Cache-Control", "No-Cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("application/msexcel; charset=UTF-8");
        response.setHeader("Content-disposition","attachment; filename=" + URLEncoder.encode(fname, "UTF-8"));// 设定输出文件头
        ServletOutputStream out = null;
        FileInputStream in = new FileInputStream(filePath); // 读入文件
        out = response.getOutputStream();
        out.flush();
        int aRead = 0;
        while ((aRead = in.read()) != -1 & in != null) {
            out.write(aRead);
        }
        out.flush();
        in.close();
        out.close();
        return;
    }
}
