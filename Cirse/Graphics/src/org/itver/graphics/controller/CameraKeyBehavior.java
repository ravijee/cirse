
package org.itver.graphics.controller;

import java.awt.event.*;
import java.awt.AWTEvent;
import java.util.Enumeration;
import javax.media.j3d.*;
import org.itver.graphics.model.Camera;

/**
 * Clase que de {@code comportamiento} que se encarga de manipular la cámara
 * mediante el teclado.
 * <br/>
 * La convención de telcas utilizadas actualmente es:
 * <ul>
 *  <li>W: Mover hacia adelante (Eje Z).</li>
 *  <li>A: Rotar hacia la izquierda.</li>
 *  <li>S: Mover hacia atrás (Eje Z).</li>
 *  <li>D: Rotar hacia la derecha.</li>
 *  <li>Q: Dar un paso hacia la izquierda (Eje X).</li>
 *  <li>E: Dar un paso hacia la derecha (Eje X).</li>
 *  <li>Espacio: Subir (Eje Y).</li>
 *  <li>X: Bajar (Eje Y).</li>
 * </ul>
 * <br/>Tanto esta clase como {@link org.itver.graphics.guitools.Control} trabajan
 * sobre un objeto de tipo {@link org.itver.graphics.controller.TransformGroupManipulator}
 * con el cual se realizan todas las operaciones de control sobre la cámara o
 * punto de visión del usuario.
 *
 * @author Karo
 * Éste código fue realizado con el apoyo de código ejemplo de Daniel Selman autor de Java3D Programming. Ver @link http://www.manning.com/selman/
 */
public class CameraKeyBehavior extends Behavior {

    private WakeupOnAWTEvent wakeupOne = null; //Class that specifies a Behavior wakeup when a specific AWT event occurs.
    private WakeupCriterion[] wakeupArray = new WakeupCriterion[1]; //
    private WakeupCondition wakeupCondition = null;
    private TransformGroup targetTG;
    private TransformGroupManipulator tgManipulator;
    private Camera camera;

    /**
     * Construye una instancia de esta clase y recice un TransformGroup con el
     * que se manipularan las transformaciones de la cámara.
     * @param targetTg TransformGroup para manipular la cámara.
     */
    public CameraKeyBehavior(TransformGroup targetTg) {
//        this.camera = camera;
        this.targetTG = targetTg;
        try {
            targetTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            targetTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        } catch (Exception e) {
        }
        tgManipulator = new TransformGroupManipulator(targetTg);
        wakeupOne = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
        wakeupArray[0] = wakeupOne;
        wakeupCondition = new WakeupOr(wakeupArray);
    }

    /**
     * Sobreescribe el método {@link javax.media.j3d.Behavior#initialize()} de
     * {@link javax.media.j3d.Behavior} para establecer una condición para
     * iniciar el comportamiento.
     */
    @Override
    public void initialize() {
        wakeupOn(wakeupCondition);
    }

    /**
     * Sobreescribe el método {@link javax.media.j3d.Behavior#processStimulus(java.util.Enumeration) }
     * para definir el comportamiento cuando la o las condiciones para iniciar
     * el mismo han sido satisfechas.
     */
    @Override
    public void processStimulus(Enumeration criteria) {
        WakeupOnAWTEvent ev;
        WakeupCriterion genericEvt;
        AWTEvent[] events;

        while (criteria.hasMoreElements()) {
            genericEvt = (WakeupCriterion) criteria.nextElement();

            if (genericEvt instanceof WakeupOnAWTEvent) {
                ev = (WakeupOnAWTEvent) genericEvt;
                events = ev.getAWTEvent();
                processAWTEvent(events);
            }
        }
        wakeupOn(wakeupCondition);
    }

    /**
     *  Define el comportamiento a seguir cuando se detecta un evento en el
     *  teclado (AWT).
     */
    private void processAWTEvent(AWTEvent[] events) {
        for (int n = 0; n < events.length; n++) {
            if (events[n] instanceof KeyEvent) {
                KeyEvent eventKey = (KeyEvent) events[n];
                if (eventKey.getID() == KeyEvent.KEY_PRESSED) {
                    int keyCode = eventKey.getKeyCode();
                    //int keyChar = eventKey.getKeyChar();
                    switch (keyCode) {
                        case KeyEvent.VK_W:
                            tgManipulator.stepForward();
                            break;

                        case KeyEvent.VK_S:
                            tgManipulator.stepBackward();
                            break;

                        case KeyEvent.VK_SPACE:
                            tgManipulator.riseUp();
                            break;

                        case KeyEvent.VK_X:

                            tgManipulator.goDown();
                            break;

                        case KeyEvent.VK_A:
                            tgManipulator.turnLeft();
                             break;

                        case KeyEvent.VK_D:
                            tgManipulator.turnRight();
                            break;

                        case KeyEvent.VK_Q:
                            tgManipulator.stepLeftward();
                            break;

                        case KeyEvent.VK_E:
                            tgManipulator.stepRightward();
                            break;

                    }
                }
            }
        }
    }

    /**
     * Devuelve el TransformGroupManipulator con el que se está manipulando la
     * cámara para compartirlo si es necesario.
     * @return TransformGroup que manipula la cámara.
     */
    public TransformGroupManipulator getTransformGroupManipulator(){
        return tgManipulator;
    }
}
