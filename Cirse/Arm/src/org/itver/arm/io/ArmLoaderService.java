/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.arm.io;

import java.io.File;
import javax.media.j3d.BranchGroup;
import org.itver.arm.controls.controllers.ArmController;
import org.itver.common.xml.XmlLoader;
import org.itver.graphics.io.LoadingService;
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
    public BranchGroup getArm(File src) {
        BranchGroup result = ArmController.singleton().readFile(src);
        return result;
    }
    
}
