/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.io;

import java.io.File;
import org.itver.arm.models.elements.AngleType;
import org.itver.arm.models.elements.ElementType;
import org.itver.arm.models.elements.Piece;
import org.itver.common.util.Converter;

/**
 *
 * Clase auxiliar encargada de crear y modificar los valores de una {@link
 * models.elements.Piece pieza} a partir de cadenas de caracteres con un formato
 * específico.
 *
 * @author Pablo Antonio Guevara González.
 */
public class PieceInterpreter {
    private Piece piece;

    /**
     * Contructor general de la clase.
     */
    public PieceInterpreter(){
    }

    /**
     * Crea una {@link models.elements.Piece pieza}, y es a la que se le
     * modificarán los valores en los demás métodos.
     *
     * @param id String con formato entero del id que tendrá la pieza.
     * @param type String con el nombre del tipo de la pieza
     * @see models.elements.ElementType tipos.
     * @param src Ruta del archivo COLLADA. @see #setSource(String source).
     */
    public void createPiece(String id, String type, String src){
        this.piece = new Piece(Integer.parseInt(id),
                         ElementType.valueOf(type));
        setSource(src);
    }

    /**
     * Devuelve la pieza con los valores que le han sido asignados.
     *
     * @return instancia de la pieza creada. NULL si no se ha creado ninguna
     * pieza.
     * @see models.elements.Piece#Piece(int, models.elements.ElementType)
     * @see models.elements.Element#setSource(java.lang.String)
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Asigna una {@link models.elements.Element#setSource(String source) ruta}
     * y su respectiva información de geometría y apariencia a partir de un
     * archivo en formato COLLADA (*.dae).
     *
     * @param source ruta del archivo COLLADA.
     * @see models.elements.Element#setSource(java.lang.String)
     */
    public void setSource(String source){
//        BranchGroup  design  = new BranchGroup();
//        DAELoader       daeLoad = new DAELoader();
//        Scene           scene   = null;
//        try {
//            scene = daeLoad.load(source);
//            design = scene.getSceneGroup();
//            piece.setSource(source, design);
//        } catch (FileNotFoundException ex) {
//            System.err.println("File not found: " + ex.getMessage());
//        } catch (IncorrectFormatException ex) {
//            System.err.println("incorrect format: " + ex.getMessage());
//        } catch (ParsingErrorException ex) {
//            System.err.println("Parsing error: " + ex.getMessage());
//        }
        piece.setSource(new File(source));
    }

    /**
     * Asigna los angulos a la pieza.
     *
     * @param angles Cadena de caracteres con los 3 ángulos en formato
     * "xx, yy, zz".
     * @param type Nombre del tipo de angulo a ser modificado
     * @see models.elements.AngleType AngleType
     * @see models.elements.Piece#setAngles(models.elements.AngleType, double[])
     */
    public void setAngles(String angles, String type){        
        double    anglesVal[] = Converter.stringToDoubleArray(angles);
        AngleType angleType   = AngleType.valueOf(type);
        piece.setAngles(angleType, anglesVal);
    }

    /**
     * Asigna las relaciones a la pieza.
     *
     * @param rels Cadena de enteros con los id's de las piezas con las que está
     * relacionada en formato "id1, id2, id3"
     * @see models.elements.Piece#setRelations(int[]) setRelations
     */
    public void setRelations(String rels){
        piece.setRelations(Converter.StringToIntArray(rels));
    }

    /**
     * Asigna un Punto de fricción a la piez.
     *
     * @param friction Cadena de dobles en formato "x, y, z" del punto de fricción que
     * tendrá la pieza.
     * @see models.elements.Piece#addFriction(double, double, double)
     * addFriction
     */
    public void addFriction(String friction){
        piece.addFriction(Converter.stringToDoubleArray(friction));
    }

    /**
     * Establece la posición y tipo de posición de la pieza.
     *
     * @param position  Cadena de dobles en formato "x, y, z" del punto en el
     * que se encuentra el centro de esta pieza.
     *
     * @param relation Cadena que indica si la relación será absoluta o relativa
     * si este parámetro es "relative" fija a la pieza como relativa, en caso
     * contrario no.
     * @see models.elements.Element#setPosition(double, double, double)
     * setPosition
     * @see models.elements.Element#setRelative(boolean) setRelative
     */
    public void setPosition(String position, String relation){
        piece.setRelative(relation.equals("relative"));
        piece.setPosition(Converter.stringToDoubleArray(position));
    }
}
