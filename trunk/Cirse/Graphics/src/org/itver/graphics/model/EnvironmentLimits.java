/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.graphics.model;

import org.itver.graphics.util.CuboidGeometry;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.image.TextureLoader;
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

/**
 * Esta clase es la representación gráfica de los objetos que actúan como
 * límites en el universo: paredes, piso y techo. Dados un ancho, alto,
 * profundidad y grosor, esta clase define un total de 6 bloques
 * limitadores:
 * <br>
 *  <ul>
 *      <li>FRONT - Pared frontal.</li>
 *      <li>BACK - Pared trasera con respecto a la pared frontal.</li>
 *      <li>LEFT - Pared izquierda.</li>
 *      <li>RIGHT - Pared derecha.</li>
 *      <li>TOP - Techo.</li>
 *      <li>BOTTOM - Piso.</li>
 *  </ul>
 * <br>
 * Instanciar esta clase da como resultado la creación de una sala que servirá
 * como escenario para una simulación robótica.
 * @author Karo
 */
public class EnvironmentLimits extends BranchGroup {

    private static EnvironmentLimits environmentLimits;
    private float                    width;
    private float                    height;
    private float                    thickness;
    private float                    deepness;
    private boolean                  selected;
    private boolean                  textureFlag;
    private File                     textureFile;
    private float                    boundingRadius;
    private BoundingSphere           environmentBounds;
    private PropertyChangeSupport    pcs;
    private GeometryInfo             geometry[];
    private Vector3f                 positions[];
    private Shape3D                  limits[];
    private Transform3D              transformations[];
    private TransformGroup           transformGroups[];
    private final int                LIMITS = 6; //Debe ser 6.
    private final int                POSITIONS = 5; //Debe ser: 5 de esos 6 límites tomarán una posición distinta porque el piso no se transformará en ningún momento
    private final int                TRANSFORMATIONS = 5;//Debe ser 5. 5 de los 6 límites se transformarán con una posición nueva (el piso no cuenta)
    private final int                GEOMETRIES = 3; //Una geometría por cada dos limites
    private final int                LEFT_RIGHT = 0; ////
    private final int                FRONT_BACK = 1; // constantes para referenciar las geometrías (GeometryInfo)
    private final int                TOP_BOTTOM = 2; ////
    private final double             ROTATION_ANGLE = Math.toRadians(90); //ángulo de giro
    /**
     * Constante indicadora para referenciar el bloque limitador izquierdo.
     */
    public final static int          LEFT = 0;
    /**
     * Constante indicadora para referenciar el bloque limitador derecho.
     */
    public final static int          RIGHT = 1;
    /**
     * Constante indicadora para referenciar el bloque limitador frontal.
     */
    public final static int          FRONT = 2;
    /**
     * Constante indicadora para referenciar el bloque limitador trasero.
     */
    public final static int          BACK = 3;
    /**
     * Constante indicadora para referenciar el bloque limitador del techo.
     */
    public final static int          TOP = 4;
    /**
     * Constante indicadora para referenciar el bloque limitador del piso.
     */
    public final static int          BOTTOM = 5;

    private EnvironmentLimits() {
        this(0, 0, 0, 0);
    }

    /**
     * Construye una nueva instancia de EnvironmentLimits que simula una sala
     * con cuatro paredes, un piso y un techo.
     * @param deepness Define la profundidad de la sala creada.
     * @param width Define el ancho de la sala creada.
     * @param height Define la altura de la sala creada.
     * @param thickness Define el grosor de cada bloque limitador en la sala
     * creada.
     */
    private EnvironmentLimits(float width, float height, float deepness, float thickness) {
        pcs = new PropertyChangeSupport(this);
        initTransformGroups();
        initTransformations();
        initLimits();
        //createDefaultAppearance();
        updateLimits(width, height, deepness, thickness);
    }

