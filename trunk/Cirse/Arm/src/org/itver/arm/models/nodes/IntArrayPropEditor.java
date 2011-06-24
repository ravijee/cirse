/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itver.arm.models.nodes;

import java.beans.PropertyEditorSupport;
import org.itver.common.util.Converter;

/**
 *
 * @author pablo
 */
public class IntArrayPropEditor extends PropertyEditorSupport{



    @Override
    public String getAsText() {
        int[] enteros = (int[]) this.getValue();
        return Converter.intArrayToString(enteros);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.setValue(Converter.StringToIntArray(text));
    }
}
