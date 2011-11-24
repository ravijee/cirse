/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.gui.util;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 * Clase con metodos estaticos para mostrar ventanas de dialogo.
 * @author pablo
 */
public final class Dialogs {

    private static File dir;
    private static final String TITLES[] = {"Open File", "Save File"};

    public void readProperties(Properties p){
        dir = new File(p.getProperty("directory"));
    }

    public void writeProperties(Properties p){
        p.setProperty("directory", dir.getAbsolutePath());
    }

    /**
     * Muestra una ventana de dialogo para abrir un archivo al usuario.
     * Regresando el archivo que se selecciono
     * @param extensionAllowed No implementado aun, extension de archivos 
     * permitidos.
     * @return Archivo seleccionado. {@code null} si no selcciono nada.
     */
    public static File openDialog(String extensionAllowed){
        JFileChooser chooser = new JFileChooser();
        String[] exts = extensionAllowed.split("[\\s]*,[\\s]*");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("", exts);
        chooser.setFileFilter(filter);
        File result = null;
        if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            result = chooser.getSelectedFile();
        return result;
    }

    /**
     * Muestra una ventana de dialogo para guardar un archivo al usuario.
     * Regresando el archivo que se selecciono.
     * @return  Archivo seleccionado para guardar. {@code null} si no selecciona
     * nada.
     * @deprecated Usar {@link #fileDialog(int, java.lang.String) FileDialog}
     */
    @Deprecated
    public static File saveDialog(){
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(dir);
        File result = null;
        if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            result = chooser.getSelectedFile();
            dir = result.getParentFile();
        }
        return result;
    }
    
    /**
     * Muestra un Dialogo para manejar archivos, los modos a elegir son los 
     * usados en {@link FileDialog FileDialog}. Las extensiones a abrir pueden 
     * ser multiples, separadas por una ",". Por ejemplo: {@code jpg, png, gif}.
     * @param mode Modo del dialogo, de acuerdo a los de {@link FileDialog
     * FileDialog}.
     * @param extAllow Extensiones sin punto del archivo permitido. separadas 
     * por coma si es mas de una.
     * {@code null} para permitir cualquiera.
     * @return Archivo seleccionado. {@code null} si no selecciona alguno.
     */
    public static File fileDialog(int mode, String extAllow){
        if(!System.getProperty("os.name").equals("Mac OS X") && 
            mode == FileDialog.LOAD)
            return Dialogs.openDialog(extAllow);
        else{
            FileDialog dialog = new FileDialog(new Frame(), TITLES[mode]);
            dialog.setMode(mode);
            if(extAllow != null)
                dialog.setFilenameFilter(new FileFilter(extAllow));
            dialog.setVisible(true);
            String file = dialog.getFile();
            String path = dialog.getDirectory();
            String extension = "";
            if(file == null)
                return null;
            else if(!(file.contains(".")) && extAllow.length() > 0 && mode == FileDialog.SAVE)
                extension = "." + extAllow.split("[\\s]*,[\\s]*", 1)[0];

                return new File(path + file + extension);
        }
    }
    /**
     * Muestra un mensaje en un dialogo de error.
     * @param message Mensaje a desplegar en el cuadro dialogo.
     */
    public static void showErrorDialog(String message){
        NotifyDescriptor descriptor = new NotifyDescriptor.Message(message,
                                                NotifyDescriptor.ERROR_MESSAGE);
        DialogDisplayer.getDefault().notify(descriptor);
    }
    
    private static class FileFilter implements FilenameFilter{
        private String[] fileNames;
        
        public FileFilter(String names){
            this.fileNames = names.split("[\\s]*,[\\s]*");
        }
        
        @Override
        public boolean accept(File dir, String name) {
            for(String fileName:fileNames)
                if(name.endsWith("." + fileName))
                    return true;
            return false;
        }
        
    }

}
