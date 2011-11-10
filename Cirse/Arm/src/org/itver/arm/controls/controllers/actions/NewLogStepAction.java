/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.controls.controllers.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.itver.arm.controls.controllers.LogController;
import org.itver.arm.models.elements.Joint;
import org.itver.arm.models.log.LogStep;
import org.openide.util.NbBundle;

/**
 * Crea un nuevo LogStep a partir del Joint seleccionado, tomando su valor
 * actual como valor a alcanzar por el interpolador.
 * @author pablo
 */
public final class NewLogStepAction extends AbstractAction {

    private final Joint context;

    public NewLogStepAction(Joint context) {
        this.putValue(NAME, NbBundle.getMessage(NewLogStepAction.class, "CTL_NewLogStepAction"));
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        LogStep step = LogController.singleton().createStep(context);
        LogController.singleton().addStep(step);
    }
}
