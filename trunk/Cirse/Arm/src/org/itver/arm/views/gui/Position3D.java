/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.views.gui;

import com.eteks.sweethome3d.j3d.DAELoader;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import org.itver.arm.models.elements.Element;

/**
 *
 * @author pablo
 */
public class Position3D implements PropertyChangeListener{
    private Element     element;
    private BranchGroup model;
    private BranchGroup arrows;
    private TransformGroup tgs[];
    
    public Position3D(){
        tgs = new TransformGroup[3];
        arrows = new BranchGroup();
        setModel();
        for (int i = 0; i < tgs.length; i++){
            Transform3D t3d = new Transform3D();
            switch(i){
                case 0:
                    t3d.rotX(Math.toRadians(90));
                    break;
                case 1:
                    t3d.rotY(Math.toRadians(90));
                    break;
                case 2:
                    t3d.rotZ(Math.toRadians(90));
            }
            tgs[i] = new TransformGroup(t3d);
            tgs[i].addChild(model.cloneTree());
            arrows.addChild(tgs[i]);
        }        
        arrows.setCapability(BranchGroup.ALLOW_DETACH);
        arrows.setPickable(false);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(element != null)
            element.removeChild(element.numChildren() - 1);
        element = (Element)evt.getNewValue();
        if(element != null)
            element.addChild(arrows);
    }

    private void setModel(){
        try {
            DAELoader loader = new DAELoader();
            model = loader.load("designs/Flecha.dae").getSceneGroup();
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IncorrectFormatException ex) {
            System.err.println(ex.getMessage());
        } catch (ParsingErrorException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
