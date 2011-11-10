/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.models.elements;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.itver.arm.threads.TransformThread;
import org.itver.common.util.ModifyTree;
import org.itver.common.util.PointsUtility;
import org.itver.common.xml.DAELoader;

/**
 * Modelo de cada pieza que conforma un brazo. Con excepción de la
 * {@link Joint Articulación}, todas las piezas se concentran aquí.
 *
 * @author Pablo Antonio Guevara González
 */
public class Piece extends Element {

    /**
     * Nombre de la propiedad que cambia al agregar o quitar una
     * {@link Friction fricción}.
     * @see #addFriction(javax.vecmath.Point3d) addFriction()
     */
    public static final String FRICTION = "friction";

    /**
     * Nombre de la propiedad que cambia al asignar un nuevo {@link Arm Brazo}
     * con la pieza.
     * @see #setArm(org.itver.arm.models.elements.Arm) setArm()
     */
    public static final String ARM = "arm";

    /**
     * Nombre de la propiedad que cambia al modificar el {@link ElementType tipo}
     * de la pieza
     * @see #setType(org.itver.arm.models.elements.ElementType) setType()
     */
    public static final String TYPE = "type";

    /**
     *Nombre de la propiedad que cambi al modificar el tamaño de la pieza
     * @see #getSize() getSize()
     */
    public static final String SIZE = "size";

    /**
     * Nombre de la propiedad que cambia al asignar una {@link Joint articulación}
     * como padre de esta pieza
     * @see #setJoint(org.itver.arm.models.elements.Joint) setJoint()
     */
    public static final String JOINT = "joint";

    /**
     * Nombre de la propiedad que cambiar al modificar alguna de las orientaciones
     * de la pieza
     * @see #setOrientation(double, org.itver.arm.models.elements.Axis) setOrientation()
     */
    public static final String ORIENTATION = "orientation";


    private ArrayList<Friction> frictions;
    private Arm arm;

    private Point3f lower;
    private Point3f upper;
    private Vector3f size;

    private ElementType type;
    private Joint joint;
    private double[] orientation;
    private BranchGroup sourceBG;

    /**
     * Constructor Default.
     * @see Element#Element() Element()
     */
    public Piece(){
        super();
        this.frictions = new ArrayList<Friction>();
        this.lower = new Point3f(1000, 1000, 1000);
        this.upper = new Point3f();
        this.size  = new Vector3f();
        this.orientation = new double[Axis.values().length];
        initBranch();
    }

    /**
     * Contructor de la pieza que asigna un {@link ElementType tipo} a la pieza
     * @param type tipo de la pieza.
     */
    public Piece(ElementType type) {
        this();
        this.type = type;
    }

    /**
     * Obtiene la lista de {@link Friction fricciones} ligadas a la pieza
     * @return La lista de fricciones
     */
    public Friction[] getFrictions() {
        Friction array[] = new Friction[frictions.size()];
        return frictions.toArray(array);
    }

    /**
     * Obtiene una {@link Friction fricción} de acuerdo al índice especificado.
     * @param index el índice de la fricción a obtener, de acuerdo al orden en
     * que fueron agregadas.
     * @return La fricción correspondiente al índice
     * @see Friction Friction
     */
    public Friction getFriction(int index) {
        return frictions.get(index);
    }

    /**
     * Agrega una {@link Friction fricción} a la pieza con posición especifica.
     * @param friction Posición de la fricción a agregar
     * @return Nueva {@link Friction Fricción} correspondiente a la posición dada
     */
    public Friction addFriction(Point3d friction) {
        Friction result = new Friction(friction, this);
        frictions.add(result);
        this.alert.firePropertyChange(FRICTION, null, friction);
        return result;
    }

    /**
     * Agrega una {@link Friction fricción} a la pieza con posición especifica.
     * @param x Posición en el plano x de la fricción
     * @param y Posición en el plano y de la fricción
     * @param z Posición en el plano z de la fricción
     * @return Nueva {@link Friction Fricción} correspondiente a la posición dada
     */
    public Friction addFriction(double x, double y, double z) {
        return addFriction(new Point3d(x, y, z));
    }

    /**
     * Agrega una {@link Friction fricción} a la pieza con posición especifica.
     * @param vals Array con los valores de la posición de la fricción. Debe ser
     * length = 3
     * @return Nueva {@link Friction Fricción} correspondiente a la posición dada
     */
    public Friction addFriction(double vals[]) {
        return addFriction(new Point3d(vals));
    }

