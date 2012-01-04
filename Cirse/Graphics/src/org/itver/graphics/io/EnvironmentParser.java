/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.graphics.io;

import java.awt.Color;
import java.io.File;
import javax.media.j3d.Material;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.itver.common.util.Converter;
import org.itver.common.xml.SceneHandler;
import org.itver.graphics.model.MainScene;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.model.SceneLight;
import org.itver.graphics.util.ComponentType;
import org.itver.graphics.util.Element;
import org.itver.graphics.util.LightType;
import org.openide.util.Lookup;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Esta clase analiza un archivo XML determinado y a partir de su contenido crea 
 * el entorno tridimensional con características específicas.
 * @see SceneHandler
 * @author Karo
 */
public class EnvironmentParser extends SceneHandler{
    private MainScene mainScene;
    private Element element;
    private Material material;
    private int lightIndex;
    private MainSceneComponent component;
    
    /**
     * Crea una instancia del analizador para una escena.
     * @param mainScene Escena a construir.
     */
    public EnvironmentParser(MainScene mainScene){
        this.mainScene = mainScene;
    }

    /**
     * Analiza el contenido de cada etiqueta XML.
     * @param ch Caracteres que contiene la etiqueta.
     * @param start Inicio de los caracteres.
     * @param length Longitud de caracteres.
     * @throws SAXException 
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content = String.valueOf(ch, start, length).replaceAll("[\\s]*", "");
        if(content.equals("\n") || content.equals("")) return;
        SceneLight light;
        switch (element){
            case background:
                float[] bg = Converter.stringToFloatArray(content);
                Color bgColor = new Color(bg[0], bg[1], bg[2]);
                mainScene.setBackgroundColor(bgColor);
                break;
            case width:
                float width = Float.parseFloat(content);
                mainScene.getEnvironmentLimits().setWidth(width);
                break;
            case height:
                float height = Float.parseFloat(content);
                mainScene.getEnvironmentLimits().setHeight(height);
                break;
            case deepness:
                float deepness = Float.parseFloat(content);
                mainScene.getEnvironmentLimits().setDeepness(deepness);
                break;
            case thickness:
                float thickness = Float.parseFloat(content);
                mainScene.getEnvironmentLimits().setThickness(thickness);
                break;
            case ambient:
                float[] ambientColor = Converter.stringToFloatArray(content);
//                mainScene.getEnvironmentLimits().setMaterialAmbientColor(new Color(ambientColor[0], ambientColor[1], ambientColor[2]));
                material.setAmbientColor(ambientColor[0], ambientColor[1], ambientColor[2]);
                break;
            case emissive:
                float[] emissiveColor = Converter.stringToFloatArray(content);
//                mainScene.getEnvironmentLimits().setMaterialEmissiveColor(new Color(emissiveColor[0], emissiveColor[1], emissiveColor[0]));
                material.setEmissiveColor(emissiveColor[0], emissiveColor[1], emissiveColor[0]);
                break;
            case diffuse:
                float[] diffuseColor = Converter.stringToFloatArray(content);
//                mainScene.getEnvironmentLimits().setMaterialDiffuseColor(new Color(diffuseColor[0], diffuseColor[1], diffuseColor[2]));
                material.setDiffuseColor(diffuseColor[0], diffuseColor[1], diffuseColor[2]);
                break;
            case specular:
                float[] specularColor = Converter.stringToFloatArray(content);
//                mainScene.getEnvironmentLimits().setMaterialSpecularColor(new Color(specularColor[0], specularColor[1], specularColor[2]));
                material.setSpecularColor(specularColor[0], specularColor[1], specularColor[2]);
                break;
            case shininess:
                float shininess = Float.parseFloat(content);
//                mainScene.getEnvironmentLimits().setMaterialShininess(shininess);
                material.setShininess(shininess);
                mainScene.getEnvironmentLimits().setLimitMaterial(material);
                break;
            case color:
                float[] colorValues = Converter.stringToFloatArray(content);
                Color lightColor = new Color(colorValues[0], colorValues[1], colorValues[2]);
                mainScene.getLightArray().get(lightIndex).setColor(lightColor);
            //Mucha repetición de código en los casos position, attenuation y direction. Ver cómo reducir.
            case position:
                light = mainScene.getLightArray().get(lightIndex);
                float[] positions = Converter.stringToFloatArray(content);
                Point3f position = new Point3f(positions[0], positions[1], positions[2]);
                if(light.getLightType() == LightType.ambient)
                    break;
                else{
                    if(light.getLightType() == LightType.directional)
                        break;
                    else{
                        if(light.getLightType() == LightType.point)
                            light.setPosition(position);
                    }  
                }
                break;
            case attenuation:
                light = mainScene.getLightArray().get(lightIndex);
                float[] attenuationValues = Converter.stringToFloatArray(content);
                Point3f attenuation = new Point3f(attenuationValues[0], attenuationValues[1], attenuationValues[2]);
                if(light.getLightType() == LightType.ambient)
                    break;
                else{
                    if(light.getLightType() == LightType.directional)
                        break;
                    else{
                        if(light.getLightType() == LightType.point)
                            light.setAttenuation(attenuation);
                    }  
                }
                break;
            case direction:
                light = mainScene.getLightArray().get(lightIndex);
                float[] vector = Converter.stringToFloatArray(content);
                Vector3f direction = new Vector3f(vector[0], vector[1], vector[2]);
                if(light.getLightType() == LightType.ambient)
                    break;
                else{
                    if(light.getLightType() == LightType.directional)
                        light.setDirection(direction);
                    else{
                        if(light.getLightType() == LightType.point)
                            break;
                    }  
                }
                break;
            case name:
                component.setComponentName(content);
                break;
            case scale:
                double scale = Double.parseDouble(content);
                component.setScale(scale);
                break;
            case location:
                double[] locationValues = Converter.stringToDoubleArray(content);
                component.setPosition(locationValues);
                break;
            case rotation:
                double[] rotationValues = Converter.stringToDoubleArray(content);
                component.setRotation(rotationValues);
                break;             
        }
    }

    /**
     * Analiza las etiquetas que abren un elemento en XML.
     * @param uri
     * @param localName 
     * @param qName Nombre de la etiqueta.
     * @param attributes Atributos del elemento.
     * @throws SAXException 
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        element = Element.valueOf(qName);
        switch(element){
            case background:
                initBackground(attributes);
                break;
            case texture:
                initLimitTexture(attributes);
                break;
            case light:
                initLight(attributes);
                break;
            case object:
               initMainSceneComponent(attributes);
                break;
            case material:
               material = new Material();
               //Pablo: Capabilities para J3D 1.3
               material.setCapability(Material.ALLOW_COMPONENT_READ);
               //-----------------------------------
               break;
//            case appearance:
//               mainScene.getEnvironmentLimits().setAppearance(new Appearance()); 
//               break; 
        }
    }
    
    /**
     * Construye el fondo de la escena
     * @param attr Atributo del fondo.
     */
    private void initBackground(Attributes attr){
        if(attr.getLength() != 0){
//        if(textureFile != null)
            File textureFile = this.relativeFile(attr.getValue("src"));
            mainScene.setBackgroundFile(textureFile);
        }    
    }
    
