/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.graphics.util;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

/**
 * Clase para realizar una representación simple de los ejes X, Y y Z en el
 * espacio.
 * @author Karo
 */
public class XYZ extends LineArray{

    /**
     * Crea una nueva instancia de XYZ con valores predeterminados.
     */
    public XYZ(){
        super(6, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        this.setCoordinate(0, new Point3f(-1000, 0, 0));
        this.setCoordinate(1, new Point3f(1000, 0, 0));

        this.setCoordinate(2, new Point3f(0, -1000, 0));
        this.setCoordinate(3, new Point3f(0, 1000, 0));

        this.setCoordinate(4, new Point3f(0, 0, -1000));
        this.setCoordinate(5, new Point3f(0, 0, 1000));

        Color3f[] colors = new Color3f[6];
        colors[0] = new Color3f(255, 0, 0);//Rojo
        colors[1] = new Color3f(255, 0, 0);//Eje x
        colors[2] = new Color3f(0, 255, 0);//Verde
        colors[3] = new Color3f(0, 255, 0);//Eje Y
        colors[4] = new Color3f(0, 0, 255);//Azul
        colors[5] = new Color3f(0, 0, 255);//Eje Z
        this.setColors(0, colors);
    }
}
