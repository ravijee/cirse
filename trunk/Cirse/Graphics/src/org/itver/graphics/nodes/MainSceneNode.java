/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.graphics.nodes;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import org.itver.common.util.Converter;
import org.itver.graphics.model.MSComponentContent;
import org.itver.graphics.model.MainScene;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.model.SceneLight;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Karo
 */
public class MainSceneNode extends AbstractNode{
    private MainScene ms;

    public MainSceneNode(MainScene ms){
        super(MSChildren.LEAF, Lookups.singleton(ms));
        this.setChildren(new MSChildren());
        this.setDisplayName("Main Scene");
        this.setName(ms.toString());
        this.ms = ms;
    }

    @Override
    public Image getIcon(int type) {
        String path = "org/itver/graphics/img/ms";
        return ImageUtilities.loadImage(Converter.convertImgPath(path, type));
    }

    @Override
    public Image getOpenedIcon(int type){
        return this.getIcon(type);
    }

    @Override
    protected Sheet createSheet() {
        Sheet result = Sheet.createDefault();
        Sheet.Set props = Sheet.createPropertiesSet();
        Sheet.Set materialProps = Sheet.createPropertiesSet();
        Sheet.Set textureProps = Sheet.createPropertiesSet();
        materialProps.setDisplayName("Material Colors");
        materialProps.setName("material");
        textureProps.setDisplayName("Material Texture");
        textureProps.setName("texture");
        try {
            Property width = new PropertySupport.Reflection(ms.getEnvironmentLimits(), float.class, "width");
            Property height = new PropertySupport.Reflection(ms.getEnvironmentLimits(), float.class, "height");
            Property deepness = new PropertySupport.Reflection(ms.getEnvironmentLimits(), float.class, "deepness");
            Property thickness = new PropertySupport.Reflection(ms.getEnvironmentLimits(), float.class, "thickness");
            Property textureFlag = new PropertySupport.Reflection(ms.getEnvironmentLimits(), boolean.class, "hasTexture", "setTextureFlag");
            Property texture = new PropertySupport.Reflection(ms.getEnvironmentLimits(), File.class, "texture");
//            Property bgFlag = new PropertySupport.Reflection(ms, boolean.class, "hasBackgroundImage", "setBackgroundFlag");
            Property background = new PropertySupport.Reflection(ms, File.class, "backgroundFile");
            Property bgColor = new PropertySupport.Reflection(ms, Color.class, "backgroundColor");
            Property ambientColor = new PropertySupport.Reflection(ms.getEnvironmentLimits(), Color.class, "getMaterialAmbientColor", "setMaterialAmbientColor");
            Property emissiveColor = new PropertySupport.Reflection(ms.getEnvironmentLimits(), Color.class, "getMaterialEmissiveColor", "setMaterialEmissiveColor");
            Property diffuseColor = new PropertySupport.Reflection(ms.getEnvironmentLimits(), Color.class, "getMaterialDiffuseColor", "setMaterialDiffuseColor");
            Property specularColor = new PropertySupport.Reflection(ms.getEnvironmentLimits(), Color.class, "getMaterialSpecularColor", "setMaterialSpecularColor");
            Property shininess = new PropertySupport.Reflection(ms.getEnvironmentLimits(), float.class, "getMaterialShininess", "setMaterialShininess");
            width.setName("Width");
            width.setShortDescription("LOLOLOLOLOL");
            height.setName("Height");
            deepness.setName("Deepness");
            thickness.setName("Thickness");
            textureFlag.setName("Activate");
            texture.setName("File");
            bgColor.setName("Background color");
//            bgFlag.setName("Background image");
            background.setName("Background file");
            ambientColor.setName("Ambient");
            emissiveColor.setName("Emissive");
            diffuseColor.setName("Diffuse");
            specularColor.setName("Specular");
            shininess.setName("Shininess");
            
            props.put(width);
            props.put(height);
            props.put(deepness);
            props.put(thickness);
            props.put(background);
            props.put(bgColor);
//            props.put(textureFlag);
            materialProps.put(ambientColor);
            materialProps.put(emissiveColor);
            materialProps.put(diffuseColor);
            materialProps.put(specularColor);
            materialProps.put(shininess);
            textureProps.put(texture);
            textureProps.put(textureFlag);
//            props.put(bgFlag);

        } catch (NoSuchMethodException ex) {
            System.err.println(ex.getMessage());
        }
        result.put(props);
        result.put(materialProps);
        result.put(textureProps);
        return result;
    }

    private class MSChildren extends Children.Keys{
        ArrayList components;
        
        public MSChildren(){
            components = new ArrayList();
            addComponents();
            this.setKeys(components);
        }

        @Override
        protected Node[] createNodes(Object key) {
            if(key instanceof MainSceneComponent){
                /*
                 * Pablo: Aqui se cargan los nodos obtenids gracias a la interfaz.
                 * Si el plugin no implementa esa interfaz, sus nodos nunca podran
                 * ser cargados en el navigator.
                 */
                MainSceneComponent msc = (MainSceneComponent)key;
                if(msc.getContent() instanceof MSComponentContent){
                    MSComponentContent content = (MSComponentContent)msc.getContent();
                    return content.getContentNode();
                } else

                return new Node[] {new SceneComponentNode(msc)};
            }else{
//                if(key instanceof SceneLight){
                    return new Node[] {new LightNode((SceneLight)key)};

//                }else{
//                    return new Node[] {new EnvironmentLimitsNode((EnvironmentLimits)key)};
//                }
            }
        }

        public final void addComponents(){
            components.addAll(MainScene.getInstance().getComponentArray());
            components.addAll(MainScene.getInstance().getLightArray());
//            components.add(MainScene.getInstance().getEnvironmentLimits());
        }
        
    }

}
