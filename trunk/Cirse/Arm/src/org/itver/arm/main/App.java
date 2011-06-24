package org.itver.arm.main;


import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import org.itver.arm.io.ArmInterpreter;
import java.awt.AWTException;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.swing.JFrame;
import javax.vecmath.Point3f;
import org.itver.arm.io.ArmSaver;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeSupport;
import javax.media.j3d.Alpha;
import javax.media.j3d.Background;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.PointLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JButton;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import org.itver.arm.models.elements.Arm;
import org.itver.arm.views.gui.PieceInfo;
import org.itver.common.util.ScreenImage;
import org.itver.common.xml.XmlLoader;

/**
 *
 * @author pablo
 */
public class App{
    private int pid = 0;
    private Arm arm;
    private JFrame aux;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    BranchGroup branch;
    Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
    SimpleUniverse universe = new SimpleUniverse(canvas);

    public App(int w, int h) {
        PieceInfo pi = new PieceInfo();
        pcs.addPropertyChangeListener(pi);

        initUniverse();

        

        final JFrame frame = new JFrame("Arm app");
        frame.add(canvas);
        JButton saveBtn = new JButton("Guardar");
        saveBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
//                XmlSaver saver = new XmlSaver("prueba.xml");
//                saver.saveXml(arm);

                ArmSaver saver = new ArmSaver(1.0f);
                saver.save(arm, "prueba2.xml");

                BufferedImage img = null;
                try {
                    img = ScreenImage.createImage(new Rectangle(250, 150, 200, 200));
                } catch (AWTException ex) {
                    System.err.println(ex.getMessage());
                }
                try {
                    ScreenImage.writeImage(img, "src/ohshi.png");
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }

        });
        frame.add(saveBtn, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(w, h);
        frame.setVisible(true);
        
        aux = new JFrame("aux");
        aux.setResizable(false);
        aux.add(pi, BorderLayout.CENTER);
        aux.setBounds(900, 0, 300, 400);
        aux.setVisible(true);

        

    }

    private void initUniverse() {        
        universe.getViewingPlatform().setNominalViewingTransform();
        createBranch();
        universe.addBranchGraph(branch);
    }

    public static void main(String[] args) {
        App app = new App(800, 500);
    }

    private void createBranch() {
        branch = new BranchGroup();
        XmlLoader loader = new XmlLoader();
        loader.setInterpreter(new ArmInterpreter());
        OrbitBehavior ob = new OrbitBehavior(canvas,
                OrbitBehavior.REVERSE_ROTATE);

            
        try {
            Scene scene = loader.load("prueba2.xml");
            BranchGroup brg = scene.getSceneGroup();
            arm = (Arm)scene.getSceneGroup().getChild(0);            
            Transform3D t3d = new Transform3D();
            t3d.setScale(0.005);

            TransformGroup tg = new TransformGroup(t3d);
            tg.addChild(brg);


            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

            branch.addChild(tg);
            PointLight light = new PointLight(new Color3f(Color.YELLOW),
                    new Point3f(0, 1, 0), new Point3f(1, 0, 0));
            light.setInfluencingBounds(new BoundingSphere(new Point3d(), 10));
            branch.addChild(light);
            universe.getViewingPlatform().setViewPlatformBehavior(ob);
            ob.setSchedulingBounds(new BoundingSphere(new Point3d(), 100));

//            Behavior beh = new KeyboardBehavior(this);
//            beh.setSchedulingBounds(new BoundingSphere());
//            branch.addChild(beh);


//            TransformGroup vpTrans = universe.getViewingPlatform().
//            getViewPlatformTransform();
//            Transform3D vpt3d = new Transform3D();
//            vpTrans.getTransform(vpt3d);
//            vpt3d.lookAt(new Point3d(0, 1, 7),
//                         new Point3d(),
//                         new Vector3d(0, 1, 0));
//            vpt3d.invert();
//            vpTrans.setTransform(vpt3d);

            Background bg = new Background(new Color3f(Color.WHITE));
            bg.setApplicationBounds(new BoundingSphere());
            branch.addChild(bg);
            branch.compile();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IncorrectFormatException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParsingErrorException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Alpha restartAlpha() {
        return new Alpha(1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,
                10, 1000, 10000, 10000, 1000, 1000, 1000, 1000);
    }

    /**
     * @return the pid
     */
    public int getPid() {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(int pid) {
        this.pid = pid;
        pcs.firePropertyChange("", "", arm.getPiece(pid));
    }

    /**
     * @return the arm
     */
    public Arm getArm() {
        return arm;
    }

    /**
     * @param arm the arm to set
     */
    public void setArm(Arm arm) {
        this.arm = arm;
    }
}
