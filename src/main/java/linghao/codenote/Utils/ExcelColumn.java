package linghao.codenote.Utils;

import java.lang.annotation.*;

/**
 * @author 凌浩,
 * @date 2019/8/21,
 * @time 11:45,
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {

    String value() default "";

    int col() default 0;

    String innerSelectPropName() default "";
}
