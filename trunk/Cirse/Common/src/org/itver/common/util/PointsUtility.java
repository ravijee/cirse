/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.common.util;

import javax.vecmath.Tuple3f;

/**
 *
 * @author pablo
 */
public class PointsUtility {
    public static void min(Tuple3f t1, Tuple3f t2){
        t1.set(t1.x < t2.x ? t1.x : t2.x,
               t1.y < t2.y ? t1.y : t2.y,
               t1.z < t2.z ? t1.z : t2.z);
//        return t1;
    }

    public static void max(Tuple3f t1, Tuple3f t2){
        t1.set(t1.x > t2.x ? t1.x : t2.x,
               t1.y > t2.y ? t1.y : t2.y,
               t1.z > t2.z ? t1.z : t2.z);
//        return t1;
    }
}
