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
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Karo
 */
public class EnvironmentParser extends SceneHandler{
    private MainScene mainScene;
    private Element element;
    private Material material;
    private int lightIndex;
    private MainSceneComponent component;
    
    public EnvironmentParser(MainScene mainScene){
        this.mainScene = mainScene;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content = String.valueOf(ch, start, length);
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
                material.setAmbientColor(ambientColor[0], ambientColor[1], ambientColor[2]);
                break;
            case emissive:
                float[] emissiveColor = Converter.stringToFloatArray(content);
                material.setEmissiveColor(emissiveColor[0], emissiveColor[1], emissiveColor[0]);
                break;
            case diffuse:
                float[] diffuseColor = Converter.stringToFloatArray(content);
                material.setDiffuseColor(diffuseColor[0], diffuseColor[1], diffuseColor[2]);
                break;
            case specular:
                float[] specularColor = Converter.stringToFloatArray(content);
                material.setSpecularColor(specularColor[0], specularColor[1], specularColor[2]);
                break;
            case shininess:
                float shininess = Float.parseFloat(content);
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
               break;
        }
    }
    
    private void initBackground(Attributes attr){
        File textureFile = new File(attr.getValue("src"));
        if(attr.getLength() != 0){
            mainScene.setBackgroundFile(textureFile);
        }
    }
    
    private void initLimitTexture(Attributes attr){
        File limitTexture = new File(attr.getValue("src"));
        if(attr.getValue("enabled").equals(Element.enabled.name())){
            mainScene.getEnvironmentLimits().setTexture(limitTexture);
        }
    }
    
    private void initLight(Attributes attr){
        LightType type = LightType.valueOf(attr.getValue("type"));
        SceneLight sceneLight = new SceneLight(type);
        mainScene.addLight(sceneLight);
        lightIndex = mainScene.getLightArray().indexOf(sceneLight);
    }
    
    private void initMainSceneComponent(Attributes attr){
        ComponentType type = ComponentType.valueOf(attr.getValue("type"));
        File source = new File(attr.getValue("src"));
        component = new MainSceneComponent(type, source);
        component.loadType();
        mainScene.addComponent(component);
    }
    
}
