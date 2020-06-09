package linghao.codenote.excel.controller;

import freemarker.template.TemplateException;
import linghao.codenote.Utils.ExcelUtils;
import linghao.codenote.demo.Student;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    @RequestMapping("/export")
    public void Export(HttpServletResponse response) throws IOException, TemplateException {
        List<Student> students = new ArrayList<>();
        students.add(new Student("abc", 12));
        students.add(new Student("bcd", 20));
        students.add(new Student("cde", 17));
        students.add(new Student("efg", 15));
        students.add(new Student("def", 25));
        Map<String, Object> root = new HashMap<>();
        root.put("list", students);
        String fileName = "学生导出";
        ExcelUtils.ftlExport(response, root, fileName, "/freemarker");
    }

}
