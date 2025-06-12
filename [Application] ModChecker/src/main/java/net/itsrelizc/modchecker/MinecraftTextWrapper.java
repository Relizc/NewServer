package net.itsrelizc.modchecker;

import java.util.*;
import java.util.regex.*;

public class MinecraftTextWrapper {
    private static final Set<Character> INVALID_LINE_START = Set.of(
        ',', '.', '!', '?', ':', ';', ')', ']', '}', '\'', '"'
    );

    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("§[0-9a-fk-or]");

    public static String wrapText(String text, int maxWidth) {
        if (text == null || maxWidth < 1) return "";

        String[] words = text.split(" ");
        StringBuilder wrapped = new StringBuilder();
        StringBuilder line = new StringBuilder("§r"); // start with reset color
        String activeColor = "§r";

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            String cleanWord = stripColorCodes(word);

            // Calculate length of current line without color codes
            int currentLineLength = stripColorCodes(line.toString()).length();

            // Track new color codes in the word
            Matcher matcher = COLOR_CODE_PATTERN.matcher(word);
            while (matcher.find()) {
                activeColor = matcher.group();  // last match is the current color
            }

            boolean wordTooLong = currentLineLength + cleanWord.length() + 1 > maxWidth;

            // Avoid starting new line with punctuation
            boolean invalidPunctuationStart = cleanWord.length() == 1 && INVALID_LINE_START.contains(cleanWord.charAt(0));

            if (wordTooLong && !invalidPunctuationStart) {
                // Push current line
                wrapped.append(line.toString().stripTrailing()).append("\n");

                // Start a new line with the last active color
                line = new StringBuilder(activeColor);
            } else if (line.length() > 0 && currentLineLength > 0) {
                line.append(" ");
            }

            line.append(word);
        }

        if (!line.isEmpty()) {
            wrapped.append(line.toString().stripTrailing());
        }

        return wrapped.toString();
    }

    // Helper to remove color codes for accurate length calculation
    private static String stripColorCodes(String input) {
        return COLOR_CODE_PATTERN.matcher(input).replaceAll("");
    }

    public static void main(String[] args) {
        String text = "§aThis is a §lvery long line §rthat has §csome colors §aand should wrap properly without losing them!";
        System.out.println(wrapText(text, 30));
    }
}
