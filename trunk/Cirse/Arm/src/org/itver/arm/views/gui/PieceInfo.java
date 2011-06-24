/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PieceInfo.java
 *
 * Created on 23-nov-2010, 14:34:27
 */
package org.itver.arm.views.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.itver.arm.models.elements.AngleType;
import org.itver.arm.models.elements.Axis;
import org.itver.arm.models.elements.Piece;

/**
 *
 * @author pablo
 */
public class PieceInfo extends javax.swing.JPanel
        implements PropertyChangeListener {

    private Piece piece;

    /** Creates new form PieceInfo */
    public PieceInfo() {
        initComponents();
        updateForm();
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        updateForm();
        try{
            piece.addListener(this);
        }catch(NullPointerException ex){
            return;
        }
    }

    public final void updateForm() {
        if (piece == null) {
            return;
        }

        int angX = (int) piece.getAngle(AngleType.joint, Axis.x);
        int angY = (int) piece.getAngle(AngleType.joint, Axis.y);
        int angZ = (int) piece.getAngle(AngleType.joint, Axis.z);
        sldX.setEnabled(true);
        sldY.setEnabled(true);
        sldZ.setEnabled(true);
        
        sldX.setMinimum((int) piece.getAngle(AngleType.min, Axis.x));
        sldX.setMaximum((int) piece.getAngle(AngleType.max, Axis.x));
        if (sldX.getMaximum() == sldX.getMinimum()) {
            sldX.setEnabled(false);
        }

        sldY.setMinimum((int) piece.getAngle(AngleType.min, Axis.y));
        sldY.setMaximum((int) piece.getAngle(AngleType.max, Axis.y));
        if (sldY.getMaximum() == sldY.getMinimum()) {
            sldY.setEnabled(false);
        }

        sldZ.setMinimum((int) piece.getAngle(AngleType.min, Axis.z));
        sldZ.setMaximum((int) piece.getAngle(AngleType.max, Axis.z));
        if (sldZ.getMaximum() == sldZ.getMinimum()) {
            sldZ.setEnabled(false);
        }

        sldX.setValue(angX);
        sldY.setValue(angY);
        sldZ.setValue(angZ);
        txtId.setText(piece.getId() + "");
        txtType.setText(piece.getType().name());

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            setPiece((Piece) evt.getNewValue());
        } catch (java.lang.ClassCastException ex) {
            updateForm();
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtType = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        sldX = new javax.swing.JSlider();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        sldY = new javax.swing.JSlider();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        sldZ = new javax.swing.JSlider();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        jLabel4.setText("Id:");
        jPanel4.add(jLabel4);

        txtId.setEditable(false);
        jPanel4.add(txtId);

        jLabel5.setText("Type:");
        jPanel4.add(jLabel5);

        txtType.setEditable(false);
        jPanel4.add(txtType);

        add(jPanel4);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

        jLabel1.setText("X:");
        jPanel1.add(jLabel1);

        sldX.setMajorTickSpacing(90);
        sldX.setMaximum(360);
        sldX.setMinorTickSpacing(10);
        sldX.setPaintLabels(true);
        sldX.setPaintTicks(true);
        sldX.setValue(0);
        sldX.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldXStateChanged(evt);
            }
        });
        jPanel1.add(sldX);

        add(jPanel1);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.X_AXIS));

        jLabel2.setText("Y:");
        jPanel2.add(jLabel2);

        sldY.setMajorTickSpacing(90);
        sldY.setMaximum(360);
        sldY.setMinorTickSpacing(10);
        sldY.setPaintLabels(true);
        sldY.setPaintTicks(true);
        sldY.setValue(0);
        sldY.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldYStateChanged(evt);
            }
        });
        jPanel2.add(sldY);

        add(jPanel2);

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.X_AXIS));

        jLabel3.setText("Z:");
        jPanel3.add(jLabel3);

        sldZ.setMajorTickSpacing(90);
        sldZ.setMaximum(360);
        sldZ.setMinorTickSpacing(10);
        sldZ.setPaintLabels(true);
        sldZ.setPaintTicks(true);
        sldZ.setValue(0);
        sldZ.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldZStateChanged(evt);
            }
        });
        jPanel3.add(sldZ);

        add(jPanel3);
    }// </editor-fold>//GEN-END:initComponents

    private void sldXStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldXStateChanged
        piece.setAngle(AngleType.joint, Axis.x, sldX.getValue());
    }//GEN-LAST:event_sldXStateChanged

    private void sldYStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldYStateChanged
        piece.setAngle(AngleType.joint, Axis.y, sldY.getValue());
    }//GEN-LAST:event_sldYStateChanged

    private void sldZStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldZStateChanged
        piece.setAngle(AngleType.joint, Axis.z, sldZ.getValue());
    }//GEN-LAST:event_sldZStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSlider sldX;
    private javax.swing.JSlider sldY;
    private javax.swing.JSlider sldZ;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtType;
    // End of variables declaration//GEN-END:variables
}
