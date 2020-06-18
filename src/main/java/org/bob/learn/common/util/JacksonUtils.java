package org.bob.learn.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Calendar;

/**
 * Jackson工具类
 */
@Slf4j
public final class JacksonUtils {

    private final static ObjectMapper OBJECT_MAPPER ;

    private final static String EXCEPTION_OBJECT_TO_STRING_DESC = "Jackson 对象转字符串异常:{}";

    private final static String EXCEPTION_STRING_TO_OBJECT_DESC = "Jackson 字符串转对象异常:{}";

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        OBJECT_MAPPER.setTimeZone(Calendar.getInstance().getTimeZone());
        OBJECT_MAPPER.findAndRegisterModules();
    }

    /**
     * 私有构造函数
     * @throws IllegalAccessException
     */
    private JacksonUtils() throws IllegalAccessException {
        throw new IllegalAccessException("拒绝访问私有构造函数");
    }

    /**
     *  Object To Json
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String toJson(T t) {
        String s = null;
        if (t != null) {
            try{
                s = OBJECT_MAPPER.writeValueAsString(t);
            } catch (JsonProcessingException e) {
                log.error(EXCEPTION_OBJECT_TO_STRING_DESC,e.toString());
                s = null;
            }
        }
        return s;
    }

    /**
     * Json To Bean
     * @param s
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String s, Class<T> clazz){
        T t = null;
        if (StringUtils.isNoneBlank(s)) {
            try {
                t = OBJECT_MAPPER.readValue(s, clazz);
            }catch (IOException e) {
                log.error(EXCEPTION_STRING_TO_OBJECT_DESC,e.toString());
            }
        }
        return  t;
    }
}
