/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.common.util;

import javax.media.j3d.Appearance;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;

/**
 *
 * @author pablo
 */
public class ModifyTree {

    public static void changeAppearance(Node node, Appearance app) {
        try {
            if (node instanceof Shape3D) {
                Shape3D shape = (Shape3D) node;
                shape.setAppearance(app);
            } else if (node instanceof Group) {
                Group group = (Group) node;
                for (int i = 0; i < group.numChildren(); i++) {
                    changeAppearance(group.getChild(i), app);
                }
            }
        } catch (javax.media.j3d.CapabilityNotSetException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static void setUserData(Node node, Object data){
        node.setUserData(data);
        if(node instanceof Group){
            Group g = (Group)node;
            for(int i = 0; i < g.numChildren(); i++)
                setUserData(g.getChild(i), data);
        }
    }
}
