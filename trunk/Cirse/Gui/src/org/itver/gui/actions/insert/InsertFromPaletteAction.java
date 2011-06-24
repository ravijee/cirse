/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.gui.actions.insert;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import org.itver.arm.exceptions.ArmStructureException;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.models.elements.ElementType;
import org.itver.arm.models.elements.Piece;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.util.ComponentType;
import org.itver.gui.visual.AlertGenerator;
import org.itver.gui.visual.NavigatorTopComponent;
import org.itver.gui.visual.palette.ItemPalette;
import org.itver.gui.visual.palette.MainPalette;
import org.openide.explorer.ExplorerManager;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author pablo
 */
public class InsertFromPaletteAction implements ActionListener{
    private MainPalette palette;
    private String      file;

    private InsertFromPaletteAction() {
    }

    public InsertFromPaletteAction(MainPalette palette){
        this.palette = palette;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ItemPalette item = palette.getSelectedItem();
        this.file = item.getFile();
        String category = palette.getSelectedCategoryName().toLowerCase();
        if(category.endsWith("s"))
            category = category.substring(0, category.lastIndexOf('s'));
        try{
            insertPiece(ElementType.valueOf(category));
        } catch (IllegalArgumentException iae) {
            try{
                insertComponent(ComponentType.valueOf(category));
            } catch(IllegalArgumentException iae2){
                System.err.println("Error en las categorias: " + category);
            }
        }

    }

    private void insertPiece(ElementType elementType) {
        ExplorerManager manager = NavigatorTopComponent.findInstance().getExplorerManager();
        if(manager.getSelectedNodes().length == 1){
            Lookup lookup = manager.getSelectedNodes()[0].getLookup();
            MainSceneComponent component = lookup.lookup(MainSceneComponent.class);
            if(component != null && component.getType() == ComponentType.arm){
                Arm arm = (Arm) component.getContent();
                Piece piece = new Piece(-1, elementType);
                piece.setSource(new File(file));
                try {
                    arm.addPiece(piece);
                } catch (ArmStructureException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else
                AlertGenerator.createErrorDialog("An Arm node must be selected.");
        } else
            AlertGenerator.createErrorDialog("There must be only one arm selected.");
    }
    
    private void insertComponent(ComponentType componentType) {
        
    }

}
