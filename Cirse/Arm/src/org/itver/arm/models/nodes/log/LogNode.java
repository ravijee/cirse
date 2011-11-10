/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.models.nodes.log;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.itver.arm.models.log.Log;
import org.itver.arm.models.log.LogOrder;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 * Nodo con un información del Log, incluye la clase para crear sus Node hijos.
 * @author pablo
 */
public final class LogNode extends AbstractNode
                     implements PropertyChangeListener{
    private static LogNode instance;
    public static final String LOG = "Log";
    private Log log;

    private LogNode(Log log){
        super(Children.LEAF, Lookups.singleton(log));
        this.log = log;
        updateChildren();
        updateName();
        updateDisplayName();
        log.addPropertyChangeListener(this);
    }

    public synchronized static LogNode singleton(){
        if(instance == null)
            instance = new LogNode(Log.singleton());
        return instance;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(Log.ORDER))
            updateChildren();
    }

    private void updateChildren(){
        this.setChildren(new ChildrenNode());
    }

    private void updateName(){
        this.setName(log.toString());
    }

    private void updateDisplayName(){
        this.setDisplayName(LOG);
    }


    private class ChildrenNode extends Children.Keys{

        public ChildrenNode(){
            this.setKeys(log.getOrders());
        }

        @Override
        protected Node[] createNodes(Object key) {
            if(key instanceof LogOrder)
                return new Node[]{new LogOrderNode((LogOrder) key)};
            else
                return null;
        }

    }

}
