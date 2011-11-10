/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.controls.controllers;

import java.io.File;
import org.itver.arm.models.elements.ElementType;
import org.itver.arm.models.elements.Joint;
import org.itver.arm.models.elements.Piece;
import org.itver.arm.models.elements.ValueType;

/**
 * Controlador de las Articulaciones. Contiene métodos de creación, modificación
 * y asignación de piezas.
 * <b>Clase singleton</b>
 * @author pablo
 */
public final class JointController{
    private static JointController instance;

    private JointController(){
    }

    public synchronized static JointController singleton(){
        if(instance == null)
            instance = new JointController();
        return instance;
    }

    /**
     * Crea una nueva Articulación con diseño basado en el archivo COLLADA dado.
     * @param file Archivo COLLADA con el diseño de la Articulación.
     * @return Articulación creada.
     */
    public Joint newFromFile(File file){
        Joint result = new Joint();
        result.setSource(file);
        return result;
    }

    /**
     * Asigna el valor de articulación a la Articulación dada
     * @param joint Articulación a modificar el valor.
     * @param valueType tipo de valor de articulación.
     * @param value Valor de articulación a cambiar.
     */
    public void setAngleVal(Joint joint, ValueType valueType, double value) {
        joint.setJointValue(valueType, value);
    }

    /**
     * Agrega la pieza dada en la articulación.
     * @param joint Articulación que contendrá la pieza.
     * @param piece Pieza que dependerá de la Articulación.
     */
    public void insertPiece(Joint joint, Piece piece){
        joint.addNext(piece);
    }

    /**
     * Crea una nueva Pieza a partir de un archivo COLLADA y la agrega en la
     * Articulación dad.
     * @param file Archivo COLLADA con el diseño de la pieza.
     * @param joint Articulación donde se añadirá la pieza.
     * @param type tipo de pieza, en caso de no ser Articulación,
     * @param isJoint {@code true} si la pieza es una articulación.
     */
    public void insertPieceFromFile(File file, Joint joint, ElementType type, boolean isJoint) {
        Piece piece;
        if(isJoint)
            piece = this.newFromFile(file);
        else 
             piece = PieceController.singleton().newFromFile(file, type);
        insertPiece(joint, piece);
    }
    
}
