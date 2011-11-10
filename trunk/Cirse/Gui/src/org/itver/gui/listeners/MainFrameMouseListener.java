/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.gui.listeners;

import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import org.itver.graphics.model.MainScene;
import org.itver.gui.visual.NavigatorTopComponent;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author pablo
 */
public class MainFrameMouseListener implements MouseListener{

    private PickCanvas picker;
    private Node nodeSelected;

    private MainFrameMouseListener(){
    }

    public MainFrameMouseListener(BranchGroup bg, Canvas3D canvas){
        this();
        this.picker = new PickCanvas(canvas, bg);
        this.picker.setTolerance(0.0f);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object objectSelected = null;

        if(e.getClickCount() <= 2)
            return;
        PickResult result = null;
        picker.setShapeLocation(e);
        result = picker.pickClosest();
        if (result != null) {
            objectSelected = result.getObject().getUserData();
            findNode(objectSelected);
            if(nodeSelected != null)
            try {
                ExplorerManager mgr = NavigatorTopComponent.findInstance().getExplorerManager();
                mgr.setSelectedNodes(new Node[]{nodeSelected});
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        MainScene.getInstance().setSelectedObject(objectSelected);
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

    private void findNode(Object userData) {
        Node node = NavigatorTopComponent.findInstance().getExplorerManager().getRootContext();
        nodeSelected = null;
        if(!node.isLeaf())
            findNodeIn(userData, node);
    }

    private void findNodeIn(Object userData, Node node) {
        if(userData == null)
            return;
        Children children = node.getChildren();
        Node find = children.findChild(userData.toString());
        if(find != null)
            nodeSelected = find;
        for(Node child: children.getNodes())
            if(!child.isLeaf())
                findNodeIn(userData, child);
    }
    

}
