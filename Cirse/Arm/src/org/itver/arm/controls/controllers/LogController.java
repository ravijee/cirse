/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.controls.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JTextField;
import org.itver.arm.controls.behaviors.JointValueInterpolator;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.models.elements.ElementType;
import org.itver.arm.models.elements.Joint;
import org.itver.arm.models.elements.Piece;
import org.itver.arm.models.elements.ValueType;
import org.itver.arm.models.log.Log;
import org.itver.arm.models.log.LogOrder;
import org.itver.arm.models.log.LogStep;
import org.itver.arm.views.gui.LogNavigatorTopComponent;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 * Controlador del {@link Log Log} y sus clases relacionadas: 
 * {@link LogOrder LogOrder} y {@link LogStep LogStep}. Incluye los métodos
 * necesarios para recorrer los pasos y órdenes.
 * <b>Clase singleton</b>
 * @author pablo
 */
public final class LogController implements PropertyChangeListener,
                                            ActionListener{
    private static LogController instance;
    private ExplorerManager manager =
            LogNavigatorTopComponent.findInstance().getExplorerManager();
    private Node[] selectedNodes;

    private LogController(){
        this.manager.addPropertyChangeListener(this);
    }

    public static synchronized LogController singleton(){
        if(instance == null)
            instance = new LogController();
        return instance;
    }

    /**
     * Recorre todos los Órdenes del Log y sus respectivos Pasos.
     * @param log
     */
    public void walk(final Log log){
        manager.removePropertyChangeListener(this);
        for (LogOrder order : log.getOrders()) {
            try {
                walk(order);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            } catch(PropertyVetoException pve){
                Exceptions.printStackTrace(pve);
            }
        }
        manager.addPropertyChangeListener(this);


    }

    /**
     * Recorre un LogOrder, ejecutando sus Pasos y esperando el
     * {@link LogOrder#getTime() tiempo} especificado.
     * @param order Orden a recorrer
     * @throws InterruptedException
     */
    public void walk(LogOrder order) throws InterruptedException, PropertyVetoException{
        manager.setSelectedNodes(new Node[]{order.getNode()});
        long currentTime = System.currentTimeMillis();
        for(LogStep step:order.getSteps())
            walk(step);
        long diference = System.currentTimeMillis() - currentTime;
        if(diference < order.getTime())
            Thread.sleep(order.getTime() - diference);
        this.updateJoints(order);

    }

    /**
     * Ejecuta un LogStep especifico, tomando el joint que este afecta como
     * punto de partida
     * @param step Paso a ejecutar
     */
    public synchronized void walk(LogStep step){
        Joint joint = step.getJoint();
        JointValueInterpolator interpolator = joint.getInterpolator();
        interpolator.setTo(step.getValue());
        interpolator.setTime(step.getTime());
        interpolator.setEnable(true);
        interpolator.start();
    }

    /**
     * Crea un LogStep de acuerdo a la pieza especificada
     * @param arm Brazo donde se encuentra la Articulación.
     * @param joint Articulación a modificar en el LogStep.
     * @return nuevo LogStep con el valor actual de la Articulación específica
     * como valor y de 5 segundos de duración.
     */
    public LogStep createStep(Joint joint){
        LogStep result = new LogStep(joint,
                             joint.getJointValue(ValueType.current),
                             5000);

        return result;
    }

    /**
     * Agrega en un LogOrder con tantos Pasos como Articulaciones tenga. Cada paso
     * es creado con los valores de cada Articulación, de acuerdo a
     * {@link #createStep(org.itver.arm.models.elements.Joint) createStep()}.
     * <br/>
     * Si no se especifica un LogOrder, crea uno nuevo.
     * @param arm Brazo a partir del cual se creará un Orden
     * @param order Orden donde se añadirán los Pasos respectivos a las
     * Articulaciones del Brazo. {@code null} para crear uno nuevo.
     * @return Orden con la lista de Pasos acordes a las Articulaciones del
     * Brazo.
     */
    public LogOrder createOrder(Arm arm, LogOrder order){
        if(order == null)
            order = new LogOrder();
        for(Piece piece : arm.getNext())
            if(piece instanceof Joint)
                order = createOrder((Joint)piece, order);
        return order;
    }

    /**
     * Agrega en un LogOrder con tantos Pasos como Articulaciones tenga. Cada paso
     * es creado con los valores de cada Articulación, de acuerdo a
     * {@link #createStep(org.itver.arm.models.elements.Joint) createStep()}.
     * <br/>
     * Si no se especifica un LogOrder, crea uno nuevo.
     * @param joint Articulación a partir de la cual se crearán los Pasos en el
     * Oren.
     * @param order Orden donde se añadirán los Pasos respectivos a las
     * Articulaciones del Brazo. {@code null} para crear uno nuevo.
     * @return Orden con la lista de Pasos acordes a las Articulaciones del
     * Brazo.
     */
    public LogOrder createOrder(Joint joint, LogOrder order){
        if(order == null)
            order = new LogOrder();
        double lastVal = this.getLastValue(joint);
        if(lastVal != joint.getJointValue(ValueType.current)){
            LogStep step = this.createStep(joint);
            order.addStep(step);
        }
        for(Piece piece:joint.getNext())
            if(piece instanceof Joint)
                order = createOrder((Joint)piece, order);
        return order;
    }

    /**
     * Agregar el LogStep especifico al LogOrder seleccionado en el navegador,
     * si no hay uno seleccionado creará uno nuevo
     * @param step Paso a agregar en el Orden seleccionado.
     */
    public void addStep(LogStep step){
        if(step == null)
            return;
        LogOrder order = this.getSelectedOrder();
        order.addStep(step);
        Log.singleton().addOrder(order);
    }

    /**
     * obtiene el LogOrder seleccionado en el LogNavigator
     * @return el LogOrder seleccionado, regresa uno nuevo si no hay uno
     * seleccionado.
     */
    public LogOrder getSelectedOrder(){
        LogOrder result = null;
        if(selectedNodes != null && selectedNodes.length == 1)
            result = selectedNodes[0].getLookup().lookup(LogOrder.class);
        if(result == null)
            result = new LogOrder();
        return result;
    }

    /**
     * Devuelve un LogOrder con los valores de apertura de la mano, tomando
     * cada Joint siguiente a cualquier pieza tipo palm. Se creará un nuevo
     * LogStep por cada Articulación a partir de la palma en el Brazo.
     * @param arm Brazo desde el que se creará un Orden.
     * @param percent Porcentaje de apertura de la mano.
     * @return Nuevo LogOrder con los pasos acordes al porcentaje especificado
     * para la mano.
     */
    public LogOrder manipulateHand(Arm arm, float percent){
        LogOrder result = new LogOrder();
        Piece palms[] = PieceController.singleton().getPiecesByType(arm, ElementType.palm);
        for(Piece palm : palms){
            Joint container = palm.getJoint();
            for(Piece finger : container.getNext())
                if(finger instanceof Joint){
                    Joint j = (Joint)finger;
                    double min = j.getJointValue(ValueType.min);
                    double max = j.getJointValue(ValueType.max);
                    double length = max - min;
                    double value = length * percent + min;
                    LogStep step = new LogStep(j, value, 5000);
                    result.addStep(step);
                }
        }
        return result;
    }

    /**
     * Escucha los cambios en el {@link LogNavigatorTopComponent LogNavigatorTopComponent}
     * para actualizar un joint de acuerdo al LogOrder seleccionado
     *
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(ExplorerManager.PROP_SELECTED_NODES) &&
           evt.getSource() == manager){
            selectedNodes = manager.getSelectedNodes();
            if(selectedNodes.length == 1){
                Lookup lookup = selectedNodes[0].getLookup();
                LogStep step = lookup.lookup(LogStep.class);
                LogOrder order = lookup.lookup(LogOrder.class);
                updateJoints(order);
                updateJoint(step);
            }

        }

    }

    @Override
    public void actionPerformed(ActionEvent ev){
        if(ev.getSource() instanceof JTextField){
            JTextField field = (JTextField) ev.getSource();
            LogStep step = this.parseLogStep(field.getText());
            addStep(step);
            field.setText("");
        }
    }

    /**
     * <p>Convierte en cadena de datos en un LogStep. La cadena debe tener el
     * siguiente formato:</p>
     * {@code
    arm:int,
    joint:int,
    value:double,
    time:long,
     * }
     * @param data Cadena con loa valores del LogStep.
     * @return LogStep creado con los valores dados. {@code null} si no se
     * reconoce el formato.
     */
    public LogStep parseLogStep(String data){
        if(!(data.matches("arm:[\\s]*[0-9]{1,5},[\\s]*"
                        + "joint:[\\s]*[0-9]{1,5},[\\s]*"
                        + "value:[\\s]*[0-9]+\\.?[0-9]*,[\\s]*"
                        + "time:[\\s]*[0-9]{1,12}")))
            return null;
        LogStep result = null;
        HashMap<String, String> map = new HashMap<String, String>();
        for(String line:data.split(",[\\s]*", 4)){
            String keys[] = line.split(":[\\s]*", 2);
            map.put(keys[0], keys[1]);
        }
        Arm arm = ArmController.singleton().getArmById(
                                            Integer.parseInt(
                                            map.get("arm")));
        if(arm != null){
            Joint joint = (Joint)PieceController.singleton().getPieceById(
                                                             arm,
                                                             Integer.parseInt(
                                                             map.get("joint")));
            if(joint != null){
                result = new LogStep(joint);
                result.setValue(Double.parseDouble(map.get("value")));
                result.setTime(Long.parseLong(map.get("time")));
            }
        }
        return result;
    }
    /**
     * <p>Convierte una cadena de datos en una serie de LogOrder.
     * El formato de la cadena debe estar representada de la siguiente manera:</p>
     * {@code 
     order: [cadena LogStep]; [cadena LogStep]; ...
     * }
     * @param data
     * @return
     */
    public LogOrder[] parseLogOrders(String data){
        if(!(data.matches("order:[\\s]*[.]*")))
            return null;
        ArrayList<LogOrder> orders = new ArrayList<LogOrder>();
        for(String d : data.split("order:[\\s]*"))
            orders.add(this.parseLogOrder(d));
        LogOrder[] result = new LogOrder[orders.size()];
        return orders.toArray(result);
    }

    private  LogOrder parseLogOrder(String data){
        LogOrder result = new LogOrder();
        for(String d : data.split("[\\s]*;[\\s]*"))
            result.addStep(this.parseLogStep(d));
        return result;
    }

    private void updateJoint(LogStep step){
        if(step != null){
            Joint joint = step.getJoint();
            joint.removeListener(RotatorViewController.singleton());
            joint.setJointValue(ValueType.current, step.getValue());
        }

    }

    private void updateJoints(LogOrder order){
        if(order != null)
            for(LogStep step : order.getSteps())
                this.updateJoint(step);        
    }
    
    private double getLastValue(Joint joint){
        double lastVal = -1;
        Log log = Log.singleton();
        if(log.getOrders().length > 0){
            LogOrder last = log.getOrders()[log.getOrders().length - 1];
            for(LogStep lastStep : last.getSteps())
                if(lastStep.getJoint() == joint)
                    lastVal = lastStep.getValue();}
        return lastVal; 
        
    }

}
