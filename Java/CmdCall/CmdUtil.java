package com.panan.callShell.cmd;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * cmd工具类
 *
 * @author panan
 * @since 2019-12-13
 */
public class CmdUtil {

    private static Set<Character> blackCharsList = new HashSet<>();

    static {
        blackCharsList.add('|');
        blackCharsList.add('&');
        blackCharsList.add('`');
        blackCharsList.add(';');
        blackCharsList.add('$');
        blackCharsList.add('>');
        blackCharsList.add('<');
        blackCharsList.add('!');
        blackCharsList.add('\\');
    }
    /**
     * cmd校验方法
     *
     * @param cmd cmd命令
     * @return 是否合法
     */
    public static boolean checkCmd(List<String> cmd) {
        boolean isSafecmd = true;
        if (cmd == null || cmd.isEmpty()) {
            return !isSafecmd;
        }

        for (String command : cmd) {
            char[] cmdChars = command.toCharArray();
            for (char chCmd : cmdChars) {
                if (blackCharsList.contains(chCmd)) {
                    isSafecmd = false;
                    break;
                }
            }
        }
        return isSafecmd;
    }
}
