/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.models.elements;

import com.eteks.sweethome3d.j3d.DAELoader;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.itver.arm.models.validators.AngleValidator;
import org.itver.arm.threads.TransformThread;
import org.itver.arm.views.Log;
import org.itver.common.util.ModifyTree;
import org.itver.common.util.PointsUtility;

/**
 * Modelo de cada pieza que conforma un brazo.
 *
 * @author Pablo Antonio Guevara González
 */
public class Piece extends Element {

    /**
     * índice que se debe poner en el método
     * {@link javax.media.j3d.Group#getChild(int) getChild(int)} para obtener la
     * apariencia de esta pieza.
     */
    public static final int INDEX_AP = 0;
    /**
     * Número menor de relaciones que puede tener la pieza.
     */
    public static final int MIN_RELS = 1;

    private final int PARENT_INDEX = 0;
    private final int N_AXES = Axis.values().length;
    private final int N_TYPES = AngleType.values().length;
    private final int SELECTED_INDEX = 0;
    private final int IDLE_INDEX = 1;
    private final int N_STATES = 2;

    private double[][] angles;
    private int[] relations;
    private ArrayList<Friction> frictions;
    private Node stateNode[];
    private Arm arm;

    private Point3f lower;
    private Point3f upper;
    private Vector3f size;

    /**
     * Constructor default de la pieza.
     * @param id id que tendrá la pieza.
     * @param type tipo de la pieza.
     */
    public Piece(int id, ElementType type) {
        super(id, type);
        this.relations = new int[MIN_RELS];
        this.relations[PARENT_INDEX] = Element.DEFAULT_ID;
        this.frictions = new ArrayList<Friction>();
        this.angles = new double[N_TYPES][N_AXES];
        this.stateNode = new Node[N_STATES];
        this.lower = new Point3f(1000, 1000, 1000);
        this.upper = new Point3f();
        this.size  = new Vector3f();
        addListeners();
    }

    /**
     * obtiene un angulo a aprtir del tipo y eje pedido.
     * @param angleType el tipo de angulo a obtener
     * @see models.elements.AngleType Tipos de angulo
     * @param axis El eje del angulo que se quiere obtener
     * @see models.elements.Axis ejes posibles.
     * @return El angulo en grados de la pieza.
     */
    public double getAngle(AngleType angleType, Axis axis) {
        return angles[angleType.ordinal()][axis.ordinal()];
    }

    /**
     * Obtiene un trío de ángulos, de acuerdo al tipo de angulo
     * @param angleType el tipo de angulo que se quiere obtener
     * @see models.elements.AngleType Tipos de ángulos
     * @return Trío de ángulos de acuerdo al tipo.
     */
    public double[] getAngles(AngleType angleType) {
        return angles[angleType.ordinal()];
    }

    /**
     * Obtiene todos los ángulos de esta pieza.
     * @return array de longitud 4*3 con todos loa ángulos en grados.
     */
    public double[][] getAngles() {
        return this.angles;
    }

    /**
     * Modifica el ángulo en el eje y tipo especificado
     * @param angleType El tipo de ángulo a cambiar
     * @see models.elements.AngleType Tipos de ángulos
     * @param axis El eje del ángulo a cambiar.
     * @see models.elements.Axis Ejes
     * @param angle El nuevo ángulo.
     */
    public void setAngle(AngleType angleType, Axis axis, double angle) {
        double old = angles[angleType.ordinal()][axis.ordinal()];
        angles[angleType.ordinal()][axis.ordinal()] = angle;
        this.alert.firePropertyChange("angles", old, angle);
//        updateRotation();
//        this.updateTransforms();
        TransformThread thread1 = new TransformThread(this, true);
        thread1.start();
    }

    /**
     * Modifica todos los ángulos del mismo tipo.
     * @param angleType el tipo de ángulo a cambiar.
     * @see models.elements.AngleType Tipos de ángulos
     * @param angles array con los nuevos ángulos {x, y, z}.
     */
    public void setAngles(AngleType angleType, double angles[]) {
        Axis axis[] = Axis.values();
        for (int i = 0; i < axis.length; i++) {
            setAngle(angleType, axis[i], angles[i]);
        }
    }

    /**
     * Modifica los ángulos del mismo eje.
     * @param axis Eje en el que se modificarán los ángulos
     * @see models.elements.Axis Ejes
     * @param angles array con los nuevos ángulos.
     */
    public void setAngles(Axis axis, double angles[]) {
        AngleType anType[] = AngleType.values();
        for (int i = 0; i < anType.length; i++) {
            setAngle(anType[i], axis, angles[i]);
        }
    }

