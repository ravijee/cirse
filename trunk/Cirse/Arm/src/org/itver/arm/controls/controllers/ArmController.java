/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.controls.controllers;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.utils.geometry.Box;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;
import org.itver.arm.io.ArmSaver;
import org.itver.arm.io.ArmHandler;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.models.elements.ElementType;
import org.itver.arm.models.elements.Joint;
import org.itver.arm.models.elements.Piece;
import org.itver.arm.views.listeners.KeysListener;
import org.itver.common.xml.SceneFromXML;
import org.itver.common.xml.SceneHandler;
import org.itver.graphics.model.MainScene;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.model.Universe;
import org.itver.graphics.util.ComponentType;
import org.itver.gui.util.Dialogs;
import org.itver.gui.visual.NavigatorTopComponent;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 * Controlador del Brazo. Maneja el ciclo de vida de uno permitiendo su creación,
 * manejo y destrucción.
 * <b>Se recomienda que todo nuevo brazo se cree y se maneje con los métodos
 * de esta clase</b>
 * <b>Clase singleton</b>
 * @author pablo
 */
public final class ArmController implements PropertyChangeListener{
    private static ArmController instance;
    private ArrayList<Arm> arms;
    private Box box;
    private Appearance apprn;
    private BranchGroup BGBox;

    private ArmController(){
        this.arms = new ArrayList<Arm>();
        NavigatorTopComponent.findInstance().getExplorerManager().addPropertyChangeListener(this);
        Universe.getInstance().getCanvas().addKeyListener(KeysListener.singleton());
        this.initBoxAppearance();
    }

    /**
     * Método singleton para la clase.
     * @return la instancia única de la clase.
     */
    public synchronized static ArmController singleton(){
        if(instance == null)
            instance = new ArmController();
        return instance;
    }

