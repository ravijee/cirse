/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.controls.behaviors;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupOnAWTEvent;
import org.itver.arm.models.elements.AngleType;
import org.itver.arm.models.elements.Axis;
import org.itver.arm.models.elements.Friction;
import org.itver.arm.models.elements.Piece;

/**
 *
 * @author pablo
 */
public class KeyboardBehavior extends Behavior implements PropertyChangeListener{

    private WakeupOnAWTEvent wa;
    private Piece piece;
    private double angle1;
    private double angle2;
    private Axis axis1;
    private Axis axis2;

    public KeyboardBehavior() {
        wa = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
        axis1 = Axis.x;
        axis2 = Axis.y;
    }

    @Override
    public void initialize() {
        wakeupOn(wa);
    }

    @Override
    public void processStimulus(Enumeration en) {
        if(piece == null){
            wakeupOn(wa);
            return;
        }
        angle1 = piece.getAngle(AngleType.joint, axis1);
        angle2 = piece.getAngle(AngleType.joint, axis2);
        
        AWTEvent events[] = wa.getAWTEvent();
        for (int i = 0; i < events.length; i++) {
            KeyEvent keyEv = (KeyEvent) events[i];
            switch (keyEv.getKeyCode()) {
                case KeyEvent.VK_UP:
                    piece.setAngle(AngleType.joint, axis1, ++angle1);
                    break;
                case KeyEvent.VK_DOWN:
                    piece.setAngle(AngleType.joint, axis1, --angle1);
                    break;
                case KeyEvent.VK_LEFT:
                    piece.setAngle(AngleType.joint, axis2, ++angle2);
                    break;
                case KeyEvent.VK_RIGHT:
                    piece.setAngle(AngleType.joint, axis2, --angle2);
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    piece.getArm().removePiece(piece);
                    break;
                case KeyEvent.VK_ENTER:
                    for(Friction f: piece.getFrictions())
                        f.setVisible(!f.isVisible());
            }
        }
        wakeupOn(wa);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getSource() instanceof Piece)
            if(evt.getPropertyName().equals("selected"))
                if(Boolean.valueOf(evt.getNewValue().toString())){
                    piece = (Piece)evt.getSource();
                }
    }

}
