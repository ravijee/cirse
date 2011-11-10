/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.models.nodes.elements;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.vecmath.Vector3d;
import org.itver.arm.controls.controllers.actions.DeleteAction;
import org.itver.arm.controls.controllers.actions.ExportArmAction;
import org.itver.arm.controls.controllers.actions.InsertJointAction;
import org.itver.arm.controls.controllers.actions.InsertPiece;
import org.itver.arm.controls.controllers.actions.SnapshotAction;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.models.elements.Element;
import org.itver.common.propertyeditors.Tuple3dEditor;
import org.itver.common.util.Converter;
import org.itver.graphics.nodes.SceneComponentNode;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;

/**
 * Nodo con información de un Arm.
 * @author pablo
 */
public final class ArmNode extends ElementNode{
    private Arm arm;
    private SceneComponentNode parentNode;
    private static final String PATH = "org/itver/arm/img/arm";

    public ArmNode(Arm arm){
        super(arm);
        this.arm = arm;
        this.parentNode = new SceneComponentNode(arm.getContainer());
        this.updateAll();
    }

    @Override
    protected void updateName(){
        this.setName(arm.toString());
    }

    @Override
    protected void updateDisplayName(){
        this.setDisplayName("Arm [" + arm.getId() + "]");
    }

    @Override
    protected void updateChildren(){
        this.setChildren(new ArmChildren(arm));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(Converter.convertImgPath(PATH, type));
    }

    @Override
    public Image getOpenedIcon(int type){
        return this.getIcon(type);
    }

    @Override
    protected Sheet createSheet(){
        Sheet result = super.createSheet();
        Sheet.Set props = result.get("element");
        for(PropertySet setParent : parentNode.getPropertySets())
            for(Property propParent : setParent.getProperties())
                props.put(propParent);

        try{
            PropertySupport.Reflection support = new PropertySupport.Reflection(arm, Vector3d.class, "support");
            support.setName(Arm.SUPPORT);
            support.setPropertyEditorClass(Tuple3dEditor.class);
            props.put(support);
        }catch(NoSuchMethodException ex){
            System.err.println(ex.getMessage());
        }

        result.put(props);
        return result;
    }

    @Override
    public Action[] getActions(boolean context) {
        ArrayList<Element> list = new ArrayList<Element>();
        list.add(arm);
        AbstractAction[] listeners = {
                            new InsertPiece(arm),
                            new InsertJointAction(arm),
                            new ExportArmAction(arm),
                            null,
                            new SnapshotAction(arm),
                            null,
                            new DeleteAction(list)
        };
        list = null;
        return listeners;
    }

}
