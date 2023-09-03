package com.chy.xxx.ms.exception;

import com.chy.xxx.ms.response.IErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 运行时业务断言，不符合期望时抛出 ServiceRuntimeException<br/>
 * IErrorCode用于接口返回具体的业务错误码、提示信息；detailMessage用于记录具体异常信息、便于排查问题。<br/>
 * <li>如果接口需要返回具体的业务错误码提示用户：传入IErrorCode参数，全局异常处理器会取IErrorCode对的code、message作为接口响应；</li>
 * <li>如果接口只需返回“999999 系统繁忙”之类的笼统错误信息：使用detailMessage即可，无需传入IErrorCode。</li>
 *
 * @author chy
 */
@Slf4j
public class RtBizAssert {

    public static void assertNotNull(Object obj, String detailMessage) {
        if (obj == null) {
            log.error("入参obj为空, detailMessage={}", detailMessage);
            throw new ServiceRuntimeException(detailMessage);
        }
    }

    public static void assertNotBlank(CharSequence charSequence, String detailMessage) {
        if (StringUtils.isBlank(charSequence)) {
            log.error("入参charSequence为空, detailMessage={}", detailMessage);
            throw new ServiceRuntimeException(detailMessage);
        }
    }

    public static void assertNotEmpty(Collection<?> collection, String detailMessage) {
        if (CollectionUtils.isEmpty(collection)) {
            log.error("入参collection为空, detailMessage={}", detailMessage);
            throw new ServiceRuntimeException(detailMessage);
        }
    }

    public static void assertNotEmpty(Map<?, ?> map, String detailMessage) {
        if (MapUtils.isEmpty(map)) {
            log.error("入参map为空, detailMessage={}", detailMessage);
            throw new ServiceRuntimeException(detailMessage);
        }
    }

    public static void assertNull(Object obj, String detailMessage) {
        if (obj != null) {
            log.error("入参obj不为空,detailMessage={}", detailMessage);
            throw new ServiceRuntimeException(detailMessage);
        }
    }

    public static void assertEmpty(Collection<?> collection, String detailMessage) {
        if (CollectionUtils.isNotEmpty(collection)) {
            log.error("入参collection不为空，detailMessage={}", detailMessage);
            throw new ServiceRuntimeException(detailMessage);
        }
    }

    public static void assertEmpty(Map<?, ?> map, String detailMessage) {
        if (MapUtils.isNotEmpty(map)) {
            log.error("入参map不为空，detailMessage={}", detailMessage);
            throw new ServiceRuntimeException(detailMessage);
        }
    }

    public static void assertTrue(Boolean bool, String detailMessage) {
        if (BooleanUtils.isNotTrue(bool)) {
            log.error("入参bool不为true，detailMessage={}", detailMessage);
            throw new ServiceRuntimeException(detailMessage);
        }
    }

    public static void assertFalse(Boolean bool, String detailMessage) {
        if (BooleanUtils.isNotFalse(bool)) {
            log.error("入参bool不为false，detailMessage={}", detailMessage);
            throw new ServiceRuntimeException(detailMessage);
        }
    }

    public static void assertNotNull(Object obj, IErrorCode errorCode, String detailMessage) {
        if (obj == null) {
            log.error("入参obj为空, errorCode={}，detailMessage={}", errorCode, detailMessage);
            throw new ServiceRuntimeException(errorCode, detailMessage);
        }
    }

    public static void assertNotBlank(CharSequence charSequence, IErrorCode errorCode, String detailMessage) {
        if (StringUtils.isBlank(charSequence)) {
            log.error("入参charSequence为空，errorCode={}，detailMessage={}", errorCode, detailMessage);
            throw new ServiceRuntimeException(errorCode, detailMessage);
        }
    }

    public static void assertNotEmpty(Collection<?> collection, IErrorCode errorCode, String detailMessage) {
        if (CollectionUtils.isEmpty(collection)) {
            log.error("入参collection为空, errorCode={}，detailMessage={}", errorCode, detailMessage);
            throw new ServiceRuntimeException(errorCode, detailMessage);
        }
    }

    public static void assertNotEmpty(Map<?, ?> map, IErrorCode errorCode, String detailMessage) {
        if (MapUtils.isEmpty(map)) {
            log.error("入参map为空, errorCode={}，detailMessage={}", errorCode, detailMessage);
            throw new ServiceRuntimeException(errorCode, detailMessage);
        }
    }

    public static void assertNull(Object obj, IErrorCode errorCode, String detailMessage) {
        if (obj != null) {
            log.error("入参obj不为空,errorCode={}，detailMessage={}", errorCode, detailMessage);
            throw new ServiceRuntimeException(errorCode, detailMessage);
        }
    }

    public static void assertEmpty(Collection<?> collection, IErrorCode errorCode, String detailMessage) {
        if (CollectionUtils.isNotEmpty(collection)) {
            log.error("入参collection不为空,errorCode={}，detailMessage={}", errorCode, detailMessage);
            throw new ServiceRuntimeException(errorCode, detailMessage);
        }
    }

    public static void assertEmpty(Map<?, ?> map, IErrorCode errorCode, String detailMessage) {
        if (MapUtils.isNotEmpty(map)) {
            log.error("入参map不为空, errorCode={}，detailMessage={}", errorCode, detailMessage);
            throw new ServiceRuntimeException(errorCode, detailMessage);
        }
    }

    public static void assertTrue(Boolean bool, IErrorCode errorCode, String detailMessage) {
        if (BooleanUtils.isNotTrue(bool)) {
            log.error("入参bool不为true，errorCode={}，detailMessage={}", errorCode, detailMessage);
            throw new ServiceRuntimeException(errorCode, detailMessage);
        }
    }

    public static void assertFalse(Boolean bool, IErrorCode errorCode, String detailMessage) {
        if (BooleanUtils.isNotFalse(bool)) {
            log.error("入参bool不为false，errorCode={}，detailMessage={}", errorCode, detailMessage);
            throw new ServiceRuntimeException(errorCode, detailMessage);
        }
    }

}
