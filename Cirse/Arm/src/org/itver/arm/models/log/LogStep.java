/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.models.log;

import org.itver.arm.models.elements.Joint;

/**
 * Modelo con información sobre el cambio en el valor de una
 * {@link Joint Articulación}. Guarda el valor y el tiempo que debería pasar
 * antes de llegar a él durante el proceso de simulación.
 * @author pablo
 */
public class LogStep extends Model{
    /**
     * Nombre de la propiedad que cambia al asignar un nuevo Brazo.
     */
    public static final String ARM = "arm";
    /**
     * Nombre de la propiedad que cambia al asignar una nueva Articulación
     */
    public static final String JOINT = "joint";
    /**
     * Nombre de la propiedad que cambia al asignar un valor sobre la
     * Articulación
     */
    public static final String VALUE = "value";

    private long time;
    private Joint joint;
    private double value;

    private LogStep(){

    }

    /**
     * Constructor default, se debe asignar la Articulación y Brazo.
     * @param arm Brazo ligado al Paso.
     * @param joint Articulación ligada al Paso.
     */
    public LogStep(Joint joint){
        this(joint, 0, 10000);
    }
    /**
     * Constructor que asigna un valor y tiempo aparte del Brazo y Articulación.
     * @param arm Brazo ligado al Paso.
     * @param joint Articulación ligada al Paso.
     * @param value valor que debe alcanzar la articulación.
     * @param time Tiempo en milisegundos que deben pasar antes de llegar al
     * valor
     */
    public LogStep(Joint joint, double value, long time){
        this.time = time;
        this.joint = joint;
        this.value = value;
    }

    /**
     * Obtiene el tiempo del Paso.
     * @return el tiempo del Paso, en milisegundos.
     */
    public long getTime() {
        return time;
    }

    /**
     * Asigna el tiempo al Paso
     * @param time el tiempo de este paso, en milisegundos.
     */
    public void setTime(long time) {
        long old = this.time;
        this.time = time;
        this.pcs.firePropertyChange(LogOrder.TIME, old, this.time);
    }

    /**
     * Obtiene la Articulación ligada al Paso.
     * @return la Articulación ligada al Paso.
     */
    public Joint getJoint() {
        return joint;
    }

    /**
     * Asigna una nueva Articulación con el Paso.
     * @param joint la nueva Articulación ligada con el Paso.
     */
    public void setJoint(Joint joint) {
        Joint old = this.joint;
        this.joint = joint;
        this.pcs.firePropertyChange(JOINT, old, this.joint);
    }

    /**
     * Obtiene el valor a alcanzar por la Articulación.
     * @return el valor a alcanzar por la Articulación.
     */
    public double getValue() {
        return value;
    }

    /**
     * Asigna un nuevo valor a alcanzar por la Articulación.
     * @param value el nuevo valor a alcanzar por la Articulación.
     */
    public void setValue(double value) {
        double old = this.value;
        this.value = value;
        this.pcs.firePropertyChange(VALUE, old, this.value);
    }

}
