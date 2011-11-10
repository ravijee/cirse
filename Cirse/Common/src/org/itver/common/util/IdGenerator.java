/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.common.util;

/**
 *
 * @author pablo
 */
public class IdGenerator {
    private static int counter;
    private static int componentId;

    public static int generateId(){
        return counter++;
    }

    public static int generateComponentId(){
        return componentId++;
    }
}
