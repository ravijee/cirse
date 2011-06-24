
package org.itver.graphics.controller;

import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import org.itver.arm.models.elements.Piece;
import org.itver.graphics.model.EnvironmentLimits;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.model.Universe;

/**
 * Clase de {@code comportamiento} con la que se implementa la selección de
 * objetos mediante el mouse.
 *
 * @author Karo
 */
public class PickComponentBehavior extends PickMouseBehavior {

    /**
     * Crea una nueva instancia del objeto.
     * @param canvas Lienzo (Canvas3D) sobre el que se encuentran renderizados
     * los objetos a seleccionar.
     * @param bg Grupo
     * @param bounds
     */
    public PickComponentBehavior(Canvas3D canvas, BranchGroup bg, Bounds bounds){
        super(canvas, bg, bounds);
        this.pickCanvas.setTolerance(0);
    }

    @Override
    public void updateScene(int i, int i1) {
        PickResult result = null;
        pickCanvas.setShapeLocation(i, i1);
        result = pickCanvas.pickClosest();
        MainSceneComponent lastPicked = null;
        for(MainSceneComponent msc: Universe.getInstance().getScene().getComponentArray()){
            msc.setSelected(false);
        }
        EnvironmentLimits.getInstance().setSelected(false);
        if(this.mevent.getClickCount() > 1)
            return;
        if(result != null){
            if(result.getObject().getUserData() instanceof MainSceneComponent){
                lastPicked = (MainSceneComponent) result.getObject().getUserData();
                lastPicked.setSelected(true);
            }
            if(result.getObject().getUserData() instanceof Piece){
                Piece piece = (Piece) result.getObject().getUserData();
                lastPicked = (MainSceneComponent) piece.getArm().getContainer();
                lastPicked.setSelected(true);
            }
            if(result.getObject().getUserData() instanceof EnvironmentLimits){
                EnvironmentLimits limitSelection = (EnvironmentLimits) result.getObject().getUserData();
                limitSelection.setSelected(true);
//                EnvironmentLimits.getInstance().setSelected(true);
            }
        }
    }
}
