package org.itver.graphics.model;


import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.io.File;
import java.io.FileNotFoundException;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import org.itver.graphics.controller.CameraKeyBehavior;
import org.itver.graphics.guitools.Control;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import org.itver.common.xml.SceneFromXML;
import org.itver.graphics.controller.PickComponentBehavior;
import org.itver.graphics.io.EnvironmentParser;
import org.itver.graphics.io.EnvironmentSaver;
import org.itver.graphics.util.LightType;
import org.openide.util.Exceptions;
import org.xml.sax.SAXException;

/**
 * Esta clase reúne los objetos necesarios, junto con el
 * {@link org.itver.graphics.model.MainScene MainScene} para construir el
 * escenario gráfico del entorno de la simulación. Sus atributos constan principalmente de
 * los {@code comportamientos} que actúan sobre los objetos que están dentro de
 * la {@code escena principal} y los asocia a un {@code SimpleUniverse}.
 *
 * @author Karo
 */
public class Universe{

    private SimpleUniverse su;
    private BoundingSphere suBounding;
    private MainScene scene;
    private Canvas3D canvas;
    private Control ctrl;
    private static Universe universe = new Universe();
    private PickComponentBehavior pickComponent;
    private CameraKeyBehavior cameraBehavior;

    /**
     * Crea una nueva instancia de Universe, con lo cual se contruye un entorno
     * de simulación completo.
     */
    private Universe(){
        canvas = new Canvas3D(setGraphicsConfiguration());
        su = new SimpleUniverse(canvas);
        createScene();
        addBehaviors();
        scene.setBackgroundColor(new Color3f(Color.WHITE));
        scene.addLight(new SceneLight(LightType.point));
        scene.compile();
        su.addBranchGraph(scene);
        rotateView(canvas);
  }

    /**
     * Carga las propiedades del entorno de simulación desde un archivo de
     * configuración guardado en disco.
     * @param file Ruta del archivo de configuración.
     */    
    public void loadFile(File file){
        try {
            EnvironmentParser parser = new EnvironmentParser(MainScene.getInstance());
            SceneFromXML reader = new SceneFromXML(parser);
            reader.load(file.getAbsolutePath());
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IncorrectFormatException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ParsingErrorException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
     /**
     * Salva la configuración  las propiedades del entorno de simulación desde 
     * un archivo de configuración XML. 
     * @param file Ruta del archivo de configuración.
     */    
    public void saveFile(File file){
        try {
            EnvironmentSaver saver = new EnvironmentSaver(1.0f);
            saver.save(Universe.getInstance(), file);
        } catch (SAXException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Crea la escena principal.
     * @see org.itver.graphics.model.MainScene
     */
    private void createScene(){
        scene = MainScene.getInstance();
        scene.addEnvironmentLimits();
//        scene.addLights();
    }

    /**
     * Crea la parte gráfica del control de movimiento de la cámara.
     * @see org.itver.graphics.guitools.Control
     */
    private void createControl(){
        ctrl = new Control(cameraBehavior.getTransformGroupManipulator());
    }

    /**
     * Obtiene el grupo de transformación de la {@code ViewingPlatform} del entorno para
     * poder transformarla.
     * @return TransformGroup asociado a la ViewingPlatform.
     */
    public TransformGroup getVPTransform(){
        return su.getViewingPlatform().getViewPlatformTransform();
    }

    /**
     * Establece la configuración de gráficos para el universo.
     * @return
     */
    private GraphicsConfiguration setGraphicsConfiguration(){
        GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
        return gc;
    }

    private void rotateView(Canvas3D c) {
        OrbitBehavior orbita = new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
        orbita.setRotXFactor(0);
        orbita.setRotYFactor(0.2);
        orbita.setRotationCenter(new Point3d(0, 5, 0));
//        orbita.setTranslateEnable(false);
        orbita.setBounds(new BoundingSphere());
        orbita.setSchedulingBounds(new BoundingSphere(new Point3d(0, 0, 0), 1000));

        ViewingPlatform vp = su.getViewingPlatform();
        vp.setViewPlatformBehavior(orbita);
        
        Transform3D t3d = new Transform3D();
        t3d.lookAt(new Point3d(0, 10, 10), new Point3d(), new Vector3d(0, 1, 0));
        t3d.invert();
        vp.getViewPlatformTransform().setTransform(t3d);
        orbita.setEnable(true);
    }


    public Canvas3D getCanvas(){
        return canvas;
    }

    public Control getControl(){
        return ctrl;
    }

    public MainScene getScene(){
        return scene;
    }

    public EnvironmentLimits getEnvirionmentLimits(){
        return scene.getEnvironmentLimits();
    }

    public void addComponent(MainSceneComponent c){
        scene.addComponent(c);
    }


    public BoundingSphere getUniverseBounding(){
        return suBounding;
    }

    private void addBehaviors() {
        pickComponent = new PickComponentBehavior(canvas, scene, new BoundingSphere(new Point3d(), 500));
                //new BoundingSphere(new Point3d(),
                //EnvironmentLimits.getInstance().getHighest()));
        pickComponent.setCapability(PickComponentBehavior.ALLOW_BOUNDS_WRITE);
        pickComponent.setCapability(PickComponentBehavior.ALLOW_BOUNDS_READ);
        pickComponent.setSchedulingBounds(new BoundingSphere(new Point3d(), 500));
                /*new BoundingSphere(new Point3d(),
                EnvironmentLimits.getInstance().getHighest())*/
        scene.addChild(pickComponent);
        cameraBehavior = new CameraKeyBehavior(getVPTransform());
        cameraBehavior.setCapability(CameraKeyBehavior.ALLOW_BOUNDS_WRITE);
        cameraBehavior.setCapability(CameraKeyBehavior.ALLOW_BOUNDS_READ);
        cameraBehavior.setSchedulingBounds(new BoundingSphere(new Point3d(),
                EnvironmentLimits.getInstance().getHighest()));
        scene.addChild(cameraBehavior);
        createControl();
    }

    public void updateBounds(){
        BoundingSphere newBounds = new BoundingSphere(new Point3d(),
                EnvironmentLimits.getInstance().getHighest());
//        pickPiece.setSchedulingBounds(newBounds);
        pickComponent.setSchedulingBounds(newBounds);
        cameraBehavior.setSchedulingBounds(newBounds);
        scene.updateLightBounds(newBounds);
//        System.out.println("NUEVOS BOUNDS:::::::::::::::::::::::::::::::::::::::::::::::::"+newBounds);
    }

    //Revisar la utilidad de que el if esté presente en ambos métodos//////////
    //(create y get Instance)//////////////////////////////////////////////////
    private synchronized static void createInstance() {
        if (universe == null) {
            universe = new Universe();
        }
    }
    public static Universe getInstance() {
        if (universe == null) {
            createInstance();
        }
        return universe;
    }
    /////////////////////////////////////////////////////////////////////

}
