package com.dn.entity;

import com.dn.common.CacheType;
import com.dn.serialize.ICacheSerialize;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CacheDefinition implements Serializable {
    // 缓存名称
    private String name;

    // 缓存所属组名称
    private String groupName;

    // 缓存设置的时间
    private long time;

    // 缓存类型
    private CacheType type;

    // 序列化服务
    private ICacheSerialize cacheSerialize;

    // 缓存对应的返回值中的泛型类型
    private List<Class> genericTypes;

    // 缓存对应的返回值类型
    private Class returnType;
}
