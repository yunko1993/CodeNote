package linghao.codenote.excel.controller;

import linghao.codenote.Utils.ExcelUtils;
import linghao.codenote.demo.Student;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 凌浩,
 * @date 2019/10/29,
 * @time 16:58,
 */
@RestController
@RequestMapping("/poiexcel")
public class PoiExcel {
    @RequestMapping("/export")
    public void exprot(HttpServletResponse response){
        List<Student> students = new ArrayList<>();
        students.add(new Student("abc", 12));
        students.add(new Student("bcd", 20));
        students.add(new Student("cde", 17));
        students.add(new Student("efg", 15));
        students.add(new Student("def", 25));
        ExcelUtils.writeExcel(response,"学生表","学生",students,Student.class);
    }
    @RequestMapping("/import")
    public List<Student> importExcel(MultipartFile file){
        return ExcelUtils.readExcel(Student.class,file);
    }

}
