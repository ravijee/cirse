/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.models.elements;

import java.beans.PropertyChangeListener;
import org.itver.arm.exceptions.ArmStructureException;
import java.util.ArrayList;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;
import org.itver.common.util.IdGenerator;

/**
 * Modelo del brazo encargada de agrupar las distintas piezas y tratarlas como
 * un brazo.
 * @author Pablo Antonio Guevara González
 */
public class Arm extends Element {

    private ArrayList<Piece> pieces = new ArrayList<Piece>();
    private Piece palm;
    private Vector3d support;
    private BranchGroup container;

    /**
     *Contructor default del brazo     *
     * @param id el id que tendrá el brazo.
     */
    public Arm(int id) {
        super(id, ElementType.arm);
        this.support = new Vector3d(0, -1, 0);
    }

    /**
     * Crea un brazo con el vector de apoyo especificado.
     * @param id id que tendrá el brazo.
     * @param x valor en x del vector
     * @param y valor en y del vector
     * @param z valor en z del vector
     */
    public Arm(int id, double x, double y, double z) {
        this(id);
        setSupport(x, y, z);
    }

    /**
     * Crea un brazo con el vector de apoyo especificado.
     * @param id id que tendrá el brazo.
     * @param support Vector de apoyo del brazo.
     */
    public Arm(int id, Vector3d support) {
        this(id);
        setSupport(support);
    }

    /**
     * Devuelve la lista completa de las piezas que conforman este brazo
     * @return Array con la lista de piezas que contiene el brazo.
     */
    public Piece[] getPieces() {
        Piece aux[] = new Piece[this.pieces.size()];
        return this.pieces.toArray(aux);
    }

    /**
     * Obtiene una pieza del brazo, de acuerdo a su id
     * @param id El id de la pieza a buscar
     * @return la pieza que corresponde a su id, null si el id no está en el
     * brazo. No retorna el brazo si se le especifica el id de éste.
     */
    public Piece getPiece(int id) {
        for (int i = 0; i < this.pieces.size(); i++) {
            if (this.pieces.get(i).getId() == id) {
                return this.pieces.get(i);
            }
        }
        return null;
    }

    public void addListenerToPieces(PropertyChangeListener listener){
        for(Piece p: this.pieces)
            p.addListener(listener);
    }

    public void removeListenerToPieces(PropertyChangeListener listener){
        for(Piece p: this.pieces)
            p.removeListener(listener);
    }

    /**
     * Agrega una pieza al brazo, ésta debe tener sus relaciones asignadas.
     * @param piece pieza a agregar al brazo.
     * @throws ArmStructureException Si ya hay una pieza con el mismo id
     * @throws ArmStructureException Si se quiere agregar más de una palma.
     * @throws ArmStructureException Si se quiere agregar un brazo al brazo.
     */
    public void insertPiece(Piece piece) throws ArmStructureException{
        TransformGroup parent;
        if (piece.getParentId() == -1) {
            parent = newTg(this);

//            this.addChild(parent);
        } else {
            parent = findParentJoint(piece).getParentTg();
        }
        switch (piece.getType()) {
            case joint:
                TransformGroup old = parent;
                parent = newTg(old);
//                old.addChild(parent);
                break;
            case palm:
                if (palm == null) {
                    palm = piece;
                } else {
                    throw new ArmStructureException("Ya hay una palma.");
                }
                break;
            case arm:
                throw new ArmStructureException("No se puede agregar un tipo: '"
                        + piece.getType() + "'.");
        }

        piece.setParentTg(parent);
        parent.addChild(piece);
        piece.setArm(this);
    }

    public void addPiece(Piece piece) throws ArmStructureException{
        if (isId(piece.getId())) {
            throw new ArmStructureException("el Id "
                    + piece.getId()
                    + " ya ha sido asignado a otra pieza");
        } if(piece.getRelations().length < 1)
            piece.setParentId(this.pieces.get(this.pieces.size() - 1).getId());
        this.pieces.add(piece);
        insertPiece(piece);
    }

    private TransformGroup newTg(Group cont) {
        BranchGroup bg = new BranchGroup();
        bg.setCapability(BranchGroup.ALLOW_DETACH);
        TransformGroup result = new TransformGroup();
        result.setCapability(Group.ALLOW_CHILDREN_WRITE);
        result.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        result.setCapability(Group.ALLOW_CHILDREN_READ);
        result.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        result.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        bg.addChild(result);
        cont.addChild(bg);
        return result;
    }

    public void removePiece(Piece piece) {
        piece.setSelected(false);
        piece.detach();
        Piece before =  null;
        if(piece.getParentId() != -1){
            before = this.getPiece(piece.getParentId());
            before.delRelation(before.getRelationId(piece.getId()));
        }
        Piece after[] = new Piece[piece.getRelations().length - 1];
        for(int i = 0; i < after.length; i++){
            after[i] = this.getPiece(piece.getRelation(i + 1));
            if(before != null){
                after[i].setParentId(before.getId());
                before.addRelation(after[i].getId());
            }
        }
        this.pieces.remove(piece);
        piece = null;
        updateArm();
    }

    public void updateArm(){
        this.palm = null;
        for(Piece p: this.getPieces())
            p.detach();

        for(Piece p: this.getPieces())
            try {
                this.insertPiece(p);
                p.updateTransforms();
            } catch (ArmStructureException ex) {
                System.err.println(ex.getMessage());
            }
        
    }

    public void removePiece(int id) {
        this.removePiece(this.getPiece(id));
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
        this.alert.firePropertyChange("support", old, support);
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
     * Cambia la id de una pieza en caso que su id ya exista.
     * @param p la pieza a cambiar su id
     */
    private void fixId(Piece p) {
        while (isId(p.getId())) {
            p.setId(IdGenerator.generateId());
        }
    }

    /**
     * Determina si ya hay una pieza con la id especificada en el brazo.
     * @param id el id a buscar si ya existe en otra pieza.
     * @return true si ya hay una pieza con la id especificada.
     */
    public boolean isId(int id) {
        return this.getPiece(id) != null ? true : false;
    }

    /**
     * Devuelve el Joint padre más cercano de la pieza especificada
     * @param piece la pieza a la que se le buscará el Joint padre
     * @return el Joint padre de la pieza
     * @throws ArmStructureException si la pieza no ha sido agregada en el
     * brazo.
     */
    private Piece findParentJoint(Piece piece) throws ArmStructureException {
        if (!isId(piece.getParentId())) {
            throw new ArmStructureException("el Id: " + piece.getParentId()
                    + " no ha sido agregado");
        }
        Piece parent = getPiece(piece.getParentId());
        while (parent.getType() != ElementType.joint) {
            parent = getPiece(parent.getParentId());
        }
        return parent;
    }

    /**
     * @return the container
     */
    public BranchGroup getContainer() {
        return container;
    }

    /**
     * @param container the container to set
     */
    public void setContainer(BranchGroup container) {
        this.container = container;
    }

    public Piece getSelectedPiece() {
        for (Piece p : this.getPieces()) {
            if (p.isSelected()) {
                return p;
            }
        }
        return null;
    }

    private void openHand(double open){
        int[] handRels = this.palm.getRelations();
        for(int i = 1; i < handRels.length; i++)
            if(this.getPiece(handRels[i]).getType() == ElementType.joint)
                this.getPiece(handRels[i]).setAngle(AngleType.joint,
                                                    Axis.x,
                                                    open);
    }
}
