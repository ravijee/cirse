/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.gui.actions.openactions;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.itver.common.util.Constants;
import org.itver.graphics.model.Universe;
//import org.itver.gui.visual.PropertyEditorTopComponent;
import org.itver.gui.util.Dialogs;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;

@ActionID(id = "org.itver.gui.actions.openactions.OpenAction", category = "File")
@ActionRegistration(iconInMenu = true, displayName = "#CTL_OpenAction", iconBase = "org/itver/gui/actions/openactions/nav_properties.png")
@ActionReferences(value = {
    @ActionReference(path = "Shortcuts", name = "DS-O"),
    @ActionReference(path = "Menu/File", position = 1450),
    @ActionReference(path = "Toolbars/File", position = 350)})
public final class OpenAction implements ActionListener {
    /*
     * Poner "bonito" este código y hacer que sólo abra un tipo de archivo
     *
     */
    private final String PATH = "Path";
    private Properties properties;



    public OpenAction(){
        properties = new Properties();
        try {
            properties.load(new FileInputStream(Constants.DATA));
        } catch (FileNotFoundException ex) {
            properties.setProperty(PATH, "");
        } catch (IOException ex) {
            
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File file = Dialogs.fileDialog(FileDialog.LOAD, "xml");            
        if(file != null){
            Universe.getInstance().getScene().clearScene();
            Universe.getInstance().loadFile(file);
        }    
//        try {
//            properties.store(new FileOutputStream(Constants.DATA), null);
//        } catch (FileNotFoundException ex) {
//            Exceptions.printStackTrace(ex);
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
    }

}

