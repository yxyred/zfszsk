

package com.css.common.validator;

import com.css.common.exception.CvaException;
import org.apache.commons.lang.StringUtils;

/**
 * 数据校验
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new CvaException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new CvaException(message);
        }
    }
}
