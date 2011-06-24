/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.common.xml;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.LoaderBase;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import org.itver.common.xml.Interpreter;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * =============================================================================
 * <br/>
 * Se llama "XmlLoader" por default, se le cambiará el nombre a
 * "nombreDelProyecto"Loader cuando haya "NombreDelProyecto"
 * <br/>
 * =============================================================================
 * <br/>
 * Clase Encargada de leer un archivo XML y recorrer cada rama. Las clases
 * que quieran interpretar esas ramas leídas deberán heredar de {@link
 * controls.generate.Interpreter Interpreter} y ser asignadas en {@link
 * #setInterpreter(controls.generate.Interpreter) setInterpreter}.
 *
 * @author Pablo Antonio Guevara González
 */
public class XmlLoader extends LoaderBase{
    private DOMParser   parser;
    private Document    xmlDocument;
    private String      fileName;
    private Reader      reader;
    private XmlWalker   walker;
    private Interpreter interpreter;

   /**
    * Contructor general
    */
    public XmlLoader(){
        parser = new DOMParser();
        walker = new XmlWalker();
    }

    /**
     * Devuelve el objeto de la clase DOMParser con la información y estructura
     * general del árbol XML
     *
     * @return el parser que ha leído el archivo. Null si no ha sido cargado
     * ningún archivo.
     * @see com.sun.org.apache.xerces.internal.parsers.DOMParser DOMParser
     */
    public DOMParser getParser() {
        return parser;
    }

    /**
     * Asigna un objeto de la clase Interpreter como oyente de XmlWalker.
     * el interprete asignado deberá ser quien devuelva la escena general leída
     * en uno de los métodos load().
     *
     * @param interpreter el intérprete que será asignado para leer el
     * documento.
     * @see controls.generate.Interpreter Interpreter
     */
    public void setInterpreter(Interpreter interpreter){
        walker.addListener(interpreter);
        this.interpreter = interpreter;
    }

    /**
     * Devuelve el Nodo padre de toda la estructura del árbol XML.
     * @return the xmlDocument en Nodo general del árbol XML.
     */
    public Document getXmlDocument() {
        return xmlDocument;
    }

    /**
     * Carga un archivo a partir de su ruta más el path asignados.
     *
     * @param string Ruta del archivo a ser leído.
     * @return Información leída en el archivo. ésta se obtendrá mediante el
     * método {@link com.sun.j3d.loaders.Scene#getSceneGroup() getSceneGroup()}
     *
     * @throws FileNotFoundException
     * @throws IncorrectFormatException
     * @throws ParsingErrorException
     */
    @Override
    public Scene load(String string) throws FileNotFoundException, 
                                            IncorrectFormatException,
                                            ParsingErrorException {
        this.fileName = string;
        this.reader   = new FileReader(fileName);
        return load();
    }

    /**
     * Función no implementada
     *
     * @param url
     * @return
     * @throws FileNotFoundException
     * @throws IncorrectFormatException
     * @throws ParsingErrorException
     */
    @Override
    public Scene load(URL url) throws FileNotFoundException, 
                                      IncorrectFormatException,
                                      ParsingErrorException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Carga una escena a partir del objeto File específico.
     * @param reader Objeto con el formato XML a leer
     * @return Información leída en el archivo. ésta se obtendrá mediante el
     * método {@link com.sun.j3d.loaders.Scene#getSceneGroup() getSceneGroup()}
     * @throws FileNotFoundException
     * @throws IncorrectFormatException
     * @throws ParsingErrorException
     */
    @Override
    public Scene load(Reader reader) throws FileNotFoundException, 
                                            IncorrectFormatException,
                                            ParsingErrorException {
        this.reader = reader;
        return load();
    }

    /**
     * Método mandado a llamar por todos los métodos "load()", recorre el árbol
     * y devuelve la escena leída de acuerdo al oyente que tiene XmlWalker
     *
     * @return la escena a devolver por algún método load()
     */
    private Scene load(){
        try{
            parser.parse(new InputSource(reader));
            reader.close();
            xmlDocument = parser.getDocument();
        } catch (SAXException ex) {
            System.err.println("Exception reading: " + ex.getMessage());
            return null;
        } catch (IOException ex) {
            System.err.println("Exception opening/closing the file: " +
                                ex.getMessage());
            return null;
        }
        walker.walkTree(xmlDocument);
        return interpreter.getScene();
    }
}
