/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.graphics.nodes;

import java.io.File;
import java.util.ArrayList;
import org.itver.graphics.model.MainScene;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.model.SceneLight;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Karo
 */
public class MainSceneNode extends AbstractNode{
    private MainScene ms;

    public MainSceneNode(MainScene ms){
        super(Children.LEAF, Lookups.singleton(ms));
        this.setChildren(new MainSceneChildren());
        this.setDisplayName("Main Scene");
        this.setName(ms.toString());
        this.ms = ms;
    }

    @Override
    protected Sheet createSheet() {
        Sheet result = Sheet.createDefault();
        Sheet.Set props = Sheet.createPropertiesSet();
        try {
            Property width = new PropertySupport.Reflection(ms.getEnvironmentLimits(), float.class, "width");
            Property height = new PropertySupport.Reflection(ms.getEnvironmentLimits(), float.class, "height");
            Property deepness = new PropertySupport.Reflection(ms.getEnvironmentLimits(), float.class, "deepness");
            Property thickness = new PropertySupport.Reflection(ms.getEnvironmentLimits(), float.class, "thickness");
            Property textureFlag = new PropertySupport.Reflection(ms.getEnvironmentLimits(), boolean.class, "hasTexture", "setTextureFlag");
            Property texture = new PropertySupport.Reflection(ms.getEnvironmentLimits(), File.class, "texture");
//            Property bgFlag = new PropertySupport.Reflection(ms, boolean.class, "hasBackgroundImage", "setBackgroundFlag");
//            Property background = new PropertySupport.Reflection(ms, File.class, "backgroundImage");
            width.setName("Width");
            height.setName("Height");
            deepness.setName("Deepness");
            thickness.setName("Thickness");
            textureFlag.setName("Texture on walls");
            texture.setName("Texture file");
//            bgFlag.setName("Background image");
//            background.setName("Background file");
            props.put(width);
            props.put(height);
            props.put(deepness);
            props.put(thickness);
            props.put(textureFlag);
            props.put(texture);
//            props.put(bgFlag);
//            props.put(background);

        } catch (NoSuchMethodException ex) {
            System.err.println(ex.getMessage());
        }
        result.put(props);
        return result;
    }

    private class MainSceneChildren extends Children.Keys{
        ArrayList components;
        
        public MainSceneChildren(){
            components = new ArrayList();
            addComponents();
            this.setKeys(components);
        }

        @Override
        protected Node[] createNodes(Object key) {
            if(key instanceof MainSceneComponent){
                return new Node[] {new SceneComponentNode((MainSceneComponent)key)};
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
