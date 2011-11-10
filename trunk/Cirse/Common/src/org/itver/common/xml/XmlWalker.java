/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.common.xml;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Clase que recorre un árbol a partir de un Node específicado en el 
 * método walkTree()
 *
 * @author Pablo Antonio Gueva González
 */
public class XmlWalker {
    private Node current;
    private PropertyChangeSupport alert;

    public XmlWalker(){
        alert = new PropertyChangeSupport(this);
    }

    /**
     * Agrega el oyente a cada cambio de nodo que se presente.
     * @param listener oyente que reaccionará a cada nuevo nodo que se vaya
     * leyendo en el árbol, la única propiedad que cambiará será "current".
     */
    public void addListener(PropertyChangeListener listener){
        this.alert.addPropertyChangeListener(listener);
    }

    /**
     *
     * Método que avisa el cambio a los oyentes, indica la propiedad y los
     * valores que han cambiado.
     * @param en nuevo valor que tendrá el objeto current
     */
    private void setCurrent(Node current) {
        Node old = this.current;
        this.current = current;
        this.alert.firePropertyChange("current", old, current);
    }


    /**
     * Avanza un árbol nodo por nodo de izquierda a derecha, informando a los
     * oyentes cada nuevo nodo que va recorriendo
     *
     * @param node el nodo padre que se desea recorrer.
     */
    public void walkTree(Node node){
        setCurrent(node);
        NodeList list = node.getChildNodes();
        for(int i = 0; i < list.getLength(); i++)
            walkTree(list.item(i));
    }

}
