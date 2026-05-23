
package com.user114514.zest2d.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighPerformanceStringFormatter {
    private final String rawString;
    private final List<Token> tokens;
    private final int placeholderCount;

    public enum TokenType {
        PLAIN_TEXT, PLACEHOLDER
    }

    public static class Token {
        public final TokenType type;
        public final String value;

        public Token(TokenType typ, String v) {
            this.type = typ;
            this.value = v;
        }
    }

    public HighPerformanceStringFormatter(String rawText) {
        if (rawText == null) {
            throw new IllegalArgumentException("Raw text cannot be null");
        }
        this.rawString = rawText;
        this.tokens = Collections.unmodifiableList(compile(rawText));
        
        int count = 0;
        for (Token t : tokens) {
            if (t.type == TokenType.PLACEHOLDER) {
                count++;
            }
        }
        this.placeholderCount = count;
    }

    private static List<Token> compile(String source) {
        List<Token> tokenList = new ArrayList<>();
        int len = source.length();
        int i = 0;
        StringBuilder sb = new StringBuilder();

        while (i < len) {
            char c = source.charAt(i);
            if (c == '%') {
                if (sb.length() > 0) {
                    tokenList.add(new Token(TokenType.PLAIN_TEXT, sb.toString()));
                    sb.setLength(0);
                }

                if (i + 1 >= len) {
                    tokenList.add(new Token(TokenType.PLAIN_TEXT, "%"));
                    i++;
                } else {
                    char next = source.charAt(i + 1);
                    if (next == '%') {
                        tokenList.add(new Token(TokenType.PLAIN_TEXT, "%"));
                        i += 2;
                    } else {
                        tokenList.add(new Token(TokenType.PLACEHOLDER, String.valueOf(next)));
                        i += 2;
                    }
                }
            } else {
                sb.append(c);
                i++;
            }
        }
        
        if (sb.length() > 0) {
            tokenList.add(new Token(TokenType.PLAIN_TEXT, sb.toString()));
        }
        
        return tokenList;
    }

    public String format(Object... args) {
        if (args.length != placeholderCount) {
            throw new IllegalArgumentException(
                String.format("Argument count mismatch. Expected: %d, Provided: %d", placeholderCount, args.length)
            );
        }

        StringBuilder handledValue = new StringBuilder(rawString.length() + args.length * 10);
        int argIndex = 0;
        
        for (Token token : tokens) {
            if (token.type == TokenType.PLAIN_TEXT) {
                handledValue.append(token.value);
            } else {
                Object arg = args[argIndex++];
                handledValue.append(arg == null ? "null" : arg.toString());
            }
        }
        
        return handledValue.toString();
    }
}
