/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.gui.visual;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author pablo
 */
public class AlertGenerator {

    public static void createErrorDialog(String message){
        NotifyDescriptor notify = new NotifyDescriptor.Message(message, NotifyDescriptor.ERROR_MESSAGE);
        DialogDisplayer.getDefault().notify(notify);
    }

}
