/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.io;

import java.io.File;
import org.itver.arm.controls.controllers.ArmController;
import org.itver.arm.models.elements.Arm;
import org.itver.graphics.io.LoadingService;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.util.ComponentType;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author pablo
 */
@ServiceProvider(service=LoadingService.class)
public class ArmLoaderService implements LoadingService{

    @Override
    public boolean isLoadable(ComponentType type) {
        return type == ComponentType.arm;
    }

    @Override
    public MainSceneComponent getArm(File src) {
        Arm arm = ArmController.singleton().readFile(src);
        MainSceneComponent result = null;
        if(arm != null){
            result = new MainSceneComponent(ComponentType.arm, src);
            arm.setContainer(result);
            result.setContent(arm);
        }        
        return result;
    }
    
}
