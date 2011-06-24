/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.gui.actions.saveactions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import org.itver.graphics.io.EnvironmentSaver;
import org.itver.graphics.model.Universe;
import org.itver.gui.visual.MainFrameTopComponent;
import org.xml.sax.SAXException;

public final class SaveAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fChooser = new JFileChooser();
        String file = "o";
        if(fChooser.showSaveDialog(MainFrameTopComponent.findInstance()) == JFileChooser.APPROVE_OPTION)
            file = fChooser.getSelectedFile().getAbsolutePath();
        if(file.equals("o"))
            return;
        EnvironmentSaver saver = new EnvironmentSaver();
        Universe universe = Universe.getInstance();
        try {
            saver.save(universe, file);
        } catch (SAXException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
