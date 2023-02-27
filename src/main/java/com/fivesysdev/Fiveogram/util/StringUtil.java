package com.fivesysdev.Fiveogram.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtil {
    public static List<String> get(String[] texts){
        String[] words = Arrays.copyOfRange(texts, 1, texts.length);
        List<String> result = new ArrayList<>();
        for (String word : words) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (c == ' ') {
                    result.add(sb.toString());
                    break;
                }
                sb.append(c);
            }
        }
        return result;
    }
}
