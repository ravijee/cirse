/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Point3dEditorPanel.java
 *
 * Created on 1/03/2011, 08:13:30 PM
 */

package org.itver.common.propertyeditors.visual;

/**
 *
 * @author Karo
 */
public class Point3dEditorPanel extends javax.swing.JPanel {

    /** Creates new form Point3dEditorPanel */
    public Point3dEditorPanel() {
        initComponents();
        x.setPropertyName("X: ");
        y.setPropertyName("Y: ");
        z.setPropertyName("Z: ");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        crappyGap = new javax.swing.JPanel();
        x = new org.itver.common.propertyeditors.visual.GenericPanel();
        crappyGap2 = new javax.swing.JPanel();
        y = new org.itver.common.propertyeditors.visual.GenericPanel();
        crappyGap3 = new javax.swing.JPanel();
        z = new org.itver.common.propertyeditors.visual.GenericPanel();
        crappyGap4 = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        add(crappyGap);
        add(x);
        add(crappyGap2);
        add(y);
        add(crappyGap3);
        add(z);
        add(crappyGap4);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel crappyGap;
    private javax.swing.JPanel crappyGap2;
    private javax.swing.JPanel crappyGap3;
    private javax.swing.JPanel crappyGap4;
    private org.itver.common.propertyeditors.visual.GenericPanel x;
    private org.itver.common.propertyeditors.visual.GenericPanel y;
    private org.itver.common.propertyeditors.visual.GenericPanel z;
    // End of variables declaration//GEN-END:variables

}
