/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.models.nodes.elements;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.vecmath.Vector3f;
import org.itver.arm.controls.controllers.actions.DeleteAction;
import org.itver.arm.models.elements.Element;
import org.itver.arm.models.elements.ElementType;
import org.itver.arm.models.elements.Piece;
import org.itver.common.propertyeditors.Tuple3dEditor;
import org.itver.common.util.Converter;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

/**
 * Nodo con información de Piece
 * @author pablo
 */
public class PieceNode extends ElementNode{
    private static final String PATH = "org/itver/arm/img/sgm";
    private Piece piece;

    public PieceNode(Piece piece){
        super(piece);
        assingModel(piece);
        this.updateAll();
    }

    protected void assingModel(Piece piece){
        this.piece = piece;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
    }

    @Override
    protected void updateDisplayName(){
        this.setDisplayName( "Piece" + " [" + piece.getId() + "]"  );
    }

    @Override
    protected void updateName(){
        this.setName(piece.toString());
    }

    @Override
    protected void updateChildren(){
        this.setChildren(Children.LEAF);
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
            Property src  = new PropertySupport.Reflection(piece, File.class, "source");
            src.setName("Source");
            props.put(src);
            
            PropertySupport.Reflection siz = new PropertySupport.Reflection(piece, Vector3f.class, "getSize", null);
            siz.setName("Size");
            siz.setPropertyEditorClass(Tuple3dEditor.class);
            props.put(siz);

            if(piece.getType() != null){
                Property typ = new PropertySupport.Reflection(piece, ElementType.class, "type");
                typ.setName("Piece Type");
                props.put(typ);
            }
        }catch(NoSuchMethodException ex){
            Exceptions.printStackTrace(ex);
        }

        result.put(props);
        return result;
    }
    
    @Override
    public Action[] getActions(boolean context) {
        ArrayList<Element> list = new ArrayList<Element>();
        list.add(piece);
        AbstractAction[] listeners = {
                            new DeleteAction(list)
        };
        list = null;
        return listeners;

    }
}