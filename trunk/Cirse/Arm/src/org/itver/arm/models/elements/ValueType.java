/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.models.elements;

/**
 * Enum con los posibles tipos de angulo.
 *
 * @author Pabo Antonio Guevara González
 */
public enum ValueType {
    /**
     * Ángulo que tendra cada {@link models.elements.Piece pieza} en alguno
     * de los 3 ejes, sin afectar la posición ni orientación de los demás.
     */
    current,
    /**
     * Ángulo menor que puede tener la {@link models.elements.Piece pieza},
     * siempre que sea tipo {@link models.elements.ElementType#joint joint}.
     */
    min,
    /**
     * Ángulo mayor que puede tener la {@link models.elements.Piece pieza},
     * siempre que sea tipo {@link models.elements.ElementType#joint joint}.
     */
    max
}
