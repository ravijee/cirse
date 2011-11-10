/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.common.propertyeditors;

import java.awt.Component;
import java.beans.PropertyEditorSupport;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

/**
 *
 * @author Karo
 */
public class FilePropertyEditor extends PropertyEditorSupport {

    private File selected;

    @Override
    public String getAsText() {
        String d = (String) getValue();
        if (d == null) {
            return "";
        }
        return d;
//        return selected.getPath();
    }
//
    @Override
    public void setAsText(String s) {
        setValue(s);
    }

    @Override
    public Component getCustomEditor() {
        return new JLabel("Whoops!");
//        JFileChooser chooser = new JFileChooser();
//        int open = 0;
//        selected = null;
//        if (open == JFileChooser.APPROVE_OPTION) {
//            selected = chooser.getSelectedFile();
//            setAsText(selected.getPath());
//        }
//        return chooser;
    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }
}
