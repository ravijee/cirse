/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.models.validators;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.itver.arm.models.elements.AngleType;
import org.itver.arm.models.elements.Axis;
import org.itver.arm.models.elements.Piece;

/**
 *
 * @author pablo
 */
public class AngleValidator implements PropertyChangeListener{
    private Piece piece;
    
    public void setPiece(Piece piece){
        this.piece = piece;
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        setPiece((Piece) evt.getSource());
        double mins[] = piece.getAngles(AngleType.min);
        double maxs[] = piece.getAngles(AngleType.max);
        double jnts[] = piece.getAngles(AngleType.joint);
        Axis axis[] = Axis.values();
        for (int i = 0; i < axis.length; i++){
            if(mins[i] > maxs[i])
                return;
            if(mins[i] > jnts[i])
                piece.setAngle(AngleType.joint, axis[i], mins[i]);
            else if(jnts[i] > maxs[i])
                piece.setAngle(AngleType.joint, axis[i], maxs[i]);
        }
    }

}
