/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.graphics.model;

import com.sun.j3d.utils.image.TextureLoader;
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import org.itver.graphics.util.XYZ;

/**
 * Como su nombre lo indica, esta clase actúa como contenedora de todos los
 * objetos necesarios para construir la <i>escena principal</i> de la simulación.
 * Entre los principales componentes de la escena se encuentran los
 * {@link EnvironmentLimits limites}, los {@link MainSceneComponent componentes}
 * y las {@link SceneLight luces}.
 * <br>
 * Mientras que las luces y los componentes son almacenados en 
 * estructuras de datos genéricas, los límites únicamente existen una vez 
 * por cada entorno.
 * </br>
 * @author Karo
 */
public class MainScene extends BranchGroup {
    private static MainScene              mainScene;
    private ArrayList<MainSceneComponent> components;
    private ArrayList<SceneLight>         lights;
    private EnvironmentLimits             eLimits;
    private Shape3D                       axes;
    private BoundingSphere                sceneBounding;
    private final Background              background;
    private PropertyChangeSupport         pcs;

    /*Pablo: Objeto seleccionado por el oyente del mouse. Cuando se selccione
     *un elemento diferente en la interfaz, este objeto cambiará.
     */
    private Object                        selectedObject;
    //======================================================================
    private Color                         backgroundColor;
    private File                          backgroundFile;
    private Boolean                       backgroundFlag;
    /**
     * Construye una nueva instancia de la escena principal.
     */
    public MainScene(/*BoundingSphere sceneBounding*/) {
//        this.sceneBounding = sceneBounding;
        background = new Background();
        background.setCapability(Background.ALLOW_IMAGE_WRITE);
        background.setCapability(Background.ALLOW_COLOR_WRITE);
        background.setApplicationBounds(new BoundingSphere(new Point3d(),
                          EnvironmentLimits.getInstance().getHighest()));
        this.addChild(background);
        axes = new Shape3D(new XYZ());
        components = new ArrayList<MainSceneComponent>();
        lights = new ArrayList<SceneLight>();
        eLimits = EnvironmentLimits.getInstance();
//        this.setBounds(sceneBounding);
        sceneBounding = eLimits.getEnvironmentBounds();

        addAxes();
        this.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        this.setCapability(Group.ALLOW_CHILDREN_WRITE);
        this.setCapability(Group.ALLOW_BOUNDS_WRITE);
        pcs = new PropertyChangeSupport(this);
    }

    /**
     * Crea los ejes de coordenadas y los agrega.
     */
    private void addAxes() {
        Appearance ap = new Appearance();
        LineAttributes la = new LineAttributes();
        la.setLineAntialiasingEnable(true);
        ap.setLineAttributes(la);
        axes.setAppearance(ap);
        this.addChild(axes);
    }

    /**
     * Agrega un componente a la escena principal.
     * @param c Componente que se va a agregar a la escena principal.
     */
    public void addComponent(MainSceneComponent c) {
        components.add(c);
        this.addChild(c);
        pcs.firePropertyChange("Components", null, c);
    }

    /**
     * Agrega una luz a la escena principal.
     * @param sceneLight Luz que será agregada.
     */
    public void addLight(SceneLight sceneLight){
//        sceneLight.getLight().setInfluencingBounds(new BoundingSphere(new Point3d(),
//                EnvironmentLimits.getInstance().getHighest()));
//        sceneLight.getLight().setCapability(Light.ALLOW_INFLUENCING_BOUNDS_WRITE);
        lights.add(sceneLight);
        addChild(sceneLight);
        pcs.firePropertyChange("Lights", null, sceneLight);
    }

    /**
     * Agrega un fondo a la escena principal.
     */
    private void addBackground(){
        background.setApplicationBounds(new BoundingSphere(new Point3d(),
                EnvironmentLimits.getInstance().getHighest()));
        this.addChild(background);
        pcs.firePropertyChange("Background", null, background);
    }

    /**
     * Establece un fondo en la escena principal.
     * @param bg Fondo a establecer.
     */
//    public void setBackground(Background bg){
//        this.background = bg;
//        addBackground();
//    }

    public void setBackgroundColor(Color3f color3f){
        this.setBackgroundColor(color3f.get());
    }

    public void setBackgroundColor(Color color){
        Color old = getBackgroundColor();
        this.backgroundColor = color;
        Color3f color3f = new Color3f(this.backgroundColor);
        background.setColor(color3f);
        pcs.firePropertyChange("color", old, color);
    }

