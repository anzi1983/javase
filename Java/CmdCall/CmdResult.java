package com.panan.callShell.cmd;

/**
 * cmd结果类
 *
 * @author panan
 * @since 2019-12-09
 */
public class CmdResult {

    private String retCode = "";

    private String message = "";

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CmdResult{" + "retCode='" + retCode + '\'' + ", message='" + message + '\'' + '}';
    }
}
