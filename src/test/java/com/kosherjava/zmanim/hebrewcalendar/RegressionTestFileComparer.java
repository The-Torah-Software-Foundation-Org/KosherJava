package com.kosherjava.zmanim.hebrewcalendar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class RegressionTestFileComparer {
    public static void main(String[] args) throws IOException {
        RegressionTestFileComparer comparer = new RegressionTestFileComparer();
        comparer.compare(new File("lakewood_calendar_iso.csv"), new File("lakewood_calendar.csv"));
        comparer.compare(new File("lakewood_zmanim_iso.csv"), new File("lakewood_zmanim.csv"));
    }
    private void compare(File expected, File actual) throws IOException {
        List<String> expectedLines = Files.readAllLines(expected.toPath());
        List<String> actualLines = Files.readAllLines(actual.toPath());
        if(expectedLines.size() != actualLines.size()) {
            System.out.println("Unequal size: " + expectedLines.size() + "vs. " + actualLines.size());
            throw new IllegalStateException();
        }
        String[] fields = expectedLines.get(0).split(",");
        for (int expectedLineNum = 0; expectedLineNum < expectedLines.size(); expectedLineNum++) {
            String[] expectedValues = expectedLines.get(expectedLineNum).split(",");
            String[] actualValues = actualLines.get(expectedLineNum).split(",");
            for (int i = 0; i < fields.length; i++) {
                String expectedValue = expectedValues[i];
                String actualValue = actualValues[i];
                if(!expectedValue.equals(actualValue)) System.out.println("Field " + fields[i] + " unequal for date " + expectedValues[0] + ". Expected: " + expectedValue + "; Actual: " + actualValue);
            }
        }
    }
}
