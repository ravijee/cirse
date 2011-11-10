/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.controls.controllers.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.itver.arm.controls.controllers.LogController;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.models.elements.Element;
import org.itver.arm.models.elements.Joint;
import org.itver.arm.models.log.Log;
import org.itver.arm.models.log.LogOrder;
import org.openide.util.NbBundle;

/**
 * Crea un LogOrder a partir del Elemento seleccionado en el Navigator, sea
 * Joint o Brazo
 * @author pablo
 */
public final class SnapshotAction extends AbstractAction {

    private final Element context;

    public SnapshotAction(Element context) {
        this.putValue(NAME, NbBundle.getMessage(SnapshotAction.class, "CTL_SnapshotAction"));
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        LogOrder order = LogController.singleton().getSelectedOrder();
        if(context instanceof Arm)
            order = LogController.singleton().createOrder((Arm)context, order);
        else if(context instanceof Joint)
            order = LogController.singleton().createOrder((Joint)context, order);
        if(order != null && order.getSteps().length > 0)
            Log.singleton().addOrder(order);
    }
}
