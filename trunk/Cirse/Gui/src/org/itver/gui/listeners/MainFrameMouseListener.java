/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.gui.listeners;

import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import org.itver.arm.models.elements.Piece;
import org.itver.gui.visual.NavigatorTopComponent;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author pablo
 */
public class MainFrameMouseListener implements MouseListener{

    private PickCanvas picker;
    private Piece   last;
    private boolean active;

    private MainFrameMouseListener(){
    }

    public MainFrameMouseListener(BranchGroup bg, Canvas3D canvas){
        this();
        this.picker = new PickCanvas(canvas, bg);
        this.picker.setTolerance(0.0f);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() != 2)
            return;
        PickResult result = null;
        picker.setShapeLocation(e);
        result = picker.pickClosest();
        if (result != null) {
            Node nodeSelected = findNode(result.getObject().getUserData());
            if(nodeSelected != null)
            try {
                NavigatorTopComponent.findInstance().getExplorerManager().setSelectedNodes(new Node[]{nodeSelected});
            } catch (PropertyVetoException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private Node findNode(Object userData) {
        Node node = NavigatorTopComponent.findInstance().getExplorerManager().getRootContext();
        if(!node.isLeaf())
            return findNodeIn(userData, node);
        return null;
    }

    private Node findNodeIn(Object userData, Node node) {
        Children children = node.getChildren();
        Node find = children.findChild(userData.toString());
        System.out.println("find: " + find);
        if(find != null)
            return find;
        for(Node child: children.getNodes())
            if(!child.isLeaf())
                return findNodeIn(userData, child);
        return null;

    }
    

}
