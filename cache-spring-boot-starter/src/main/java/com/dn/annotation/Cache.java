package com.dn.annotation;

import com.dn.common.CacheType;
import com.dn.serialize.ICacheSerialize;
import com.dn.serialize.support.DefaultCacheSerialize;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

    // 缓存名称, 默认使用 方法名(参数类型1:参数值1,参数类型2:参数值2) 作为默认的名称
    String name() default "";

    // 缓存对应的组名称, 当标注了 @ClearCache 的 update 和 delete 被调用后, 同组名称下的缓存全体失效
    String group() default "";

    // 缓存有效期, 值低于 0 表示无有效期限制
    long time() default 0;

    /*
     * 自定义缓存的 序列化机制 和 反序列化机制 ,默认是将返回值转换为 json,
     * 然后获取缓存的时候, 将缓存中的内容转换为 JavaBean
     */
    Class<? extends ICacheSerialize> serialize() default DefaultCacheSerialize.class;

    // 缓存的类型, 默认普通缓存
    CacheType type() default CacheType.BASIC;
}
