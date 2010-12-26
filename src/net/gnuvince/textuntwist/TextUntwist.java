package net.gnuvince.textuntwist;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Scanner;

public class TextUntwist {
    private static int SIZE = 26;
    private int[] seed;

    public TextUntwist(String word) {
        seed = cannonize(word);
    }

    /**
     * Return an array of 26 ints containing the number of
     * occurrences of each letter in `word`.
     */
    private static int[] cannonize(String word) {
        int[] letters = new int[SIZE];
        for (int i = 0; i < word.length(); ++i)
            ++letters[word.charAt(i) - 'a'];
        return letters;
    }

    /**
     * Is `subword` a substring made from the letters in seed?
     */
    public boolean isSubWord(String subword) {
        int[] sw_freqs = cannonize(subword);

        for (int i = 0; i < SIZE; ++i) {
            // if word contains more of any letter than seed, 
            // then it is not a subword.
            if (seed[i] - sw_freqs[i] < 0)
                return false;
        }
        return true;
    }
    
    /**
     * Allow user some time to switch to his web browser
     */
    public static void countDown(int seconds) {
        try {
            System.out.println("Switch to Text-Twist now!");
            for (int i = seconds; i >= 0; --i) {
                System.out.println(i);
                Thread.sleep(1000);
            }
        }
        catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: TextTwist <dict> <word>");
            System.exit(1);
        }
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(args[0]));
            TextUntwist tt = new TextUntwist(args[1]);
            Robot robot = new Robot();

            countDown(3); // Give time to switch to game
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (tt.isSubWord(word)) {
                    word = word.toUpperCase(); // Letters have to be typed in upper case.
                    for (int i = 0; i < word.length(); ++i) {
                        robot.keyPress(word.charAt(i));
                        robot.keyRelease(word.charAt(i));
                        robot.delay(67); // Typing too fast causes game display bugs. 
                    }
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_ENTER);
                }
            }
        }
        catch (Exception e) {
            System.err.println(e);
        }
        finally {
            scanner.close();
        }
    }
}
