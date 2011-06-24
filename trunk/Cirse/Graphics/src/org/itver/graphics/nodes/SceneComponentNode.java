/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.graphics.nodes;

import java.io.File;
import javax.vecmath.Point3d;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.models.elements.Piece;
import org.itver.arm.models.nodes.PieceNode;
import org.itver.common.propertyeditors.FilePropertyEditor;
import org.itver.common.propertyeditors.Point3dEditor;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.util.ComponentType;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Karo
 */
public class SceneComponentNode extends AbstractNode{
    private MainSceneComponent msc;

    public SceneComponentNode(MainSceneComponent msc){
        super(Children.LEAF, Lookups.singleton(msc));
        this.setDisplayName(msc.getType().name()+": "+msc.getComponentName());
        this.setName(msc.toString());
        this.msc = msc;
        if(msc.getType() == ComponentType.arm)
            this.setChildren(new SceneComponentChildren());

    }

    @Override
    protected Sheet createSheet(){
        Sheet result = Sheet.createDefault();
        Sheet.Set props = Sheet.createPropertiesSet();
        try {
            Property id = new PropertySupport.Reflection(msc, int.class, "id");
            Property name = new PropertySupport.Reflection(msc, String.class, "componentName");
            Property type = new PropertySupport.Reflection(msc, ComponentType.class, "type");
            PropertySupport.Reflection position = new PropertySupport.Reflection(msc, Point3d.class, "position");
            Property scale = new PropertySupport.Reflection(msc, double.class, "scale");
            Property rotations = new PropertySupport.Reflection(msc, double[].class, "rotation");
            Property source = new PropertySupport.Reflection(msc, File.class, "getSource", null);
            id.setName("ID");
            name.setName("Name");
            type.setName("Type");
            position.setName("Position");
            scale.setName("Scale");
            source.setName("Source");
            rotations.setName("Rotation");
            position.setPropertyEditorClass(Point3dEditor.class);
//            source.setPropertyEditorClass(FilePropertyEditor.clas);
            props.put(id);
            props.put(name);
            props.put(type);
            props.put(position);
            props.put(scale);
            props.put(rotations);
            props.put(source);
        } catch (NoSuchMethodException ex) {
            System.err.println(ex.getMessage());
        }
        result.put(props);
        return result;
    }

    private class SceneComponentChildren extends Children.Keys{

        public SceneComponentChildren(){
            Arm arm = (Arm) msc.getContent();
            this.setKeys(arm.getPieces());
        }

        @Override
        protected Node[] createNodes(Object t) {
            Piece p = (Piece)t;
            return new Node[]{new PieceNode(p)};
        }

    }


}
