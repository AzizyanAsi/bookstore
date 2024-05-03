package net.bookstore.common.api;

import net.bookstore.common.data.StringConstants;
import net.bookstore.common.util.TimestampUtils;
import org.springframework.http.HttpStatusCode;

import java.util.LinkedHashMap;
import java.util.Map;

public class ApiResponseHelpers {

    public static Map<String, Object> getDefaultErrorResponseBody(HttpStatusCode statusCode, String cause) {

        Map<String, Object> error = getDefaultResponseBody();
        error.put(StringConstants.STATUS, statusCode.value());
        error.put(StringConstants.CAUSE, cause);
        return error;
    }

    public static Map<String, Object> unsuccessfulAuthResponse(String cause, ApplicationErrorCode applicationErrorCode) {

        Map<String, Object> uAuth = getDefaultResponseBody();
        uAuth.put(StringConstants.CAUSE, cause);
        uAuth.put(StringConstants.APP_ERROR_CODE, applicationErrorCode.getCode());
        return uAuth;
    }

    private static Map<String, Object> getDefaultResponseBody() {

        Map<String, Object> defaultBody = new LinkedHashMap<>();
        defaultBody.put(StringConstants.TIMESTAMP, TimestampUtils.getTimestampZeroOffset());
        return defaultBody;
    }
}
