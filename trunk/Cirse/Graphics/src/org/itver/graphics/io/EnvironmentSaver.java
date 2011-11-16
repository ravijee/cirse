/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.graphics.io;

import java.io.File;
import java.util.ArrayList;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.itver.common.util.Converter;
import org.itver.common.xml.XmlSaver;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.model.SceneLight;
import org.itver.graphics.model.Universe;
import org.xml.sax.SAXException;

/**
 * Esta clase guarda la configuración de las propiedades del entorno tridimensional
 * en un archivo XML.
 * @author Karo
 */
public class EnvironmentSaver {
    private XmlSaver saver;
    private float version;

    /**
     * Crea un {@code EnvironmentSaver} recibiendo la versión de documento
     * que va a guardar.
     * @param version Version del documento XML.
     */
    public EnvironmentSaver(float version){
        saver = new XmlSaver();
        this.version = version;
    }
    
    /**
     * Guarda la configuración del entorno tridimensional en un archivo 
     * especificado.
     * @param universe Universo cuya configuración se desea guardar.
     * @param file Archivo en el que se guardará la configuración.
     * @throws SAXException 
     */
    public void save(Universe universe, File file) throws SAXException{
        saver.startFile(XmlSaver.DOCTYPE_UNIVERSE, file.getAbsolutePath());
        generateUniverse(universe);
        saver.endFile();
    }
    
    /**
     * Genera la estructura del archivo XML del Universo.
     * @param universe Universo a guardar.
     * @throws SAXException 
     */
    private void generateUniverse(Universe universe) throws SAXException{
        saver.addAttribute("version", XmlSaver.CDATA, version+"");
        saver.startTag("universe");
        saver.startTag("content");
        generateBackground(universe);
        generateLimits(universe);
        generateLights(universe);
        generateComponents(universe);
        saver.closeTag("content");
        saver.closeTag("universe");
    }

    /**
     * Genera la estructura del archivo XML para el fondo de la escena.
     * @param universe Universo a guardar.
     * @throws SAXException 
     */
    private void generateBackground(Universe universe) throws SAXException {
        File bgFile = universe.getScene().getBackgroundFile();
        if(bgFile != null){
            saver.addAttribute("src", XmlSaver.CDATA, bgFile.getPath());
        }    
//        }else{
//            Color3f bgColor = new Color3f(universe.getScene().getBackgroundColor());
//            saver.startTag("background", Converter.tuple3fToString(bgColor));
//        }
        Color3f bgColor = new Color3f(universe.getScene().getBackgroundColor());
        saver.startTag("background", Converter.tuple3fToString(bgColor));
    }

    /**
     * Genera la estructura del archivo XML para los límites.
     * @param universe Universo a guardar.
     * @throws SAXException 
     */
    private void generateLimits(Universe universe) throws SAXException {
        float[] values = universe.getEnvirionmentLimits().getEnvironmentValues();
        String[] tagNames = new String[]{"width", "height", "deepness", "thickness",
                                         "ambient", "emissive", "diffuse", "specular"};
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
            saver.addAttribute("src", XmlSaver.CDATA, universe.getEnvirionmentLimits().getTexture().getPath());
            saver.addAttribute("enabled", XmlSaver.CDATA, String.valueOf(universe.getEnvirionmentLimits().hasTexture()));
            saver.startTag("texture");
            saver.closeTag("texture");
        }
        saver.closeTag("appearance");
        saver.closeTag("limits");
    }

    /**
     * Genera la estructura del archivo XML para las luces del universo.
     * @param universe Universo a guardar.
     * @throws SAXException 
     */
    private void generateLights(Universe universe) throws SAXException {
        ArrayList<SceneLight> lights = universe.getScene().getLightArray();
        for (int i = 0; i < lights.size(); i++) {
            saver.addAttribute("type", XmlSaver.CDATA, lights.get(i).getLightType().name());
            saver.startTag("light");
            Color3f color = new Color3f(lights.get(i).getColor());
            saver.startTag("color", Converter.tuple3fToString(color));
            switch(lights.get(i).getLightType()){
                case ambient:
                case point:
                    Point3f position = lights.get(i).getPosition();
                    saver.startTag("position", Converter.tuple3fToString(position));
                    Point3f attenuation = lights.get(i).getAttenuation();
                    saver.startTag("attenuation", Converter.tuple3fToString(attenuation));
                    break;
                case directional:
                    Vector3f direction = lights.get(i).getDirection();
                    saver.startTag("direction", Converter.tuple3fToString(direction));
                    break;
            }
            saver.closeTag("light");
        }
    }

    /**
     * Genera la estructura del archivo XML para los componentes de la escena.
     * @param universe Universo a guardar.
     * @throws SAXException 
     */
    private void generateComponents(Universe universe) throws SAXException {
        ArrayList<MainSceneComponent> components = universe.getScene().getComponentArray();
        for (int i = 0; i < components.size(); i++) {
            saver.addAttribute("src", XmlSaver.CDATA, components.get(i).getSource().getPath());
            saver.addAttribute("type", XmlSaver.CDATA, components.get(i).getType().name());
            saver.startTag("object");
            saver.startTag("name", ""+components.get(i).getComponentName());
            saver.startTag("scale", String.valueOf(components.get(i).getScale()));
            saver.startTag("location", Converter.tuple3dToString(components.get(i).getPosition()));
            saver.startTag("rotation", Converter.tuple3dToString(components.get(i).getRotation()));
            saver.closeTag("object");
        }
    }
}