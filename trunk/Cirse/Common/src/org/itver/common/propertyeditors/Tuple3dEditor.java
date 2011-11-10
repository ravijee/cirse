/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.common.propertyeditors;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyEditorSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import org.itver.common.propertyeditors.visual.Tuple3dEditorPanel;
import org.itver.common.util.Converter;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;

/**
 * Muestra Cualquier {@link Tuple3d Tuple3d} en la ventana de propiedades, así 
 * como un panel al presionar el botón "..."
 * @author Karo
 */
public class Tuple3dEditor extends PropertyEditorSupport
                           implements ExPropertyEditor, VetoableChangeListener{
    private Tuple3dEditorPanel panel;
    private PropertyEnv ev;
    private Object value;

    @Override
    public String getAsText() {
        value = this.getValue();
        String position = "";
        if(value != null)
            position = value.toString().replaceAll("[\\(\\)]+", "");
        return String.format(position);
    }

    @Override
    public void setAsText(String text) {
        try {
            text = text.replaceAll("[\\(\\)\\s]+", "");
            if(value == null)
                return;
            if(value instanceof Tuple3d)
                value = new Point3d(Converter.stringToDoubleArray(text));
            else if(value instanceof Tuple3f)
                value = new Point3f(Converter.stringToFloatArray(text));
            setValue(value);
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
        }
    }

    @Override
    public Component getCustomEditor() {
        String[] vals = this.getAsText().replace(" ", "").split(",");
        panel = new Tuple3dEditorPanel();
        if(vals != null){
            panel.setX(vals[0]);
            panel.setY(vals[1]);
            panel.setZ(vals[2]);
        }
        ev.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
        ev.addVetoableChangeListener(this);
        return panel;
    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

    @Override
    public void attachEnv(PropertyEnv pe) {
        this.ev = pe;
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        this.setValue(panel.getTuple3d());
    }
    
}