    /**
     * Modifica todos los ángulos de la pieza por los nuevos, el array debe ser
     * tamaño 4*3
     * @param angles conjunto de nuevos ángulos a cambiar en la pieza.
     */
    public void setAngles(double angles[][]) {
        AngleType anType[] = AngleType.values();
        Axis axis[] = Axis.values();
        for (int i = 0; i < anType.length; i++) {
            for (int j = 0; j < axis.length; j++) {
                setAngle(anType[i], axis[j], angles[i][j]);
            }
        }
    }

    /**
     * Devuelve el total de relaciones de esta pieza, en la posición 0 esta el
     * id del padre
     * @return las relacines del padre.
     */
    public int[] getRelations() {
        return relations;
    }

    /**
     * Devuelve el id de una pieza relacionada con esta pieza, de acuerdo a la
     * posición en el arreglo.
     * @param index la posición en el arreglo
     * @return Id de la pieza relacionada con esta pieza.
     */
    public int getRelation(int index) {
        return relations[index];
    }

    /**
     * Devuelve la posición en el arreglo del id relacionado con la pieza
     * @param relation el id relacionado con la pieza
     * @return la posición que ocupa ese id en el arreglo de relaciones de la
     * pieza. -1 si el id no está en la pieza.
     */
    public int getRelationId(int relation) {
        for (int i = 0; i < relations.length; i++) {
            if (relations[i] == relation) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 
     * @param relations the relations to set
     */
    public void setRelations(int[] relations) {
        this.relations = new int[relations.length];
        for (int i = 0; i < relations.length; i++) {
            setRelation(i, relations[i]);
        }
    }

    public void setRelation(int index, int relation) {
        int old[] = this.getRelations();
        this.relations[index] = relation;
        this.alert.firePropertyChange("relation",
                old,
                relations);
    }

    public void addRelation(int relation){
        int aux[] = this.getRelations();
        this.relations = new int[aux.length + 1];
        System.arraycopy(aux, 0, relations, 0, aux.length);
        relations[relations.length - 1] = relation;
        this.alert.firePropertyChange("relation", aux, this.relations);
    }

    public void delRelation(int index){
        if((index < 0) || (index >= relations.length))
            return;
        int aux[] = this.getRelations();
        this.relations = new int[aux.length - 1];
        for (int i = 0; i < aux.length; i++)
            if(i != index)
                relations[i < index ? i : i - 1] = aux[i];
        this.alert.firePropertyChange("relation", aux, this.relations);
    }

    /**
     * @return the frictions
     */
    public Friction[] getFrictions() {
        Friction array[] = new Friction[frictions.size()];
        return frictions.toArray(array);
    }

    public Friction getFriction(int index) {
        return frictions.get(index);
    }

    public void addFriction(Point3d friction) {
        frictions.add(new Friction(friction, this));
        this.alert.firePropertyChange("frictions", "", friction);
    }

    public void addFriction(double x, double y, double z) {
        addFriction(new Point3d(x, y, z));
    }

    public void addFriction(double vals[]) {
        addFriction(new Point3d(vals));
    }

    public void setFriction(Point3d friction, int index) {
        Point3d old = new Point3d(friction);
        this.getFriction(index).setPosition(friction);
        this.alert.firePropertyChange("frictions", old, friction);
    }

    public void setFriction(double x, double y, double z, int index) {
        setFriction(new Point3d(x, y, z), index);
    }

    public void delFriction(int index) {
        Friction old = frictions.get(index);
        frictions.remove(index);
        this.alert.firePropertyChange("frictions", old, "");
    }

    /**
     * 
     * @param source File containign the source desgin
     * @param node   Node containing the Shape3d to be asigned
     */
    private void setSource(BranchGroup node) {
        node.setCapability(BranchGroup.ALLOW_DETACH);
        addCapabilities(node);
        Vector3f old = size;
        size.sub(upper, lower);
        this.alert.firePropertyChange("size", old, size);
//        size.scale(0.0254f);
        ModifyTree.setUserData(node, this);
        try {
            for(int i = 0; i < stateNode.length; i++)
                stateNode[i] = node.cloneTree();
            ModifyTree.changeAppearance(stateNode[SELECTED_INDEX],
                                        selectedAppearance());
            this.mainTg.setChild(stateNode[IDLE_INDEX], INDEX_AP);
        } catch (IndexOutOfBoundsException ex) {
            this.mainTg.addChild(stateNode[IDLE_INDEX]);
        }
    }

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
            System.err.println("File not found: " + ex.getMessage());
        } catch (IncorrectFormatException ex) {
            System.err.println("incorrect format: " + ex.getMessage());
        } catch (ParsingErrorException ex) {
            System.err.println("Parsing error: " + ex.getMessage());
        }
    }

