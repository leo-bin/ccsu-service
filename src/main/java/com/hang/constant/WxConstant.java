package com.hang.constant;

/**
 * @author test
 * @date 19-4-26
 * *****************
 * function:
 */
public class WxConstant {

    public static final boolean DEBUG = true;

    public static final class PlatformType {

        public static final String MINI_PROGRAM = "MiniProgram";

        public static final String Android = "Android";

        public static final String IOS = "IOS";

    }

    public static final class WxApi {
        public static final String CODE_TO_SESSION = "https://api.weixin.qq.com/sns/jscode2session?appid={appId}&secret={appSecret}&js_code={code}&grant_type=authorization_code";
    }

    public static final String TEST_OPEN_ID = "o8Tn70BKPhACUHANmCmh0S9amdkM";

    public static final String TEST_SESSION_ID = "test-session-id";

}
