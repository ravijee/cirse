/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ItemPalette.java
 *
 * Created on 20-mar-2011, 12:13:58
 */

package org.itver.gui.visual.palette;

import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

/**
 *
 * @author pablo
 */
public final class ItemPalette extends javax.swing.JPanel {
    /*
     * title
     * image
     * description
     * file
     */
    public static final int MIN_LENGTH = 80;

    private final short TITLE = 0;
    private final short DESC = 1;
    private final short FILE = 2;
    private String[] vals;

    /** Creates new form ItemPalette */
    private ItemPalette() {
        initComponents();
        vals = new String[FILE + 1];
    }

    public ItemPalette(String title){
        this();
        this.setTitle(title);
    }
    
    public void addActionListener(ActionListener listener){
        this.button.addActionListener(listener);
    }
    
    public void removeActionListener(ActionListener listener){
        this.button.removeActionListener(listener);
    }
    public String getTitle(){
        return this.vals[TITLE];
    }

    public void setTitle(String title){
        this.vals[TITLE] = title;
        updateToolTip();
    }

    public void setImage(String path){
        
        this.button.setIcon(new ImageIcon(path));
    }

    public String getDescription(){
        return this.vals[DESC];
    }

    public void setDescription(String description){
        this.vals[DESC] = description;
        updateToolTip();
    }

    public String getFile(){
        return this.vals[FILE];
    }

    public void setFile(String path){
        this.vals[FILE] = path;
        this.button.setActionCommand(path);
    }

    public JToggleButton getButton(){
        return this.button;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        button = new javax.swing.JToggleButton();

        setOpaque(false);
        setLayout(new java.awt.BorderLayout());

        button.setIcon(new javax.swing.JLabel() {
            public javax.swing.Icon getIcon() {
                try {
                    return new javax.swing.ImageIcon(
                        new java.net.URL("http://www.certussoft.com/Images/Arm64.png")
                    );
                } catch (java.net.MalformedURLException e) {
                }
                return null;
            }
        }.getIcon());
        button.setText(org.openide.util.NbBundle.getMessage(ItemPalette.class, "ItemPalette.button.text")); // NOI18N
        add(button, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton button;
    // End of variables declaration//GEN-END:variables

    private void updateToolTip() {
        this.button.setToolTipText(vals[TITLE] + "\n\n" + vals[DESC]);

    }

}