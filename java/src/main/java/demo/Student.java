package demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 凌浩,
 * @date 2019/8/23,
 * @time 14:05,
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private String name;

    private Integer age;
}
