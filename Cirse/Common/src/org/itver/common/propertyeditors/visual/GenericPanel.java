/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GenericPanel.java
 *
 * Created on 1/03/2011, 08:16:42 PM
 */

package org.itver.common.propertyeditors.visual;

import java.awt.event.FocusListener;
import java.io.Serializable;

/**
 *
 * @author Karo
 */
public class GenericPanel extends javax.swing.JPanel implements Serializable{

//    /** Creates new form GenericPanel */
//    public GenericPanel(String propertyName){
//        property.setText(propertyName);
//        initComponents();
//    }

    public GenericPanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        property = new javax.swing.JLabel();
        propertyText = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(530, 25));
        setLayout(new java.awt.BorderLayout(10, 10));

        property.setText(org.openide.util.NbBundle.getMessage(GenericPanel.class, "GenericPanel.property.text")); // NOI18N
        add(property, java.awt.BorderLayout.LINE_START);

        propertyText.setText(org.openide.util.NbBundle.getMessage(GenericPanel.class, "GenericPanel.propertyText.text")); // NOI18N
        add(propertyText, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    public void addListener(FocusListener listener){
        this.propertyText.addFocusListener(listener);
    }
    
    public void removeListener(FocusListener listener){
        this.propertyText.removeFocusListener(listener);
    }

    public void setPropertyName(String propName){
        property.setText(propName);
    }

    public void setPropertyValue(String value){
        propertyText.setText(value);
    }

    public String getPropertyName(){
        return property.getText();
    }

    public String getPropertyValue(){
        return propertyText.getText();
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel property;
    private javax.swing.JTextField propertyText;
    // End of variables declaration//GEN-END:variables

}