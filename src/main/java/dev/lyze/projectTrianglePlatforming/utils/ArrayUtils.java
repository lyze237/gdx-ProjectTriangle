package dev.lyze.projectTrianglePlatforming.utils;

import lombok.experimental.UtilityClass;
import lombok.var;

@UtilityClass
public class ArrayUtils {
    public double[] convertToDoubleArray(float[] arr) {
        var newArr = new double[arr.length];

        for (int i = 0; i < arr.length; i++) {
            newArr[i] = arr[i];
        }

        return newArr;
    }

    public float[] convertToFloatArray(double[] arr) {
        var newArr = new float[arr.length];

        for (int i = 0; i < arr.length; i++) {
            newArr[i] = (float) arr[i];
        }

        return newArr;
    }
}