    /**
     * Inicializa las posibles posiciones para cada una de las paredes y el
     * techo en un arreglo de posiciones
     */
    private void initPositions() {
        positions = new Vector3f[POSITIONS];
        positions[LEFT] = new Vector3f(-width - thickness, height, 0);
        positions[RIGHT] = new Vector3f(width + thickness, height, 0);
        positions[BACK] = new Vector3f(0, height, -deepness - thickness);
        positions[FRONT] = new Vector3f(0, height, deepness + thickness);
        positions[TOP] = new Vector3f(0, height + height, 0);
    }

    /**
     * Inicializa los objetos TransformGroup para cada una de las paredes, que
     * son las que van a girar
     */
    private void initTransformGroups() {
        transformGroups = new TransformGroup[LIMITS];
        for (int i = 0; i < transformGroups.length; i++) {
            transformGroups[i] = new TransformGroup();
            transformGroups[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        }
    }

    /**
     * Inicializa las transformaciones que van a aplicarse a las paredes
     * en un arreglo de Transform3D
     */
    private void initTransformations() {
        transformations = new Transform3D[TRANSFORMATIONS];
        for (int i = 0; i < transformations.length; i++) {
            transformations[i] = new Transform3D();
        }
    }

    /**
     * Inicializa los límites en un arreglo de shapes y agrega cada una
     * a un TransformGroup en el arreglo de TG's, y ese mismo lo agrega
     * a 'this', otro TransformGroup
     */
    private void initLimits() {
        limits = new Shape3D[LIMITS];
        for (int i = 0; i < limits.length; i++) {
            limits[i] = new Shape3D();
            limits[i].setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
            limits[i].setCapability(Shape3D.ENABLE_COLLISION_REPORTING);
            limits[i].setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
            limits[i].setCapability(Shape3D.ALLOW_APPEARANCE_READ);
            limits[i].setCollidable(true);
            limits[i].setUserData(this);
            transformGroups[i].addChild(limits[i]);
            this.addChild(transformGroups[i]);
        }
        //Pablo: Agregué esto para darle apariencia a los límites al crearlo
        //TODO: Esto es temporal, quitar cuando las apariencias, texuras y/o 
        //materiales se hagan atributos
        Material material = new Material();
        material.setCapability(Material.ALLOW_COMPONENT_READ);
        material.setCapability(Material.ALLOW_COMPONENT_WRITE);
        this.setLimitMaterial(material);
        //======================================================================
    }

    /**
     * Inicializa las geometrías necesarias (GeometryInfo) para dibujar cada
     * bloque que representa piso y paredes
     */
    private void initGeometryInfo() {
        geometry = new GeometryInfo[GEOMETRIES];
        for (int i = 0; i < geometry.length; i++) {
            geometry[i] = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
        }
    }

    /**
     * Coloca la geometría predeterminada dentro del arreglo de geometrías
     */
    private void setGeometryInfo() {
        geometry[LEFT_RIGHT].setCoordinates(CuboidGeometry.generateGeometry(height, thickness, deepness));
        geometry[FRONT_BACK].setCoordinates(CuboidGeometry.generateGeometry(width, thickness, height));
        geometry[TOP_BOTTOM].setCoordinates(CuboidGeometry.generateGeometry(width, thickness, deepness));
        for (int i = 0; i < geometry.length; i++) {
            geometry[i].setCoordinateIndices(CuboidGeometry.setIndices());
            geometry[i].setStripCounts(CuboidGeometry.setStripCounts());
        }
    }

    /**
     * Crea las normales y las asigna a las geometrías del arreglo de
     * GeometryInfo
     */
    private void createNormals() {
        NormalGenerator[] normals = new NormalGenerator[GEOMETRIES];
        for (int i = 0; i < normals.length; i++) {
            normals[i] = new NormalGenerator();
            normals[i].generateNormals(geometry[i]);
        }
        for (int i = 0; i < limits.length; i++) {
            limits[i].setGeometry(geometry[i / 2].getGeometryArray());
        }
    }

    /**
     * @deprecated 
     * Método obsoleto. No usar. 
     */

    private void createDefaultAppearance() {
        Appearance ap = new Appearance();
        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.MODULATE);
        ap.setTextureAttributes(ta);
        Color3f alumDiffuse = new Color3f(0.37f, 0.37f, 0.37f);
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f alumSpecular = new Color3f(0.89f, 0.89f, 0.89f);
        Material material = new Material(alumDiffuse, black, alumDiffuse, alumSpecular, 17);
        material.setLightingEnable(true);
        ap.setMaterial(material);
        for (int i = 0; i < limits.length; i++) {
            limits[i].setAppearance(ap);
        }
    }
    
