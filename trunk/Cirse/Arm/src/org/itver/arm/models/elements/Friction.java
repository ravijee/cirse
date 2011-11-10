/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.models.elements;

import com.sun.j3d.utils.geometry.Cone;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * TODO: componer esto cuando sea necesario
 * @author pablo
 */
public class Friction extends BranchGroup{
    private final Point3d  position;
    private final Vector3d orientation;
    private       Piece    piece;
    private       double   strength;
    private       boolean  visible;
    
    private       TransformGroup tg;
    private       Transform3D    t3d;

    private Friction(){
        super();
        this.setCapability(ALLOW_DETACH);

        this.position = new Point3d();
        this.orientation = new Vector3d();

        Cone cone = new Cone(0.5f, 1);

        this.t3d = new Transform3D();
        this.tg = new TransformGroup(t3d);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        tg.addChild(cone);
        this.addChild(tg);

    }

    private Friction(Point3d position){
        this();
        this.setPosition(position);

    }

    public Friction(Point3d position, Piece parent){
        this(position);
        this.piece = parent;
    }

    /**
     * @return the position
     */
    public Point3d getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public final void setPosition(Point3d position) {
        this.position.set(position);
        t3d.setTranslation(new Vector3d(position));
        tg.setTransform(t3d);
    }

    /**
     * @return the strength
     */
    public double getStrength() {
        return strength;
    }

    /**
     * @param strength the strength to set
     */
    public void setStrength(double strength) {
        this.strength = strength;
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
        if(visible)
            this.piece.getMainTg().addChild(this);
        else
            this.detach();
    }

    /**
     * @return the orientation
     */
    public Vector3d getOrientation() {
        return orientation;
    }

    /**
     * @param orientation the orientation to set
     */
    public final void setOrientation(Vector3d orientation) {
        this.orientation.set(orientation);
    }

    /**
     * @return the piece
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * @param piece the piece to set
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

}
