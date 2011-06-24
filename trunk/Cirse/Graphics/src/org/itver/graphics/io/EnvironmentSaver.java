/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.graphics.io;

import org.itver.common.util.Converter;
import org.itver.common.xml.EnvironmentDom;
import org.itver.common.xml.XmlSaver;
import org.itver.graphics.model.EnvironmentLimits;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.model.Universe;
import org.xml.sax.SAXException;

/**
 * TODO: Probar que este código sirva
 * @author pablo
 */
public class EnvironmentSaver {
    private XmlSaver        xmlSaver;
    private Universe        universe;
    private EnvironmentDom  dom;
    private float           version;

    public EnvironmentSaver(){
        xmlSaver = new XmlSaver();
    }

    public EnvironmentSaver(float version){
        this();
        this.version = version;
    }

    public void save(Universe universe, String fileName) throws SAXException{
        this.universe = universe;
        xmlSaver.startFile(XmlSaver.DOCTYPES_DIR + XmlSaver.DOCTYPE_UNIVERSE,
                                                                   fileName);
        xmlSaver.addAttribute("version", XmlSaver.CDATA, version + "");
        xmlSaver.startTag(dom.universe.name());
        saveHead();
        saveContent();
        xmlSaver.closeTag(dom.universe.name());
        xmlSaver.endFile();
    }

    private void saveHead() throws SAXException {
        xmlSaver.startTag(dom.head.name());
        xmlSaver.closeTag(dom.head.name());
    }

    private void saveContent() throws SAXException {
        xmlSaver.startTag(dom.content.name());
        saveBackground();
        saveLimits();
        saveLights();
        saveObjects();
        xmlSaver.closeTag(dom.content.name());
    }

    private void saveBackground() {
        //TODO: obtener background
    }

    private void saveLimits() throws SAXException {
        EnvironmentLimits limits = universe.getEnvirionmentLimits();
        xmlSaver.addAttribute("width", XmlSaver.CDATA, limits.getWidth() + "");
        xmlSaver.addAttribute("height", XmlSaver.CDATA, limits.getHeight() + "");
        xmlSaver.addAttribute("deepness", XmlSaver.CDATA, limits.getDeepness() + "");
        xmlSaver.addAttribute("thickness", XmlSaver.CDATA, limits.getThickness() + "");
        xmlSaver.startTag(dom.limits.name());
        saveLimitsAppearance();
        xmlSaver.closeTag(dom.limits.name());
    }

    private void saveLights() {
        //TODO: Obtener las luces
    }

    private void saveObjects() throws SAXException {
        for(MainSceneComponent object : universe.getScene().getComponentArray())
            saveObject(object);
    }

    private void saveObject(MainSceneComponent object) throws SAXException {
        xmlSaver.addAttribute("type", XmlSaver.CDATA, object.getType().name());
        xmlSaver.addAttribute("src", XmlSaver.CDATA, object.getSource().getPath());
        xmlSaver.addAttribute("id", XmlSaver.CDATA, object.getId() + "");
        if(object.getScale() != 1)
            xmlSaver.addAttribute("scale", XmlSaver.CDATA, object.getScale() + "");
        if(object.getComponentName() != null)
            xmlSaver.addAttribute("name", XmlSaver.CDATA, object.getComponentName());
        xmlSaver.startTag(dom.object.name());
        savePosition(object);
        saveAngles(object);
        xmlSaver.closeTag(dom.object.name());
    }

    private void savePosition(MainSceneComponent object) throws SAXException {
        String position = Converter.tuple3dToString(object.getPosition());
        xmlSaver.startTag(dom.objPos.name(), position);
    }

    private void saveAngles(MainSceneComponent object) throws SAXException {
        String angles = Converter.doubleArrayToString(object.getRotation());
        xmlSaver.startTag(dom.angles.name(), angles);
    }

    private void saveLimitsAppearance() {
        //TODO: obtener apariencia de los limites
    }


}
