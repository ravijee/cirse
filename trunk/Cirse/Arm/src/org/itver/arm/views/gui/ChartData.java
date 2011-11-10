/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.views.gui;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import org.itver.arm.models.elements.Joint;

/**
 *
 * @author pablo
 */
public class ChartData{
    private final ArrayList<Double> values;
    private final GeneralPath shape;
    private final Joint joint;
    private final Color color = new Color((float)Math.random(), 
                                (float)Math.random(), 
                                (float)Math.random(),
                                0.8f);        
    
    public ChartData(Joint joint){
        this.values = new ArrayList<Double>();
        shape = new GeneralPath();
        this.joint = joint;
    }
    
    public void addValue(double value){
        this.values.add(value);
    }
    
    public void clear(){
        this.values.clear();
    }
    
    public Joint getJoint(){
        return this.joint;
    }
    
    public double getMax(){
        double result = 0;
        for(double val : this.values)
            if(val > result)
                result = val;
        return result;
    }
    
    public double getTime(){
        return this.values.size();
    }
    
    public Shape getShape(int width, int height, double max){
        shape.reset();
        shape.moveTo(0, height);
        if(this.values.size() > 1){
            double xSpace = width / (this.values.size() - 1);
            int x = 0;
            int y = 0;
            for(int i = 0; i < this.values.size(); i++){
                x = (int) Math.round(i * xSpace);
                y = (int) Math.round(height - this.values.get(i) * height / max);
                shape.lineTo(x, y);
            }
            shape.lineTo(width, y);
            shape.lineTo(width, height);
            shape.closePath();
        }
        return shape;
    }
    
    public Color getColor(){
        return this.color;
    }
}
