/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.controls.behaviors;

import javax.media.j3d.Alpha;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformInterpolator;
import javax.vecmath.Vector3d;
import org.itver.arm.models.elements.Axis;
import org.itver.arm.models.elements.Joint;
import org.itver.arm.models.elements.ValueType;
import org.itver.arm.models.log.Log;

/**
 * <p>Interpolator que cambia la orientación o posición de una
 * {@link Joint Articulación} de acuerdo al {@link Log Log}.</p>
 * <p>La modificación de la Articulación la modifica tomando como valor inicial
 * el {@link Joint#getJointValue(org.itver.arm.models.elements.ValueType)
 * valor de Articulación} y como final el asignado en
 * {@link #setTo(double)  setTo()}. La transformación de un valor a otro la
 * hace en tanto tiempo como el especificado en {@link #setTime(long) setTime()}
 * </p>
 * <p>Su uso se limita al proceso de simulado y es creado automáticamente por
 * cada nueva instancia de Joint, <b>no debe asignarse uno nuevo de forma
 * externa.</b></p>
 * <p><b>Ya cuenta con una clase Alpha que modifica automáticamente, no es
 * recomendable asignar una nueva</b></p>
 * <p>Uso:</p>
 * {@code
    Joint joint = new Joint();
    JointValueInterpolator interpolator = joint.getInterpolator();
    interpolator.set… //Modificar valores aquí
 * }
 * @author pablo
 */
public final class JointValueInterpolator extends TransformInterpolator{

    private Joint joint;
    private double to;
    private Alpha alpha;
    private double from;

    /**
     * Constructor principal donde se asigna este Interpolator a su respectiva
     * Articulación
     * @param joint Articulación relacionada con este Interpolator
     */
    public JointValueInterpolator(Joint joint){
        this(joint, 0, 0);
    }

    /**
     * Constructor que permite definir un valor final y un tiempo para que la
     * articulación alcance ese punto.
     * @param joint Articulación ligada al Interpolator.
     * @param to Valor final a alcanzar.
     * @param time Tiempo en el que se alcanzará el valor final. En milisegundos
     */
    public JointValueInterpolator(Joint joint, double to, long time){
        alpha = new Alpha();
        alpha.setMode(Alpha.INCREASING_ENABLE);
        alpha.setLoopCount(1);
        alpha.setIncreasingAlphaDuration(time);
        super.setAlpha(alpha);
        this.to = to;
        this.joint = joint;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void computeTransform(float value, Transform3D td) {
        double current = (to - from) * value + from;
        joint.setJointValue(ValueType.current, current);        
        Axis jointAxis = joint.getAxis();
        boolean rotate = joint.isMotion();
        Vector3d translate = new Vector3d(joint.getPosition());
//        double scale = td.getScale();
        if(rotate)
            this.rotate(current, td, jointAxis);
        else
            this.translate(current, translate, jointAxis);
        td.setTranslation(translate);
//        td.setScale(scale);
    }

    /**
     * Asigna el valor final a alcanzar por la articulación.
     * @param to Valor a alcanzar,
     */
    public void setTo(double to){
        this.to = to;
    }

    /**
     * Tiempo para alcanzar el valot final. en milisegundos.
     * @param time
     */
    public void setTime(long time){
        alpha.setIncreasingAlphaDuration(time);
    }

    /**
     * inicia la interpolación
     */
    public void start(){
        from = joint.getJointValue(ValueType.current);
        alpha.setStartTime(System.currentTimeMillis());
    }

    private void rotate(double current, Transform3D td, Axis jointAxis) {
        switch(jointAxis){
            case x:
                td.rotX(Math.toRadians(current));
                break;
            case y:
                td.rotY(Math.toRadians(current));
                break;
            case z:
                td.rotZ(Math.toRadians(current));
        }
    }

    private void translate(double current, Vector3d translate, Axis jointAxis){
        switch(jointAxis){
            case x:
                translate.x += current;
                break;
            case y:
                translate.y += current;
                break;
            case z:
                translate.z += current;
        }
        
    }

    @Override
    public void setAlpha(Alpha alpha){
        
    }

}
