package org.ybygjy.basic.annotation.brfw.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * �޸���ĳ�Ա����
 * @author WangYanCheng
 * @version 2009-12-15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,
        ElementType.METHOD
})
public @interface Persistent {
    /**
     * value
     * @return
     */
    String value() default "";
}
