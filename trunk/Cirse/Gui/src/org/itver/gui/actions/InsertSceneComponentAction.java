/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JFileChooser;
import org.itver.common.util.Constants;
import org.itver.common.util.IdGenerator;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.model.Universe;
import org.itver.graphics.util.ComponentType;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Scene",
id = "org.itver.gui.actions.InsertSceneComponentAction")
@ActionRegistration(displayName = "#CTL_InsertSceneComponentAction")
@ActionReferences({
    @ActionReference(path = "Menu/Scene/Insert", position = 3333)
})
@Messages("CTL_InsertSceneComponentAction=Component")
public final class InsertSceneComponentAction implements ActionListener {
    private final String PATH = "Path";
    private Properties properties;

    public InsertSceneComponentAction() {
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
        File directory = new File(properties.getProperty(PATH));
        Universe universe = Universe.getInstance();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(directory);
        int state = fileChooser.showOpenDialog(null);
        if (state == JFileChooser.APPROVE_OPTION) {
            directory = fileChooser.getCurrentDirectory();
            File selectedFile = fileChooser.getSelectedFile();
            String source = selectedFile.getPath();
            if (source.endsWith(".dae")) {
                MainSceneComponent bgc = new MainSceneComponent(ComponentType.furniture, selectedFile);
                bgc.loadType();
                universe.addComponent(bgc);
            } else {
                if (source.endsWith(".off")) {
                    MainSceneComponent bgc = new MainSceneComponent(ComponentType.pickable, selectedFile);
                    bgc.loadType();
                    universe.addComponent(bgc);
                }
            }
        }
        properties.setProperty("Path", directory.getPath());
        try {
            properties.store(new FileOutputStream(Constants.DATA), null);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
