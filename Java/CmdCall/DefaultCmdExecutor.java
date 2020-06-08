package com.panan.callShell.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 默认cmd执行器
 *
 * @author panan
 * @since 2019-12-09
 */
public final class DefaultCmdExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCmdExecutor.class);

    private static final String ERROR_MESSAGE = "execute cmd failed. info {}";

    private static final DefaultCmdExecutor INST = new DefaultCmdExecutor();

    private ExecutorService executor = Executors.newCachedThreadPool(new CommThreadFactory("CmdRun"));

    private DefaultCmdExecutor() {
    }

    /**
     * 构造器
     *
     * @return DefaultCmdExecutor
     */
    public static DefaultCmdExecutor instance() {
        return INST;
    }



    /**
     * 执行shell
     *
     * @param cmd cmd命令
     * @param timeoutMilliseconds 超时时间
     * @param workDir 路径
     * @return 结果
     */
    public CmdResult executeShell(List<String> cmd, int timeoutMilliseconds, String workDir, Map<String, String> env) {
        CmdResult result = new CmdResult();
        boolean isCmdSafe = CmdUtil.checkCmd(cmd);
        if (!isCmdSafe) {
            result.setRetCode(CmdExecuteRetCode.EXECUTE_ERROR);
            result.setMessage("Command inject.");
            LOGGER.error("Command inject.");
            return result;
        }

        Future<CmdResult> future = executor.submit(new CmdRunner(cmd, workDir, env));

        try {
            result = future.get(timeoutMilliseconds, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException e) {
            result.setRetCode(CmdExecuteRetCode.EXECUTE_ERROR);
            result.setMessage(e.getMessage());
            future.cancel(true);
            LOGGER.error(ERROR_MESSAGE, e);
        } catch (TimeoutException e) {
            result.setRetCode(CmdExecuteRetCode.EXECUTE_TIME_OUT);
            result.setMessage("Run command timeout");
            future.cancel(true);
            LOGGER.error(ERROR_MESSAGE, e);
        }
        return result;
    }
}
