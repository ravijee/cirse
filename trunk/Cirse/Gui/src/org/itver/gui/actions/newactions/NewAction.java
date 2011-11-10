/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.gui.actions.newactions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.itver.gui.visual.palette.MainPalette;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;

@ActionID(id = "org.itver.gui.actions.newactions.NewAction", category = "File")
@ActionRegistration(iconInMenu = true, displayName = "#CTL_NewAction", iconBase = "org/itver/gui/actions/newactions/nav_properties.png")
@ActionReferences(value = {
    @ActionReference(path = "Shortcuts", name = "DS-N"),
    @ActionReference(path = "Menu/File", position = 1200),
    @ActionReference(path = "Toolbars/File", position = 200)})
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
