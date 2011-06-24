/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.gui.actions.openactions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JFileChooser;
import org.itver.arm.models.elements.Arm;
import org.itver.common.util.Constants;
import org.itver.common.util.IdGenerator;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.model.Universe;
import org.itver.graphics.util.ComponentType;
//import org.itver.gui.visual.PropertyEditorTopComponent;
import org.itver.gui.visual.MainFrameTopComponent;
import org.itver.gui.visual.PropertiesWindowTopComponent;
import org.openide.util.Exceptions;

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
        File directory = new File(properties.getProperty(PATH));
        Universe universe = Universe.getInstance();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(directory);
        int state = fileChooser.showOpenDialog(null);
        if (state == JFileChooser.APPROVE_OPTION) {
            directory = fileChooser.getCurrentDirectory();
            File selectedFile = fileChooser.getSelectedFile();
            String source = selectedFile.getPath();
            if(source.endsWith(".arm")){
                MainSceneComponent bgc = new MainSceneComponent(IdGenerator.generateComponentId(), ComponentType.arm, selectedFile);
                bgc.loadType();
                bgc.setScale(0.03);
                universe.addComponent(bgc);
                Arm arm = (Arm) bgc.getTransformGroup().getChild(0);
                arm.addListenerToPieces(PropertiesWindowTopComponent.getDefault().getEditor());
            }else{
                if(source.endsWith(".dae")){
                    MainSceneComponent bgc = new MainSceneComponent(IdGenerator.generateComponentId(), ComponentType.furniture, selectedFile);
                    bgc.addPropertyChangeListener(PropertiesWindowTopComponent.getDefault().getEditor());
                    bgc.loadType();
                    bgc.setScale(0.03);
                    universe.addComponent(bgc);
                }else{
                    if(source.endsWith(".off")){
                        MainSceneComponent bgc = new MainSceneComponent(IdGenerator.generateComponentId(), ComponentType.pickable, selectedFile);
                        bgc.addPropertyChangeListener(PropertiesWindowTopComponent.getDefault().getEditor());
                        bgc.loadType();
                        bgc.addPropertyChangeListener(PropertiesWindowTopComponent.findInstance().getEditor());
                        universe.addComponent(bgc);
                    }else{
                        if(source.endsWith(".env")){
                            Universe.getInstance().loadFile(source);
                        }
                    }
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

