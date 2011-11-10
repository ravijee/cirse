/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.models.elements;

import java.util.ArrayList;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Group;
import javax.media.j3d.Interpolator;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;
import org.itver.arm.controls.behaviors.JointValueInterpolator;
import org.itver.arm.threads.TransformThread;

/**
 * Modela una Articulación en el Brazo. 
 * @author pablo
 */
public class Joint extends Piece{

    /**
     * Nombre de la propiedad que cambia al modificar el tipo de Articulación
     * @see #setMotion(boolean) setMotion()
     */
    public static final String MOTION = "motion";

    /**
     * Nombre de la propiedad que cambia al modificar alguno de los valores propios de la articulación
     * @see #setJointValue(org.itver.arm.models.elements.ValueType, double) setJointValue()
     */
    public static final String JOINT_VALUE = "val";

    /**
     * Nombre de la propiedad que cambia al insertar o retirar {@link Piece piezas}
     * dependientes de esta Articulación
     */
    public static final String NEXT = "next";


    /**
     * Nombre de la propiedad que cambia al modificar el eje de libertad sobre
     * el que trabaja esta Articulacion.
     */
    public static final String AXIS = "axis";

    /**
     *Identifica a la Articulación como Rotacional (R)
     * @see #setMotion(boolean) setMotion()
     */
    public final static boolean ROTATIONAL      = true;

    /**
     * Identifica a la Articulación como Traslacional (T)
     * @see #setMotion(boolean) setMotion()
     */
    public final static boolean TRANSLATIONAL   = false;

    private boolean motion;
    private TransformGroup jointTG;
    private ArrayList<Piece> next;
    private JointValueInterpolator interpolator;
    private Axis axis;
    private double[] jointVals;

