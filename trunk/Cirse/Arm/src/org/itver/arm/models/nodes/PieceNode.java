/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.models.nodes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import org.itver.arm.models.elements.ElementType;
import org.itver.arm.models.elements.Piece;
import org.itver.common.propertyeditors.Point3dEditor;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author pablo
 */
public class PieceNode extends AbstractNode
                       implements PropertyChangeListener{
    private Piece piece;

    public PieceNode(Piece piece){
        super(Children.LEAF, Lookups.singleton(piece));
        this.piece = piece;
        this.piece.addListener(this);
        this.setName(piece.toString());
        updateDisplayName();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("id"))
            updateDisplayName();
    }

    private void updateDisplayName(){
        this.setDisplayName("[" + piece.getId() + "]: " + piece.getType().name());
    }

    @Override
    protected Sheet createSheet(){
        Sheet result = Sheet.createDefault();
        Sheet.Set props = Sheet.createPropertiesSet();
        try{
            Property id   = new PropertySupport.Reflection(piece, int.class, "id");
            id.setName("Id");
            props.put(id);

            Property type = new PropertySupport.Reflection(piece, ElementType.class, "getType", null);
            type.setName("Type");
            props.put(type);

            PropertySupport.Reflection pos  = new PropertySupport.Reflection(piece, Point3d.class, "position");
            pos.setName("Position");
            pos.setPropertyEditorClass(Point3dEditor.class); 
            props.put(pos);

            Property src  = new PropertySupport.Reflection(piece, File.class, "source");
            src.setName("Source");
            props.put(src);

            PropertySupport.Reflection rels = new PropertySupport.Reflection(piece, int[].class, "relations");
            rels.setName("Relations");
            rels.setPropertyEditorClass(IntArrayPropEditor.class);
            props.put(rels);

            Property siz = new PropertySupport.Reflection(piece, Vector3f.class, "getSize", null);
            siz.setName("Size");
            props.put(siz);


        }catch(NoSuchMethodException ex){
            System.err.println(ex.getMessage());
        }

        result.put(props);
        return result;
    }
}
