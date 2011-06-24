/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.graphics.model;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Light;
import javax.media.j3d.PointLight;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.itver.graphics.util.LightType;

/**
 * Esta clase representa los diferentes tipos de luces que pueden existir en la
 * {@code MainScene}: luz ambiental, luz direccional y luz sobre un punto.
 *
 * Por conveniencia, esta clase mantiene una relación de composición con la clase
 * {@code Light} de Java, y no de herencia.
 * @see javax.media.j3d.Light
 * @see javax.media.j3d.AmbientLight
 * @see javax.media.j3d.DirectionalLight
 * @see javax.media.j3d.PointLight
 * @author Karo
 */
public class SceneLight{
    private PropertyChangeSupport pcs;
    private BranchGroup branchLight;
    private Color color;
    private LightType type;
    private final Light light;
    private Vector3f direction;
    private Point3f position;
    private Point3f attenuation;
    private int index;

    /**
     * Crea una luz de tipo específico.
     * @param type Tipo de la luz a crear.
     */
    public SceneLight(LightType type) {
        this.type = type;
        pcs = new PropertyChangeSupport(this);
        branchLight = new BranchGroup();
        color = new Color(0);
        switch (type) {
            case ambient:
                light = new AmbientLight();
                break;
            case directional:
                light = new DirectionalLight();
                break;
            case point:
                light = new PointLight();
                break;
            default:
                light = null;
        }
        branchLight.addChild(light);
        light.setCapability(Light.ALLOW_COLOR_WRITE);
        light.setCapability(Light.ALLOW_INFLUENCING_BOUNDS_WRITE);
    }

    /**
     * Regresa la luz de la clase.
     * @return Luz creada.
     */
    public Light getLight() {
        return light;
    }

    /**
     * Regresa el tipo de la luz.
     * @return Tipo de luz.
     */
    public LightType getLightType() {
        return type;
    }

    public BranchGroup getBranchLight(){
        return branchLight;
    }

    /**
     * Cambia el color de la luz.
     * @param color3f Color nuevo para la luz.
     */
    public void setColor(Color3f color3f){
        this.setColor(color3f.get());
    }

    /**
     * Cambia el color de la luz.
     * @param color Color nuevo para la luz.
     */
    public void setColor(Color color) {
        Color old = getColor();
        this.color = color;
        Color3f color3f = new Color3f(this.color);
        light.setColor(color3f);
        pcs.firePropertyChange("color", old, color);
    }

    /**
     * Regresa el color de la luz.
     * @return Color de la luz.
     */
    public Color getColor() {
        return color;
    }

    public Point3f getAttenuation() {
        return attenuation;
    }

    public void setAttenuation(Point3f attenuation) {
        this.attenuation = attenuation;
        if(light instanceof PointLight){
            PointLight pl = (PointLight)light;
            pl.setAttenuation(attenuation);
        }
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
        if(light instanceof DirectionalLight){
            DirectionalLight dl = (DirectionalLight)light;
            dl.setDirection(direction);
        }
    }

    public Point3f getPosition() {
        return position;
    }

    public void setPosition(Point3f position) {
        this.position = position;
        if(light instanceof PointLight){
            PointLight pl = (PointLight)light;
            pl.setPosition(position);
        }
    }

    public void setEnable(boolean state) {
        light.setEnable(state);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl){
        pcs.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl){
        pcs.removePropertyChangeListener(pcl);
    }


}
