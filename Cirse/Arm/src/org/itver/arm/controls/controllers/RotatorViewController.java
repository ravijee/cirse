/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.controls.controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.itver.arm.models.elements.Axis;
import org.itver.arm.models.elements.Joint;
import org.itver.arm.models.elements.Piece;
import org.itver.arm.models.elements.ValueType;
import org.itver.arm.views.gui.RotationPan;
import org.itver.arm.views.gui.RotationsPan;
import org.itver.arm.views.gui.RotatorViewTopComponent;

/**
 * Controlador entre las Piezas y la Vista principal de su orientación.
 * Automáticamente actualiza la vista en base a la pieza seleccionada en el
 * navegador del Entorno.
 * <b>Clase singleton</b>
 * @author pablo
 * @see RotationsPan Vista Principal.
 */
public final class RotatorViewController implements PropertyChangeListener,
                                                    ChangeListener{
    private static RotatorViewController instance;

    public static final float   MOTION_RELATION = 0.01f;

    private RotationsPan view = RotatorViewTopComponent.findInstance().getRotationsPanel();
    private Piece model;

    private RotatorViewController(){
    }

    public synchronized static RotatorViewController singleton(){
        if(instance == null)
            instance = new RotatorViewController();
        return instance;
    }

    /**
     * Escucha los cambios en la pieza seleccionada y actualiza la vista
     * en caso de haber cambios.
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(Joint.AXIS) || evt.getPropertyName().equals(Joint.JOINT_VALUE))
            this.updateMainView();
    }

    /**
     * Escucha los cambios en la vista y actualiza la Pieza seleccionada.
     * @param evt
     */
    @Override
    public void stateChanged(ChangeEvent evt) {
        if(model != null){
            if(evt.getSource() instanceof JCheckBox)
                updateMainView();
            else{
                model.removeListener(instance);
                Axis axes[] = Axis.values();
                double[] vals = new double[axes.length];
                for(int i = 0 ; i < axes.length; i++){
                    RotationPan panel = view.getPanelFromAxis(axes[i]);
                    vals[i] = panel.getValue();
                }
                if(view.isJointBoxSelected()){
                    Joint joint = (Joint)model;
                    double value = vals[joint.getAxis().ordinal()];
                    if(!joint.isMotion())
                        value *= MOTION_RELATION;
                    JointController.singleton().setAngleVal(joint,
                                                              ValueType.current,
                                                              value);
                }
                else
                    model.setOrientation(vals);
                model.addListener(instance);
            }
        }
    }

    /**
     * Establece la Pieza a representar en la vista.
     * @param piece la Pieza a mostrar en la vista.
     */
    public void setModel(Piece piece) {
        if(model != null)
            model.removeListener(instance);
        this.setSelectedPiece(piece);
        if(piece instanceof Joint)
            view.setJointBoxSelected(true);
    }

    private void setSelectedPiece(Piece piece){
        this.model = piece;
        view.removeChangeListener(instance);
        view.setJointBoxEnabled(model instanceof Joint);
        if(model == null){
            RotatorViewTopComponent.findInstance().setTitle("");
            for(Axis axis: Axis.values())
                view.getPanelFromAxis(axis).setEnabled(false);
        }
        else{
            updateMainView();
            model.addListener(instance);
        }
    }

    private void updateMainView() {
        if(model == null)
            return;
        view.removeChangeListener(instance);
        model.removeListener(instance);
        RotatorViewTopComponent.getDefault().setTitle((model instanceof Joint ?
                                                                       "Joint" :
                                                                       "Piece")
                                                  + " [" + model.getId() + "]");
        for (Axis axis : Axis.values()) {
            RotationPan axisView = view.getPanelFromAxis(axis);
            axisView.setEnabled(true);
            double val = model.getOrientation(axis);
            double min = 0;
            double max = 360;
            if(view.isJointBoxSelected()){
                Joint joint = (Joint)model;
                axisView.setEnabled(joint.getAxis() == axis);
                val = joint.getJointValue(ValueType.current) * (joint.isMotion() ? 1 : 1 / MOTION_RELATION);
                min = joint.getJointValue(ValueType.min);
                max = joint.getJointValue(ValueType.max);
            }
            axisView.setMax(max);
            axisView.setMin(min);
            axisView.setValue(val);
        }
        view.addChangeListener(instance);
        model.addListener(instance);
    }
}
