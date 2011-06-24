/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.common.xml;

import com.sun.j3d.loaders.Scene;
import java.beans.PropertyChangeListener;

/**
 * Clase abstracta de la que heredan aquellas que leerán los distintos formatos
 * usados (en lenaguaje XML), una instancia de esta clase debe ser agregada en
 * el método {@link controls.xml.readers.ArmLoader#setInterpreter(Interpreter)
 * setInterpreter(Interpreter)}.
 * 
 * @author Pablo Antonio Guevara González
 *

 */
public abstract class Interpreter implements PropertyChangeListener{
    /**
     * Devuele la escena completa que ha sido interpretada desde el XML.
     * @return información del BranchGroup leído.
     * @see com.sun.j3d.loaders.Loader
     * @see com.sun.j3d.loaders.Scene
     */
    public abstract Scene getScene();
}
