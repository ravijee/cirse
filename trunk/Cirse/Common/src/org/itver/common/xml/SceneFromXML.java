/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.common.xml;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.LoaderBase;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import org.openide.util.Exceptions;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * <p>Clase base para la creación de una {@link Scene Scene} a partir de un
 * archivo XML.</p>
 * <h2>Uso común:</h2>
 * {@code
        File file = new File("/la/ruta/de/tu/archivo");
        //SceneHandler es la clase a heredar para crear un propio intérprete
        SceneHandler handler = new SceneHandler();
        handler.setFileName(file.getParentFile().getAbsolutePath());
        SceneFromXML loader = new SceneFromXML(handler);
        try {
            Scene scene = loader.load(new FileReader(file));
            BranchGroup result = scene.getSceneGroup();
            [...]
        } catch (FileNotFoundException ex) {
            [...]
        }
 * }
 * @author pablo
 */
public class SceneFromXML extends LoaderBase {

    private SceneHandler handler;

    private SceneFromXML(){
        
    }

    /**
     * Constructor default donde se asignar el intérprete.
     * @param handler intérprete que manejará el archivo XML.
     */
    public SceneFromXML(SceneHandler handler){
        this.handler = handler;
    }

    /**
     * {@inheritDoc}
     * @param fileName ruta del archivo a abrir
     * @return
     * @throws FileNotFoundException
     * @throws IncorrectFormatException
     * @throws ParsingErrorException
     */
    @Override
    public Scene load(String fileName) throws FileNotFoundException, IncorrectFormatException, ParsingErrorException {
        return loadXML(new InputSource(new FileReader(fileName)));
    }

    /**
     * {@inheritDoc}
     * @param url URL a abrir
     * @return
     * @throws FileNotFoundException
     * @throws IncorrectFormatException
     * @throws ParsingErrorException
     */
    @Override
    public Scene load(URL url) throws FileNotFoundException, IncorrectFormatException, ParsingErrorException {
        try {
            return loadXML(new InputSource(url.toURI().toString()));
        } catch (URISyntaxException ex) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * @param reader Bytes del archivo a leer.
     * @return
     * @throws FileNotFoundException
     * @throws IncorrectFormatException
     * @throws ParsingErrorException
     */
    @Override
    public Scene load(Reader reader) throws FileNotFoundException, IncorrectFormatException, ParsingErrorException {
        return loadXML(new InputSource(reader));
    }

    private Scene loadXML(InputSource source){
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.parse(source);
            source.getCharacterStream().close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SAXException ex) {
            Exceptions.printStackTrace(ex);
        }
        return handler.getScene();
    }
}
