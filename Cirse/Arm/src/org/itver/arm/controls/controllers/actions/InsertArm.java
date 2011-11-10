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
import org.itver.gui.util.Dialogs;
import org.openide.util.NbBundle;

/**
 * Abre un archivo XML con información sobre un Brazo, creándolo en escena.
 * @author pablo
 */
public final class InsertArm extends AbstractAction {

    public InsertArm(){
        this.putValue(NAME, NbBundle.getMessage(InsertArm.class, "CTL_InsertArm"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File file = Dialogs.fileDialog(FileDialog.LOAD, "xml");
        if(file != null)
            ArmController.singleton().importArm(file);
    }
}