    /**
     * Crea un material para aplicarlo a la apariencia de los bloques limitadores.
     * @param ambient Color de ambiente.
     * @param emissive Color ____
     * @param diffuse Color ___
     * @param specular Color ___
     * @param shininess Brillo del material.
     */
    private void updateMaterial(Color3f ambient, Color3f emissive, Color3f diffuse, Color3f specular, float shininess){
        Appearance appearance = new Appearance();
        appearance.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
        appearance.setCapability(Appearance.ALLOW_TEXTURE_READ);
        appearance.setCapability(Appearance.ALLOW_TEXTURE_ATTRIBUTES_WRITE);
        appearance.setCapability(Appearance.ALLOW_TEXGEN_WRITE);
        appearance.setCapability(Appearance.ALLOW_MATERIAL_READ);
        appearance.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
        Material material = new Material(ambient, emissive, diffuse, specular, shininess);
        //Pablo: Capabilities para J3d 1.3
        material.setCapability(Material.ALLOW_COMPONENT_READ);
        material.setCapability(Material.ALLOW_COMPONENT_WRITE);
        //----------------------------------
        appearance.setMaterial(material);
        setAppearance(appearance);
    }
    
    /**
     * Aplica un material determinado a la apariencia de los bloques limitadores.
     * @param material Material de los límites.
     */
    public void setLimitMaterial(Material material){
        Appearance appearance = new Appearance();
        appearance.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
        appearance.setCapability(Appearance.ALLOW_TEXTURE_READ);
        appearance.setCapability(Appearance.ALLOW_TEXTURE_ATTRIBUTES_WRITE);
        appearance.setCapability(Appearance.ALLOW_TEXGEN_WRITE);
        appearance.setCapability(Appearance.ALLOW_MATERIAL_READ);
        appearance.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
        appearance.setMaterial(material);
        setAppearance(appearance);
//        for (int i = 0; i < limits.length; i++) {
//            limits[i].setAppearance(appearance);
//        }
    }
    //********************************************************************************
    //EL SIGUIENTE BLOQUE DE MÉTODOS FORMA PARTE DE UN ASPECTO IMPORTANTE A 
    //CONSIDERAR SOBRE ESTA CLASE:
    //Como se puede ver, la clase no cuenta con atributos de Appearance ni 
    //Material (lo mismo aplica para los atributos del material, que son 4 colores
    //y un flotante), lo que hizo que la implementación de los siguientes métodos se
    //volviera (muy probablemente) más compleja y extensa de lo que en realidad
    //podría haber sido si se hubieran puestos dos atributos para cada cosa.
    //
    //En lugar de eso, todo se realiza a través de variables locales que se asignan
    //a los respectivos atributos Appearance de cada uno de los Shape3D que representan
    //cada uno de los límites.
    //
    //Evaluar si en un futuro es conveniente cambiar o dejarse así.
    //Si se llega a dejar así, asegurar que no se repitan líneas de código ni 
    //de que se usen líneas nuevas de código para hacer cosas que ya hacen otros
    //métodos de esta misma clase.
    //********************************************************************************
    
