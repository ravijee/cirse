/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.controls.controllers.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.itver.arm.controls.controllers.ArmController;
import org.openide.util.NbBundle;

/**
 * Crea un nuevo Manipulador con el archivo obtenido como su primer Pieza.
 * @author pablo
 */
public final class NewArm extends AbstractAction {

    public NewArm(){
        this.putValue(NAME, NbBundle.getMessage(NewArm.class, "CTL_NewArm"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ArmController.singleton().createArm();
    }

}
