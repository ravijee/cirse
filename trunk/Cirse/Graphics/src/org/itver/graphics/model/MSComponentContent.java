/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.graphics.model;

import org.openide.nodes.Node;

/**
 * Interfaz para obtener la lista de nodos de algun plugin.
 * Ya que cada nuevo elemento hecho por un plugin <b>debe añadirse</b> en un 
 * MainSceneComponent que ya es que se añade en el MainScene. Los nodos de estos
 * necesitan una forma de obtener los de los nodos. para eso se utiliza esta 
 * interfaz, que deben implementar los nuevos Elementos de algún plugin.
 * @author pablo
 */
public interface MSComponentContent{
    public Node[] getContentNode();

}
