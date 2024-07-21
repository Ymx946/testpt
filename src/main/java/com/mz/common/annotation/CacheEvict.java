package com.mz.framework.annotation;

import java.lang.annotation.*;

/**
 * @author yangh
 * @date 2024/06/20 15:19
 * @desc 清理缓存注解
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheEvict {

    /**
     * 根据指定的缓存key释缓存
     */
    String[] key() default {};

    /**
     * 指定要释放的缓存的类
     *
     * @return
     */
    Class<?>[] type() default {};

    /**
     * 指定要释放哪个方法的缓存
     *
     * @return
     */
    String[] method() default {};

    /**
     * 是否清空所有缓存
     *
     * @return
     */
    boolean releaseAll() default false;
}
