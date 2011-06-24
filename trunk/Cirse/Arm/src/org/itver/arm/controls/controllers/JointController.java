/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.controls.controllers;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.itver.arm.models.elements.AngleType;
import org.itver.arm.models.elements.Axis;
import org.itver.arm.models.elements.ElementType;
import org.itver.arm.models.elements.Piece;
import org.itver.arm.views.gui.ArmControlTopComponent;
import org.itver.arm.views.gui.RotationPan;
import org.itver.arm.views.gui.RotationsPan;

/**
 *
 * @author pablo
 */
public class JointController implements PropertyChangeListener,
                                        ChangeListener{
    private static JointController instance;
    private Piece joint;
    private RotationsPan view;

    private JointController(){
        view = ArmControlTopComponent.findInstance().getRotationsPanel();
        view.addChangeListener(this);
    }

    public synchronized static JointController singleton(){
        if(instance == null)
            instance = new JointController();
        return instance;
    }

    /*
     * Del modelo a la vista
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Piece piece = (Piece) evt.getSource();
        if(piece == joint)
            updateView();
    }

    /*
     * De la vista al modelo.
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        Axis axes[] = Axis.values();
        for(Axis axis : axes){
            RotationPan panel = this.view.getPanelFromAxis(axis);
            if(panel.getValue() != (int)this.joint.getAngle(AngleType.joint, axis)
               && panel.isEnabled())
                this.joint.setAngle(AngleType.joint, axis, panel.getValue());
        }
        
    }

    /**
     * @return the joint
     */
    public Piece getJoint() {
        return joint;
    }

    /**
     * @param joint the joint to set
     */
    public synchronized void setJoint(Piece joint) {
//        if(joint.getType() == ElementType.joint){
            if(this.joint != null)
                this.joint.removeListener(this);
            this.joint = joint;
            this.joint.addListener(this);
            ArmControlTopComponent.findInstance().setJointId(joint.getId());
            updateView();
//        }
    }

    private synchronized void updateView() {
        Axis axes[] = Axis.values();
        for(Axis axis : axes){
            RotationPan panel = view.getPanelFromAxis(axis);
            if(joint.getAngle(AngleType.max, axis) ==
               joint.getAngle(AngleType.min, axis)){
                panel.setEnabled(false);
            }
            else{
                panel.setEnabled(true);
//                if(panel.getMax() != joint.getAngle(AngleType.max, axis))
                    panel.setMax((int) joint.getAngle(AngleType.max, axis));

//                if(panel.getMin() != joint.getAngle(AngleType.min, axis))
                    panel.setMin((int) joint.getAngle(AngleType.min, axis));

//                if(panel.getValue() != joint.getAngle(AngleType.joint, axis))
                    panel.setValue((int) joint.getAngle(AngleType.joint, axis));
            }
            panel.repaint();

        }
     }
    
}
