/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.controls.controllers.actions;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import org.itver.arm.controls.controllers.ArmController;
import org.itver.arm.models.elements.Arm;
import org.itver.gui.util.Dialogs;
import org.openide.util.NbBundle;

/**
 * Acción definida para guardar un brazo seleccionado en un archivo.
 * @author pablo
 */
public final class ExportArmAction extends  AbstractAction{
    private Arm arm;

    public ExportArmAction(){
        this.putValue(NAME, NbBundle.getMessage(ExportArmAction.class, "CTL_ExportArmAction"));
    }

    public ExportArmAction(Arm arm){
        this();
        this.arm = arm;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(arm != null){
            File file = Dialogs.fileDialog(FileDialog.SAVE, "xml");            
            if(file != null){
                ArmController.singleton().exportArm(arm, file);
            }
        }
    }


}
