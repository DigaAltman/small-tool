package com.dn.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClearCache {

    // 缓存对应的组名称, 当标注了 @ClearCache 的 update 和 delete 被调用后, 同组名称下的缓存全体失效
    String group() default "";
}
