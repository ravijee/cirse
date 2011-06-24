/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.graphics.util;

/**
 * Clase que contiene los posibles tipos de componentes en el entorno de
 * simulación.
 * <br>
 * <ul>
 *  <li>Arm: Brazo robótico.</li>
 *  <li>Picable: Cualquier objeto que pueda ser sujetable por un brazo robótico.</li>
 *  <li>Furniture: Objetos decorativos del entorno de simulación.</li>
 * @author Karo
 */
public enum ComponentType {
    arm, pickable, furniture
}