    public void setBackgroundFile(File file){
        this.backgroundFile = file;
        TextureLoader myLoader = new TextureLoader(backgroundFile.getPath(), null);
        ImageComponent2D myImage = myLoader.getImage();
        background.setImage(myImage);
    }

    public File getBackgroundFile(){
        return backgroundFile;
    }

    /**
     * Devuelve el fondo de la escena principal.
     * @return El fondo actual de la escena principal.
     */
    public Background getBackground(){
        return background;
    }

    public Color getBackgroundColor(){
        return backgroundColor;
    }

    public void setBackgroundFlag(boolean flag){
        boolean old = hasBackgroundImage();
        this.backgroundFlag = flag;
        pcs.firePropertyChange("BackgroundImage", old, flag);
        if(!backgroundFlag)
            setBackgroundColor(backgroundColor);

    }

    public boolean hasBackgroundImage(){
        return backgroundFlag;

    }

    /**
     * Regresa las luces de la escena principal.
     * @return El arreglo de luces.
     */
    public ArrayList<SceneLight> getLightArray(){
        return lights;
    }

    /**
     * Agrega los {@link EnvironmentLimits límites} a la escena principal.
     */
    public void addEnvironmentLimits() {
        this.addChild(eLimits);
        pcs.firePropertyChange("EnvironmentLimits", null, eLimits);
    }

    /**
     * Regresa los límites de la escena principal.
     * @return Los límites del entorno.
     */
    public EnvironmentLimits getEnvironmentLimits(){
        return eLimits;
    }

    /**
     * Regresa los componentes de la escena principal.
     * @return El arreglo de componentes.
     */
    public ArrayList<MainSceneComponent> getComponentArray(){
        return components;
    }

    /**
     * Actualiza los límites del entorno sobre los que actúan las luces.
     * @param newBounds Nueva delimitación.
     */
    public void updateLightBounds(BoundingSphere newBounds){
        for (int i = 0; i < lights.size(); i++) {
            lights.get(i).getLight().setInfluencingBounds(newBounds);
        }
    }

    /**
     * Regresa un de acuerdo con el ID especificado.
     * @param id Identificador del componente que se desea obtener.
     * @return Componente específico.
     */
    public MainSceneComponent getComponent(int id){
        MainSceneComponent msc = null;
        for(int i = 0; i < components.size(); i++){
            if(id == components.get(i).getId() ){
                msc = components.get(i);
                break;
            }
        }
        return msc;
    }

    /**
    * Agrega un oyente para mantener en comunicación sobre los cambios
    * realizados en las propiedades.
    * @param listener Escucha los cambios realizados en las propiedades.
    */
    public void addPropertyChangeListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Elimina un oyente tipo PropertyChangeListener de este objeto.
     * @param listener El objeto oyente a eliminar.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener){
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Método para crear una instancia única de esta clase.
     */
     private synchronized static void createInstance() {
        if (mainScene == null) {
            mainScene = new MainScene();
        }
    }

    /**
     * Regresa la instancia única de la clase.
     * @return Instancia única de MainScene.
     */
    public static MainScene getInstance() {
        if (mainScene == null) {
            createInstance();
        }
        return mainScene;
    }
    /**
     * Llamada por el oyente de mouse cuando se seleccione un nuevo objeto del
     * mouse. <b>No llamar desde otro lado</b>
     * @param objectSelected objeto seleccionado por el mainScene
     */
    public void setSelectedObject(Object objectSelected) {
        this.selectedObject = objectSelected;
        this.pcs.firePropertyChange("selected", objectSelected, null);
    }

    /**
     * Llamada por cualquier clase para obtener el objeto seleccionado por el 
     * mouse. Para obtener el objeto seleccionado en escena. {@code null} si no 
     * se ha seleccionado nada.
     * @return 
     */
    public Object getSelectedObject(){
        return this.selectedObject;
    }
    
    /**
     * Limpia la escena y sus componentes.
     */
    public void clearScene(){
        for (int i = 0; i < components.size(); i++)
            components.get(i).detach();
        for (int i = 0; i < lights.size(); i++)
            lights.get(i).detach();
        components.clear();
        lights.clear();
        eLimits.updateLimits(0, 0, 0, 0);
        background.setImage(null);
        background.setColor(new Color3f(0, 0, 0));
    }
}
