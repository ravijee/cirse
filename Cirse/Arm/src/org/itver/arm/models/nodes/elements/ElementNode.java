/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.models.nodes.elements;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.vecmath.Point3d;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.models.elements.Element;
import org.itver.arm.models.elements.Joint;
import org.itver.common.propertyeditors.Tuple3dEditor;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.Lookups;

/**
 * Nodo con la información de todos los Element.
 * @author pablo
 */
public abstract class ElementNode extends AbstractNode
                       implements PropertyChangeListener{
    private final Element element;

    public ElementNode(Element element){
        super(Children.LEAF, Lookups.singleton(element));
        this.element = element;
        this.element.addListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if(prop.equals(Arm.NEXT) || prop.equals(Joint.NEXT))
            updateChildren();
    }

    abstract void updateDisplayName();

    abstract void updateName();

    protected void updateAll(){
        updateChildren();
        updateDisplayName();
        updateName();
    }

    protected  void updateChildren(){
        
    }

    @Override
    protected Sheet createSheet(){
        Sheet result = Sheet.createDefault();
        Sheet.Set props = Sheet.createPropertiesSet();
        props.setName("element");
        try{
            Property id   = new PropertySupport.Reflection(element, int.class, "getId", null);
            id.setName("ID");
            props.put(id);

            PropertySupport.Reflection pos  = new PropertySupport.Reflection(element, Point3d.class, "position");
            pos.setName("Position");
            pos.setPropertyEditorClass(Tuple3dEditor.class);
            props.put(pos);

            Property src  = new PropertySupport.Reflection(element, File.class, "getSource", null);
            src.setName("Source");
            props.put(src);


        }catch(NoSuchMethodException ex){
            System.err.println(ex.getMessage());
        }

        result.put(props);
        return result;
    }
    
    @Override
    public boolean canDestroy(){
        return true;
    }
    
    @Override
    public void destroy(){
        
    }

}