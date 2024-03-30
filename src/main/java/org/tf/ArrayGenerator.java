package org.tf;

import java.util.*;

public class ArrayGenerator {

    public static int[] generateArray(int length, int min, int max) {
        int[] array = new int[length];
        max++;
        for (int i = 0; i < length; i++) {
            array[i] = new Random().nextInt(min, max);
        }
        return array;
    }

    public static int[][] generateArrays(int amount, int length, int min, int max) {
        int[][] array = new int[amount][length];
        for (int i = 0; i < amount; i++) {
            array[i] = generateArray(length, min, max);
        }
        return array;
    }
}
