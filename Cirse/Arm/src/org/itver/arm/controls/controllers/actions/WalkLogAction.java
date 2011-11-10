/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.controls.controllers.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.itver.arm.controls.controllers.ArmController;
import org.itver.arm.controls.controllers.RotatorViewController;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.threads.ChartDataThread;
import org.itver.arm.threads.LogThread;
import org.itver.arm.views.gui.ChartCanvas;
import org.openide.util.NbBundle;

/**
 * Acción definida para recorrer todo el Log actual e iniciar la simulación.
 * @author pablo
 */
public final class WalkLogAction extends AbstractAction {

    public WalkLogAction(){
        this.putValue(NAME, NbBundle.getMessage(WalkLogAction.class, "CTL_WalkLogAction"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LogThread thread = new LogThread();
        RotatorViewController.singleton().setModel(null);
        thread.start();
        
        Thread canvas = new Thread(new Runnable() {
            @Override
            public void run() {
                ChartCanvas.singleton().reset();
                while(LogThread.isRunning())
                    ChartCanvas.singleton().repaint();
            }
        });
        canvas.start();
        
        for(Arm arm : ArmController.singleton().getArms()){
            ChartDataThread dataThread = new ChartDataThread(arm);
            dataThread.start();
        }
    }
}
