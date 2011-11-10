/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.models.log;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Clase con soporte para avisar el cambio de los valores de propiedades.
 * @author pablo
 */
public abstract class Model {
    protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Agrega un oyente a los cambios de propiedades.
     * @param listener Oyente de cambios de propiedades.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener){
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * Quita un oyente de los cambios de propiedades.
     * @param listener Oyente a quitar de los cambios de propiedades. No hace
     * nada si el oyente no estaba previamente asignado.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener){
        this.pcs.removePropertyChangeListener(listener);
    }
}
