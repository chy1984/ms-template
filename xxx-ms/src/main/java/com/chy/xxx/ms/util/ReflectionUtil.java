package com.chy.xxx.ms.util;

import com.chy.xxx.ms.exception.RtBizAssert;
import com.chy.xxx.ms.exception.ServiceRuntimeException;

import java.lang.reflect.Field;

/**
 * 反射工具类
 *
 * @author chy
 */
public class ReflectionUtil {

    /**
     * 获取指定对象的字段值
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        Field field = ReflectionUtil.getField(obj.getClass(), fieldName);
        RtBizAssert.assertNotNull(field, String.format("%s中找不到%s字段", obj.getClass(), fieldName));
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new ServiceRuntimeException(String.format("从%s获取%s字段值失败", obj.getClass(), fieldName), e);
        }
    }

    /**
     * 获取指定字段
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        RtBizAssert.assertNotNull(clazz, "clazz不能为空");
        RtBizAssert.assertNotNull(clazz, "fieldName不能为空");

        //getDeclaredField()只能获取到当前类中声明的字段，不能获取从父类继承的字段，需要向上逐级查找
        while (clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    private ReflectionUtil() {
    }

}
