/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.graphics.controller;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;


/**
 * Esta clase maniupla un {@link javax.media.j3d.TransformGroup} para aplicar
 * diversas transformaciones a la cámara.
 *
 * <br/>Una instancia de esta clase es compartida por las clases
 * {@link org.itver.graphics.guitools.Control} y
 * {@link org.itver.graphics.controller.CameraKeyBehavior}, con lo cual se logra
 * manipular la cámara por medio de la interfaz gráfica de usuario, del
 * teclado o ambos.
 *
 * <br/>La cámara realiza cuatro movimientos:
 * <ul>
 *  <li>Movimientos de profundidad: Desplazamientoo en el eje z.</li>
 *  <li>Movimientos de altura: Depslazamiento en el eje y.</li>
 *  <li>Movimientos laterales: Desplazamiento en el eje x.</li>
 *  <li>Movimientos giratorios: Giro sobre su propio eje.</li>
 *
 * </ul>
 * <br/>Existen dos valores de importancia para lograr estos movimientos.
 * <ul>
 *  <li>{@code Factor}: Define la magnitud del movimiento oen profundidad de la cámara.
 *      Entre más alto sea el valor del factor, más grandes serán los "pasos"
 *      que da la cámara al avanzar.</li>
 *  <li>{@code Degrees}: Define la cantidad de grados que la cámara girará cuando se
 *      le indique que debe girar.
 * </ul>
 * <br>Los movimientos afectados por el {@code factor} son de profundidad,
 *     altura y laterales; mientras que los movimientos giratorios sólo dependen
 *     de {@code degrees}.
 *
 * @author Karo
 */
public class TransformGroupManipulator{
    private double angle;
    private double degrees;
    private double factor;
    private TransformGroup targetTG; //TransformGroup compartido para manipular la camara
    private Transform3D t3d;
    private Vector3f translate;

    /**
     * Crea una instancia de esta clase con valores predeterminados.
     * @param targetTG
     */
    public TransformGroupManipulator(TransformGroup targetTG){
        this.targetTG = targetTG;
        translate = new Vector3f();
        t3d = new Transform3D();
        //************Estos tres valores son los defaults pero los debe tomar
        angle = 0;    //del archivo.
        degrees = 3;
        factor = 0.25;
        //*****************************************************************
    }

    /**
     * Mueve la cámara un paso hacia adelante.
     */
    public void stepForward(){
        getTransform();
        translate.z += -factor * Math.cos(Math.toRadians(angle));
        translate.x += -factor * Math.sin(Math.toRadians(angle));
        setTransform();
    }

    /**
     * Mueve la cámara un paso hacia atrás.
     */
    public void stepBackward(){
        getTransform();
        translate.z += factor * Math.cos(Math.toRadians(angle));
        translate.x += factor * Math.sin(Math.toRadians(angle));
        setTransform();
    }

    /**
     * Mueve la cámara un paso hacia la izquierda.
     */
    public void stepLeftward(){
        getTransform();
        translate.x += -factor * Math.cos(Math.toRadians(angle));
        translate.z += factor * Math.sin(Math.toRadians(angle));
        setTransform();
    }

    /**
     * Mueve la cámara un paso a la derecha.
     */
    public void stepRightward(){
        getTransform();
        translate.x += factor * Math.cos(Math.toRadians(angle));
        translate.z += -factor * Math.sin(Math.toRadians(angle));
        setTransform();
    }

    /**
     * Sube la cámara.
     */
    public void riseUp(){
        getTransform();
        translate.y += factor;
        setTransform();
    }

    /**
     * Baja la cámara.
     */
    public void goDown(){
        getTransform();
        translate.y += -factor;
        setTransform();
    }

    /**
     * Gira hacia la izquierda.
     */
    public void turnLeft(){
        getTransform();
        Transform3D rot = new Transform3D();
        angle+=degrees;
        rot.rotY(Math.toRadians(degrees));
        t3d.mul(rot);
        setTransform();
    }

    /**
     * Gira hacia la derecha.
     */
    public void turnRight(){
        getTransform();
        Transform3D rot = new Transform3D();
        angle -= degrees;
        rot.rotY(Math.toRadians(-degrees));
        t3d.mul(rot);
        setTransform();
    }

    /**
     * Obtiene la transformación del objeto.
     */
    private void getTransform(){
        targetTG.getTransform(t3d);
        t3d.get(translate);
    }

    /**
     * Mueve o transforma el objeto.
     */
    private void setTransform(){
        t3d.setTranslation(translate);
        targetTG.setTransform(t3d);
    }

    /**
     * Devuelve el ángulo de giro.
     * @return Valor del ángulo.
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Establece el ángulo de giro.
     * @param angle Ángulo de giro.
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Devuelve el factor de movimiento.
     * @return Factor de movimiento.
     */
    public double getFactor() {
        return factor;
    }

    /**
     * Establece el factor de movimiento.
     * @param factor Magnintud del movimiento.
     */
    public void setFactor(double factor) {
        this.factor = factor;
    }

    /**
     * Devuelve la cantidad de grados que gira la cámara.
     * @return Grados para el giro.
     */
    public double getDegrees() {
        return degrees;
    }

    /**
     * Establece la cantidad de grados por los que gira la cámara.
     * @param mov Cantidad de grados a girar.
     */
    public void setDegrees(double mov) {
        this.degrees = mov;
    }

    /**
     * Obtiene la matriz de tranformaciones ({@link javax.media.j3d.Transform3D})
     * del objeto.
     * @return Transform3D del objeto.
     */
    public Transform3D getT3d() {
        return t3d;
    }

    /**
     * Establece una matriz de transformación ({@link javax.media.j3d.Transform3D})
     * nueva para el objeto.
     * @param t3d Nueva matriz de transformación.
     */
    public void setT3d(Transform3D t3d) {
        this.t3d = t3d;
    }

    /**
     * Devuelve el {@link javax.media.j3d.TransformGroup} asociado al objeto.
     * @return TransformGroup
     */
    public TransformGroup getTargetTG() {
        return targetTG;
    }

    /**
     * Establece un {@link javax.media.j3d.TransformGroup} nuevo para el objeto.
     * @param targetTG Nuevo TransformGroup.
     */
    public void setTargetTG(TransformGroup targetTG) {
        this.targetTG = targetTG;
    }

    /**
     * Obtiene el vector de dirección del objeto.
     * @return
     */
    public Vector3f getTranslate() {
        return translate;
    }

    /**
     * Establece un vector de dirección nuevo para el objeto.
     * @param translate Nuevo vector de dirección.
     */
    public void setTranslate(Vector3f translate) {
        this.translate = translate;
    }

}