    /**
     * Constructor default. Crea una Articulación de tipo Rotacional.
     * @see Piece#Piece() Piece()
     */
    public Joint(){
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void initBranch(){
        initAttrs();
        this.removeAllChildren();
        this.addChild(this.jointTG);
        this.jointTG.addChild(this.mainTg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updatePosition3D(){
        TransformThread thread = new TransformThread(this.jointTG, new Vector3d(this.position));
        thread.start();
    }

    /**
     * Asigna una nueva {@link Piece Pieza} como dependiente de esta articulación.
     * A la pieza se le {@link Piece#setJoint(org.itver.arm.models.elements.Joint) asigna }
     * esta articulación como la padre, así como el brazo de éste, automáticamente.
     * @param piece La pieza a añadir como dependiente de la Articulación.
     */
    public void addNext(Piece piece){
        this.jointTG.addChild(piece);
        piece.setJoint(this);
        piece.setArm(this.getArm());
        this.next.add(piece);
        this.alert.firePropertyChange(NEXT, null, piece);
    }

    /**
     * Obtiene la lista de piezas que son dependientes de ésta articulación.
     * @return Copia del array con la lista de piezas dependientes de ésta articulación.
     */
    public Piece[] getNext(){
        Piece[] result = new Piece[this.next.size()];
        this.next.toArray(result);
        return result;
    }

    /**
     * Retira una pieza como dependiente de la articulación, Quitándola también
     * del BranchGroup y asignando null en su
     * {@link Piece#setJoint(org.itver.arm.models.elements.Joint) Joint}
     * y {@link Piece#setArm(org.itver.arm.models.elements.Arm) Brazo}.
     * @param piece la pieza a retirar del grupo de piezas dependientes de la
     * Articulación. Si la pieza no es actualmente parte de ésta, no hace nada.
     */
    public void removeNext(Piece piece){
        if(this.next.contains(piece)){
            if(piece instanceof Joint){
                Joint joint = (Joint)piece;
                fixNextFromJoint(joint.getNext());
            }
            piece.detach();
            piece.setJoint(null);
            piece.setArm(this.getArm());
            this.next.remove(piece);
            this.alert.firePropertyChange(NEXT, piece, null);
        }
    }

    /**
     * Obtiene el tipo de Articulación
     * @return el tipo de articulación
     * @see #ROTATIONAL ROTACIONAL
     * @see #TRANSLATIONAL TRASLACIONAL
     *
     */
    public boolean isMotion() {
        return motion;
    }

    /**
     * Cambia el tipo de Articulación
     * @param motion el tipo de Articulación
     * @see #ROTATIONAL ROTACIONAL
     * @see #TRANSLATIONAL TRASLACIONAL
     */
    public void setMotion(boolean motion) {
        boolean old = this.motion;
        this.motion = motion;
        this.startJointTrans();
        this.alert.firePropertyChange(MOTION, old, this.motion);
    }

    /**
     * Obtiene el {@link Interpolator Interpolator} asociado con esta Articulación
     * @return el Interpolator asociado a la articulación.
     * @see Interpolator Interpolator (Java 3D)
     */
    public JointValueInterpolator getInterpolator(){
        return this.interpolator;
    }

    /**
     * Obtiene el valor de Articulación.
     * El valor de Articulación puede estar en grados o cm. de acuerdo a el tipo
     * de Articulación que sea.
     * @param type El tipo de valor a obtener (Mínimo, Máximo, Actual)
     * @return El valor deseado en base al tipo.
     * @see #setMotion(boolean) Tipo de Articulación
     */
    public double getJointValue(ValueType type){
        return this.jointVals[type.ordinal()];
    }

    /**
     * Asigna el valor de la Articulación.
     * El valor de Articulación puede estar en grados o cm. de acuerdo a el tipo
     * de Articulación que sea.
     * @param type El tipo de valor a asignar
     * @param value El valor de la articulación (grados o cm, dependiendo de el
     * tipo)
     * @see #setMotion(boolean) Tipo de Articulación
     */
    public void setJointValue(ValueType type, double value){
        double old = this.jointVals[type.ordinal()];
        this.jointVals[type.ordinal()] = value;
        this.startJointTrans();
        this.alert.firePropertyChange(JOINT_VALUE, old, value);
    }

    /**
     * Obtiene el valor mínimo de la Articulación.
     * @return el valor mínimo de la Articulación
     */
    public double getJointMin(){
        return this.jointVals[ValueType.min.ordinal()];
    }

    /**
     * Asigna el valor mínimo de la Articulación
     * @param min El valor mínimo que podrá tener la Articulación
     */
    public void setJointMin(double min){
        double old = this.getJointMin();
        this.jointVals[ValueType.min.ordinal()] = min;
        this.alert.firePropertyChange(JOINT_VALUE, old, min);
    }

    /**
     * Obtiene el máximo valor de la Articulación
     * @return el máximo valor de la Articulación
     */
    public double getJointMax(){
        return this.jointVals[ValueType.max.ordinal()];
    }

    /**
     * Asigna el máximo valor de la Articulación.
     * @param max el valor máximo que podrá tener la Articulación
     */
    public void setJointMax(double max){
        double old = this.getJointMax();
        this.jointVals[ValueType.max.ordinal()] = max;
        this.alert.firePropertyChange(JOINT_VALUE, old, max);
    }

    /**
     * Obtiene el eje de libertad sobre el que actúa la Articulación,
     * @return Objeto con el eje de libertad sobre el que actúa la Articulación
     * (x y z)
     */
    public Axis getAxis(){
        return this.axis;
    }

    /**
     * Asigna el eje de libertad sobre el que actúa la Articulación,
     * @param axis Eje de libertad sobre el que actúa la Articulación,
     */
    public void setAxis(Axis axis){
        Axis old = this.axis;
        this.axis = axis;
        this.startJointTrans();
        this.alert.firePropertyChange(AXIS, old, this.axis);
    }
    
    @Override
    public Joint clone(){
        Joint result = (Joint)super.clone();
        result.setAxis(axis);
        for(ValueType type: ValueType.values())
            result.setJointValue(type, this.getJointValue(type));
        result.setMotion(motion);
        for(Piece piece : this.getNext())
            result.addNext(piece.clone());
        return result;
    }
    
    @Override
    protected void finalize() throws Throwable{
        super.finalize();
        this.axis = null;
        this.interpolator = null;
        this.next.clear();
        this.next = null;
        this.jointVals = null;
        this.jointTG = null;
    }

    private void initAttrs(){

        this.jointVals = new double[ValueType.values().length];
        this.jointVals[ValueType.max.ordinal()] = 360;

        this.axis = Axis.x;

        this.jointTG = new TransformGroup();
        this.jointTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.jointTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        this.jointTG.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        this.jointTG.setCapability(Group.ALLOW_CHILDREN_WRITE);

        this.next = new ArrayList<Piece>();

        this.motion = ROTATIONAL;

        this.interpolator = new JointValueInterpolator(this);
        this.interpolator.setTarget(this.jointTG);
        this.interpolator.setEnable(false);
        this.interpolator.setSchedulingBounds(new BoundingSphere());
        this.jointTG.addChild(interpolator);
    }

    private void fixNextFromJoint(Piece[] pieces){
        for(Piece piece : pieces){
            piece.detach();
            this.addNext(piece);
        }
    }

    private void startJointTrans(){
        TransformThread thread;
        double rots[] = new double[Axis.values().length];
        rots[this.axis.ordinal()] = this.jointVals[ValueType.current.ordinal()];
        if(this.motion){ //ROTATE
            thread = new TransformThread(this.jointTG,
                                        rots);
        }
        else{          //TRANSLATE
            Vector3d sum = new Vector3d(rots);
            sum.add(this.position);
            thread = new TransformThread(this.jointTG,
                          sum);
        }
        thread.start();
    }
}
