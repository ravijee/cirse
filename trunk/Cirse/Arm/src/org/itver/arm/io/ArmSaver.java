/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.io;

import java.io.IOException;
import javax.vecmath.Point3d;
import org.itver.arm.models.elements.AngleType;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.models.elements.Piece;
import org.itver.common.util.Converter;
import org.itver.common.xml.ArmDom;
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
    private Arm      arm;
    private XmlSaver saver;
    private ArmDom   dom;
    private Piece    piece;
    private float    version;

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
    public void save(Arm arm, String fileName) {
        this.arm = arm;
        try {
            saver.startFile(XmlSaver.DOCTYPES_DIR + XmlSaver.DOCTYPE_ARM, fileName);
            saver.addAttribute("version", XmlSaver.CDATA, version + "");
            saver.startTag(dom.arm.name());
            saver.startTag(dom.content.name());
            saveSupport();
            savePieces();
            saver.closeTag(dom.content.name());
            saver.closeTag(dom.arm.name());
            saver.endFile();
        } catch (SAXException ex) {
            System.err.println(ex.getMessage());
        }

    }

    private void saveSupport() throws SAXException {
        String content = Converter.tuple3dToString(arm.getSupport());
        saver.startTag(dom.support.name(), content);

    }

    private void savePieces() throws SAXException {
        for (Piece p : arm.getPieces()) {
            savePiece(p);
        }
    }

    private void savePiece(Piece piece) {
        this.piece = piece;
        try {
            saver.addAttribute("id",   XmlSaver.CDATA, "" + piece.getId());
            saver.addAttribute("type", XmlSaver.CDATA, piece.getType().name());
            try {
                saver.addAttribute("src", XmlSaver.CDATA, piece.getSource().getCanonicalPath());
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            saver.startTag(dom.piece.name());
            saveRelations();
            savePosition();
            saveAngles();
            saveFrictions();
            saver.closeTag(dom.piece.name());
        } catch (SAXException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void savePosition() throws SAXException {
        saver.addAttribute("rel", XmlSaver.CDATA,
                piece.isRelative() ? "relative" : "absolute");
        String content = Converter.tuple3dToString(piece.getPosition());
        saver.startTag(dom.position.name(), content);

    }

    private void saveAngles() throws SAXException {
        AngleType angType[] = AngleType.values();
        for (int i = 0; i < angType.length; i++) {
            saver.addAttribute("class", XmlSaver.CDATA, angType[i].name());
            saver.startTag(dom.angle.name(),
                    Converter.doubleArrayToString(piece.getAngles(angType[i])));

        }
    }

    private void saveFrictions() throws SAXException {
        for (int i = 0; i < piece.getFrictions().length; i++) {
            Point3d friction = piece.getFriction(i).getPosition();
            saver.startTag(dom.friction.name(),
                    Converter.tuple3dToString(friction));

        }
    }

    private void saveRelations() throws SAXException {
        saver.startTag(dom.relations.name(),
                Converter.intArrayToString(piece.getRelations()));

    }
}
