/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.graphics.model;

import Objetos3D.Archivo;
import com.eteks.sweethome3d.j3d.DAELoader;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import org.itver.arm.io.ArmInterpreter;
import org.itver.arm.models.elements.Arm;
import org.itver.common.util.ModifyTree;
import org.itver.common.xml.XmlLoader;
import org.itver.graphics.controller.CollisionDetector;
import org.itver.graphics.util.ComponentType;

/**
 * Esta clase representa cada objeto existente en el entorno de simulación
 * mediante la definición de los atributos y comportamientos principales que
 * posee cada uno, tales como su posición en el espacio, escala, tipo, etc.
 *
 *<br>
 * Los objetos de esta clase pueden pertenecer a diversas categorías:
 *  <br>
 *      <ul>
 *          <li>
 *              Arm - Cualquier brazo robótico.
 *          </li>
 *          <li>
 *              Pickable - Todos aquellos objetos que pueden ser sujetados por el
 *              brazo robótico.
 *          </li>
 *          <li>
 *              Furniture - Todos aquellos objetos decorativos que no pueden ser
 *              sujetados por el brazo robótico.
 *          </li>
 *      </ul>
 *  </br>
 * </br>
 * @see org.itver.graphics.util.ComponentType
 * @author Karo
 */
public class MainSceneComponent extends BranchGroup {
    private double         scale; //Escala para el tamaño *
    private double[]       rotation; //Águlos que definen la orientación del objeto *
    private boolean        selected; //si el componente está seleccionado o no
    private int            id; //Identificador para efectos de programación *
    private Appearance     selectedAp; //Define la apariencia del objeto cuando está seleccionado
    private ComponentType  type; //Si es brazo, objeto estático o mueble *
    private Point3d        position; //Posición en el espacio *
    private File           source; //Ruta asociada al archivo en el que está la información del componente *
    private String         name; //Nombre legible para el usuario final *
    private boolean        colliding;
    private TransformGroup transformGroup;
    private PropertyChangeSupport pcs;
    private Transform3D t3d;

    //Hecho por Pablo
    private Object content;
    
    /**
     * Construye una nueva instancia de MainSceneComponent con valores
     * predefinidos.
     */
    private MainSceneComponent(){
        scale          = 0.25;
        position       = new Point3d(0, 0 , 0);
        rotation       = new double[]{0, 0, 0};
        selectedAp     = new Appearance();
        selected       = false;
        name           = null;
        t3d = new Transform3D();
        transformGroup = new TransformGroup();
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformGroup.setCapability(Group.ALLOW_CHILDREN_READ);
        transformGroup.setCapability(Group.ENABLE_COLLISION_REPORTING);
        this.setCapability(Group.ALLOW_BOUNDS_READ);
        addChild(transformGroup);
        addCollisionBehavior();
        pcs = new PropertyChangeSupport(this);
    }

    /**
     * Construye una nueva instancia de MainSceneComponent dados un
     * indentificador único, tipo y ruta.
     * @param id Entero identificador para cada objeto.
     * @param type Tipo de componente.
     * @param source Ruta en la que se localiza el archivo del componente a crear.
     * @see org.itver.graphics.util.ComponentType
     */
    public MainSceneComponent(int id, ComponentType type, File source){
        this();
        this.source = source;
        this.id     = id;
        this.type   = type;

    }

