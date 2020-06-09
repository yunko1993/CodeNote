package linghao.codenote.Utils;

import linghao.codenote.demo.Student;
import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linghao,
 * @date 2019/12/17,
 * @time 1:50 下午
 */
public class ListUtil {

    public static <T> void print(List<T> datas) {
        System.out.println(datas);
    }

    private void test(Class<Student> clazz) throws NoSuchMethodException {
        System.out.println(clazz.getConstructor());
    }

    public static void main(String[] args) throws NoSuchMethodException {
//        Student student = new Student("1",1);
//        List<Student> students = new ArrayList<>();
//        students.add(student);
//     //   ListUtil.print(students);
//        String cl = students.get(0).getClass().toGenericString();
//       // String[] c =cl.split("\\.");
//       // String name = c[c.length-1];
//        String cls = Student.class.toGenericString();
//        System.out.println(cl.equals(cls));

        ListUtil listUtil = new ListUtil();

        listUtil.test(Student.class);


    }


}
