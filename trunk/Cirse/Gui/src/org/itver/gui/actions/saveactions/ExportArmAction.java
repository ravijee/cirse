/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.gui.actions.saveactions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import org.itver.arm.io.ArmSaver;
import org.itver.arm.models.elements.Arm;
import org.itver.graphics.model.MainScene;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.util.ComponentType;
import org.itver.gui.visual.MainFrameTopComponent;

public final class ExportArmAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fChooser = new JFileChooser();
        String file = "";
        if(fChooser.showSaveDialog(MainFrameTopComponent.findInstance()) == JFileChooser.APPROVE_OPTION)
            file = fChooser.getSelectedFile().getAbsolutePath();
        if(file.equals(""))
            return;
        ArmSaver saver = new ArmSaver(0.1f);
        for(MainSceneComponent msc: MainScene.getInstance().getComponentArray()){
            if(msc.getType() == ComponentType.arm)
                saver.save((Arm) msc.getTransformGroup().getChild(0), file);
        }
    }
}
