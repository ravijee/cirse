/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.exceptions;

import org.itver.arm.models.elements.Axis;



/**
 *
 * @author pablo
 */
public class IncorrectAngleException extends Exception{
    private String  message;
    private Axis    axis;
    public IncorrectAngleException(String message){
        this.message = message;
    }

    public IncorrectAngleException(String message, Axis axis){
        this.message = message;
        this.axis = axis;
    }

    @Override
    public String getMessage(){
        return message;
    }

    public Axis getAxis(){
        return this.axis;
    }

}