    //CUIDADO con este método
    //Regresa el material asociado a la apriencia de los límites
    private Material getAppearanceMaterial(){
        Appearance reference = limits[0].getAppearance();
        Material material = reference.getMaterial();
        return material;
    }
    //CUIDADO con este método
    //Regresa los colores del material de la apariencia aplicada a todos los límites
    //Los regresa ordenados como : ambient(0), emissive(1), diffuse(2) y specular(3)
    public Color3f[] getMaterialColors(){
        Color3f[] colors = new Color3f[4];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = new Color3f();
        }
        getAppearanceMaterial().getAmbientColor(colors[0]);
        getAppearanceMaterial().getEmissiveColor(colors[1]);
        getAppearanceMaterial().getDiffuseColor(colors[2]);
        getAppearanceMaterial().getSpecularColor(colors[3]);
        return colors;
    }
    //CUIDADO con este método
    //Tiene getAppearance()[0] porque todos los límites tienen la misma Appearance.
    public Color getMaterialAmbientColor(){
        Color3f ambientColor = new Color3f();
        getAppearance()[0].getMaterial().getAmbientColor(ambientColor);
        return ambientColor.get(); 
    }
    //CUIDADO con este método
    public void setMaterialAmbientColor(Color ambientColor){
        Color old = getMaterialAmbientColor();
        Color3f nColor = new Color3f(ambientColor);
        //Material mat = new Material();
        updateMaterial(nColor, new Color3f(getMaterialEmissiveColor()), 
                               new Color3f(getMaterialDiffuseColor()), 
                               new Color3f(getMaterialSpecularColor()), 
                       this.getMaterialShininess());
        if(textureFlag)
                setTexture(textureFile);
        pcs.firePropertyChange("ambientColor", old, nColor);
    }
        public Color getMaterialEmissiveColor(){
        Color3f emissiveColor = new Color3f();
        getAppearance()[0].getMaterial().getEmissiveColor(emissiveColor);
        return emissiveColor.get(); 
    }
    //CUIDADO con este método
    public void setMaterialEmissiveColor(Color emissiveColor){
        Color old = getMaterialEmissiveColor();
        Color3f nColor = new Color3f(emissiveColor);
        updateMaterial(new Color3f(getMaterialAmbientColor()), nColor, 
                            new Color3f(getMaterialDiffuseColor()), 
                            new Color3f(getMaterialSpecularColor()), 
                            getMaterialShininess());
        if(textureFlag)
                setTexture(textureFile);
        pcs.firePropertyChange("emissiveColor", old, nColor);
    }    
    
    public Color getMaterialDiffuseColor(){
        Color3f diffuseColor = new Color3f();
        getAppearance()[0].getMaterial().getDiffuseColor(diffuseColor);
        return diffuseColor.get(); 
    }
    
    //CUIDADO con este método
    public void setMaterialDiffuseColor(Color diffuseColor){
        Color old = getMaterialDiffuseColor();
        Color3f nColor = new Color3f(diffuseColor);
        this.updateMaterial(new Color3f(getMaterialAmbientColor()), 
                            new Color3f(getMaterialEmissiveColor()), nColor, 
                            new Color3f(getMaterialSpecularColor()), 
                            getMaterialShininess());
        if(textureFlag)
                setTexture(textureFile);
        pcs.firePropertyChange("diffuseColor", old, nColor);
    }    
    
    public Color getMaterialSpecularColor(){
        Color3f specularColor = new Color3f();
        getAppearance()[0].getMaterial().getSpecularColor(specularColor);
        return specularColor.get(); 
    }
    //CUIDADO con este método
    public void setMaterialSpecularColor(Color specularColor){
        Color old = getMaterialSpecularColor();
        Color3f nColor = new Color3f(specularColor);
        this.updateMaterial(new Color3f(getMaterialAmbientColor()), 
                            new Color3f(getMaterialEmissiveColor()), 
                            new Color3f(getMaterialDiffuseColor()), nColor, 
                            getMaterialShininess());
        if(textureFlag)
                setTexture(textureFile);
        pcs.firePropertyChange("specularColor", old, nColor);
    }
    
    //CUIDADO con este método
    //Regresa el brillo del material aplicado a todos los límites
    public float getMaterialShininess(){
        float shininess = getAppearanceMaterial().getShininess();
        return shininess;
    }
    
    public void setMaterialShininess(float shininess){
        float old = getMaterialShininess();
        this.updateMaterial(new Color3f(getMaterialAmbientColor()), 
                            new Color3f(getMaterialEmissiveColor()), 
                            new Color3f(getMaterialDiffuseColor()), 
                            new Color3f(getMaterialSpecularColor()), shininess);
        if(textureFlag)
                setTexture(textureFile);
        pcs.firePropertyChange("shininess", old, shininess);
    }
    //***********************************************************************
    //***********************************************************************
    //FIN DE BLOQUE**********************************************************
    //***********************************************************************
    //***********************************************************************
    
    /**
     * Establece una apariencia dada a un bloque limitador específico.
     * @param limit Límite sobre el que se quiere aplicar la apariencia.
     * @param app Apariencia que se desea aplicar a un límite específico.
     */
    private void setAppearance(int limit, Appearance app) {
        limits[limit].setAppearance(app);
    }

    /**
     * Obtiene la apariencia de un límite dado.
     * @param limit Límite del que se desea obtener su apariencia.
     * @return Apariencia del límite especificado.
     */
    private Appearance getAppearance(int limit) {
        return limits[limit].getAppearance();
    }

    /**
     * Establece una apariencia determinada a todos los límites de entorno.
     * @param app Apariencia que se desea poner en todos los límites.
     */
    public void setAppearance(Appearance app) {
        for (int i = 0; i < limits.length; i++) {
            limits[i].setAppearance(app);
        }
    }

    /**
     * Regresa un arreglo que contiene las apariencias de cada uno de los
     * límites.
     * @return Arreglo de todas las apariencias.
     */
    private Appearance[] getAppearance() {
        Appearance[] limitApp = new Appearance[LIMITS];
        for (int i = 0; i < limits.length; i++) {
            limitApp[i] = limits[i].getAppearance();
        }
        return limitApp;
    }

    public void setTexture(File textureFile) {
        
            File old = getTexture();
            this.textureFile = textureFile;
            pcs.firePropertyChange("Texture", old, textureFile);
            TextureLoader textureLoad = new TextureLoader(textureFile.getPath(), null);
            ImageComponent2D textureIm = textureLoad.getScaledImage(128, 128);
            Texture t = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB, textureIm.getWidth(), textureIm.getHeight());
            t.setCapability(Texture.ALLOW_ENABLE_WRITE);
            t.setImage(0, textureIm);
