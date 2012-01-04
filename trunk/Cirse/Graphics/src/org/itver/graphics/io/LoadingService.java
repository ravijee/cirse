/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.graphics.io;

import java.io.File;
import org.itver.graphics.model.MainSceneComponent;
import org.itver.graphics.util.ComponentType;

/**
 *
 * @author Karolap02
 */
public interface LoadingService {
    
    public boolean isLoadable(ComponentType type);
    
    public MainSceneComponent getArm(File src);
    
}
