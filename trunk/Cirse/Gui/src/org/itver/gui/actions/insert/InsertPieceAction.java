/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.gui.actions.insert;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import org.itver.gui.visual.palette.ControllerPalette;
import org.itver.gui.visual.palette.MainPalette;

/**
 *
 * @author pablo
 */
public class InsertPieceAction implements ActionListener{

    private ControllerPalette controller = new ControllerPalette();
    private MainPalette       palette;

    @Override
    public void actionPerformed(ActionEvent e) {
        createPaletteFromDirectory("designs");
        palette = controller.getMainPalette();
        palette.addActionListener(new InsertFromPaletteAction(palette));
        palette.setVisible(true);
    }

    private void createPaletteFromDirectory(String dir) {
        File mainDir = new File(dir);
        if(mainDir.isDirectory()){
            File[] list = mainDir.listFiles();
            for(File nextFile : list)
                if(nextFile.getName().endsWith(".arm"))
                    controller.addElementFromFile(nextFile);
                else if(nextFile.getName().endsWith(".pal"))
                    controller.addCategoryFromFile(nextFile);

        }
    }


}
