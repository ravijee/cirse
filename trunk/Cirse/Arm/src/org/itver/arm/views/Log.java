/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.itver.arm.models.elements.Piece;
import org.openide.windows.IOProvider;
import org.openide.windows.OutputWriter;

/**
 *
 * @author pablo
 */
public class Log implements PropertyChangeListener{

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Piece piece = (Piece) evt.getSource();
        String oldV = evt.getOldValue().toString();
        String newV = evt.getNewValue().toString();
        OutputWriter writer = IOProvider.getDefault().getIO("Log", false).getOut();
        writer.println("Piece '" + piece.getId() + "', changed in " +
                evt.getPropertyName() + " from '" + oldV + "' to '"
                + newV + "'.");
    }

}