    /**
     * Carga un tipo objeto a partir de un tipo de archivo leido.
     */
    public void loadType(){
        switch(type){
            case arm:
                XmlLoader loader = new XmlLoader();
                ArmInterpreter ai = new ArmInterpreter();
                loader.setInterpreter(ai);
                try {
                    Scene scene = loader.load(source.getPath());
                    transformGroup.addChild(scene.getSceneGroup());
                    Arm arm = (Arm) scene.getSceneGroup();
                    this.content = arm;
                    arm.setContainer(this);

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainSceneComponent.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IncorrectFormatException ex) {
                    Logger.getLogger(MainSceneComponent.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParsingErrorException ex) {
                    Logger.getLogger(MainSceneComponent.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case pickable:
                Archivo pickable = new Archivo(source.getPath());
                pickable.getObjeto().setAppearance(createDefaultAppearance());
                
                setShape3D(pickable.getObjeto());
                ModifyTree.setUserData(transformGroup, this);
                break;
            case furniture:
                DAELoader furniture = new DAELoader();
                try {
                    Scene scene = furniture.load(source.getPath());
                    transformGroup.addChild(scene.getSceneGroup());
                    ModifyTree.setUserData(transformGroup, this);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainSceneComponent.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IncorrectFormatException ex) {
                    Logger.getLogger(MainSceneComponent.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParsingErrorException ex) {
                    Logger.getLogger(MainSceneComponent.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }

    /**
     * Crea y regresa una apariencia con valores predefinidos.
     * @return Apariencia creada.
     */
    public Appearance createDefaultAppearance(){
        Appearance ap = new Appearance();
        ap.setMaterial(new Material());
        return ap;

    }

    /**
     * Regresa el TransformGroup del componente actual.
     * @return TransformGroup del componente.
     */
    public TransformGroup getTransformGroup() {
        return transformGroup;
    }

    /**
     * Cambia el TransformGroup del objeto actual por uno nuevo.
     * @param transformGroup Nuevo TransformGroup
     */
    public void setTransformGroup(TransformGroup transformGroup) {
        this.transformGroup = transformGroup;
    }

    /**
     * Regresa la apariencia del objeto actual.
     * @return Apariencia del objeto.
     */
    public Appearance getAppearance() {
        return selectedAp;
    }
    /**
     * Cambia la apariencia del objeto actual por una nueva.
     * @param appearance Nueva apariencia.
     */
    public void setAppearance(Appearance appearance) {
        this.selectedAp = appearance;
    }

    /**
     * Regresa el indentificador del objeto acutal.
     * @return Número entero.
     */
    public int getId() {
        return id;
    }

    /**
     * Coloca un identificador al objeto actual.
     * @param id Número entero.
     */
    public void setId(int id) {
        int old = getId();
        this.id = id;
        pcs.firePropertyChange("Id", old, id);
    }

    /**
     * Regresa el nombre del objeto actual.
     * @return String que representa el nombre.
     */
    public String getComponentName() {
        return name;
    }

    /**
     * Cambia el nombre del objeto actual por uno nuevo.
     * @param name String para representar el nombre.
     */
    public void setComponentName(String name) {
        String old = getComponentName();
        this.name = name;
        pcs.firePropertyChange("Name", old, name);
    }

    /**
     * Regresa el punto en el que está posicionado el objeto actual.
     * @return Point3d que representa la posición.
     */
    public Point3d getPosition() {
        return position;
    }

    /**
     * Actualiza la posición del componente en la escena gráfica a partir de
     * tres puntos dados. Actua como osetPosition
     * @param xpos Posición en x.
     * @param ypos Posición en y.
     * @param zpos Posición en z.
     */
    public void updatePosition(double xpos, double ypos, double zpos) {
        setXPosition(xpos);
        setYPosition(ypos);
        setZPosition(zpos);
        setPositionTransform();
    }

    /*Valorar para borrar o dejar
    public void updateRotation(){

    }
    */

    /**
     * Establece la posición del objeto actual.
     * @param newPos Posición del objeto expresada con un arreglo de valores
     * primitivos.
     */
    public void setPosition(double[] newPos){
        updatePosition(newPos[0], newPos[1], newPos[2]);
    }

    /**
     * Establece la posición del objeto actual.
     * @param newPos Posición del objeto expresada con tipo Point3d.
     */
    public void setPosition(Point3d newPos){
        updatePosition(newPos.x, newPos.y, newPos.z);
    }

    /**
     * Actualiza la matriz de transformación para realizar una traslación.
     */
    private void setPositionTransform(){
        t3d.setTranslation(new Vector3d(position));
        transformGroup.setTransform(t3d);
    }

    /**
     * Cambia la posición actual en X por una nueva.
     * @param xpos Valor de la posición en X.
     */
    public void setXPosition(Double xpos){
        double old = getPosition().x;
        position.x = xpos;
        pcs.firePropertyChange("Posición en x", old, position.x);
        setPositionTransform();
    }

    /**
     * Cambia la posición actual en Y por una nueva.
     * @param ypos Valor de la posición en Y.
     */
    public void setYPosition(Double ypos){
        double old = getPosition().y;
        position.y = ypos;
        pcs.firePropertyChange("Posición en y", old, position.y);
        setPositionTransform();
    }

    /**
     * Cambia la posición actual len Z por una nueva.
     * @param zpos Valor de la posición en Z.
     */
    public void setZPosition(Double zpos){
        double old = getPosition().z;
        position.z = zpos;
        pcs.firePropertyChange("Posición en z", old, position.z);
        setPositionTransform();
    }


    /**
     * Obtiene un arreglo cuyos valores representan la rotación del objeto
     * actual.
     * @return Arreglo de valores tipo double.
     */
    public double[] getRotation() {
        return rotation;
    }

    /**
     * Coloca un arreglo cuyos valores representan la rotación de objeto actual
     * por uno nuevo.
     * @param rotation Arreglo de valores tipo double.
     */
    public void setRotation(double[] rotation) {
        this.rotation = rotation;
    }

    /**
     * Realiza una rotación de acuerdo a los valores de rotación actuales.
     */
    public void setRotationTransform(){
        Transform3D rot = new Transform3D();
        t3d.rotX(Math.toRadians(rotation[0]));
        rot.rotY(Math.toRadians(rotation[1]));
        t3d.mul(rot);
        rot.rotZ(Math.toRadians(rotation[2]));
        t3d.mul(rot);
        t3d.setScale(scale);
        t3d.setTranslation(new Vector3d(position));
        transformGroup.setTransform(t3d);
    }

    /**
     * Obtiene el valor de la escala del objeto actual.
     * @return Valor tipo double que representa la escala.
     */
    public double getScale() {
        return scale;
    }

    /**
     * Cambia el valor de la escala del objeto actual por uno nuevo.
     * @param scale Valor tipo double para representar la escala.
     */
    public void setScale(double scale) {
        this.scale = scale;
        t3d = new Transform3D();
        t3d.setScale(scale);
        transformGroup.setTransform(t3d);
    }

   /**
    * Cambia el estado de selección del objeto actual.
    * @param selected Si está seleccionado debe ser {@code true}, de lo contrario,
    * {@code false}.
    */
   public void setSelected(boolean selected){
        boolean old = isSelected();
        this.selected = selected;
        pcs.firePropertyChange("Selected", old, selected);
    }

   /**
    * Indica si el objeto actual está seleccionado o no.
    * @return {@code true} si está seleccionado, {@code flase} si no lo está.
    */
    public boolean isSelected(){
        return selected;
    }

    /**
     * Cambia el estado de colisión del objeto actual.
     * @param colliding Si está colisionando debe ser {@code true}, de lo contrario,
     * {@code false}.
     */
    public void setColliding(boolean colliding){
        boolean old = isColliding();
        this.colliding = colliding;
        pcs.firePropertyChange("Colliding", old, colliding);
    }

    /**
    * Indica si el objeto actual está colisionando con otro objeto o no.
    * @return {@code true} si está colisionando, {@code flase} si no lo está.
    */
    public boolean isColliding(){
        return colliding;
    }

    /**
     * Regresa una cadena de caracteres con la ruta del archivo fuente del objeto
     * actual.
     * @return Tipo String que representa la ruta.
     */
    public File getSource() {
        return source;
    }


    /**
     * Establece la ruta del archivo fuente del que se están leyendo las
     * características del objeto actual.
     * @param source Ruta.
     */
    public void setSource(File source) {
        File old = getSource();
        this.source = source;
        pcs.firePropertyChange("Source", old, source);
    }

    /**
     * Regresa el tipo de componente.
     * @return Tipo de componente.
     * @see org.itver.graphics.util.ComponentType
     */
    public ComponentType getType() {
        return type;
    }

    /**
     * Establece el tipo de componente del objeto actual.
     * @param type Tipo de componente.
     * @see org.itver.graphics.util.ComponentType
     */
    public void setType(ComponentType type) {
        ComponentType old = getType();
        this.type = type;
        pcs.firePropertyChange("Type", old, type);
    }

    /**
     * Agrega una Shape3D al TransformGroup del objeto actual.
     * @param shape Shape3D de la figura a agregar.
     */
    public void setShape3D(Shape3D shape){
        transformGroup.addChild(shape);
    }

    /**
    * Agrega un oyente para mantener en comunicación sobre los cambios
    * realizados en las propiedades.
    * @param listener Escucha los cambios realizados en las propiedades.
    */
    public void addPropertyChangeListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Elimina un oyente tipo PropertyChangeListener de este objeto.
     * @param listener El objeto oyente a eliminar.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener){
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Regresa un arreglo de cadenas de caracteres que contiene los nombres
     * de los campos a mostrar en la tabla de propiedades del objeto.
     * @return Arreglo con los nombres de los atributos editables del objeto.
     */

    public Object getContent(){
        return this.content;
    }

    public static String[] getFieldNames(){
        return new String []{"Id",
                             "Name",
                             "Type",
                             "Position",
                             "Rotation",
                             "Scale",
                             "Source"};
    }

    /**
     * Agrega un comportamiento para la detección de las colisiones del objeto.
     * @see org.itver.graphics.controller.CollisionDetector
     */
    private void addCollisionBehavior(){
        CollisionDetector collidable = new CollisionDetector(this, new BoundingSphere(new Point3d(), 1000));
        this.addChild(collidable);
    }

}
