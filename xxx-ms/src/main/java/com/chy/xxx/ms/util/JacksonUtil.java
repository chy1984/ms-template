package com.chy.xxx.ms.util;

import com.chy.xxx.ms.exception.ServiceRuntimeException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * jackson工具类
 *
 * @author chy
 */
public class JacksonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String toJsonStr(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new ServiceRuntimeException("序列化失败", e);
        }
    }

    public static <T> T parse(String jsonStr, Class<T> type) {
        try {
            return MAPPER.readValue(jsonStr, type);
        } catch (Exception e) {
            throw new ServiceRuntimeException("反序列化失败", e);
        }
    }

    public static <T> T parse(String jsonString, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(jsonString, typeReference);
        } catch (Exception e) {
            throw new ServiceRuntimeException("反序列化失败", e);
        }
    }

    public static <T> List<T> parseList(String jsonStr, Class<T> clazz) {
        try {
            JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
            return MAPPER.readValue(jsonStr, javaType);
        } catch (Exception e) {
            throw new ServiceRuntimeException("反序列化为list失败", e);
        }
    }

    public static <K, V> Map<K, V> parseMap(String jsonStr, Class<K> keyClazz, Class<V> valueClazz) {
        try {
            JavaType javaType = MAPPER.getTypeFactory().constructParametricType(Map.class, keyClazz, valueClazz);
            return MAPPER.readValue(jsonStr, javaType);
        } catch (Exception e) {
            throw new ServiceRuntimeException("反序列化为map失败", e);
        }
    }

    private JacksonUtil() {
    }

}
