/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CategoryPalette.java
 *
 * Created on 20-mar-2011, 12:43:24
 */

package org.itver.gui.visual.palette;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;

/**
 *
 * @author pablo
 */
public final class CategoryPalette extends javax.swing.JPanel
                             implements ActionListener{

    private GridLayout layout;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private ItemPalette selectedItem;
    private String title;

    /** Creates new form CategoryPalette */
    private CategoryPalette() {
        initComponents();
    }

    public CategoryPalette(String title){
        this();
        this.setTitle(title);
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public void addItem(ItemPalette item){
        this.mainPanel.add(item);
        Dimension dims = this.mainPanel.getSize();
        dims.height += ItemPalette.MIN_LENGTH;
        this.mainPanel.setSize(dims);
        item.getButton().addActionListener(this);
        this.buttonGroup.add(item.getButton());
        this.layout.setRows(mainPanel.getComponentCount() / 3);
    }

    public ItemPalette getSelectedItem(){
        return this.selectedItem;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JToggleButton){
            JToggleButton button = (JToggleButton) e.getSource();
            if(button.getParent() instanceof ItemPalette){
                setSelectedItem((ItemPalette) button.getParent());
            }
        }

    }

    private void setSelectedItem(ItemPalette item){
        this.selectedItem = item;
        updateDescription();
    }

    private void updateDescription(){
        this.descriptionArea.setText("<html><b>" +
                selectedItem.getTitle() + "</b><br/>" +
                selectedItem.getDescription() + "</html>");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        descriptionArea = new javax.swing.JTextPane();

        setOpaque(false);
        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBackground(new java.awt.Color(238, 238, 238));
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setOpaque(false);

        mainPanel.setOpaque(false);
        mainPanel.setLayout(new java.awt.GridLayout(1, 3, 2, 2));
        jScrollPane1.setViewportView(mainPanel);
        layout = (GridLayout)mainPanel.getLayout();

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.BorderLayout());

        descriptionArea.setContentType(org.openide.util.NbBundle.getMessage(CategoryPalette.class, "CategoryPalette.descriptionArea.contentType_1")); // NOI18N
        descriptionArea.setEditable(false);
        descriptionArea.setText(org.openide.util.NbBundle.getMessage(CategoryPalette.class, "CategoryPalette.descriptionArea.text_1")); // NOI18N
        descriptionArea.setToolTipText(org.openide.util.NbBundle.getMessage(CategoryPalette.class, "CategoryPalette.descriptionArea.toolTipText")); // NOI18N
        descriptionArea.setOpaque(false);
        descriptionArea.setPreferredSize(new java.awt.Dimension(0, 100));
        jPanel2.add(descriptionArea, java.awt.BorderLayout.PAGE_START);

        add(jPanel2, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane descriptionArea;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables



}
