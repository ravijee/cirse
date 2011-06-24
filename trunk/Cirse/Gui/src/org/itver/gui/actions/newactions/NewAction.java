/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.gui.actions.newactions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.itver.gui.visual.palette.MainPalette;

public final class NewAction implements ActionListener {

    private MainPalette palette;

    @Override
    public void actionPerformed(ActionEvent e) {
        

    }

    @Override
    protected void finalize() throws Throwable {
        palette.removeActionListener(this);
        palette = null;
        super.finalize();
    }


}
