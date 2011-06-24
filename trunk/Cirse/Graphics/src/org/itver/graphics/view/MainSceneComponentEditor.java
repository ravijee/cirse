/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainSceneComponentEditor.java
 *
 * Created on 7/12/2010, 10:04:29 AM
 */

package org.itver.graphics.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.itver.arm.models.elements.AngleType;
import org.itver.arm.models.elements.Element;
import org.itver.arm.models.elements.Piece;
import org.itver.common.util.Converter;
import org.itver.graphics.model.EnvironmentLimits;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.model.Universe;

/**
 *
 * @author Karo
 */

public class MainSceneComponentEditor extends javax.swing.JPanel implements PropertyChangeListener{
    private EnvironmentLimits limitSelection;
    private MainSceneComponent componentSelection;
    private Piece selectedPiece;

    /** Creates new form MainSceneComponentEditor */
    public MainSceneComponentEditor() {
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

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(255, 300));
        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(255, 300));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Propiedad", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setAutoscrolls(false);
        table.setPreferredSize(new java.awt.Dimension(100, 300));
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(MainSceneComponentEditor.class, "MainSceneComponentEditor.table.columnModel.title0")); // NOI18N
        table.getColumnModel().getColumn(1).setHeaderValue(org.openide.util.NbBundle.getMessage(MainSceneComponentEditor.class, "MainSceneComponentEditor.table.columnModel.title1")); // NOI18N

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {
        if(!Boolean.valueOf(evt.getNewValue().toString()) && 
                evt.getPropertyName().toLowerCase().equals("selected"))
            return;
        if(evt.getSource() instanceof MainSceneComponent){
//            if(evt.getPropertyName().equals("Selected")){
//                if(Boolean.valueOf(evt.getNewValue().toString())){
                    componentSelection = (MainSceneComponent) evt.getSource();
                    System.out.println("instanceof MSC:  "+componentSelection.isSelected());
                    getMainSceneComponentValues();
//                }
//            }
        }
        if(evt.getSource() instanceof EnvironmentLimits){
//                if(evt.getPropertyName().equals("Selected")){
//                    if(Boolean.valueOf(evt.getNewValue().toString())){
                        limitSelection = (EnvironmentLimits) evt.getSource();
                        System.out.println("instanceof EL: "+limitSelection.isSelected());
                        getEnvironmentLimitsValues();
//                    }
//                }
        }
        if(evt.getSource() instanceof Element){
//            if(evt.getPropertyName().equals("selected")){
//                if(Boolean.valueOf(evt.getNewValue().toString())){
                    selectedPiece = (Piece) evt.getSource();
                    System.out.println("instanceof Piece"+selectedPiece.isSelected());
                    getPieceValues();
//                }
//            }
        }
    }


    public void getMainSceneComponentValues(){
        boolean [] editable = {false, true, false, true, true, true, false};
        DefaultTableModel tableModel = this.createPropertyTableModel(MainSceneComponent.getFieldNames().length, editable);
        for (int i = 0; i < MainSceneComponent.getFieldNames().length; i++) {
            tableModel.setValueAt(MainSceneComponent.getFieldNames()[i], i, 0);
        }
        tableModel.setValueAt(componentSelection.getId(),           0, 1);
        tableModel.setValueAt(componentSelection.getComponentName(),         1, 1);
        tableModel.setValueAt(componentSelection.getType(),         2, 1);
        tableModel.setValueAt(componentSelection.getPosition().toString().replace("(", "").replace(")", ""),     3, 1); ////Quitar estos replace que////////////cambian el formato de la posicion.
        tableModel.setValueAt(Converter.doubleArrayToString(
                              componentSelection.getRotation()),    4, 1);
        tableModel.setValueAt(componentSelection.getScale(),        5, 1);
        tableModel.setValueAt(componentSelection.getSource(),       6, 1);
        tableModel.addTableModelListener(new TableListener());
        table.setModel(tableModel);

    }

    public void getEnvironmentLimitsValues(){
        boolean [] editable = {false, true, true, true, true};
//        DefaultTableModel tableModel = this.createPropertyTableModel(EnvironmentLimits.getFieldNames().length, editable);
//        for (int i = 0; i < EnvironmentLimits.getFieldNames().length; i++) {
//            tableModel.setValueAt(EnvironmentLimits.getFieldNames()[i], i, 0);
//        }
//        tableModel.setValueAt("Limits", 0, 1);
//        tableModel.setValueAt(limitSelection.getWidth()*2, 1, 1);
//        tableModel.setValueAt(limitSelection.getHeight()*2, 2, 1);
//        tableModel.setValueAt(limitSelection.getDeepness()*2, 3, 1);
//        tableModel.setValueAt(limitSelection.getThickness()*2, 4, 1);
//        tableModel.addTableModelListener(new TableListener());
//        table.setModel(tableModel);

    }

    public void getPieceValues(){
        boolean editable [] = {false, true, true, true, true, true, true, false};
        DefaultTableModel tableModel = createPropertyTableModel(Piece.getFieldNames().length, editable);
        for (int i = 0; i < Piece.getFieldNames().length; i++) {
              tableModel.setValueAt(Piece.getFieldNames()[i], i, 0);
        }
        tableModel.setValueAt(selectedPiece.getId(),                      0, 1);
        tableModel.setValueAt(selectedPiece.getType(),                    1, 1);
        tableModel.setValueAt(selectedPiece.getPosition().toString().replace("(", "").replace(")", ""),                2, 1);
        tableModel.setValueAt(Converter.doubleArrayToString(selectedPiece.getAngles(AngleType.min)),     3, 1);
        tableModel.setValueAt(Converter.doubleArrayToString(selectedPiece.getAngles(AngleType.max)),     4, 1);
        tableModel.setValueAt(Converter.doubleArrayToString(selectedPiece.getAngles(AngleType.current)), 5, 1);
        tableModel.setValueAt(Converter.doubleArrayToString(selectedPiece.getAngles(AngleType.joint)),   6, 1);
        tableModel.setValueAt(selectedPiece.getSource(),                  7, 1);
        tableModel.addTableModelListener(new TableListener());
        table.setModel(tableModel);
    }

    public DefaultTableModel createPropertyTableModel(int rowCount, boolean [] editable){
        Object [] columnCount = new Object[]{"PropertyName", "Value"};
        final boolean [] edit = editable;
        DefaultTableModel tableModel = new DefaultTableModel(columnCount, rowCount){
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if(mColIndex == 0)
                    return false;
                else
                    return edit[rowIndex];
            }
        };
        return tableModel;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

    private class TableListener implements TableModelListener{
        @Override
        public void tableChanged(TableModelEvent e) {
            DefaultTableModel model = (DefaultTableModel) e.getSource();
            String newValue = (String) model.getValueAt(e.getLastRow(), 1);
            if(componentSelection != null && componentSelection.isSelected()){
                switch(e.getLastRow()){
                    case 1://Cambia el nombre
                        componentSelection.setComponentName(newValue);
                        break;
                    case 3:
                        componentSelection.setPosition(Converter.stringToDoubleArray(newValue));
                        break;
                    case 4:
                        componentSelection.setRotation(Converter.stringToDoubleArray(newValue));
                        componentSelection.setRotationTransform();
                        break;
                    case 5:
                        componentSelection.setScale(Double.parseDouble(newValue));
                        break;
                }
            }else{
                if(limitSelection != null && limitSelection.isSelected()){
                    String [] limitValues = {model.getValueAt(1, 1).toString(),
                                             model.getValueAt(2, 1).toString(),
                                             model.getValueAt(3, 1).toString(),
                                             model.getValueAt(4, 1).toString()};
                
                    limitSelection.updateLimits(Float.parseFloat(limitValues[0])/2,
                                                Float.parseFloat(limitValues[1])/2,
                                                Float.parseFloat(limitValues[2])/2,
                                                Float.parseFloat(limitValues[3])/2);
                    Universe.getInstance().updateBounds();
                    
                }else{
                    if(selectedPiece != null && selectedPiece.isSelected()){
                        System.out.println(selectedPiece.isSelected());
                        System.out.println("3er if");
                        System.out.println(newValue);
                        switch(e.getLastRow()){
                            case 2:
                                selectedPiece.setPosition(Converter.stringToDoubleArray(newValue));
                                break;
                            case 3:
                                selectedPiece.setAngles(AngleType.min, Converter.stringToDoubleArray(newValue));
                                break;
                            case 4:
                                selectedPiece.setAngles(AngleType.max, Converter.stringToDoubleArray(newValue));
                                break;
                            case 5:
                                selectedPiece.setAngles(AngleType.current, Converter.stringToDoubleArray(newValue));
                                break;
                            case 6:
                                selectedPiece.setAngles(AngleType.joint, Converter.stringToDoubleArray(newValue));
                                break;
                        }

                    }
                }
            }
        }
    }
}
