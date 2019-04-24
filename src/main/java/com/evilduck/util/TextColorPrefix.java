package com.evilduck.util;

public enum TextColorPrefix {

    DEFAULT("```\n"),
    GREEN("```CSS\n"),
    CYAN("```yaml\n"),
    BLUE("```md\n"),    //DOESN'T WORK!!!
    YELLOW("```fix\n"),
    ORANGE("```glsl\n"),
    RED("```diff\n-");

    private final String color;

    TextColorPrefix(final String color) {
        this.color = color;
    }

    public static String getTextInColor(final TextColorPrefix colorPrefix,
                                        final String text) {
        return colorPrefix.toString() + text + "\n```";
    }

    @Override
    public String toString() {
        return color;
    }
}
