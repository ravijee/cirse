/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.models.elements;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import org.itver.arm.threads.TransformThread;
import org.itver.common.util.IdGenerator;

/**
 *  Modelo padre de cada elemento del brazo, incluído el mismo brazo, las clases 
 * que quisieran representar el modelo de alguna pieza del brazo, deberían 
 * heredar de ésa clase.
 *
 * @author Pablo Antonio Guevara González
 */
public class Element extends BranchGroup{
    /**
     * Id defult para el contructor de la pieza, si éste se asigna, se le dará
     * un valor progresivo que aumenta cada vez que se crea una pieza con este
     * id
     */
    public static final int DEFAULT_ID = -1;
    
    private     int             id;

    /**
     * Objeto encargado de avisar a los oyentes cuando un atributo ha cambiado.
     */
    protected   PropertyChangeSupport alert;

    /**
     * Tipo de elemento que es esta instancia
     * @see models.elements.ElementType Tipos
     */
    protected   ElementType     type;

    /**
     * Posición del centro de la pieza en el mundo virtual
     */
    protected   Point3d         position;
    /**
     * Tipo de posición que ocupa este elemento.
     * Si es verdadero, su posición será sumada con la de los elementos que lo
     * contienen.
     */
    protected   boolean         relative;
    /**
     * Ruta del archivo que contiene la geometría y apariencia de este elemento.
     * @see models.elements.Piece#setSource(java.lang.String, javax.media.j3d.Node)
     * Piece.setSource
     * @see controls.generate.PieceInterpreter#setSource(java.lang.String)
     * PieceInterpreter.setSource
     */
    protected   File            source;
    /**
     * Estado del modelo en determinado momento de la aplicacion.
     * @see models.elements.State States
     */
    protected   boolean         selected;
    /**
     * TransformGroup más próximo que contiene este elemento.
     */
    protected   TransformGroup  parentTg;

    protected TransformGroup  mainTg;

    /**
     * Constructor general de un elemento
     */
    private Element(){
        super();
        this.alert        = new PropertyChangeSupport(this);
        this.position     = new Point3d();
//        this.source       = "";
        this.mainTg = new TransformGroup();
        this.addChild(mainTg);
        this.setPickable(true);

        this.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        this.setCapability(Group.ALLOW_CHILDREN_WRITE);
        this.setCapability(Group.ALLOW_CHILDREN_READ);
        this.mainTg.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        this.mainTg.setCapability(Group.ALLOW_CHILDREN_WRITE);
        this.mainTg.setCapability(Group.ALLOW_CHILDREN_READ);
        this.mainTg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        this.mainTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        this.setCapability(BranchGroup.ALLOW_DETACH);
    }

    /**
     * Contrcutor default de un elemento, para el id {@link #DEFAULT_ID
     * DEFAULT_ID} puede ser tomado.
     *
     * @param id el id que tendrá este elemento.
     *
     * @param type el tipo de elemento que será
     * @see models.elements.ElementType Tipos.
     */
    protected Element(int id, ElementType type){
        this();
        int n = IdGenerator.generateId();
        setId(id < 0 ? n : id);
        setType(type);
    }

    protected Element(int id, ElementType type, File src){
        this(id, type);
        this.source = src;
    }

    /**
     * Asigna un oyente a los cmabios en los atributos.
     * @param listener oyente de los cambios en los atributos.
     */
    public void addListener(PropertyChangeListener listener){
        this.alert.addPropertyChangeListener(listener);
    }

    /**
     * Asigna un oyente a los cambios del atributo especificado.
     * @param listener oyente de los cambios del atributo.
     * @param propertyName nombre del atributo a escuchar.
     */

