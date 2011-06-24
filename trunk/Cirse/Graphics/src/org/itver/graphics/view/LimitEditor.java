/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LimitEditor.java
 *
 * Created on 25/11/2010, 09:56:37 AM
 */

package org.itver.graphics.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.itver.graphics.model.EnvironmentLimits;

/**
 *
 * @author Karo
 */
public class LimitEditor extends javax.swing.JPanel implements PropertyChangeListener {
    private EnvironmentLimits ev;
    /** Creates new form LimitEditor */
    private LimitEditor() {
        initComponents();
        setLimitInfo();
    }

    public LimitEditor(EnvironmentLimits ev){
        this.ev = ev;
        this.ev.addPropertyChangeListener(this);
        initComponents();
        setLimitInfo();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ancho = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        alto = new javax.swing.JTextField();
        profundidad = new javax.swing.JTextField();
        grosor = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        ancho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anchoActionPerformed(evt);
            }
        });

        jLabel1.setText("Ancho");

        jLabel2.setText("Alto");

        jLabel3.setText("Profundidad");

        jLabel4.setText("Grosor");

        alto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                altoActionPerformed(evt);
            }
        });

        profundidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profundidadActionPerformed(evt);
            }
        });

        grosor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grosorActionPerformed(evt);
            }
        });

        jButton1.setText("Aumentar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Disminuir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(54, 54, 54)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(profundidad, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                            .addComponent(alto, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                            .addComponent(ancho, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                            .addComponent(grosor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ancho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(alto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(profundidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(grosor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void anchoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anchoActionPerformed
        ev.setWidth(Float.parseFloat(ancho.getText()));
    }//GEN-LAST:event_anchoActionPerformed

    private void altoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_altoActionPerformed
        ev.setHeight(Float.parseFloat(alto.getText()));
    }//GEN-LAST:event_altoActionPerformed

    private void profundidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profundidadActionPerformed
        ev.setDeepness(Float.parseFloat(profundidad.getText()));
    }//GEN-LAST:event_profundidadActionPerformed

    private void grosorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grosorActionPerformed
        ev.setThickness(Float.parseFloat(grosor.getText()));
    }//GEN-LAST:event_grosorActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ev.setHeight(ev.getHeight()+5);
        ev.setWidth(ev.getWidth()+5);
        ev.setDeepness(ev.getDeepness()+5);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        ev.setHeight(ev.getHeight()-5);
        ev.setWidth(ev.getWidth()-5);
        ev.setDeepness(ev.getDeepness()-5);
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField alto;
    private javax.swing.JTextField ancho;
    private javax.swing.JTextField grosor;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField profundidad;
    // End of variables declaration//GEN-END:variables

    public void propertyChange(PropertyChangeEvent evt) {
        ev = (EnvironmentLimits) evt.getSource();
        ancho.setText(""+ev.getWidth());
        alto.setText(""+ev.getHeight());
        profundidad.setText(""+ev.getDeepness());
        grosor.setText(""+ev.getThickness());
    }

    private void setLimitInfo(){
        try{
            ancho.setText(""+ev.getWidth());
            alto.setText(""+ev.getHeight());
            profundidad.setText(""+ev.getDeepness());
            grosor.setText(""+ev.getThickness());
        }catch(NullPointerException ex){
            ancho.setText("0");
            alto.setText("0");
            profundidad.setText("0");
            grosor.setText("0");
        }
    }

}
