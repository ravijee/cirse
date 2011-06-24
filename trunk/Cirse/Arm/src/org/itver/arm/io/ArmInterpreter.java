package org.itver.arm.io;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.SceneBase;
import org.itver.arm.exceptions.ArmStructureException;
import java.beans.PropertyChangeEvent;
import javax.media.j3d.BoundingSphere;
import org.itver.arm.controls.behaviors.KeyboardBehavior;
import org.itver.arm.models.elements.*;
import org.itver.common.util.Converter;
import org.itver.common.xml.ArmDom;
import org.itver.common.xml.Interpreter;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Clase encargada de interpretar cada etiqueta del archivo xml que se va
 * leyendo, una vez que lee el primer brazo etiqueta "content", es agregado en 
 * el BranchGroup para su uso por la clase Loader.
 *
 * Esta clase intrínsicamente se agrega como oyente de XmlWalker, por lo que
 * estará interpretando etiqueta por etiqueta recorrida.
 *
 * @author Pablo Antonio Guevara González
 */
public class ArmInterpreter extends Interpreter{
    private Arm              arm;
    private PieceInterpreter pieceInt;
    private SceneBase        scene;
    private KeyboardBehavior keyBehavior;

    /**
     * Constructor primario de la clase.
     */
    public ArmInterpreter(){
        pieceInt = new PieceInterpreter();
        scene    = new SceneBase();
        keyBehavior = new KeyboardBehavior();
    }

    /**
     * Crea y agrega un brazo en el BranchGroup principal con id "-1".
     * Los anteriores serán borrados.
     *
     * @see models.elements.Arm#Arm(int)
     */
    public void createArm(){
        this.arm        = new Arm(-1);       
        keyBehavior.setSchedulingBounds(new BoundingSphere());
        arm.addChild(keyBehavior);
        scene.setSceneGroup(arm);
    }

    /**
     * Devuelve el objeto Arm que se ha ido modificando con los métodos de esta
     * clase
     *
     * @return el objeto Arm creado.
     */
    public Arm getArm(){
        return arm;
    }

    /**
     * Asigna un soporte (Vector3d) a partir de un String con el formato
     * "xx, yy, zz" al brazo.
     *
     * @param support String con los valores del vector de soporte del brazo.
     * @see models.elements.Arm#setSupport(double, double, double)
     */
    public void setSupport(String support){
        arm.setSupport(Converter.stringToDoubleArray(support));
    }

    /**
     * Asgina una posición (Point3d) a partir de un String con el formato
     * "xx, yy, zz" al brazo.
     *
     * @param position String con los valores del Punto que indica la posición
     * del Brazo.
     * @see models.elements.Element#setPosition(double, double, double)
     */
    public void setPosition(String position){
        arm.setPosition(Converter.stringToDoubleArray(position));
    }

    /**
     * A partir del nombre de una etiqueta de un archivo XML (org.w3c.dom.Node)
     * determina que hacer con ésta para crear o modificar los valores de un
     * brazo o pieza de éste.
     *
     * @param node objeto con la información de la etiqueta XML.
     * @throws ArmStructureException Si al momento de agregar una pieza en el
     * brazo, éste contiene datos incongruentes.
     */
    private void readNode(Node node) throws ArmStructureException{
        ArmDom          dom;
        NamedNodeMap    attrs = node.getAttributes();        
        try{
            dom = ArmDom.valueOf(node.getNodeName());
        }catch(java.lang.IllegalArgumentException ex){
            return;
        }
        switch(dom){
            case content:
                createArm();
                break;

            case support:
                setSupport(node.getTextContent());
                break;

            case piece:
                pieceInt.createPiece(attrs.getNamedItem("id").getTextContent(),
                                    attrs.getNamedItem("type").getTextContent(),
                                    attrs.getNamedItem("src").getTextContent());
                pieceInt.getPiece().addListener(keyBehavior);
                break;

            case angle:
                pieceInt.setAngles(node.getTextContent(),
                                  attrs.getNamedItem("class").getTextContent());
                break;

            case relations:
                pieceInt.setRelations(node.getTextContent());
                arm.addPiece(pieceInt.getPiece());
                break;

            case friction:
                pieceInt.addFriction(node.getTextContent());
                break;

            case position:
                String parentName = node.getParentNode().getNodeName();
                if(parentName.equals(ArmDom.content.name()))
                    setPosition(node.getTextContent());
                else if(parentName.equals(ArmDom.piece.name()))
                    pieceInt.setPosition(node.getTextContent(),
                            attrs.getNamedItem("rel").getTextContent());
        }
    }

    /**
     * Devuelve el objeto Scene creado de acuerdo a lo que se ha ido leyendo del
     * archivo.
     *
     * @return el objeto Scene con la información leída.
     * @see com.sun.j3d.loaders.Loader
     * @see com.sun.j3d.loaders.Scene
     */
    @Override
    public Scene getScene(){
        return scene;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            readNode((Node) evt.getNewValue());
        } catch (ArmStructureException ex) {
            System.err.println("Error al crear dato: " + ex.getMessage());
        }
    }
}