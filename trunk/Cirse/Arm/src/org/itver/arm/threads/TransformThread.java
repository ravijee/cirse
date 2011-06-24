/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.threads;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;
import org.itver.arm.models.elements.AngleType;
import org.itver.arm.models.elements.Element;
import org.itver.arm.models.elements.ElementType;
import org.itver.arm.models.elements.Piece;

/**
 *
 * @author pablo
 */
public class TransformThread extends Thread{
    private Element invoker;
    private boolean rotate;

    public TransformThread(Element invoker, boolean rotate){
        this.invoker = invoker;
        this.rotate = rotate;
    }

    @Override
    public void run(){
        if(rotate)
            rotate();
        else
            move();
    }

    private void rotate() {
        Vector3d pos = new Vector3d(invoker.getPosition());
        Piece piece = (Piece)invoker;
        if(invoker.getType() == ElementType.joint)
            updateRotation(invoker.getParentTg(), piece.getAngles(AngleType.joint));
        updateRotation(invoker.getMainTg(), piece.getAngles(AngleType.current));
    }

    private void move() {
        Vector3d translation = new Vector3d(invoker.getPosition());
        if(invoker.getType() == ElementType.joint)
            updatePosition(invoker.getParentTg(), translation);
        else
            updatePosition(invoker.getMainTg(), translation);
    }

    private void updateRotation(TransformGroup tg, double[] angles) {
        Transform3D t3d = new Transform3D();
        tg.getTransform(t3d);
        Transform3D newTg = new Transform3D();
        Vector3d translation = new Vector3d();
        t3d.get(translation);
        for (int i = 0; i < angles.length; i++) {
            double d = Math.toRadians(angles[i]);
            switch(i){
                case 0: t3d.rotX(d); break;
                case 1: t3d.rotY(d); break;
                case 2: t3d.rotZ(d); break;
            }
            newTg.mul(t3d);
        }
        newTg.setTranslation(translation);
        tg.setTransform(newTg);
    }

    private void updatePosition(TransformGroup tg, Vector3d translation) {
        Transform3D trans3d = new Transform3D();
        tg.getTransform(trans3d);
        trans3d.setTranslation(translation);
        tg.setTransform(trans3d);
    }
}