//            t.setEnable(textureFlag);
    //        Appearance ap = new Appearance();
            Appearance ap = limits[0].getAppearance();
            ap.setTexture(t);
            TextureAttributes textureAttr = new TextureAttributes();
            textureAttr.setTextureMode(TextureAttributes.COMBINE);
            ap.setTextureAttributes(textureAttr);
            float n = 5.0f;
            TexCoordGeneration tcg = new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR,
                                                            TexCoordGeneration.TEXTURE_COORDINATE_2,
                                                            new Vector4f(0, n, n, 0), new Vector4f(n, 0, 0, n));
            ap.setTexCoordGeneration(tcg);
            this.setAppearance(ap);
    }

    public File getTexture() {
        return textureFile;
    }

    public void setTextureFlag(boolean texture){
        boolean old = hasTexture();
        this.textureFlag = texture;
        pcs.firePropertyChange("Texture", old, texture);
        //este if se agregó por el problema de que Material y Appearance no son atributos.
        if(textureFlag)
                setTexture(textureFile);
        for (int i = 0; i < limits.length; i++) {
            limits[i].getAppearance().getTexture().setEnable(texture);
        }
    }

    public boolean hasTexture(){
        return textureFlag;
    }

    public void setSelectedLimit(int limit) {
    }

    /**
     * Regresa el arreglo de objetos Shape3D que representan los límites.
     * @return Los límites.
     */
    public Shape3D[] getShapeLimits() {
        return limits;
    }

    /**
     * Realiza las transformaciones de rotación y traslación con los objetos
     * contenidos en transformations y se los asigna a dos transformGroups
     * correspondientes a las paredes "left" y right"
     */
    private void transformToXY() {
        transformations[LEFT].rotZ(ROTATION_ANGLE);
        transformations[LEFT].setTranslation(positions[LEFT]);
        transformGroups[LEFT].setTransform(transformations[LEFT]);
        transformations[RIGHT].rotZ(ROTATION_ANGLE);
        transformations[RIGHT].setTranslation(positions[RIGHT]);
        transformGroups[RIGHT].setTransform(transformations[RIGHT]);
    }

    /**
     * Realiza las transformaciones de rotación y traslación con los objetos
     * contenidos en transformations y se los asigna a dos transformGroups
     * correspondientes a las paredes "frente" y "atrás"
     */
    private void transformToYZ() {
        transformations[FRONT].rotX(ROTATION_ANGLE);
        transformations[FRONT].setTranslation(positions[FRONT]);
        transformGroups[FRONT].setTransform(transformations[FRONT]);
        transformations[BACK].rotX(ROTATION_ANGLE);
        transformations[BACK].setTranslation(positions[BACK]);
        transformGroups[BACK].setTransform(transformations[BACK]);
    }

    /**
     * Realiza la transformación de traslación correspondiente al bloque limitador
     * del techo para colocarlo en la parte superior de la sala.
     */
    private void transformTop() {
        transformations[TOP].setTranslation(positions[TOP]);
        transformGroups[TOP].setTransform(transformations[TOP]);
    }

    /**
     * Realiza una actualización en los valores de los bloques limitadores. Este
     * método es llamado siempre que se realicen cambios en los límites, ya sea
     * por medio de la ejecución del programa o por un evento iniciado por el
     * usuario final
     * @param width Nuevo valor del ancho de la sala creada.
     * @param height Nuevo valor de la altura de la sala creada.
     * @param deepness Nuevo valor de la profundidad de la sala creada.
     * @param thickness Nuevo valor del grosor de cada bloque limitador en la
     * sala.
     */
    public final void updateLimits(float width, float height, float deepness, float thickness) {
        setWidth(width);
        setHeight(height);
        setDeepness(deepness);
        setThickness(thickness);
        calculateEnvironmentBounds();
    }

    /**
     * Agrega un oyente para mantener en comunicación sobre los cambios
     * realizados en ciertas propiedades.
     * @param listener Escucha los cambios realizados en las propiedades.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Elimina un oyente tipo PropertyChangeListener de este objeto.
     * @param listener El objeto oyente a eliminar.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Obtiene el valor actual de la profundidad en la sala.
     * @return Valor de profundidad.
     */
    public float getDeepness() {
        return deepness;
    }

    /**
     * Cambia la profundidad almacenada por una nueva y reemplaza las posiciones
     * y geometrías anteriores en los bloques limitadores para graficarlos con
     * el nuevo valor.
     * @param deepness Nueva profundidad.
     */
    public void setDeepness(float deepness) {
        float temp = getDeepness();
        this.deepness = deepness;
        updateGeometryAndPositions();
        pcs.firePropertyChange("Deepness", temp, deepness);
    }

    /**
     * El valor actual de la altura de las paredes de la sala.
     * @return Valor de altura.
     */
    public float getHeight() {
        return height;
    }

    /**
     * Cambia la altura almacenada por una nueva y reemplaza las posiciones
     * y geometrías anteriores en los bloques limitadores para graficarlos con
     * el nuevo valor.
     * @param height El nuevo valor de la altura.
     */
    public void setHeight(float height) {
        float temp = getHeight();
        this.height = height;
        updateGeometryAndPositions();
        pcs.firePropertyChange("Height", temp, height);
    }

    /**
     * El valor actual del grosor de los bloques limitadores de la sala.
     * @return Valor de grosor.
     */
    public float getThickness() {
        return thickness;
    }

    /**
     * Cambia el grosor almacenada por uno nuevo y reemplaza las posiciones
     * y geometrías anteriores en los bloques limitadores para graficarlos con
     * el nuevo valor.
     * @param thickness El nuevo valor del grosor.
     */
    public void setThickness(float thickness) {
        float temp = getThickness();
        this.thickness = thickness;
        initGeometryInfo();
        updateGeometryAndPositions();
        pcs.firePropertyChange("Thickness", temp, thickness);
    }

    /**
     * El valor actual del ancho de los bloques limitadores correspondientes a las
     * paredes.
     * @return Valor de ancho.
     */
    public float getWidth() {
        return width;
    }

    /**
     * Cambia el ancho almacenado por uno nuevo y reemplaza las posiciones
     * y geometrías anteriores en los bloques limitadores para graficarlos con
     * el nuevo valor.
     * @param width Nuevo valor del ancho de los bloques limitadores
     * conrrespondientes a las paredes.
     */
    public void setWidth(float width) {
        float temp = getWidth();
        this.width = width;
        updateGeometryAndPositions();
        pcs.firePropertyChange("Width", temp, width);
    }

    /**
     * Establece en true o false el atributo de selección de los límites.
     * @param selected Si está seleccionado debe ser true, de lo contrario, false.
     */
    public void setSelected(boolean selected) {
        boolean aux = isSelected();
        this.selected = selected;
        pcs.firePropertyChange("Selected", aux, selected);
    }

    /**
     * Regresa true o false según la selección del objeto actual.
     * @return Si está seleccionado regresa true, de lo contrario regresa false.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Crea nuevas posiciones y objetos GeometryInfo para poder actualizar los
     * bloques limitadores debido a cambios en las propiedades de ancho, alto
     * profundidad y grosor.
     */
    private void updateGeometryAndPositions() {
        initPositions();
        initGeometryInfo();
        setGeometryInfo();
        createNormals();
        transformToXY();
        transformToYZ();
        transformTop();
    }

    //Revisar la utilidad de que el if esté presente en ambos métodos//////////
