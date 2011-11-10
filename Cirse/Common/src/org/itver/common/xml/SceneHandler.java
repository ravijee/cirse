/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.common.xml;

import com.sun.j3d.loaders.Loader;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.SceneBase;
import javax.media.j3d.BranchGroup;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Clase de la que se be heredar para crear un nuevo
 * {@link BranchGroup BranchGroup} a partir de un archivo XML. Su uso se
 * describe en {@link SceneFromXML SceneFromXML}
 * @author pablo
 * @see SceneFromXML SceneFromXML
 * @see DefaultHandler DefaultHandler
 */
public class SceneHandler extends DefaultHandler{
    /**
     * Scene necesaria para los {@link Loader Loader} en Java 3D
     */
    protected SceneBase scene;
    /**
     * Ruta de la carpeta contenedora del archivo a interpretar.
     */
    protected String parentPath;

    public SceneHandler() {
        super();
        this.scene = new SceneBase();
    }
    
    public Scene getScene(){
        return scene;
    }

    public void setFileName(String name){
        this.parentPath = name;
    }

}
