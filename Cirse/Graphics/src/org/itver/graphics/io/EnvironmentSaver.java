/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.graphics.io;

import java.io.File;
import org.itver.common.util.Converter;
import org.itver.common.xml.EnvironmentDom;
import org.itver.common.xml.XmlSaver;
import org.itver.graphics.model.EnvironmentLimits;
import org.itver.graphics.model.MainScene;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.model.Universe;
import org.xml.sax.SAXException;

/**
 * TODO: Probar que este código sirva
 * @author pablo
 */
public class EnvironmentSaver {
    private XmlSaver saver;
    private float version;
    private File file;

    public EnvironmentSaver(float version){
        saver = new XmlSaver();
        this.version = version;
    }

    public void save(Universe universe, File file) throws SAXException{
        saver.startFile(XmlSaver.DOCTYPE_UNIVERSE, file.getAbsolutePath());
        saver.addAttribute("version", XmlSaver.CDATA, version + "");
        saver.startTag("content");
        saver.startTag("background");
        saver.addAttribute("src", XmlSaver.CDATA, universe.getScene().getBackgroundFile().getPath());
        saver.startTag("background");
//        xmlSaver.startFile(XmlSaver.PUBLIC_URL + XmlSaver.DOCTYPE_UNIVERSE,
//                                                                   fileName);
//        xmlSaver.addAttribute("version", XmlSaver.CDATA, version + "");
//        xmlSaver.startTag(dom.universe.name());
//        saveHead();
//        saveContent();
//        xmlSaver.closeTag(dom.universe.name());
//        xmlSaver.endFile();
    }

    private void saveHead() throws SAXException {

    }

    private void saveContent() throws SAXException {

    }

    private void saveBackground() {
        //TODO: obtener background
    }

    private void saveLimits() throws SAXException {

    }

    private void saveLights() {
        //TODO: Obtener las luces
    }

    private void saveObjects() throws SAXException {

    }

    private void saveObject(MainSceneComponent object) throws SAXException {

    }

    private void savePosition(MainSceneComponent object) throws SAXException {

    }

    private void saveAngles(MainSceneComponent object) throws SAXException {
//        String angles = Converter.doubleArrayToString(object.getRotation());
//        xmlSaver.startTag(dom.angles.name(), angles);
    }

    private void saveLimitsAppearance() {
        //TODO: obtener apariencia de los limites
    }


}
