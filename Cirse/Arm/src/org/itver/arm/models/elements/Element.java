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
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import org.itver.arm.threads.TransformThread;
import org.itver.common.util.IdGenerator;

/**
 *  Modelo padre de cada elemento del brazo, incluído el mismo brazo. La idea de
 * hacerlo BrachGroup es para poder agregarlo y retirarlo del entorno en tiempo
 * de ejecución. Las clases que quisieran representar el modelo de alguna pieza
 * del brazo, deberían heredar de {@link Piece Piece}.
 *
 * @see BranchGroup BranchGroup
 *
 * @author Pablo Antonio Guevara González
 */
public abstract class Element extends BranchGroup{
    /**
     * Id defult para el contructor de la pieza, si éste se asigna, se le dará
     * un valor progresivo que aumenta cada vez que se crea una pieza con este
     * id
     */
    public static final int DEFAULT_ID = -1;

    /**
     * Nombre de la propiedad cuando cambia la posicion
     */
    public static final String POSITION = "position";

    /**
     * Nombre de la propiedad cuando cambia el archivo
     */
    public static final String SOURCE = "source";

    /**
     * Nombre de la propiedad cuando cambia el ID
     */
    public static final String ID = "id";
    
    private     int             id;

    /**
     * Objeto encargado de avisar a los oyentes cuando un atributo ha cambiado.
     */
    protected   PropertyChangeSupport alert;

    /**
     * Posición del TransformGroup Principal en el entorno.
     */
    protected   Point3d         position;

    /**
     * Archivo ligado a este elemento.
     */
    protected   File            source;

    /**
     * TransformGroup principal de la pieza. Aqui se realizan los cambios en
     * orientacion, posicion y escala.
     */
    protected TransformGroup  mainTg;

    /**
     * Contructor default. Genera un Id autoincrementable.
     */
    protected  Element(){
        super();
        this.id = IdGenerator.generateId();
        this.alert        = new PropertyChangeSupport(this);
        this.position     = new Point3d();
        this.mainTg = new TransformGroup();
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
     * Contructor a usar con un archivo default, No realiza nada con el archivo,
     * solo lo asigna a su respectivo atributo.
     * @param src Archivo ligado al modelo.
     * @see Element#source source.
     */
    protected Element(File src){
        this();
        this.source = src;
    }

    /**
     * Constructor que asigna un Archivo y un Id. Se ignorará el ID auto-generado
     * @param src Archivo ligado al modelo
     * @param id número de referencia para el modelo.
     */
    protected Element(File src, int id){
        this(src);
        this.id = id;
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
     * Obtiene el ID asignado a este elemento
     * @return el ID del modelo. Número con que se identifica este modelo.
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
        this.alert.firePropertyChange(ID, old, id);
    }

    /**
     * Obtiene la posición del centro de este objeto.
     * @return la posición del objeto respecto a la articulación que lo contiene
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
        this.alert.firePropertyChange(POSITION, old, position);
        updatePosition3D();
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
     * Obtiene el archivo ligado al Elemento.
     * @return la ruta del archivo con la información del objeto. null si no ha
     * sido asignado alguno.
     */
    public File getSource() {
        return source;
    }

    /**
     * Asigna un archivo ligado a este elemento, reemplazando el anterior. No
     * realiza nada con el archivo.
     * @param source la ruta del archivo.
     */
    public void setSource(File source) {
        File old = this.source;
        this.source = source;
        this.alert.firePropertyChange(SOURCE, old, this.source);
    }

    /**
     * Obtiene el TransformGroup principal del elemento.
     * @return the mainTg
     */
    public TransformGroup getMainTg() {
        return mainTg;
    }
    
    @Override
    protected void finalize() throws Throwable{
        super.finalize();
        this.alert = null;
        this.mainTg = null;
        this.position = null;
        this.source = null;
    }

    /**
     * Transformacion sobre el TransformGroup principal para trasladarlo en un
     * vector similar al punto de la posición menos el origen.
     * @see TransformThread TransformThread.
     */
    protected void updatePosition3D(){
        TransformThread thread = new TransformThread(this.mainTg, new Vector3d(this.position));
        thread.start();
    }
}
