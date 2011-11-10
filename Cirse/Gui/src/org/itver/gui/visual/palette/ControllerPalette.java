/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.gui.visual.palette;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.itver.common.xml.Interpreter;
import org.itver.common.xml.XmlLoader;
import org.openide.util.Exceptions;
import org.w3c.dom.Node;

/**
 *
 * @author pablo
 */
public final class ControllerPalette extends Interpreter
        implements ActionListener {

    private MainPalette catalog;
    private String fileName;
    private CategoryPalette category;
    private boolean choosed;

    public ControllerPalette() {
        this.catalog = new MainPalette();
        this.catalog.addActionListener(this);
    }

    public void addCategoryFromFile(File file) {
        XmlLoader loader = new XmlLoader();
        loader.setInterpreter(this);
        try {
            loader.load(new FileReader(file));
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IncorrectFormatException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ParsingErrorException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void addElementFromFile(File file) {
        this.fileName = file.getAbsolutePath();
        XmlLoader loader = new XmlLoader();
        loader.setInterpreter(this);
        try {
            loader.load(new FileReader(file));
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public MainPalette getMainPalette() {
        return this.catalog;
    }

    @Override
    public void actionPerformed(ActionEvent av) {
        this.choosed = av.getActionCommand().equals("accept");
    }

    @Override
    public Scene getScene() {
        return null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Node node = (Node) evt.getNewValue();
        this.readNode(node);
    }

    private void readNode(Node node) {
        DOM dom;
        try {
            dom = DOM.valueOf(node.getNodeName());
        } catch (java.lang.IllegalArgumentException ex) {
            return;
        }
        String title = obtainAttributeContent(node, "title");
        if(title.equals(""))
            throw new IncorrectFormatException("Must have a title Attribute");
        ItemPalette item;

        switch (dom) {
            case category:
                setCategory(title);
                break;
            case item:
                item = new ItemPalette(title);
                item.setImage(obtainAttributeContent(node, "img"));
                item.setFile(obtainAttributeContent(node, "file"));
                item.setDescription(node.getTextContent());
                category.addItem(item);
                break;
            case meta:
                String categoryName = obtainAttributeContent(node, "category");
                if (categoryName.equals(""))
                    throw new IncorrectFormatException
                            ("Must have a category attribute");
                setCategory(categoryName);

                item = new ItemPalette(title);
                item.setImage(obtainAttributeContent(node, "img"));
                item.setFile(fileName);
                item.setDescription(node.getTextContent());
                category.addItem(item);
        }

    }

    private void setCategory(String title) {
        category = catalog.getCategory(title);
        if (category == null) {
            category = new CategoryPalette(title);
            this.catalog.addCategory(category);
        }
    }

    private String obtainAttributeContent(Node node, String attrName){
        Node attr = node.getAttributes().getNamedItem(attrName);
        String attrContent = "";
        if(attr != null)
            attrContent = attr.getNodeValue();
        return attrContent;
    }

    private enum DOM {
        /*
        <palette>
        <category title="Category 1">
        <item title="" img="" file="">
        Donec id elit non mi porta gravida at eget metus.
        </item>
        <item title="" img="" file="">
        Donec id elit non mi porta gravida at eget metus.
        </item>
        <item title="" img="" file="">
        Donec id elit non mi porta gravida at eget metus.
        </item>
        </category>
        </palette>
         *
         *
        <meta title="" img="" file="" category="">Description here</meta>
         */

        category,
        item,
        meta
    }
}
