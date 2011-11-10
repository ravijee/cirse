/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.graphics.io;

import java.io.File;
import javax.media.j3d.Material;
import javax.vecmath.Color3f;
import org.itver.common.util.Converter;
import org.itver.common.xml.XmlSaver;
import org.itver.graphics.model.Universe;
import org.itver.graphics.util.Element;
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
        
//        xmlSaver.startFile(XmlSaver.PUBLIC_URL + XmlSaver.DOCTYPE_UNIVERSE,
//                                                                   fileName);
//        xmlSaver.addAttribute("version", XmlSaver.CDATA, version + "");
//        xmlSaver.startTag(dom.universe.name());
//        saveHead();
//        saveContent();
//        xmlSaver.closeTag(dom.universe.name());
//        xmlSaver.endFile();
    }
    
    private void generateUniverse(Universe universe) throws SAXException{
        saver.startTag("universe");
        saver.addAttribute("version", XmlSaver.CDATA, version + "");
        saver.startTag("content");
        generateBackground(universe);
        generateLimits(universe);
    }

    private void generateBackground(Universe universe) throws SAXException {
        File bgFile = universe.getScene().getBackgroundFile();
        if(bgFile != null){
            saver.startTag("background");
            saver.addAttribute("src", XmlSaver.CDATA, bgFile.getPath());
            saver.closeTag("background");
        }else{
            Color3f bgColor = new Color3f(universe.getScene().getBackgroundColor());
            saver.startTag("background", Converter.tuple3fToString(bgColor) );
        }
    }

    private void generateLimits(Universe universe) throws SAXException {
        float[] values = universe.getEnvirionmentLimits().getEnvironmentValues();
        String[] tagNames = new String[]{"width", "height", "deepness", "thickness",
                                         "ambient", "emissive", "difuse", "specular"};
        Color3f[] materialColors = universe.getEnvirionmentLimits().getMaterialColors();
        saver.startTag("limits");
        for (int i = 0; i < values.length; i++)
             saver.startTag(tagNames[i], String.valueOf(values[i]));
        saver.startTag("appearance");
        saver.startTag("material");
        for (int i = 0; i < materialColors.length; i++)
            saver.startTag(tagNames[i+4], Converter.tuple3fToString(materialColors[i]));
        saver.startTag("shininess", String.valueOf(universe.getScene().getEnvironmentLimits().getMaterialShininess()));
        saver.closeTag("material");
        if(universe.getScene().getEnvironmentLimits().getTexture()!= null){
            saver.startTag("texture");
            saver.addAttribute("src", XmlSaver.CDATA, universe.getEnvirionmentLimits().getTexture().getPath());
            saver.closeTag("texture");
        }
        saver.closeTag("appearance");
        
        //        String w = String.valueOf(universe.getScene().getEnvironmentLimits().getWidth());
//        String h = String.valueOf(universe.getScene().getEnvironmentLimits().getHeight());
//        String d = String.valueOf(universe.getScene().getEnvironmentLimits().getDeepness());
//        String t = String.valueOf(universe.getScene().getEnvironmentLimits().getThickness());
//        saver.startTag("width", w);
//        saver.startTag("height", h);
//        saver.startTag("deepness", d);
//        saver.startTag("thickness", t);
    }

    private void generateLight() {
        //TODO: Obtener las luces
    }

    private void generateComponent() throws SAXException {

    }
}