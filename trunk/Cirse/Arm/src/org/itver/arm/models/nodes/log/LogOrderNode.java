/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.models.nodes.log;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.itver.arm.models.log.LogOrder;
import org.itver.arm.models.log.LogStep;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 * Nodo con información de un LogOrder, contiene la clase para crear sus Node
 * hijos.
 * @author pablo
 */
public final class LogOrderNode extends AbstractNode
                          implements PropertyChangeListener{
    private LogOrder order;

    public LogOrderNode(LogOrder order){
        super(Children.LEAF, Lookups.singleton(order));
        this.order = order;
        this.order.setNode(this);
        updateChildren();
        updateName();
        updateDisplayName();
        order.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if(prop.equals(LogOrder.STEPS))
            updateChildren();
        else if(prop.equals(LogOrder.NUMBER))
            updateDisplayName();
//        else if(prop.equals(LogOrder.TIME))
        updateName();
    }

    private void updateChildren(){
        this.setChildren(new LogOrderChildren());
    }

    private void updateName(){
        this.setName(order.toString());
    }

    private void updateDisplayName(){
        this.setDisplayName("#" + this.order.getOrder());
    }


    private class LogOrderChildren extends Children.Keys{

        public LogOrderChildren(){
            this.setKeys(order.getSteps());
        }

        @Override
        protected Node[] createNodes(Object key) {
            if(key instanceof LogStep)
                return new Node[]{new LogStepNode((LogStep) key)};
            else
                return null;
        }

    }

}
