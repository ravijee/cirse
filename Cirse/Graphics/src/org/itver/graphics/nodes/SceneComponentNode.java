/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.graphics.nodes;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.vecmath.Point3d;
import org.itver.common.propertyeditors.Tuple3dEditor;
import org.itver.common.util.Converter;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.util.ComponentType;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Karo
 */
public class SceneComponentNode extends AbstractNode implements PropertyChangeListener{
    private MainSceneComponent msc;
    private static final String PATH = "org/itver/graphics/img/msc";

    public SceneComponentNode(MainSceneComponent msc){
        super(Children.LEAF, Lookups.singleton(msc));
        this.msc = msc;
        this.msc.addPropertyChangeListener(this);
        this.setDisplayName(msc.getType().name()+": "+msc.getComponentName());
        this.setName(msc.toString());
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
        Sheet result = Sheet.createDefault();
        Sheet.Set props = Sheet.createPropertiesSet();
        try {
            Property id = new PropertySupport.Reflection(msc, int.class, "id");
            Property name = new PropertySupport.Reflection(msc, String.class, "componentName");
            Property type = new PropertySupport.Reflection(msc, ComponentType.class, "type");
            PropertySupport.Reflection position = new PropertySupport.Reflection(msc, Point3d.class, "position");
            Property scale = new PropertySupport.Reflection(msc, double.class, "scale");
            PropertySupport.Reflection rotations = new PropertySupport.Reflection(msc, Point3d.class, "rotation");
            Property source = new PropertySupport.Reflection(msc, File.class, "getSource", null);
            id.setName("ID");
            name.setName("Name");
            type.setName("Type");
            position.setName("Position");
            scale.setName("Scale");
            source.setName("Source");
            rotations.setName("Rotation");
            position.setPropertyEditorClass(Tuple3dEditor.class);
            rotations.setPropertyEditorClass(Tuple3dEditor.class);
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String name = evt.getPropertyName();
        if(name.equals("Name")){
            this.setDisplayName(msc.getType().name()+": "+msc.getComponentName());
        }   
    }

}
