/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.models.nodes.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.itver.arm.models.elements.Joint;
import org.itver.arm.models.elements.Piece;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 * Crea los Node hijos de un Joint
 * @author pablo
 */
public final class JointChildren extends Children.Keys {

    public JointChildren(Joint joint) {
        this.setKeys(createKeys(joint));
    }

    @Override
    protected Node[] createNodes(Object key) {
        PieceNode result[] = new PieceNode[1];
        if(key instanceof Joint)
            result[0] = new JointNode((Joint) key);
        else
            result[0] = new PieceNode((Piece) key);

        return result;
    }

    private Collection createKeys(Joint joint) {
        ArrayList result = new ArrayList();
        result.addAll(Arrays.asList(joint.getNext()));
        return result;
    }
}
