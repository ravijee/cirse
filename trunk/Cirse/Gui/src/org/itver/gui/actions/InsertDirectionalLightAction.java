/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itver.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.itver.graphics.model.MainScene;
import org.itver.graphics.model.SceneLight;
import org.itver.graphics.util.LightType;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Scene",
id = "org.itver.gui.actions.InsertDirectionalLightAction")
@ActionRegistration(displayName = "#CTL_InsertDirectionalLightAction")
@ActionReferences({
    @ActionReference(path = "Menu/Scene/Insert/Light", position = 3433)
})
@Messages("CTL_InsertDirectionalLightAction=Directional Light")
public final class InsertDirectionalLightAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        MainScene.getInstance().addLight(new SceneLight(LightType.directional));
    }
}
