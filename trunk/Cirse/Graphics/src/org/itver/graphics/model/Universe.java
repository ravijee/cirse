package org.itver.graphics.model;


import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.awt.GraphicsConfiguration;
import java.io.FileNotFoundException;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Canvas3D;
import javax.vecmath.Point3d;
import org.itver.graphics.controller.CameraKeyBehavior;
import org.itver.graphics.guitools.Control;
import javax.media.j3d.TransformGroup;
import org.itver.common.xml.XmlLoader;
import org.itver.graphics.controller.PickComponentBehavior;
import org.itver.graphics.io.EnvironmentInterpreter;

/**
 * Esta clase reúne los objetos necesarios, junto con el
 * {@link org.itver.graphics.model.MainScene MainScene} para construir el
 * escenario gráfico del entorno de la simulación. Sus atributos constan principalmente de
 * los {@code comportamientos} que actúan sobre los objetos que están dentro de
 * la {@code escena principal} y los asocia a un {@code SimpleUniverse}.
 *
 * @see <a href="http://download.oracle.com/docs/cd/E17802_01/j2se/javase/technologies/desktop/java3d/forDevelopers/J3D_1_3_API/j3dapi/javax/media/j3d/Behavior.html" target="_blank">Behavior</a>
 * @see <a href="http://download.java.net/media/java3d/javadoc/1.4.0/com/sun/j3d/utils/universe/SimpleUniverse.html" target="_blank">SimpleUniverse</a>
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
        loadFile("envi1.env");
        addBehaviors();
        scene.compile();
        su.addBranchGraph(scene);
        rotateView(canvas);
//        canvas.addMouseListener(new PickPiece(canvas, scene));
  }

    /**
     * Carga las propiedades del entorno de simulación desde un archivo de
     * configuración guardado en disco.
     * @param file Ruta del archivo de configuración.
     */
    public void loadFile(String file){
        try {
            XmlLoader loader = new XmlLoader();
            EnvironmentInterpreter interpreter = new EnvironmentInterpreter(scene);
            loader.setInterpreter(interpreter);
            scene = (MainScene) loader.load(file).getSceneGroup();
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IncorrectFormatException ex) {
            System.err.println(ex.getMessage());
        } catch (ParsingErrorException ex) {
            System.err.println(ex.getMessage());
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
        orbita.setRotYFactor(0.5);
        orbita.setRotationCenter(new Point3d(0, 5, 0));
//        orbita.setTranslateEnable(false);
        orbita.setBounds(new BoundingSphere());
        orbita.setSchedulingBounds(new BoundingSphere(new Point3d(0, 0, 0), 1000));

        ViewingPlatform vp = su.getViewingPlatform();
        vp.setViewPlatformBehavior(orbita);
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
        pickComponent = new PickComponentBehavior(canvas, scene, new BoundingSphere(new Point3d(),
                EnvironmentLimits.getInstance().getHighest()));
        pickComponent.setCapability(PickComponentBehavior.ALLOW_BOUNDS_WRITE);
        pickComponent.setCapability(PickComponentBehavior.ALLOW_BOUNDS_READ);
        pickComponent.setSchedulingBounds(new BoundingSphere(new Point3d(),
                EnvironmentLimits.getInstance().getHighest()));
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
        System.out.println("NUEVOS BOUNDS:::::::::::::::::::::::::::::::::::::::::::::::::"+newBounds);
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
