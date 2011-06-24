/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.gui.actions.insert;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.itver.gui.visual.palette.ControllerPalette;
import org.itver.gui.visual.palette.MainPalette;
import org.openide.util.Exceptions;

public final class InsertAction implements ActionListener {

    private MainPalette palette;

    @Override
    public void actionPerformed(ActionEvent e) {
        Properties bundle = new Properties();
        try {
            bundle.load(new FileInputStream("Gui/src/org/itver/gui/Bundle.properties"));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        ControllerPalette controller = new ControllerPalette();
        controller.addCategoryFromFile(new File(bundle.getProperty("pieces_palette")));
        palette = controller.getMainPalette();
        palette.setVisible(true);
        palette.addActionListener(new InsertFromPaletteAction(palette));
    }
}
