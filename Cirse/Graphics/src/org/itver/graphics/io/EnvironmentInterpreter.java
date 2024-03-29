/*
 * To change this template: choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.graphics.io;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.SceneBase;
import java.beans.PropertyChangeEvent;
import java.io.File;
import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.itver.common.util.Converter;
import org.itver.common.xml.EnvironmentDom;
import org.itver.common.xml.Interpreter;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.model.MainScene;
import org.itver.graphics.model.SceneLight;
import org.itver.graphics.util.ComponentType;
import org.itver.graphics.util.LightType;

/**
 *
 * @author pablo
 */
@Deprecated
public class EnvironmentInterpreter extends Interpreter {

    private SceneBase scene;
    private MainScene mainScene;
    private SceneLight     sceneLight; //Cambié esto para lo de las luces 4/3/2011 2:58pm
    private MainSceneComponent object;

    private Appearance limitApp;
    private Material   material;
    private Texture    texture;

    public EnvironmentInterpreter() {
        scene = new SceneBase();
        this.mainScene = MainScene.getInstance();
    }

    public EnvironmentInterpreter(MainScene parent){
        this();
        this.mainScene = parent;
    }

    @Override
    public Scene getScene() {
        scene.setSceneGroup(mainScene);
        return this.scene;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        readNode((Node) evt.getNewValue());
    }

    private void readNode(Node node) {
        EnvironmentDom dom;
        NamedNodeMap   attrs = node.getAttributes();
        try{
            dom = EnvironmentDom.valueOf(node.getNodeName());
        }catch(java.lang.IllegalArgumentException ex){
            return;
        }
        switch (dom) {
            case background:
                String type = attrs.getNamedItem("type").getTextContent();
                if(type.equals("color"))
                    mainScene.setBackgroundColor(new Color3f(Converter.stringToFloatArray(
                                                 node.getTextContent())));
                else if(type.equals("image")){
                    try{
                        File bgFile = new File(attrs.getNamedItem("src").getTextContent());
                        mainScene.setBackgroundFile(bgFile);
                    }catch(NullPointerException ex){
                        System.err.println(ex.getMessage());
                    }
                }
                break;
            case limits:
                float width = Float.parseFloat(attrs.getNamedItem("width").
                                                         getTextContent());
                float height = Float.parseFloat(attrs.getNamedItem("height").
                                                         getTextContent());
                float deepness = Float.parseFloat(attrs.getNamedItem("deepness").
                                                         getTextContent());
                float thickness = Float.parseFloat(attrs.getNamedItem("thickness").
                                                         getTextContent());
                mainScene.getEnvironmentLimits().updateLimits(width, height,
                                                        deepness, thickness);
//                mainScene.getEnvironmentLimits().calculateUniverseBounds();
                break;
            case light:
                type = attrs.getNamedItem("type").getTextContent();
                sceneLight = new SceneLight(LightType.valueOf(type));
                scene.addLightNode(sceneLight.getLight());
                mainScene.addLight(sceneLight);
                break;
            case object:
                type       = attrs.getNamedItem("type").getTextContent();
                String src = attrs.getNamedItem("src").getTextContent();
                String id  = attrs.getNamedItem("id").getTextContent();

                object = new MainSceneComponent(Integer.parseInt(id),
                                                  ComponentType.valueOf(type),
                                                  new File(src));
                object.loadType();
                if(attrs.getNamedItem("scale") != null){
                    String sca = attrs.getNamedItem("scale").getTextContent();
                    object.setScale(Double.valueOf(sca));
                }
                if(attrs.getNamedItem("name") != null){
                    String name = attrs.getNamedItem("name").getTextContent();
                    object.setComponentName(name);
                }
                scene.addViewGroup(object.getTransformGroup());
                mainScene.addComponent(object);
                break;
            case appearance:
                limitApp = new Appearance();
                break;
            case material:
                material = new Material();
                break;
            case texture:
//                TextureLoader textureLoad = new TextureLoader(attrs.getNamedItem("src").getTextContent(), null);
//                ImageComponent2D textureIm = textureLoad.getScaledImage(128, 128);
//                texture = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB, textureIm.getWidth(), textureIm.getHeight());
//                texture.setImage(0, textureIm);
//                limitApp.setTexture(texture);
//                TextureAttributes textureAttr = new TextureAttributes();
//                textureAttr.setTextureMode(TextureAttributes.REPLACE);
//                limitApp.setTextureAttributes(textureAttr);
//                TexCoordGeneration tcg = new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR,
//                                                                 TexCoordGeneration.TEXTURE_COORDINATE_2);
//                limitApp.setTexCoordGeneration(tcg);
////                limitApp.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST, 0.7f));
////                mainScene.getEnvironmentLimits().setAppearance(EnvironmentLimits.RIGHT, limitApp);
//                mainScene.getEnvironmentLimits().setAppearance(limitApp);
                mainScene.getEnvironmentLimits().setTexture(new File(attrs.getNamedItem("src").getTextContent()));
                boolean enabled = Boolean.parseBoolean(attrs.getNamedItem("enabled").getTextContent());
                mainScene.getEnvironmentLimits().setTextureFlag(enabled);
                break;
            case ambient:
                material.setAmbientColor(new Color3f(
                        Converter.stringToFloatArray(node.getTextContent())));
                break;
            case emissive:
                material.setEmissiveColor(new Color3f(
                        Converter.stringToFloatArray(node.getTextContent())));
                break;
            case diffuse:
                material.setDiffuseColor(new Color3f(
                        Converter.stringToFloatArray(node.getTextContent())));
                break;
            case specular:
                material.setSpecularColor(new Color3f(
                        Converter.stringToFloatArray(node.getTextContent())));
                break;
            case shininess:
                material.setShininess(Float.parseFloat(node.getTextContent()));
                limitApp.setMaterial(material);
//                mainScene.getEnvironmentLimits().setAppearance(limitApp);
                break;
            case color:
                sceneLight.setColor(new Color3f(
                        Converter.stringToFloatArray(node.getTextContent())));
                break;
            case position:
                sceneLight.setPosition(new Point3f(Converter.stringToFloatArray
                                                    (node.getTextContent())));
//                if(sceneLight instanceof PointLight){
//                    PointLight pl = (PointLight)sceneLight;
//                    pl.setPosition(new Point3f(Converter.stringToFloatArray
//                                                    (node.getTextContent())));
//                } else if(sceneLight instanceof DirectionalLight){
//                    DirectionalLight dl = (DirectionalLight)sceneLight;
//                    dl.setDirection(new Vector3f(Converter.stringToFloatArray
//                                                    (node.getTextContent())));
//                }
                break;
            case direction:
                sceneLight.setDirection(new Vector3f(Converter.stringToFloatArray
                                                    (node.getTextContent())));
            case atenuation:
//                PointLight pl = (PointLight)sceneLight;
//                pl.setAttenuation(new Point3f(Converter.stringToFloatArray(
//                                                     node.getTextContent())));
                sceneLight.setAttenuation(new Point3f(Converter.stringToFloatArray(
                                                     node.getTextContent())));
                break;            
            case objPos: 
                object.setPosition(Converter.stringToDoubleArray(node.getTextContent()));
                break;
            case angles:
                double rotation[] = Converter.stringToDoubleArray(
                                            node.getTextContent());
                object.setRotation(rotation);
                break;
        }
    }
}