//    //(create y get Instance)//////////////////////////////////////////////////
    private synchronized static void createInstance() {
        if (environmentLimits == null) {
            environmentLimits = new EnvironmentLimits();
        }
    }

    /**
     * Regresa el objeto EnvironmentLimits.
     * @return La instancia única de EnvironmentLimits.
     */
    public static EnvironmentLimits getInstance() {
        if (environmentLimits == null) {
            createInstance();
        }
        return environmentLimits;
    }
    /////////////////////////////////////////////////////////////////////

    /**
     * Calcula la esfera de limitación dentro de la cual se ubica el ambiente de
     * simulación tomando en cuenta las dimensiones de los bloques limitadores.
     */
    public void calculateEnvironmentBounds() {
        int tolerance = 2;
        float[] dimensions = {width, height, deepness};
        for (int i = 0; i < dimensions.length; i++) {
            if (boundingRadius < dimensions[i]) {
                boundingRadius = dimensions[i];
            }
        }
        boundingRadius += tolerance;
        environmentBounds = new BoundingSphere(new Point3d(0, 0, 0), boundingRadius);
    }

    /**
     * Regresa el valor más alto del alto, ancho y profundidad de la sala.
     * @return El valor más alto.
     */
    public float getHighest() {
        return boundingRadius;
    }

    /**
     * Regresa una BoundingSphere que delimita los alcances del ambiente.
     * @return BoundingSphere del ambiente tridimensional.
     */
    public BoundingSphere getEnvironmentBounds() {
        return environmentBounds;
    }
    
    public float[] getEnvironmentValues(){
        float[] values = new float[]{getWidth(), getHeight(), getDeepness(), getThickness()};
        return values;
        
        
    }
}
