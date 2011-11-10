/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.io;

import java.io.File;
import javax.vecmath.Point3d;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.models.elements.Element;
import org.itver.arm.models.elements.Joint;
import org.itver.arm.models.elements.Piece;
import org.itver.arm.models.elements.ValueType;
import org.itver.common.util.Converter;
import org.itver.common.xml.XmlSaver;
import org.openide.util.Exceptions;
import org.xml.sax.SAXException;

/**
 * Clase encargada de guardar un objeto de la clase {@link models.elements.Arm
 * Arm} en un archivo XML.
 * @author pablo
 * @see models.elements.Arm Arm
 */
public class ArmSaver {
    private XmlSaver saver;
    private float    version;
    private File file;

    private ArmSaver() {
    }

    /**
     * Constructor default. Crea una instancia que guardará el brazo en lenguaje
     * XML
     * @param version versión XML a guardar del formato.
     */
    public ArmSaver(float version) {
        saver = new XmlSaver();
        this.version = version;
    }

    /**
     * Guarda el brazo especificado en la ruta de archivo con el nombre.
     * @param arm brazo a guardar.
     * @param fileName nombre del archivo a guardar en disco.
     */
    public void save(Arm arm, File file) {
        this.file = file;
        try {
            saver.startFile(XmlSaver.DOCTYPE_ARM,

                            file.getAbsolutePath());
            saver.addAttribute("version", XmlSaver.CDATA, version + "");
            saver.startTag("arm");
            saver.startTag("content");
            saveSupport(arm);
            savePosition(arm);
            savePieces(arm);
            saver.closeTag("content");
            saver.closeTag("arm");
            saver.endFile();
        } catch (SAXException ex) {
            System.err.println(ex.getMessage());
        }

    }

    private void saveSupport(Arm arm) throws SAXException {
        String content = Converter.tuple3dToString(arm.getSupport());
        saver.startTag("support", content);

    }

    private void savePieces(Arm arm) throws SAXException {
        for (Piece p : arm.getNext()) {
            walkPiece(p);
        }
    }

    private void savePiece(Piece piece) {
//        this.piece = piece;
        try {
//            saver.addAttribute("id",   XmlSaver.CDATA, "" + piece.getId());
            
            try {
                String armPath = this.file.getParentFile().getAbsolutePath() + "/";
                String piecePath = piece.getSource().getAbsolutePath();
                saver.addAttribute("src", XmlSaver.CDATA, piecePath.replace(armPath, ""));
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
            if(piece instanceof Joint){
                Joint joint = (Joint)piece;
                saver.addAttribute("axis", XmlSaver.CDATA, joint.getAxis().name());
                saver.addAttribute("motion", XmlSaver.CDATA, joint.isMotion() ? "rotational" : "translational");
                saver.startTag("joint");
            }
            else{
                saver.addAttribute("type", XmlSaver.CDATA, piece.getType().name());
                saver.startTag("piece");
            }
//            saveRelations();
            savePosition(piece);
            saveOrientation(piece);
            saveFrictions(piece);
            if(piece instanceof Joint)
                saveJointOrientation((Joint) piece);
            else
                saver.closeTag("piece");
        } catch (SAXException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void savePosition(Element element) throws SAXException {
        String content = Converter.tuple3dToString(element.getPosition());
        saver.startTag("position", content);

    }

    private void saveOrientation(Piece piece) throws SAXException {
        saver.startTag("orientation",
                Converter.doubleArrayToString(piece.getOrientation()));
    }

    private void saveJointOrientation(Joint joint) throws SAXException{
        ValueType valType[] = ValueType.values();
        for (int i = 0; i < valType.length; i++) {
            saver.addAttribute("class", XmlSaver.CDATA, valType[i].name());
            saver.startTag("jorientation", "" + joint.getJointValue(valType[i]));

        }
    }

    private void saveFrictions(Piece piece) throws SAXException {
        for (int i = 0; i < piece.getFrictions().length; i++) {
            Point3d friction = piece.getFriction(i).getPosition();
            saver.startTag("friction",
                    Converter.tuple3dToString(friction));

        }
    }

    private void walkPiece(Piece p){
        savePiece(p);
        if(p instanceof Joint){
            Joint parent = (Joint)p;
            for(Piece p2 : parent.getNext())
                walkPiece(p2);
            try {
                saver.closeTag("joint");
            } catch (SAXException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
