/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.io;

import com.sun.j3d.loaders.Scene;
import java.io.File;
import java.util.Stack;
import javax.media.j3d.BranchGroup;
import org.itver.arm.controls.controllers.ArmController;
import org.itver.arm.controls.controllers.JointController;
import org.itver.arm.controls.controllers.PieceController;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.models.elements.Axis;
import org.itver.arm.models.elements.Element;
import org.itver.arm.models.elements.ElementType;
import org.itver.arm.models.elements.Joint;
import org.itver.arm.models.elements.Piece;
import org.itver.arm.models.elements.ValueType;
import org.itver.common.util.Converter;
import org.itver.common.xml.SceneFromXML;
import org.itver.common.xml.SceneHandler;
import org.itver.gui.util.Dialogs;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Manejador SAX para leer el archivo de un Brazo. Crea un nuevo {@link Arm Brazo}
 * y lo asigna como {@link BranchGroup BranchGroup} principal en la
 * {@link Scene Escena}.
 * @author pablo
 * @see SceneHandler SceneHandler
 * @see SceneFromXML SceneFromXML
 */
public class ArmHandler extends SceneHandler{

    private Arm             arm;
    private Element         last;
    private Tags            tag;
    private Stack<Joint>    joints;
    private ValueType       valueType;

    private enum            Tags{
        arm,
        content,
        joint,
        piece,
        position,
        jorientation,
        orientation,
        friction
    } 



    public ArmHandler() {
        super();
        this.joints = new Stack<Joint>();        
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String chars = String.valueOf(ch, start, length).replaceAll("[\\s]*", "");
        if("\n".equals(chars) || "".equals(chars))
            return;
        double[] vals = new double[3];
        if(tag != Tags.jorientation)
            vals = Converter.stringToDoubleArray(chars);
        switch(tag){
            case position:
                last.setPosition(vals);
                break;
            case jorientation:
                if(last instanceof Joint){
                    double value = Double.parseDouble(chars);
                    Joint joint = (Joint)last;
                    JointController.singleton().setAngleVal(joint, valueType, value);
                }
                break;
            case orientation:
                if(last instanceof Piece){
                    Piece piece = (Piece)last;
                    PieceController.singleton().setOrientation(piece, vals);
                }
                break;
            case friction:
                if(last instanceof Piece){
                    Piece piece = (Piece)last;
                    PieceController.singleton().addFrictionToPiece(piece, vals);
                }
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try{
            switch(Tags.valueOf(qName)){
                case joint:
                    this.joints.pop();
            }
        }catch(IllegalArgumentException ex){
            return;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try{
            tag = Tags.valueOf(qName);
        }catch(IllegalArgumentException ex){
            return;
        }
        switch(tag){
            case content:
                arm = new Arm();
                this.scene.setSceneGroup(arm);
                last = arm;
                break;

            case joint:
            case piece:
                Piece piece = this.createPiece(attributes);
                if(piece != null)
                    last = piece;
                break;

            case jorientation:
                this.setJointValue(attributes);
                break;
        }
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        Dialogs.showErrorDialog(e.getMessage());
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        Dialogs.showErrorDialog(e.getMessage());
    }



    private Piece createPiece(Attributes attributes){
        String fileName = attributes.getValue("src");
        File file = new File(fileName);
        if(!(file.isAbsolute()))
            file = new File(this.parentPath, fileName);
        Piece result = null;
        if(fileName != null){
            if(tag == Tags.joint){
                result = JointController.singleton().newFromFile(file);
                Joint jres = (Joint)result;
                jres.setMotion(attributes.getValue("motion").equalsIgnoreCase("rotational"));
                jres.setAxis(Axis.valueOf(attributes.getValue("axis")));
            }
            else{
                String pieceType = attributes.getValue("type");
                result = PieceController.singleton().newFromFile(file, ElementType.valueOf(pieceType));
            }
            if(this.joints.size() > 0)
                JointController.singleton().insertPiece(this.joints.peek(), result);
            else
                ArmController.singleton().insertPiece(arm, result);

            if(result instanceof Joint)
                this.joints.push((Joint)result);
        }
        return result;
    }

    private void setJointValue(Attributes attributes){
        String type = attributes.getValue("class");
        valueType = ValueType.valueOf(type);
    }

}
