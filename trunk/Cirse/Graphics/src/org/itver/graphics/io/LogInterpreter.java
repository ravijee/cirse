/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.graphics.io;

import com.sun.j3d.loaders.Scene;
import java.beans.PropertyChangeEvent;
import javax.vecmath.Point3d;
import org.itver.arm.models.elements.AngleType;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.models.elements.Axis;
import org.itver.arm.models.elements.Piece;
import org.itver.common.util.Converter;
import org.itver.common.xml.Interpreter;
import org.itver.graphics.model.MainScene;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author pablo
 */
public class LogInterpreter extends Interpreter{
    private enum LogDom{
        rot,
        mov
    }

    private void readNode(Node node) {
        LogDom dom;
        NamedNodeMap attrs = node.getAttributes();
        try{
            dom = LogDom.valueOf(node.getNodeName());
        }catch(java.lang.IllegalArgumentException ex){
            return;
        }
            Arm arm = (Arm) MainScene.getInstance().getComponentById(
                            Integer.parseInt(attrs.getNamedItem("arm").
                            getTextContent())).getTransformGroup().getChild(0);
            Piece piece = arm.getPiece(Integer.parseInt(
                        attrs.getNamedItem("piece").getTextContent()));
            double value = Double.parseDouble(attrs.getNamedItem("value").
                        getTextContent());
            Axis axis = Axis.valueOf(attrs.getNamedItem("axis").
                        getTextContent());
        switch(dom){
            case rot:
                piece.setAngle(AngleType.joint, axis, value);
                break;
            case mov:
                piece.setPosition(axis, value);

        }
    }


    @Override
    public Scene getScene() {
        return null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        readNode((Node) evt.getNewValue());
    }

}
