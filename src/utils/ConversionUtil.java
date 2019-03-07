package utils;

import flash.Vector;

public class ConversionUtil {


    public static Vector<Integer> toIntVector(int[] array) {
        Vector<Integer> vector = new Vector<>();

        for (int element : array) {
            vector.add(element);
        }

        return vector;
    }

    public static Vector<Integer> toIntVector(String data) {
        return toIntVector(data, ",");
    }

    private static Vector<Integer> toIntVector(String data, String delimiter) {

        String[] splitData = data.split(delimiter);

        Vector<Integer> ints = new Vector<>(splitData.length);

        for (int i = 0; i < splitData.length; i++) {

            String d = splitData[i];

            d = d.replaceAll("[^\\d.]", "");

            try {
                ints.put(i, Integer.parseInt(d));
            } catch (Exception e) {
                System.err.println("Error while converting '" + data + "' to int vector. Data '" + data + "', : '" + splitData[i] + "' is not a valid int.");
            }
        }

        return ints;

    }

}