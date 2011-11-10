/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.threads;

import org.itver.arm.controls.controllers.LogController;
import org.itver.arm.models.log.Log;

/**
 * Thread encargado de comenzar una simulación.
 * @author pablo
 */
public final class LogThread extends Thread{
    private static boolean running;
    
    @Override
    public void run() {
        running = true;
        LogController.singleton().walk(Log.singleton());
        running = false;
    }
    
    public static boolean isRunning(){
        return running;
    }
}
