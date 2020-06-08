package com.panan.callShell.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * cmd执行类
 *
 * @author panan
 * @since 2019-12-09
 */
public class CmdRunner implements Callable<CmdResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmdRunner.class);

    private static final String ERROR = "-1";

    private String workDir = "";

    private List<String> cmd = null;

    private Map<String, String> env = null;

    /**
     * 构造器
     *
     * @param cmd cmd命令
     * @param workDir 执行路径
     */
    public CmdRunner(List<String> cmd, String workDir, Map<String, String> env) {
        this.workDir = workDir;
        this.cmd = cmd;
        this.env = env;
    }

    @Override
    public CmdResult call() {

        CmdResult result = new CmdResult();

        boolean isSafe = CmdUtil.checkCmd(cmd);
        if (!isSafe) {
            result.setRetCode(ERROR);
            result.setMessage("Execute cmd failed.Command injection.");
            return result;
        }

        try {
            ProcessBuilder builder = new ProcessBuilder();
            setWorkDir(builder);

            if (env != null && !env.isEmpty()) {
                Map<String, String> runEnv = builder.environment();
                runEnv.putAll(env);
            }

            builder.command(cmd);
            Process process = builder.start();

            // any error massage
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream());
            // any output
            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream());
            errorGobbler.start();
            outputGobbler.start();

            int retCode = process.waitFor();
            result.setRetCode(String.valueOf(retCode));

            // Any error
            errorGobbler.join(); // Handle condition
            outputGobbler.join(); // process ends before the threads finish

            String message = outputGobbler.getMessage();
            result.setMessage(message);
        } catch (IOException e) {
            result.setRetCode(ERROR);
            result.setMessage("Run command failed. The file does not exist or has no permission.");
            LOGGER.error("Run command failed. The file does not exist or has no permission.");
        } catch (Exception e) {
            result.setRetCode(ERROR);
            result.setMessage("Run command failed.");
            LOGGER.error("Run command failed.");
        }

        return result;
    }

    private void setWorkDir(ProcessBuilder builder) {
        if (workDir == null || workDir.trim().isEmpty()) {
            return;
        }

        File dir = new File(workDir);
        builder.directory(dir);

    }

    /**
     * StreamGobbler
     */
    static class StreamGobbler extends Thread {
        private final int bufferSize = 2048;
        private final int readStatus = -1;
        private InputStream inputStream;
        private String message;

        /**
         * Process子实例线程
         *
         * @param inputStream Process子实例线程输入流
         * @throws Exception 更新异常
         */
        public StreamGobbler(InputStream inputStream) throws Exception {
            if (inputStream == null) {
                LOGGER.error("input stream is null.");
                throw new Exception("error");
            }
            this.inputStream = inputStream;
            this.message = "";
        }

        /**
         * 获取Process子实例信息
         *
         * @return
         */
        public String getMessage() {
            return message;
        }

        @Override
        public void run() {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream(bufferSize)) {
                byte[] byteBuf = new byte[bufferSize];
                int byteLen;
                while ((byteLen = inputStream.read(byteBuf)) != readStatus) {
                    bos.write(byteBuf, 0, byteLen);
                }
                message = new String(bos.toByteArray(), "UTF-8");
            } catch (IOException e) {
                LOGGER.error("get string from stream failed.");
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        LOGGER.error("close process inputStream failed.");
                    }
                }
            }
        }
    }
}
