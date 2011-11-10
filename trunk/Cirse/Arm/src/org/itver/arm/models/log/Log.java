/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.models.log;

import java.util.ArrayList;
import org.itver.arm.models.elements.Element;


/**
 * Modelo contenedor de todos Órdenes, los cuales se ejecutarán de manera
 * secuencial.
 * <b>Clase singleton</b>
 * @author pablo
 */
public final class Log extends Model{
    /**
     * Nombre de la propiedad que cambia al agregar o retirar una
     * {@link LogOrder Orden}.
     */
    public static final String ORDER = "order";
    private static Log instance;
    private ArrayList<LogOrder> orders;

    private Log(){
        this.orders = new ArrayList<LogOrder>();
    }

    public synchronized static Log singleton(){
        if(instance == null)
            instance = new Log();
        return instance;
    }

    /**
     * Agrega una {@link LogOrder Orden} al Log principal. Cambiando su número de
     * acuerdo a como fue incluído. No hace nada si el Orden ya está incluído.
     * @param order Nueva orden a agregar en el Log. 
     */
    public void addOrder(LogOrder order){
        if(this.orders.contains(order))
            return;
        if(order.getOrder() == Element.DEFAULT_ID){
            this.orders.add(order);
            order.setOrder(this.orders.size() - 1);
        }
        else
            this.orders.add(order.getOrder(), order);
        this.pcs.firePropertyChange(ORDER, null, order);
    }

    /**
     * Retira una Orden del Log.
     * @param order Orden a retirar del log.
     */
    public void removeOrder(LogOrder order){
        this.orders.remove(order);
        this.pcs.firePropertyChange(ORDER, order, null);
    }

    /**
     * Devuelve una copia del arreglo de {@link LogOrder Órdenes} con todas
     * aquellas que están incluídas en el Log.
     * @return Copia del arreglo con las órdenes agregadas en el Log.
     */
    public LogOrder[] getOrders(){
        LogOrder[] result = new LogOrder[this.orders.size()];
        return this.orders.toArray(result);
    }

}
