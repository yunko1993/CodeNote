package demo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author 凌浩,
 * @date 2019/8/23,
 * @time 14:03,
 */
public class ListSort {
    public static void main(String[] args) {

        List<Student> students = new ArrayList<>();

        students.add(new Student("abc", 12));
        students.add(new Student("bcd", 20));
        students.add(new Student("cde", 17));
        students.add(new Student("efg", 15));
        students.add(new Student("def", 25));
        students.sort(Comparator.comparing(Student::getName));
        for (Student stu : students) {
            System.out.println(stu);
        }
    }
}
