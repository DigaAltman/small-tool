package com.dn.config;

import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

// @ConditionalOnProperty(value = "dn.cache")
@Data
public class CacheBean implements Serializable {

    /**
     * 当用户设置此缓存为随机缓存, 此随机时间范围就会生效
     * timeout = cache.time() + new Random().nextInt(randomTimeRange)
     */
    @Min(5)
    private int randomTimeRange = 30;

    /**
     * 默认的缓存所属组前缀
     */
    private String groupNamePrefix = "dn:cache:";
}
