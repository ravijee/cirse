/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.common.util;

import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;

/**
 *
 * @author pablo
 */
public class Converter {

    public static String tuple3fToString(Tuple3f p){
        String result = "";
        float[] vals = new float[3];
        p.get(vals);
        for (int i = 0; i < vals.length; i++)
            result += ", " + vals[i];
        return result.substring(2);
    }

    public static String color3f4fToString(Object color, boolean b) {
        String result = "";
        Color3f c3;
        Color4f c4;
        float[] vals = new float[4];
        if(b){
            c3 = (Color3f) color;
            c3.get(vals);

        }
        else {
            c4 = (Color4f) color;
            c4.get(vals);
        }

        for (int i = 0; i < vals.length; i++)
            result += ", " + vals[i];
        return result.substring(2);
    }

    public static String intArrayToString(int[] indexP) {
        try{
            String result = "";
            for (int i = 0; i < indexP.length; i++)
                result += ", " + indexP[i];
            return result.substring(2);
        }catch(NullPointerException ex){
            return "";
        }
    }

    public static String tuple3dToString(Tuple3d support) {
        double[] vals = new double[3];
        support.get(vals);
        return doubleArrayToString(vals);
    }

    public static String doubleArrayToString(double[] vals) {
        String result = "";
        for (int i = 0; i < vals.length; i++)
            result += ", " + vals[i];
        return result.substring(2);
    }

    public static double[] stringToDoubleArray(String text){
        text = text.replace(" ", "");
        String tokens[] = text.split(",");
        double result[] = new double[tokens.length];
        for (int i = 0; i < tokens.length; i++)
            result[i] = Double.valueOf(tokens[i]);
        return result;
    }

    public static float[] stringToFloatArray(String text){
        double doubles[] = stringToDoubleArray(text);
        float  result[]  = new float[doubles.length];
        for (int i = 0; i < result.length; i++)
            result[i] = (float)doubles[i];
        return result;
    }

    public static int[] StringToIntArray(String text) {
        text = text.replace(" ", "");
        String[] tokens = text.split(",");
        int[] array = new int[tokens.length];
        for (int i = 0; i < array.length; i++)
            array[i] = Integer.parseInt(tokens[i]);
        
        return array;
    }

}