    public Node getAppearanceNode() {
        return this.getChild(INDEX_AP);
    }

    /**
     * @return the parentId
     */
    public int getParentId() {
        return getRelation(PARENT_INDEX);
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(int parentId) {
        setRelation(PARENT_INDEX, parentId);
    }

    @Override
    public void setSelected(boolean selected){
        if(selected == this.selected)
            return;
        super.setSelected(selected);
        this.mainTg.setChild(stateNode[selected ? SELECTED_INDEX : IDLE_INDEX],
                             INDEX_AP);
        
    }

    private void addListeners() {
        AngleValidator angVal = new AngleValidator();
        this.addListener(angVal, "angles");

        Log log = new Log();
        this.addListener(log);
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

    private Appearance selectedAppearance() {
        Appearance result = new Appearance();
        ColoringAttributes col;
        Color3f color = new Color3f(Color.BLUE);
        col = new ColoringAttributes(color, ColoringAttributes.NICEST);

        TransparencyAttributes trans = new TransparencyAttributes(
                TransparencyAttributes.NICEST, 0.9f);

        result.setColoringAttributes(col);
        result.setTransparencyAttributes(trans);
        return result;
    }

    /**
     * @return the arm
     */
    public Arm getArm() {
        return arm;
    }

    /**
     * @param arm the arm to set
     */
    public void setArm(Arm arm) {
        this.arm = arm;
    }

    private void calcLimits(Shape3D shape) {
        for(int ng = 0; ng < shape.numGeometries(); ng++){
            GeometryArray geom = (GeometryArray)shape.getGeometry(ng);
            float vertex[] = geom.getInterleavedVertices();
            int nv = geom.getVertexCount();
            int words = vertex.length / nv;
            for(int i = words - 3 ; i < vertex.length; i += words){
                Point3f point = new Point3f(vertex[i],
                                            vertex[i + 1],
                                            vertex[i + 2]);
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
        }
    }

    /**
     * @return the lower
     */
    public Point3f getLower() {
        return new Point3f(lower);
    }

    /**
     * @return the upper
     */
    public Point3f getUpper() {
        return new Point3f(upper);
    }

    /**
     * @return the size
     */
    public Vector3f getSize() {
        return new Vector3f(size);
    }

//    private void updateRotation(TransformGroup tg, double []angles){
//        Transform3D t3d = new Transform3D();
//        tg.getTransform(t3d);
//        Transform3D newTg = new Transform3D();
//        Vector3d translation = new Vector3d();
//        t3d.get(translation);
//        for (int i = 0; i < angles.length; i++) {
//            double d = Math.toRadians(angles[i]);
//            switch(i){
//                case 0: t3d.rotX(d); break;
//                case 1: t3d.rotY(d); break;
//                case 2: t3d.rotZ(d); break;
//            }
//            newTg.mul(t3d);
//        }
//        newTg.setTranslation(translation);
//        tg.setTransform(newTg);
//    }

//    public void updateRotation(){
//        Vector3d pos = new Vector3d(this.getPosition());
//        if(this.getType() == ElementType.joint)
//            updateRotation(this.getParentTg(), this.getAngles(AngleType.joint));
//        updateRotation(this.getMainTg(), this.getAngles(AngleType.current));
//    }

    public void updateTransforms(){
//        this.updatePosition();
//        this.updateRotation();
        TransformThread thread1 = new TransformThread(this, true);
        TransformThread thread2 = new TransformThread(this, false);
        thread1.start();
        thread2.start();
    }
    
    public static String[] getFieldNames(){
        return new String[]{"Id",
                            "Type",
                            "Position",
                            "Min Angles",
                            "Max Angles",
                            "Current Angles",
                            "Orientation",
                            "Source",
                            };
    }

    public void setPosition(Axis axis, double value) {
        Point3d pos = this.getPosition();
        switch(axis){
            case x: pos.x = value; break;
            case y: pos.y = value; break;
            case z: pos.z = value; break;
        }
        this.setPosition(position);
    }
}
