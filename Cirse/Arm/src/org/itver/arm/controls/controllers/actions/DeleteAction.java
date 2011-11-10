/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.controls.controllers.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import org.itver.arm.controls.controllers.ArmController;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.models.elements.Element;
import org.itver.arm.models.elements.Piece;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Manipulator",
id = "org.itver.arm.controls.controllers.actions.DeleteAction")
@ActionRegistration(displayName = "#CTL_DeleteAction")
@ActionReferences({
    @ActionReference(path = "Menu/Manipulator", position = 450, separatorBefore = 400)
})
@Messages("CTL_DeleteAction=Delete")
public final class DeleteAction extends AbstractAction {

    private final List<Element> context;

    public DeleteAction(List<Element> context) {
        this.context = context;
        this.putValue(NAME, NbBundle.getMessage(DeleteAction.class, "CTL_DeleteAction"));
    }
    
    @Override
    public void actionPerformed(ActionEvent ev) {
        for (Element element : context) {
            if(element instanceof Arm)
                ArmController.singleton().removeArm((Arm)element);
            else if(element instanceof Piece){
                Piece piece = (Piece)element;
                Arm arm = piece.getArm();
                if(arm != null && piece != null)
                    arm.removePiece(piece);
            }
        }
    }
}
