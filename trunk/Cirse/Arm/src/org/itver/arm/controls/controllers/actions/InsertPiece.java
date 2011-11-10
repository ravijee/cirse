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
import org.itver.arm.controls.controllers.JointController;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.models.elements.Element;
import org.itver.arm.models.elements.ElementType;
import org.itver.arm.models.elements.Joint;
import org.itver.gui.util.Dialogs;
import org.openide.util.NbBundle;

/**
 * Inserta una pieza en el Elemento seleccionado en el Navigator, puede ser
 * Arm o Joint.
 * @author pablo
 */
public class InsertPiece extends  AbstractAction{

    private Element element;
    private boolean insJoint;

    public InsertPiece(Element element){
        this.element = element;
        this.setName();
    }

    public InsertPiece(Element element, boolean insertJoint){
        this(element);
        this.insJoint = insertJoint;
    }

    protected void setName(){
        this.putValue(NAME, NbBundle.getMessage(InsertPiece.class, "CTL_InertPiece"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Joint joint = element instanceof Joint ? (Joint)element : null;
        Arm arm = element instanceof Arm ? (Arm)element : null;
        File file = null;
        if(joint != null || arm != null)
            file = Dialogs.fileDialog(FileDialog.LOAD, "dae");

        if(file != null)
            if(joint != null)
                JointController.singleton().insertPieceFromFile(file, joint, ElementType.segment, insJoint);
            else if(arm != null)
                ArmController.singleton().insertPieceFromFile(arm, file, ElementType.segment, insJoint);
    }
}
