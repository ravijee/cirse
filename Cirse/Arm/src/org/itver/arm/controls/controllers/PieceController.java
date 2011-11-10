/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.controls.controllers;

import java.io.File;
import java.util.ArrayList;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.models.elements.Element;
import org.itver.arm.models.elements.ElementType;
import org.itver.arm.models.elements.Joint;
import org.itver.arm.models.elements.Piece;

/**
 * Controlador para las Piezas, contiene métodos para su creación, búsqueda y
 * control.
 * <b>Clase singleton</b>
 * @author pablo
 */
public final class PieceController {
    private static PieceController instance;
    private Piece foundPiece;       //Pieza a encontrar por Id
    private ArrayList<Piece> foundPieces;

    private PieceController(){
        this.foundPieces = new ArrayList<Piece>();
    }

    public synchronized static PieceController singleton(){
        if(instance == null)
            instance = new PieceController();
        return instance;
    }

    /**
     * Crea una nueva Pieza con diseño basado en el archivo COLLADA.
     * @param file Archivo COLLADA con el diseño de la pieza.
     * @param type Tipo de la Pieza a crear.
     * @return La Pieza creada.
     */
    public Piece newFromFile(File file, ElementType type){
        Piece result;
        result = new Piece(type);
        result.setSource(file);
        return result;
    }

    /**
     * Modifica la orientación de la Pieza de acuerdo a los valores dados.
     * @param piece Pieza a modificar.
     * @param vals Array con los nuevos valores de orientación.
     * @see Piece#setOrientation(double[]) Piece.setOrientation()
     */
    public void setOrientation(Piece piece, double[] vals) {
        piece.setOrientation(vals);
    }

    /**
     * Agrega a la Pieza una Fricción en la posición dada.
     * @param piece Pieza a asignar una nueva fricción.
     * @param vals Posición de la Fricción a crear.
     */
    public void addFrictionToPiece(Piece piece, double[] vals) {
        piece.addFriction(vals);
    }

    /**
     * Busca en un Brazo una Pieza de acuerdo a su ID.
     * @param arm Brazo en el que se buscará la pieza
     * @param id ID de la pieza a buscar.
     * @return la Pieza correspondiente al ID, {@code null} si no existe.
     */
    public Piece getPieceById(Arm arm, int id){
        this.foundPiece = null;
        for(Piece p:arm.getNext())
            findPiece(p, id, null);
        return this.foundPiece;
    }

    /**
     * Busca en el Brazo todas las Piezas del tipo especifico.
     * @param arm Brazo donde se realizará la búsqueda de las Piezas.
     * @param type Tipo de las piezas a buscar.
     * @return Arreglo de Piezas del mismo tipo.
     */
    public Piece[] getPiecesByType(Arm arm, ElementType type){
        Piece[] result = null;
        this.foundPieces.clear();
        for(Piece p : arm.getNext())
            findPiece(p, Element.DEFAULT_ID, type);
        result = new Piece[this.foundPieces.size()];
        return this.foundPieces.toArray(result);
    }

    private void findPiece(Piece p, int id, ElementType type) {
        if(p.getId() == id){
            this.foundPiece = p;
            return;
        }
        if(p.getType() == type)
            this.foundPieces.add(p);
        if(p instanceof Joint){
            Joint j = (Joint)p;
            for(Piece p2 : j.getNext())
                findPiece(p2, id, type);
        }
    }
}