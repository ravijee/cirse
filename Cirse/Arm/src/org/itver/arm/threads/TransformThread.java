/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.threads;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;
import org.itver.arm.models.elements.Element;

/**
 * Thread encargado de actualizar el TransformGroup principal de cualquier
 * {@link Element Element}
 * @author pablo
 */
public class TransformThread extends Thread{

    private TransformGroup invokerTG;
    private double[] rotation;
    private Vector3d translation;

    private TransformThread(){
    }
    
    private TransformThread(TransformGroup tg){
        this.invokerTG = tg;
    }

    public TransformThread(TransformGroup tg, Vector3d traslation, double [] rotation){
        this(tg, traslation);
        this.rotation = rotation;
    }

    public TransformThread(TransformGroup tg, Vector3d translation){
        this(tg);
        this.translation = translation;
    }

    public TransformThread(TransformGroup tg, double[] rotation){
        this(tg);
        this.rotation = rotation;
    }

    @Override
    public void run(){
        if(this.invokerTG != null){
            if(this.rotation != null)
                updateRotation();
            if(this.translation != null)
                updatePosition();
        }

    }

    private void updateRotation() {
        Transform3D t3d = new Transform3D();
        this.invokerTG.getTransform(t3d);
        Transform3D newTg = new Transform3D();
        Vector3d transAux = new Vector3d();
        t3d.get(transAux);
        for (int i = 0; i < this.rotation.length; i++) {
            double d = Math.toRadians(this.rotation[i]);
            switch(i){
                case 0: t3d.rotX(d); break;
                case 1: t3d.rotY(d); break;
                case 2: t3d.rotZ(d); break;
            }
            newTg.mul(t3d);
        }
        newTg.setTranslation(transAux);
        this.invokerTG.setTransform(newTg);
    }

    private void updatePosition() {
        Transform3D trans3d = new Transform3D();
        this.invokerTG.getTransform(trans3d);
        trans3d.setTranslation(translation);
        this.invokerTG.setTransform(trans3d);
    }
}
