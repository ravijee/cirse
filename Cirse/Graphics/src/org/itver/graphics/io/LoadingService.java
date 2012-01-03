/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.graphics.io;

import java.io.File;
import javax.media.j3d.BranchGroup;
import org.itver.graphics.util.ComponentType;

/**
 *
 * @author Karolap02
 */
public interface LoadingService {
    
    public boolean isLoadable(ComponentType type);
    
    public BranchGroup getArm(File src);
    
}
