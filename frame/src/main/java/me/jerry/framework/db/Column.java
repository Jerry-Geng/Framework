package me.jerry.framework.db;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库字段注解
 * @author JerryGeng
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Column {
	/**
	 * 字段是否可以为空
	 * @return
	 */
    boolean nullable() default false;
    /**
     * 字段是否具有唯一性约束
     * @return
     */
    boolean unique() default false;
}