    /**
     * {@inheritDoc}
     * Método que escucha los cambios cuando se selecciona un elemento.
     * @param evt
     */
    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equalsIgnoreCase(ExplorerManager.PROP_SELECTED_NODES)){
            BGBox.detach();
            Piece selected = this.getSelectedPiece();
            RotatorViewController.singleton().setModel(selected);
            if(selected != null)
                this.selectPiece(selected);
        }
    }

    /**
     * Crea un nuevo Brazo y lo agrega en el Entorno.
     * @return El nuevo Brazo creado.
     */
    public Arm createArm(){
        Arm arm = new Arm();
        this.armToScene(arm);
        return arm;
    }

    /**
     * Asigna el Brazo especificado al Entorno.
     * @param arm Brazo a añadir al Entorno.
     */
    public void armToScene(Arm arm){
        MainSceneComponent component = new MainSceneComponent(ComponentType.arm, arm.getSource());
        arm.setContainer(component);
        component.setContent(arm);
        MainScene.getInstance().addComponent(component);
        this.addArmToArray(arm);
    }

    /**
     * Crea un nuevo Brazo, lo añade al Entorno y le asigna la Articulación
     * especifica mediante el archivo COLLADA.
     * @param file archivo COLLADA con información del diseño de la Articulación
     * @see Joint#setSource(java.io.File) Joint.setSource()
     */
    public void createArmWithPiece(File file){
        if(file != null) {
            Arm arm = this.createArm();
            Joint piece = JointController.singleton().newFromFile(file);
            arm.addPiece(piece);
        }
    }

    /**
     * Crea un nuevo brazo y lo agrega al Entorno. El archivo debe ser del tipo
     * XML válido con información completa sobre el brazo.
     * @param file Archivo XML válido con información sobre el brazo.
     */
    public void importArm(File file){
        Arm arm = this.readFile(file);
        if(arm != null)
            //TODO: agregar en el array el brazo creado desde un archivo de Env
            this.armToScene(arm);
    }
    
    public Arm readFile(File file){
        Arm result = null;
        SceneHandler handler = new ArmHandler();
        handler.setFileName(file.getParentFile().getAbsolutePath());
        SceneFromXML loader = new SceneFromXML(handler);
       try {
            Scene scene = loader.load(new FileReader(file));
            result = (Arm)scene.getSceneGroup();
            result.setSource(file);
        } catch (FileNotFoundException ex) {
            Dialogs.showErrorDialog("File not found: " + file.getPath());
            result = null;
        } catch(NullPointerException np){
            Dialogs.showErrorDialog("File not compatible: " + file.getPath());
            result = null;
        } catch(Exception ife){
            Exceptions.printStackTrace(ife);
            result = null;
        }
        return result;
    }

    /**
     * Retira el Brazo del Entorno y lo borra de la memoria.
     * @param arm Brazo a borrar.
     */
    public void removeArm(Arm arm){
        MainSceneComponent container = arm.getContainer();
        arm.detach();
        //TODO: Hacer un metodo en MainScene que borre MainSceneComponent
        container.detach();
        this.removeAllPieces(arm);
        this.arms.remove(arm);        
    }

    /**
     * Inserta una pieza en el Brazo de acuerdo al archivo COLLADA que define su
     * diseño.
     * @param arm Brazo en el que se insertará la pieza.
     * @param file Archivo COLLADA con información del diseño de la pieza.
     * @param type Tipo de la pieza, en caso que no sea Articulación.
     * @param isJoint {@code true} si la pieza es una Articulación
     * @return Pieza creada a partir del archivo.
     * @see #insertPiece(org.itver.arm.models.elements.Arm, org.itver.arm.models.elements.Piece)
     * InsertPiece()
     */
    public Piece insertPieceFromFile(Arm arm, File file, ElementType type, boolean isJoint){
        Piece result;
        if(isJoint)
            result = JointController.singleton().newFromFile(file);
        else
            result = PieceController.singleton().newFromFile(file, type);
        this.insertPiece(arm, result);
        return result;
    }

    /**
     * Inserta una pieza en el Brazo. La pieza será insertada al principio del
     * Brazo, ignorando cualquier articulación que éste contenga.
     * @param arm Brazo en el que se insertará la Pieza.
     * @param piece Pieza a insertar en el principio del Brazo.
     * @see Arm#addPiece(org.itver.arm.models.elements.Piece) Arm.addPiece()
     */
    public void insertPiece(Arm arm, Piece piece) {
        arm.addPiece(piece);
    }

    /**
     * Retira la Pieza del Brazo.
     * @param arm Brazo del que retirar la Pieza.
     * @param piece Pieza a retirar.
     * @see Arm#removePiece(org.itver.arm.models.elements.Piece) Arm.removePiece()
     */
    public void removePieceFromArm(Arm arm, Piece piece){
        arm.removePiece(piece);        
    }

    /**
     * Guarda el Brazo en un archivo XML válido.
     * @param arm Brazo a guardar.
     * @param file Archivo en el que se guardará el Brazo.
     */
    public void exportArm(Arm arm, File file){
        ArmSaver saver = new ArmSaver(1.0f);
        saver.save(arm != null ? arm : this.getSelectedArm(), file);
    }

    private void addArmToArray(Arm arm){
        this.arms.add(arm);
    }

    /**
     * Obtiene el Brazo seleccionado en el navegador principal del Entorno.
     * @return Brazo seleccionado en el navegador del Entorno. si una pieza se
     * encuentra seleccionada, obtiene su brazo contenedor.
     */
    public Arm getSelectedArm(){
        Node sNodes[] = NavigatorTopComponent.findInstance().getExplorerManager().getSelectedNodes();
        Arm result = null;
        for(Node sNode: sNodes){
            result = sNode.getLookup().lookup(Arm.class);
            if(result != null)
                return result;
        }
        Piece piece = this.getSelectedPiece();
        if(piece != null)
            result = piece.getArm();
        return result;
    }

    /**
     * Obtiene la pieza seleccionaa en el Navegador del Entorno.
     * @return Pieza seleccionada en el navegador del Entorno.
     */
    public Piece getSelectedPiece(){
        Node sNodes[] = NavigatorTopComponent.findInstance().getExplorerManager().getSelectedNodes();
        for(Node sNode: sNodes){
            Piece result = sNode.getLookup().lookup(Piece.class);
            if(result != null)
                return result;
        }
        return null;
    }

    /**
     * Obtiene un brazo a partir de su ID. La búsqueda la hace solo entre los
     * Brazos actualmente agregados al Entorno.
     * @param id ID del brazo a buscar
     * @return Brazo correspondiente al ID especifico. {@code null} si no existe
     * algún Brazo con el respectivo ID.
     */
    public Arm getArmById(int id){
        Arm result = null;
        for(Arm arm: this.arms)
            if(arm.getId() == id)
                result = arm;
        return result;
    }


    private synchronized void selectPiece(Piece piece){
        this.resizeBox(piece);
        BGBox.removeAllChildren();
        BGBox.addChild(box);
        piece.getMainTg().addChild(BGBox);
    }

    private void initBoxAppearance(){
        BGBox = new BranchGroup();
        BGBox.setCapability(BranchGroup.ALLOW_DETACH);
        BGBox.setPickable(false);
        BGBox.setCollidable(false);
        apprn = new Appearance();

        TransparencyAttributes ATran = new TransparencyAttributes(TransparencyAttributes.NICEST,
                                                                       0.75f);

        ColoringAttributes AColo = new ColoringAttributes(new Color3f(Color.BLUE),
                                                          ColoringAttributes.NICEST);
        apprn.setTransparencyAttributes(ATran);
        apprn.setColoringAttributes(AColo);
    }

    private  void resizeBox(Piece piece){
        float dims[] = new float[3];
        Vector3f size = piece.getSize();
        if(size.length() != 0){
            float scale = 0.5f;
            size.scale(scale);
            size.get(dims);
            this.box = new Box(dims[0], dims[1], dims[2], apprn);
        }
    }
    
    
    public void printSceneGraph(javax.media.j3d.Node node, int tab){
        if(!(node instanceof TransformGroup || node instanceof BranchGroup))
            return;
        for(int i = 0; i < tab; i++)
            System.out.print("   ");
        System.out.print(node + "\n");
        if(node instanceof javax.media.j3d.Group){
            javax.media.j3d.Group group = (javax.media.j3d.Group)node;
            tab++;
            for(int i = 0; i < group.numChildren(); i++)
                this.printSceneGraph(group.getChild(i), tab);
        }
    }
    
    public Arm[] getArms(){
        Arm[] result = new Arm[this.arms.size()];
        return this.arms.toArray(result);
    }
    
    private void removeAllPieces(Arm arm){
        for(Piece piece : arm.getNext()){
            if(piece instanceof Joint)
                removeAllPieces((Joint)piece);
            arm.removePiece(piece);
        }
                
    }
    
    private void removeAllPieces(Joint joint){
        for(Piece piece : joint.getNext()){
            if(piece instanceof Joint)
                removeAllPieces((Joint)piece);
            joint.removeNext(piece);
        }            
    }
}