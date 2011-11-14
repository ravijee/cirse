/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.gui.actions.saveactions;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import org.itver.graphics.model.Universe;
import org.itver.gui.util.Dialogs;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;

@ActionID(id = "org.itver.gui.actions.saveactions.SaveAction", category = "File")
@ActionRegistration(iconInMenu = true, displayName = "#CTL_SaveAction", iconBase = "org/itver/gui/actions/saveactions/Save.png")
@ActionReferences(value = {
    @ActionReference(path = "Menu/File", position = 1550),
    @ActionReference(path = "Toolbars/File", position = 500)})
public final class SaveAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
            File file = Dialogs.fileDialog(FileDialog.SAVE, "xml");            
            if(file != null)
                Universe.getInstance().saveFile(file);
    }
//        JFileChooser fChooser = new JFileChooser();
//        String file = "o";
//        if(fChooser.showSaveDialog(MainFrameTopComponent.findInstance()) == JFileChooser.APPROVE_OPTION)
//            file = fChooser.getSelectedFile().getAbsolutePath();
//        if(file.equals("o"))
//            return;
//        EnvironmentSaver saver = new EnvironmentSaver();
//        Universe universe = Universe.getInstance();
//        try {
//            saver.save(universe, file);
//        } catch (SAXException ex) {
//            System.err.println(ex.getMessage());
//        }
    
}
