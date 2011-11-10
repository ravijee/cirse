/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.models.nodes.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.models.elements.Joint;
import org.itver.arm.models.elements.Piece;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 * Crea los Node hijos de un ArmNode. 
 * @author pablo
 */
public final class ArmChildren extends Children.Keys {

    public ArmChildren(Arm arm) {
        this.setKeys(createKeys(arm));
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

    private Collection createKeys(Arm arm) {
        ArrayList result = new ArrayList();
        result.addAll(Arrays.asList(arm.getNext()));
        return result;
    }
}
