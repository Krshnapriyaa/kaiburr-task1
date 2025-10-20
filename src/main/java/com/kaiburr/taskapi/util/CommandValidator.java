package com.kaiburr.taskapi.util;

import java.util.regex.Pattern;

public class CommandValidator {
    // Deny dangerous sequences/tokens
    private static final Pattern DANGEROUS = Pattern.compile(
        "(;|\\|\\||&&|\\||>|<|`|\\$\\(|\\bshutdown\\b|\\breboot\\b|\\brm\\b|\\bpasswd\\b|\\bchown\\b|\\bchmod\\b|\\bsudo\\b|\\bnc\\b|\\bnmap\\b|\\bcurl\\b|\\bwget\\b)",
        Pattern.CASE_INSENSITIVE
    );

    public static boolean isSafe(String cmd) {
        if (cmd == null || cmd.trim().isEmpty()) return false;
        // Basic whitespace-only size check
        if (cmd.length() > 1000) return false;
        // No dangerous tokens
        return !DANGEROUS.matcher(cmd).find();
    }
}