    /**
     * Inicializa una textura para los límites en caso de que exista.
     * @param attr Atributo de la textura.
     */
    private void initLimitTexture(Attributes attr){
        File limitTexture = this.relativeFile(attr.getValue("src"));
        if(attr.getValue("enabled").equals(String.valueOf(Boolean.TRUE))){
            mainScene.getEnvironmentLimits().setTexture(limitTexture);
            mainScene.getEnvironmentLimits().setTextureFlag(true);
        }
    }
    
    /**
     * Crea tantas luces como haya en el arreglo de luces MainScene.
     * @param attr Atributo de la luz.
     */
    private void initLight(Attributes attr){
        LightType type = LightType.valueOf(attr.getValue("type"));
        SceneLight sceneLight = new SceneLight(type);
        mainScene.addLight(sceneLight);
        lightIndex = mainScene.getLightArray().indexOf(sceneLight);
    }
    
    /**
     * Crea tantos MainSceneComponents como haya en el arreglo de componentes
     * MainScene
     * @param attr Atributos de los componentes.
     */
    private void initMainSceneComponent(Attributes attr){
        ComponentType type = ComponentType.valueOf(attr.getValue("type"));
        File source = this.relativeFile(attr.getValue("src"));
        component = new MainSceneComponent(type, source);
        if(type == ComponentType.furniture || type == ComponentType.pickable){
            component.loadType();
            mainScene.addComponent(component);    
        }else{
            for (LoadingService svc : Lookup.getDefault().lookupAll(LoadingService.class)) {
                component = svc.getArm(source);
                mainScene.addComponent(component);
            }
        }    
    }
    
    //Pablo
    //Metodo para crear el archivo relativo
    //TODO: Probar este metodo en Windows
    /**
     * Crea un objeto File con el nombre de archivo dado, creandolo a partir de
     * la carpeta contenedora del archivo leido.
     * @param fileName Nombre del archivo a leer
     * @return Objeto File aparitr del archivo a leer
     */
    private File relativeFile(String fileName){
        //Validación del parametro
        if(fileName == null)
            return null;
        
        //Se crea un objeto File con la ruta inicial, que se obtiene directo del
        //Atributo "src" del archivo
        File result = new File(fileName);
        
        //Si el objeto no contiene una ruta absoluta, se debe referenciar a 
        //partir de la dirección del XML que se esta leyendo
        if(!result.isAbsolute())
            result = new File(this.parentPath, fileName);
        return result;
    }
    
}
