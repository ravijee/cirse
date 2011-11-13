/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.graphics.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * NO usar.
 * @author Karo
 * @deprecated 
 */
public class Camera {
    private PropertyChangeSupport x;
    private Point3d eye;
    private Point3d center;
    private Vector3d up;

    public Camera(){
        x = new PropertyChangeSupport(this);
        eye = new Point3d(0,5,10);
        center = new Point3d(0,0,0);
        up = new Vector3d(0, 1, 0);
    }

    public void addListener(PropertyChangeListener listener){
        x.addPropertyChangeListener(listener);
    }

    public Point3d getCenter() {
        return new Point3d(center);
    }

    public void setCenter(Point3d center) {
        Point3d aux = new Point3d(this.center);
        this.center.set(center);
        x.firePropertyChange("center", aux, center);

    }

    public Point3d getEye() {
        return new Point3d(eye);
    }

    public void setEye(Point3d eye) {
        Point3d aux = new Point3d(this.eye);
        this.eye.set(eye);
        x.firePropertyChange("eye", aux, eye);
    }

    public Vector3d getUp() {
        return new Vector3d(up);
    }

    public void setUp(Vector3d up) {
        Vector3d aux = new Vector3d(this.up);
        this.up.set(up);
        x.firePropertyChange("up", aux, up);
    }
    
}
