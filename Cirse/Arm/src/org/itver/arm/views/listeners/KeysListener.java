/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.views.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.vecmath.Vector3d;
import org.itver.arm.controls.controllers.ArmController;
import org.itver.arm.models.elements.Piece;

/**
 * Oyente de los eventos del teclado.
 * @author pablo
 */
public final class KeysListener implements KeyListener{

    private static KeysListener instance;

    private KeysListener(){
        
    }

    public synchronized static KeyListener singleton() {
        if(instance == null)
            instance = new KeysListener();
        return instance;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Piece piece = ArmController.singleton().getSelectedPiece();
        if(piece != null){
            double factor = 0.01;
            if(e.isShiftDown())
                factor += 0.1;
            Vector3d translation = null;
            switch(e.getKeyCode()){
               case KeyEvent.VK_UP:
                   if(e.isControlDown())
                       translation = new Vector3d(0, 0, factor);
                   else
                       translation = new Vector3d(0, factor, 0);
                   break;
               case KeyEvent.VK_DOWN:
                   if(e.isControlDown())
                       translation = new Vector3d(0, 0, -factor);
                   else
                       translation = new Vector3d(0, -factor, 0);
                   break;
               case KeyEvent.VK_LEFT:
                   translation = new Vector3d(factor, 0, 0);
                   break;
               case KeyEvent.VK_RIGHT:
                   translation = new Vector3d(-factor, 0, 0);
                   break;
                case KeyEvent.VK_DELETE:
                    ArmController.singleton().removePieceFromArm(piece.getArm(), piece);
            }
            if(translation != null)
                piece.translate(translation);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
