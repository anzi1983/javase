package com.panan.callShell.cmd;

/**
 * cmd状态类
 *
 * @author panan
 * @since 2019-12-09
 */
public class CmdExecuteRetCode {

    /**
     * 调用失败
     */
    public static final String EXECUTE_ERROR = "-1";

    /**
     * 调用超时
     */
    public static final String EXECUTE_TIME_OUT = "10001";

    private CmdExecuteRetCode() {
    }
}
