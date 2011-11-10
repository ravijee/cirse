/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.common.xml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Clase encargada de iniciar, llenar y cerrar un archivo XML, al intanciar este
 * objeto se debe espicificar el nombre del archivo que utilizará,
 * {@link #startFile(java.lang.String) empezarlo} espeficicando el doctype en el
 * que se basará, llenarlo y {@link #endFile() cerrándolo} para asegurar que el
 * archivo se creará.
 *
 * @author Pablo
 */
public class XmlSaver{
    /**
     * Codificiación default para los archivos XML
     */
    public static final String ENCODING_DEFAULT = "ISO-8859-1";

    public static final String PUBLIC_SYSTEM = "-//Entorno3D//doctypes/";
    public static final String LANGUAGE        = "//EN";

    /**
     * Ruta default donde se encuentran los doctypes de lo archivos manejados
     */
    public static final String PUBLIC_URL     = "http://monkeyarm.googlecode.com/files/";

    /**
     * Ruta default del Doctype que especifica los datos del xml del brazo
     */
    public static final String DOCTYPE_ARM      = "dtdArm.dtd";

    public static final String DOCTYPE_PIECE    = "dtdPiece.dtd";
    
    /**
     * Uri default (vacío)
     */
    public static final String URI              = "";

    /**
     * Nombre local default (vacío)
     */
    public static final String LOCAL_NAME       = "";

    /**
     * CDATA
     */
    public static final String CDATA            = "CDATA";
    public static final String DOCTYPE_UNIVERSE = "dtdUniverse.dtd";

    private AttributesImpl     atts;
    private TransformerHandler xmlHandler;
    private Transformer        serializer;
    private String             file;
    private FileOutputStream   handler;
    /**
     * Constructor default, crea un objeto XmlSaver que manejará la estructura a
     * almacenar en el documento XML
     */
    public XmlSaver(){
        this.atts = new AttributesImpl();
    }
   
    /**
     * Agrega un atributo al siguiente tag que se quiera crear. En caso de
     * querer agregar más de un atributo, se debe llamar este método tantas
     * veces como sea necesario.
     *
     * @param name nombre del atributo.
     * @param type tipo de atributo
     * @param value valor que tendrá el atributo
     */
    public void addAttribute(String name, String type, String value) {
        this.atts.addAttribute(URI, LOCAL_NAME, name, type, value);
    }

    /**
     * Borra todos los atributos agregados. Este método se manda a llamar
     * automáticamente al crear un tag
     * @see #startTag(java.lang.String) starttag(String)
     */
    public void resetAtts() {
        this.atts.clear();
    }

    /**
     * Inicia una etiqueta, agrega el contenido específico y la cierra
     * posteriormente.
     * @param tag nombre de la etiqueta.
     * @param content caracteres que irán entre el inicio y cierre de la
     * etiqueta
     * @see #startTag(java.lang.String) startTag(String)
     * @throws SAXException
     */
    public void startTag(String tag, String content) throws SAXException {
        startTag(tag);
        xmlHandler.characters(content.toCharArray(), 0, content.length());
        closeTag(tag);
    }

    /**
     * Inicia una etiqueta pero no la cierra. Ideal para aquellas etiquetas que
     * engloben otras dentro de ellas. después de crear la etiqueta, borra la
     * lista de atributos, en caso de haber.
     *
     * @param tag etiqueta a abrir, una vez abierta debe ser cerrada en un
     * posterior momento.
     * @see #resetAtts() resetAtts()
     * @see #closeTag(java.lang.String) closeTag(String)
     * @throws SAXException
     */
    public void startTag(String tag) throws SAXException {
        xmlHandler.startElement(URI, LOCAL_NAME, tag, atts);
        resetAtts();
    }

    /**
     * Cierra una etiqueta previamente {@link #startTag(java.lang.String)
     * abierta}
     * @param tag etiqueta a cerrar
     * @throws SAXException
     */
    public void closeTag(String tag) throws SAXException {
        xmlHandler.endElement(URI, LOCAL_NAME, tag);
    }

    /**
     * Crea un archivo con el nombre previamente establecido. Este método debe
     * ser llamado antes de comenzar a guardar datos en el docuemnto xml.
     * @param dtd ruta del Doctype en el que se basará el archivo.
     * @param file nombre del archivo
     */
    public void startFile(String dtd, String file) {
        this.file = file;
        StreamResult rs;
        SAXTransformerFactory tf;
        try {
            handler = new FileOutputStream(this.file);
            rs = new StreamResult(handler);
            tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            xmlHandler = tf.newTransformerHandler();
            setXmlProperties(XmlSaver.ENCODING_DEFAULT, true, dtd);
            xmlHandler.setResult(rs);
            xmlHandler.startDocument();
        } catch (SAXException ex) {
            System.err.println(ex.getMessage());
        } catch (TransformerConfigurationException ex) {
            System.err.println(ex.getMessage());
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Cierra el manejador del archivo y libera la memoria del archivo.
     */
    public void endFile() {
        try {
            xmlHandler.endDocument();
            handler.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } catch (SAXException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     *
     * @param encoding
     * @param indent
     * @param doctype
     */
    private void setXmlProperties(String encoding, boolean indent, String doctype) {
        serializer = xmlHandler.getTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, encoding);
        serializer.setOutputProperty(OutputKeys.INDENT, indent ? "yes" : "no");
        serializer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, PUBLIC_SYSTEM + doctype + LANGUAGE);
        serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, PUBLIC_URL + doctype);
    }
}