    /**
     * Borra una {@link Friction Fricción} de la pieza
     * @param friction Fricción a borrar. Si no ha sido agregada o es null no
     * hace nada
     */
    public void delFriction(Friction friction) {
        frictions.remove(friction);
        this.alert.firePropertyChange(FRICTION, friction, null);
    }


    /**
     * Asigna el archivo a la pieza y lo carga mediante un DAELoader. El archivo
     * debe ser en formato COLLADA. Generalmente con extensión .dae
     * @param source Archivo COLLADA con la forma de la pieza.
     */
    @Override
    public void setSource(File source){
        super.setSource(source);
        BranchGroup  design  = new BranchGroup();
        DAELoader       daeLoad = new DAELoader();
        Scene           scene   = null;
        try {
            scene = daeLoad.load(source.getAbsolutePath());
            design = scene.getSceneGroup();
            this.setSource(design);
        } catch (FileNotFoundException ex) {
            System.err.println(source.getAbsolutePath() + ": File not found: " + ex.getMessage());
        } catch (IncorrectFormatException ex) {
            System.err.println(source.getAbsolutePath() + ": Incorrect format: " + ex.getMessage());
        } catch (ParsingErrorException ex) {
            System.err.println(source.getAbsolutePath() + ": Parsing error: " + ex.getMessage());
        }
    }


    /**
     * Devuelve el {@link Arm Brazo} al que se encuentra actualmente ligado la pieza, null
     * si no ha sido ligado alguno.
     * @return El Brazo que contiene la pieza.
     */
    public Arm getArm() {
        return arm;
    }

    /**
     * Asigna un {@link Arm Brazo} como contenedor de la pieza, reemplazando el anterior.
     * @param arm El brazo que contiene la pieza.
     */
    public void setArm(Arm arm) {
        Arm old = this.arm;
        this.arm = arm;
        this.alert.firePropertyChange(ARM, old, this.arm);
    }


    /**
     * Obtiene el punto más bajo en la forma de la pieza, si no ha sido
     * {@link #setSource(java.io.File) asignado} un archivo, devuelve un Punto
     * en el origen.
     * @return El punto más bajo de la estructura de la pieza
     * @see #getSize() getSize()
     */
    public Point3f getLower() {
        return new Point3f(lower);
    }

    /**
     * Obtiene el punto más alto en la forma de la pieza, si no ha sido
     * {@link #setSource(java.io.File) asignado} un archivo, devuelve un Punto
     * en el origen.
     * @return El punto más alto de la estructura de la pieza
     * @see #getSize() getSize()
     */
    public Point3f getUpper() {
        return new Point3f(upper);
    }

    /**
     * Obtiene un vector a partir de la resta entre el punto
     * {@link #getLower() más bajo} y el {@link #getUpper() más alto}.
     * @return El tamaño de la estructura de le pieza. Si no ha sido asignado
     * un archivo, devuelve un vector de longitud 0
     */
    public Vector3f getSize() {
        return new Vector3f(size);
    }

    /**
     * Asigna una {@link Joint articulación} como contenedora de la pieza
     * @param joint La articulación de la que depende la pieza.
     */
    protected void setJoint(Joint joint){
        Joint old = this.joint;
        this.joint = joint;
        this.alert.firePropertyChange(JOINT, old, this.joint);
    }

    /**
     * Obtiene la articulación de la que depende la pieza
     * @return la articulación de la que depende la pieza. null si no depende
     * de alguna.
     */
    public Joint getJoint(){
        return this.joint;
    }

    /**
     * Inicializa la jerarquía en los nodos del BranchGroup. Sobreescribir solo
     * si es necesario.
     */
    protected void initBranch(){
        this.addChild(this.mainTg);
    }

    /**
     * Modifica la orientación en el eje y valor específico.
     * @param value nuevo valor de la orientación. En grados.
     * @param axis Eje sobre que se modifica el valor.
     */
    public void setOrientation(double value, Axis axis){
        double old = this.orientation[axis.ordinal()];
        this.orientation[axis.ordinal()] = value;
        TransformThread thread = new TransformThread(this.mainTg, this.orientation);
        thread.start();
        this.alert.firePropertyChange(ORIENTATION, old, value);
    }

