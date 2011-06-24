/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.graphics.nodes;

import java.io.File;
import org.itver.graphics.model.EnvironmentLimits;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.Lookups;

/**
 * CLASE EN DESUSO
 *
 * @author Karo
 */
public class EnvironmentLimitsNode extends AbstractNode{
    private EnvironmentLimits ev;

    public EnvironmentLimitsNode(EnvironmentLimits ev){
        super(Children.LEAF, Lookups.singleton(ev));
        this.setDisplayName("EnvironmentLimits");
        this.setName(ev.toString());
        this.ev = ev;
    }

    @Override
    protected Sheet createSheet(){
        Sheet result = Sheet.createDefault();
        Sheet.Set props = Sheet.createPropertiesSet();
        try {
            Property width = new PropertySupport.Reflection(ev, float.class, "width");
            Property height = new PropertySupport.Reflection(ev, float.class, "height");
            Property deepness = new PropertySupport.Reflection(ev, float.class, "deepness");
            Property thickness = new PropertySupport.Reflection(ev, float.class, "thickness");
            width.setName("Width");
            height.setName("Height");
            deepness.setName("Deepness");
            thickness.setName("Thickness");
            props.put(width);
            props.put(height);
            props.put(deepness);
            props.put(thickness);
        } catch (NoSuchMethodException ex) {
            System.err.println(ex.getMessage());
        }
        result.put(props);
        return result;
    }
}
