/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.common.propertyeditors;

import java.awt.Component;
import java.beans.PropertyEditorSupport;
import javax.swing.JLabel;
import javax.vecmath.Point3d;
import org.itver.common.propertyeditors.visual.Point3dEditorPanel;
import org.itver.common.util.Converter;

/**
 *
 * @author Karo
 */
public class Point3dEditor extends PropertyEditorSupport{

    @Override
    public String getAsText() {
        String position = getValue().toString().replace("(", "").replace(")", "");
        if (position == null) {
            return "No position set";
        }
        return String.format(position);
    }

    @Override
    public void setAsText(String s) {
        try{
            Point3d position = new Point3d(Converter.stringToDoubleArray(s));
            setValue(position);
        }catch(IllegalArgumentException iae){
            System.out.println("lol no se puede"); //ponerlo en dialogo
        }
    }

    @Override
    public Component getCustomEditor(){
        Point3dEditorPanel panel = new Point3dEditorPanel();
        return panel;
    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }



}