    /**
     * modifica la orientación en los 3 ejes, en base a los valores del array.
     * @param values valores de la nueva orientación. En grados. debe ser length = 3
     */
    public void setOrientation(double[] values){
        double[] old = this.orientation;
        if(values.length == 3)
            this.orientation = values;
        TransformThread thread = new TransformThread(this.mainTg, this.orientation);
        thread.start();
        this.alert.firePropertyChange(ORIENTATION, old, values);
    }

    /**
     * Obtiene la orientación actual de la pieza en los 3 ejes.
     * @return Array con los valores de la orientación. En grados.
     */
    public double[] getOrientation(){
        return this.orientation;
    }

    /**
     * Obtiene la orientación sobre el eje especificado.
     * @param axis Eje sobre el que se desea obtener la orientación.
     * @return Valor de la orientación sobre el eje. En grados.
     */
    public double getOrientation(Axis axis){
        return this.orientation[axis.ordinal()];
    }

    /**
     * @return the type
     */
    public ElementType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(ElementType type) {
        ElementType old = type;
        this.type = type;
        this.alert.firePropertyChange(TYPE, old, this.type);
    }
    
    @Override
    public Piece clone(){
        Piece result = new Piece();
        for(Axis axis : Axis.values())
            result.setOrientation(this.getOrientation(axis), axis);
        for(Friction friction : this.getFrictions())
            result.addFriction(friction.getPosition());
        result.setPosition(position);
        result.setSource(source);
        result.setType(type);
        return result;
    }
    
    @Override
    protected void finalize() throws Throwable{
        super.finalize();
        if(this.arm != null)
            this.arm.removePiece(this);
        this.arm = null;
        if(this.joint != null)
            this.joint.removeNext(this);
        this.joint = null;
        if(this.sourceBG.isLive())
            this.sourceBG.detach();
        this.sourceBG = null;
        this.frictions.clear();
        this.frictions = null;
        this.lower = null;
        this.orientation = null;
        this.size = null;
        this.type = null;
        this.upper = null;
    }

    private void setSource(BranchGroup source) {
        source.setCapability(BranchGroup.ALLOW_DETACH);
        addCapabilities(source);
        Vector3f old = size;
        size.sub(upper, lower);
        validateSize();
        this.alert.firePropertyChange(SIZE, old, size);
        ModifyTree.setUserData(source, this);
        if(this.sourceBG != null)
            this.sourceBG.detach();
        this.mainTg.addChild(source);
        this.sourceBG = source;

    }


    private void addCapabilities(Node node) {
        if (node instanceof Shape3D) {
            calcLimits((Shape3D) node);
            node.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
            node.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        } else if (node instanceof Group) {
            Group g = (Group) node;
            node.setCapability(Group.ALLOW_CHILDREN_READ);
            for (int i = 0; i < g.numChildren(); i++) {
                addCapabilities(g.getChild(i));
            }
        }
    }

    private void calcLimits(Shape3D shape) {
        for(int ng = 0; ng < shape.numGeometries(); ng++){
            GeometryArray geom = (GeometryArray)shape.getGeometry(ng);
            int flag = geom.getVertexFormat() & GeometryArray.INTERLEAVED;
            if(flag != 0)
                interleavedGeometry(geom, shape);
        }
    }
    
    private void interleavedGeometry(GeometryArray geom, Shape3D shape){
        float vertex[] = geom.getInterleavedVertices();
            int nv = geom.getVertexCount();
            int words = vertex.length / nv;
            for(int i = words - 3 ; i < vertex.length; i += words){
                Point3f point = new Point3f(vertex[i],
                                            vertex[i + 1],
                                            vertex[i + 2]);
                applyTransform(shape, point);
                
            }
    }
    
    private void applyTransform(Shape3D shape, Point3f point){
        Node parent = shape.getParent();
                while(parent != null){
                    if(parent instanceof TransformGroup){
                        TransformGroup tg = (TransformGroup)parent;
                        Transform3D aux = new Transform3D();
                        tg.getTransform(aux);
                        aux.transform(point);
                    }
                    parent = parent.getParent();
                }
                PointsUtility.min(lower, point);
                PointsUtility.max(upper, point);
    }
    
    private void validateSize(){
        if(upper.distance(new Point3f()) <= 0){
            this.upper.set(new Point3f());
            this.lower.set(new Point3f());
        }
    }
}
