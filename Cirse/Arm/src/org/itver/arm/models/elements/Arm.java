/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.models.elements;

import java.util.ArrayList;
import javax.vecmath.Vector3d;
import org.itver.arm.models.nodes.elements.ArmNode;
import org.itver.graphics.model.MSComponentContent;
import org.itver.graphics.model.MainSceneComponent;
import org.openide.nodes.Node;

/**
 * Modelo del brazo encargado de agrupar las primeras piezas del brazo. No todas
 * están asignadas dentro de éste. Por lo que es importante buscarlas en cada
 * {@link Joint Joint} concurrentemente, o bien utilizar los metodos del
 * {@link org.itver.arm.controls.controllers.ArmController controlador} del brazo
 * @author Pablo
 */
public class Arm extends Element implements MSComponentContent{

    /**
     * Nombre de la propiedad que cambia al asignar o retirar una pieza
     * @see #addPiece(org.itver.arm.models.elements.Piece) addPiece()
     * @see #removePiece(org.itver.arm.models.elements.Piece) removePiece()
     */
    public static final String NEXT = "next";

    /**
     * Nombre de la propiedad que cambia al modificar el soporte del brazo
     * @see #setSupport(javax.vecmath.Vector3d) setSupport()
     */
    public static final String SUPPORT = "support";

    /**
     * Nombre de la propiedad que cambia al asignar un nuevo contenedor en el
     * Entorno
     * @see #setContainer(org.itver.graphics.model.MainSceneComponent) setContainer()
     */
    public static final String CONTAINER = "container";

    private ArrayList<Piece> next = new ArrayList<Piece>();
    private Vector3d support;
    private MainSceneComponent container;

    /**
     * Constructor Default de un brazo, asigna un soporte de (0, -1, 0)
     * @see Element#Element()
     */
    public Arm(){
        super();
        this.support = new Vector3d(0, -1, 0);
    }

    /**
     * Crea un brazo con el vector de apoyo especificado.
     * @param x valor en x del vector
     * @param y valor en y del vector
     * @param z valor en z del vector
     */
    public Arm(double x, double y, double z) {
        this();
        setSupport(x, y, z);
    }

    /**
     * Crea un brazo con el vector de apoyo especificado.
     * @param support Vector de apoyo del brazo.
     */
    public Arm(Vector3d support) {
        this();
        setSupport(support);
    }

    /**
     * Devuelve la lista completa de las piezas que conforman este brazo
     * @return Array con la lista de piezas que contiene el brazo.
     */
    public Piece[] getNext() {
        Piece aux[] = new Piece[this.next.size()];
        return this.next.toArray(aux);
    }

    /**
     * Devuelve el vector de apoyo del brazo.
     * @return el vector de apoyo del brazo
     */
    public Vector3d getSupport() {
        return new Vector3d(support);
    }

    /**
     * Reemplaza el vector de apoyo del brazo.
     * @param support el Vector de Apoyo nuevo.
     */
    public final void setSupport(Vector3d support) {
        Vector3d old = new Vector3d(this.support);
        this.support.set(support);
        this.alert.firePropertyChange(SUPPORT, old, support);
    }

    /**
     * Reemplaza el vector de apoyo del brazo.
     * @param x el valor en x del Vector de apoyo.
     * @param y el valor en y del Vector de apoyo.
     * @param z el valor en z del Vector de apoyo.
     */
    public final void setSupport(double x, double y, double z) {
        setSupport(new Vector3d(x, y, z));
    }

    /**
     * Reemplaza el vector de apoyo del brazo.
     * @param vector Array de <b>longitud 3</b> con los valores del vector de apoyo
     * del brazo.
     */
    public void setSupport(double vector[]) {
        setSupport(new Vector3d(vector));
    }

    /**
     * Devuelve el {@link MainSceneComponent MainSceneComponent} que contiene
     * este brazo.
     * @return El contenedor del brazo o null si no se ha asignado uno.
     */
    public MainSceneComponent getContainer() {
        return container;
    }

    /**
     * Reemplaza el {@link MainSceneComponent MainSceneComponent} ligado al
     * brazo por uno nuevo
     * @param container the container to set
     */
    public void setContainer(MainSceneComponent container) {
        MainSceneComponent old = this.container;
        this.container = container;
        this.alert.firePropertyChange(CONTAINER, old, this.container);
    }

    /**
     * Agrega una nueva {@link Piece pieza} al brazo, agregándola al principio
     * de éste. Las piezas que necesiten ser agregadas consiguientes a otras
     * deben utilizar el metodo {@link Joint#addNext(org.itver.arm.models.elements.Piece)
     * Joint.addNext()}
     * @param piece la pieza a agregar al principio del brazo.
     */
    public void addPiece(Piece piece){
        piece.setArm(this);
        this.addChild(piece);
        this.next.add(piece);
        this.alert.firePropertyChange(NEXT, null, piece);
    }

    /**
     * Retira una {@link Piece pieza} del brazo. Si no existe no hace nada.
     * @param piece la Pieza a eliminar del brazo.
     */
    public void removePiece(Piece piece){
        piece.setArm(null);
        
        if(this.next.contains(piece)){
            piece.detach();
            this.next.remove(piece);
        }
        else if(piece.getJoint() != null)
            piece.getJoint().removeNext(piece);

        
        this.alert.firePropertyChange(NEXT, piece, null);
    }

    /**
     * Obtiene la lista de Nodos referentes al modelo.
     * @return la lista de Nodos referentes al modelo. Utilizados por la 
     * Plataforma de NetBeans.
     */
    @Override
    public Node[] getContentNode() {
        return new Node[]{new ArmNode(this)};
    }
    

    @Override
    public Arm clone(){
        Arm result = new Arm();
        result.setSupport(support);
        for(Piece piece : this.getNext())
            result.addPiece(piece.clone());
        return result;
    }
    
    @Override
    protected void finalize() throws Throwable{
        super.finalize();
        this.container = null;
        this.next.clear();
        this.next = null;
        this.support = null;
    }

}
