/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.threads;

import java.util.ArrayList;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.models.elements.Joint;
import org.itver.arm.models.elements.Piece;
import org.itver.arm.models.elements.ValueType;
import org.itver.arm.views.gui.ChartCanvas;
import org.itver.arm.views.gui.ChartData;
import org.openide.util.Exceptions;

/**
 *
 * @author pablo
 */
public class ChartDataThread extends Thread{
    private final Arm arm;
    private final ArrayList<ChartData> datas;
    private final ChartCanvas canvas;
    private static final long TIME = 1000;
    
    public ChartDataThread(Arm arm){
        this.arm = arm;
        this.canvas = ChartCanvas.singleton();
        datas = new ArrayList<ChartData>();
    }
    
    public Arm getArm(){
        return this.arm;
    }
    
    @Override
    public void run(){
        this.createDatas();
        this.addDatasToCanvas();
        while(LogThread.isRunning()){
            try {
                Thread.sleep(TIME);
                updateDatas();
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
            
        }
        
    }
    
    private void addDatasToCanvas(){
        for(ChartData data : this.datas)
            canvas.addData(data);
    }
    
    private void updateDatas(){
        for(ChartData data : this.datas)
            data.addValue(data.getJoint().getJointValue(ValueType.current));
    }
    
    private void createDatas(){
        this.datas.clear();
        for(Piece p : arm.getNext())
            if(p instanceof Joint)
                createData((Joint)p);
    }
    
    private void createData(Joint joint){
        ChartData data = new ChartData(joint);
        data.addValue(joint.getJointValue(ValueType.current));
        this.datas.add(data);
        for(Piece p: joint.getNext())
            if(p instanceof Joint)
                createData((Joint)p);
    }
}
