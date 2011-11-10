/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.graphics.util;

import javax.vecmath.Point3f;

/**
 * Clase que representa la geometría de un prisma rectangular mediante métodos
 * estáticos.
 * @author Karo
 */
public class CuboidGeometry{

    /**
     * Define un arreglo de puntos con los que se dibujará el rectángulo.
     * @param x Ancho.
     * @param y Alto.
     * @param z Profundidad.
     * @return Arreglo de puntos.
     */
    public static Point3f[] generateGeometry(float x, float y, float z){
        Point3f[] points = {new  Point3f(x, y, z),
                            new  Point3f(-x, y, z),
                            new  Point3f(x, -y, z),
                            new  Point3f(x, y, -z),
                            new  Point3f(-x, -y, z),
                            new  Point3f(x, -y, -z),
                            new  Point3f(-x, y, -z),
                            new  Point3f(-x, -y, -z)};
        return points;
    }

    /**
     * Define un arreglo de enteros con los que se indica la unión entre los
     * puntos definidos por #generateGeometry(float, float, float).
     * @return Arreglo de índices.
     */
    public static int[] setIndices(){
        int[] indices = {1,4,2,0, 0,2,5,3, 3,5,7,6,
                         6,7,4,1, 3,6,1,0, 7,5,2,4};
        return indices;
    }

    /**
     * Define un arreglo que indica el intervalo de agrupación de los puntos
     * definidos en #generateGeometry(float, float, float).
     * @return
     */
    public static int[] setStripCounts(){
        int[] strip = {4,4,4,4,4,4};
        return strip;
    }

}