    public void addListener(PropertyChangeListener listener,
                                          String propertyName){
        this.alert.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Determina si este objeto tiene al menos un oyente.
     * @param propertyName
     * @return true si hay al menos un oyente
     */
    public boolean hasListeners(String propertyName){
        return this.alert.hasListeners(propertyName);
    }

    public void removeListener(PropertyChangeListener listener){
        this.alert.removePropertyChangeListener(listener);
    }

    /**
     * Obtiene la id asignada a este elemento
     * @return la id asignada.
     */
    public int getId() {
        return id;
    }

    /**
     * cambia la id por una nueva
     * @param id nueva id
     */
    public final void setId(int id){
        int old = this.id;
        this.id = id;
        this.alert.firePropertyChange("id", old, id);
    }

    /**
     * Obtiene el tipo de elemento de este objeto.
     *
     * @return El tipo de elemento
     * @see models.elements.ElementType Tipos
     */
    public ElementType getType() {
        return type;
    }

    /**
     * Obtiene la posición del centro de este objeto.
     * @return la posición del objeto, de acuerdo al tipo de posición
     * @see #isRelative()
     */
    public Point3d getPosition() {
        return new Point3d(position);
    }

    /**
     * Modifica la posición del objeto por una nueva
     * @param position El punto de la nueva posición del objeto.
     */
    public void setPosition(Point3d position) {
        Point3d old = new Point3d(this.getPosition());
        this.position.set(position);
        this.alert.firePropertyChange("position", old, position);
        TransformThread thread = new TransformThread(this, false);
        thread.start();
//        updatePosition();
    }

    /**
     * Modifica la posición del objeto por una nueva.
     * @param x el valor en x del punto
     * @param y el valor en y del punto
     * @param z el valor en z del punto
     */
    public void setPosition(double x, double y, double z){
        setPosition(new Point3d(x, y, z));
    }

    /**
     * Modifica la posición del objeto por una nueva
     * @param position array de tamaño 3 con los valores del punto de la
     * posición.
     */
    public void setPosition(double position[]){
        setPosition(new Point3d(position));
    }

    /**
     * Traslada la posición de acuerdo a la fórmula position += translation;
     * @param translation vector con la traslación de la posición del punto.
     */
    public void translate(Vector3d translation){
        Point3d newPos = new Point3d();
        newPos.add(this.position, translation);
        setPosition(newPos);
    }
    /**
     * Modifica el tipo de el elemento.
     * @param type El nuevo tipo del elemento.
     * @see models.elements.ElementType Tipos.
     */
    public final void setType(ElementType type) {
        this.setPickable((type == ElementType.joint) ||
                         (type == ElementType.arm));
        ElementType old = getType();
        this.type = type;
        this.alert.firePropertyChange("type", old, type);
    }

    /**
     * Determina si la posición del objeto es relativa o no.
     * En caso de ser relativa su posición será
     * <b>position += jointMasCercano.getPosition()</b>
     * @return true si es relativa
     */
    public boolean isRelative() {
        return relative;
    }

    /**
     * Modifica la posición del objeto a ser o no relativa
     * @param relative si será o no relativa a la posición del joint más
     * cercano.
     */
    public void setRelative(boolean relative) {
        this.relative = relative;
        this.alert.firePropertyChange("positionRelative", !relative, relative);
//        updatePosition();
    }

    /**
     * Obtiene la ruta del archivo con la geometría y apariencia del elemento.
     * @return la ruta del archivo con la información del objeto.
     */
    public File getSource() {
        return source;
    }

    /**
     * Asigna la ruta de la información de geometría y apariencia del elemento.
     * @param source la ruta del archivo.
     */
    protected void setSource(File source) {
        String old = "";
        if(this.source != null)
            old = getSource().getAbsolutePath();
        this.source = source;
        this.alert.firePropertyChange("source", old, this.getSource().getAbsolutePath());
    }


//    public File getSrc(){
//        return new File(this.source);
//    }
//
//    public void setSrc(File src){
//        this.setSource(src.getAbsolutePath());
//    }

    /**
     * Devuelve el TransformGroup que cambiará esta pieza cuando una
     * articulación rote.
     * @return El TransformGroup que contiene esta pieza.
     */
    public TransformGroup getParentTg() {
        return parentTg;
    }

    /**
     * Asigna un TransformGroup que contiene a esta pieza
     * @param parentTg el TransformGroup que contiene a esta pieza
     */
    public void setParentTg(TransformGroup parentTg) {
        this.parentTg = parentTg;
    }

    /**
     * @return the state
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the state to set
     */
    public void setSelected(boolean selected) {
        boolean old = this.selected;
        this.selected = selected;
        alert.firePropertyChange("selected", old, selected);
    }

    /**
     * @return the mainTg
     */
    public TransformGroup getMainTg() {
        return mainTg;
    }

    /**
     * @param mainTg the mainTg to set
     */
    public void setMainTg(TransformGroup mainTg) {
        this.mainTg = mainTg;
    }

    public void updatePosition(){
        Vector3d translation = new Vector3d(this.getPosition());
        if(this.getType() == ElementType.joint)
            updatePosition(this.getParentTg(), translation);
        else
            updatePosition(this.getMainTg(), translation);

    }

    private void updatePosition(TransformGroup tg, Vector3d translation){
        Transform3D trans3d = new Transform3D();
        tg.getTransform(trans3d);
        trans3d.setTranslation(translation);
        tg.setTransform(trans3d);
    }

}
