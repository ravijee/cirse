/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.graphics.nodes;

import java.awt.Color;
import java.awt.Image;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.PointLight;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.itver.common.propertyeditors.Tuple3dEditor;
import org.itver.common.util.Converter;
import org.itver.graphics.model.SceneLight;
import org.itver.graphics.util.LightType;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Karo
 */
public class LightNode extends AbstractNode {
    private SceneLight sceneLight;

    private static final String PATH = "org/itver/graphics/img/sl";

    public LightNode(SceneLight sceneLight) {
        super(Children.LEAF, Lookups.singleton(sceneLight));
        this.setDisplayName("light: "+
                 (sceneLight.getLight() instanceof AmbientLight ? "Ambient Light" :
                 (sceneLight.getLight() instanceof PointLight ? "Point Light":"Directional Light")));
        this.setName(sceneLight.toString());
        this.sceneLight = sceneLight;
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(Converter.convertImgPath(PATH, type));
    }

    @Override
    public Image getOpenedIcon(int type){
        return this.getIcon(type);
    }

    @Override
    protected Sheet createSheet() {
        Sheet result = Sheet.createDefault();
        Sheet.Set props = Sheet.createPropertiesSet();
        try {
//            Property color = new PropertySupport.Reflection(light, Color3f.class, null, "setColor");
//            color.setName("Color");
//            props.put(color);
//            if (light instanceof PointLight) {
//                PointLight point = (PointLight) light;
//                Property position = new PropertySupport.Reflection(point, Point3f.class, null, "setPosition");
//                position.setName("Position");
//                Property attenuation = new PropertySupport.Reflection(point, Point3f.class, null, "setAttenuation");
//                attenuation.setName("Attenuation");
//                props.put(position);
//                props.put(attenuation);
//            } else {
//                if (light instanceof DirectionalLight) {
//                    DirectionalLight directional = (DirectionalLight) light;
//                    Property direction = new PropertySupport.Reflection(directional, Vector3f.class, null, "setDirection");
//                    direction.setName("Direction");
//                    props.put(direction);
//                }
//            }

                Property color = new PropertySupport.Reflection(sceneLight, Color.class, "color");
                color.setName("Color");
                props.put(color);
                if (sceneLight.getLightType() == LightType.point) {
                    PropertySupport.Reflection position = new PropertySupport.Reflection(sceneLight, Point3f.class, "position");
                    position.setName("Position");
                    position.setPropertyEditorClass(Tuple3dEditor.class);
                    PropertySupport.Reflection attenuation = new PropertySupport.Reflection(sceneLight, Point3f.class, "attenuation");
                    attenuation.setName("Attenuation");
                    attenuation.setPropertyEditorClass(Tuple3dEditor.class);
                    props.put(position);
                    props.put(attenuation);
                } else {
                    if (sceneLight.getLightType() == LightType.directional) {
                        PropertySupport.Reflection direction = new PropertySupport.Reflection(sceneLight, Vector3f.class, "direction");
                        direction.setName("Direction");
                        direction.setPropertyEditorClass(Tuple3dEditor.class);
                        props.put(direction);
                    }
                }
        } catch (NoSuchMethodException ex) {
            System.err.println(ex.getMessage());
        }
        result.put(props);
        return result;
    }
}
