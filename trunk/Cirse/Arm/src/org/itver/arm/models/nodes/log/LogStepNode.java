/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.models.nodes.log;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.itver.arm.models.elements.Joint;
import org.itver.arm.models.log.LogStep;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.lookup.Lookups;

/**
 * Nodo con información de un LogStep,
 * @author pablo
 */
public final class LogStepNode extends AbstractNode
                         implements PropertyChangeListener{
    private static final String ARM = "Arm: ";
    private static final String JOINT = " Joint: ";
    private static final String VALUE = " Value: ";

    private LogStep step;

    public LogStepNode(LogStep step){
        super(Children.LEAF, Lookups.singleton(step));
        this.step = step;
        updateName();
        updateDisplayName();
        step.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.updateDisplayName();
        this.updateName();
    }

    @Override
    protected Sheet createSheet() {
        Sheet result = Sheet.createDefault();
        Sheet.Set props = Sheet.createPropertiesSet();
        try {
            Property time = new PropertySupport.Reflection(step, long.class, "time");
            time.setName("Time");
            props.put(time);
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        }

        result.put(props);

        return result;
    }



    private void updateName(){
        this.setName(step.toString());
    }

    private void updateDisplayName(){
        Joint joint = step.getJoint();
        this.setDisplayName(ARM + joint.getArm().getId() +
                            JOINT + joint.getId() +
                            VALUE + step.getValue());
    }

}
