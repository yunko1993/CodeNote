package linghao.codenote.demo;

import linghao.codenote.Utils.ExcelColumn;
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
    @ExcelColumn(value = "姓名",col = 1)
    private String name;
    @ExcelColumn(value = "年龄",col = 2)
    private Integer age;
}
