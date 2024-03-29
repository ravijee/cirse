/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RotationsPan.java
 *
 * Created on 24-feb-2011, 20:01:10
 */

package org.itver.arm.views.gui;

import javax.swing.event.ChangeListener;
import org.itver.arm.models.elements.Axis;

/**
 * Panel que engloba 3 {@link RotationPan RotationPan}, permitiendo asignar
 * oyentes a todos.
 * @author pablo
 */
public final class RotationsPan extends javax.swing.JPanel {
    private RotationPan[] panels = new RotationPan[Axis.values().length];

    /**
     * Constructor default para un componente con 3 RotationPan.
     */
    public RotationsPan() {
        initComponents();
        initPanels();
    }

    /**
     * {@link RotationPan#addChangeListener(javax.swing.event.ChangeListener)
     * Agrega} un oyente a cada uno de los RotationPan del panel..
     * @param listener Oyente a agregar en cada componente del panel.
     */
    public void addChangeListener(ChangeListener listener){
        for(int i = 0; i < panels.length; i++)
            panels[i].addChangeListener(listener);
        this.jointBox.addChangeListener(listener);
    }

    /**
     * {@link RotationPan#removeChangeListener(javax.swing.event.ChangeListener)
     * Retira} un oyente a cada uno de los RotationPan del panel..
     * @param listener Oyente a retirar en cada componente del panel.
     */
    public void removeChangeListener(ChangeListener listener){
        for(int i = 0; i < panels.length; i++)
            panels[i].removeChangeListener(listener);
        this.jointBox.removeChangeListener(listener);
    }

    /**
     * Obtiene el RotationPan ligado al Eje especifico.
     * @param axis Eje al que está ligado el Panel RotationPan
     * @return RotationPan ligado al Eje.
     */
    public RotationPan getPanelFromAxis(Axis axis){
        return this.panels[axis.ordinal()];
    }

    /**
     * Determina si el checkbox está seleccionado o no.
     * @return {@code true} si el checkbox está actualmente seleccionado.
     */
    public boolean isJointBoxSelected(){
        return this.jointBox.isSelected();
    }

    /**
     * Asigna el estado de selección del checkbox.
     * @param selected Estado de selección del checkbox
     */
    public void setJointBoxSelected(boolean selected){
        this.jointBox.setSelected(selected);
    }

    /**
     * Activa o desactiva el checkbox, quitando la selección del mismo.
     * @param enabled activación del checkbox.
     */
    public void setJointBoxEnabled(boolean enabled){
        this.jointBox.setEnabled(enabled);
        this.setJointBoxSelected(false);
    }
    /**
     * Determina si el checkbox se encuentra o no activado.
     * @return estado actual del checkbox.
     */
    public boolean isJointBoxEnabled(){
        return this.jointBox.isEnabled();
    }

    private void initPanels(){
        for(int i = 0; i < this.panels.length; i++){
            this.panels[i] = new RotationPan(Axis.values()[i]);
            this.panels[i].setEnabled(false);
            this.add(this.panels[i]);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jointBox = new javax.swing.JCheckBox();

        setOpaque(false);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jointBox.setText(org.openide.util.NbBundle.getMessage(RotationsPan.class, "RotationsPan.jointBox.text")); // NOI18N
        jointBox.setEnabled(false);
        add(jointBox);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jointBox;
    // End of variables declaration//GEN-END:variables

}
