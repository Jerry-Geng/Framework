package me.jerry.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**View反射注解
 * @author JerryGeng
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented()
public @interface AutoFindView {
	/**
	 * 待反射的View的id
	 * @return
	 */
    int value() default -1;
    /**
     * view反射成功后自动绑定的监听器类型
     * @return
     */
    Class[] listeners() default {};
}
