package org.bob.learn.common.constant;


import org.springframework.data.redis.connection.DataType;

/**
 * Redis Key枚举
 */
public enum RedisKey {

    /**
     * 网关省份Redis Key模式表达式
     * 正常网关：gateway.province:mac-null
     * 重复网关：gateway.province:mac-sn
     */
    GATEWAY_PROVINCE("gateway.province:%s-%s",DataType.STRING,-1,"redis.province.expireTime"),

    /**
     * 网关绑定Redis Key模式表达式
     * 正常网关：dhome:preset:gateway:share:mac_null
     * 重复网关：dhome:preset:gateway:share:mac_sn
     */
    GATEWAY_BIND("dhome:preset:gateway:share:%s_%s",DataType.STRING,604800,"" +
            "" +
            "");

    /**
     * Redis Key的模式表达式
     */
    private final String pattern;

    /**
     * Redis Key的数据类型
     */
    private final DataType type;

    /**
     * Redis Key的默认过期时间
     * 系统配置获取不到有效配置时生效
     * 例外：THREAT_CONFIG有效期为-1，即不过期
     */
    private final long defaultExpiration;

    /**
     * Redis Key的过期时间配置key,对应于数据库系统参数表中的参数名和Redis中模块配置[threat:config]中的Hash Key
     */
    private final String expirationConfigKey;

    RedisKey(String pattern, DataType type, long defaultExpiration, String expirationConfigKey){
        this.pattern = pattern;
        this.type = type;
        this.defaultExpiration = defaultExpiration;
        this.expirationConfigKey = expirationConfigKey;
    }

    public String getPattern() {
        return pattern;
    }

    public DataType getType() {
        return type;
    }

    public long getDefaultExpiration() {
        return defaultExpiration;
    }

    public String getExpirationConfigKey() {
        return expirationConfigKey;
    }
}
