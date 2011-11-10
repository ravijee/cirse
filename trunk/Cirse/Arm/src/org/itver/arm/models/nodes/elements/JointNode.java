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
import org.itver.arm.controls.controllers.actions.DeleteAction;
import org.itver.arm.controls.controllers.actions.InsertJointAction;
import org.itver.arm.controls.controllers.actions.InsertPiece;
import org.itver.arm.controls.controllers.actions.NewLogStepAction;
import org.itver.arm.controls.controllers.actions.SnapshotAction;
import org.itver.arm.models.elements.Axis;
import org.itver.arm.models.elements.Element;
import org.itver.arm.models.elements.Joint;
import org.itver.arm.models.elements.Piece;
import org.itver.common.util.Converter;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;

/**
 * Nodo con información de Joint.
 * @author pablo
 */
public class JointNode extends PieceNode{
    private static final String PATH = "org/itver/arm/img/joint";

    private Joint joint;

    public JointNode(Joint joint){
        super(joint);
    }

    @Override
    protected void assingModel(Piece piece){
        super.assingModel(piece);
        this.joint = (Joint) piece;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
    }

    @Override
    protected void updateDisplayName(){
        this.setDisplayName( "Joint" +  " [" + joint.getId() + "]"  );
    }

    @Override
    protected void updateName(){
        this.setName(joint.toString());
    }

    @Override
    protected void updateChildren(){
        this.setChildren(new JointChildren(joint));
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
        try{
            Property min = new PropertySupport.Reflection(joint, double.class, "jointMin");
                min.setName("Minimum value");
                props.put(min);

                Property max = new PropertySupport.Reflection(joint, double.class, "jointMax");
                max.setName("Maximum value");
                props.put(max);

                Property axis = new PropertySupport.Reflection(joint, Axis.class, "axis");
                axis.setName("Axis");
                props.put(axis);

                Property mot = new PropertySupport.Reflection(joint, boolean.class, "motion");
                mot.setName("Rotational");
                props.put(mot);
        }catch(NoSuchMethodException ex){
            System.err.println(ex.getMessage());
        }

        result.put(props);
        return result;
    }

    @Override
    public Action[] getActions(boolean context) {
        ArrayList<Element> list = new ArrayList();
        list.add(joint);
        AbstractAction[] listeners = {new InsertPiece(joint),
                            new InsertJointAction(joint),
                            null,
                            new NewLogStepAction((Joint)joint),
                            new SnapshotAction((Joint)joint),
                            null,
                            new DeleteAction(list)
        };
        list = null;
        return listeners;

    }

}
