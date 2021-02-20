package com.dn.config;

import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

// @ConditionalOnProperty(value = "dn.cache")
@Data
public class CacheBean implements Serializable {

    /**
     * 当用户设置此缓存为随机缓存, 此随机时间范围就会生效
     * timeout = cache.time() + new Random().nextInt(randomTime)
     */
    @Min(5)
    private int randomTime = 30;


    /**
     * 一次续命时长
     */
    @Min(5)
    private int heatTime = 10;


}
