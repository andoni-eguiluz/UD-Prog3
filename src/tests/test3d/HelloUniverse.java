package tests.test3d;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;

import javax.media.j3d.*;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.ColorCube;

public class HelloUniverse extends Applet {

    public BranchGroup createSceneGraph() {

        // Create the root of the branch graph

        BranchGroup objRoot = new BranchGroup();

        // Create the TransformGroup node and initialize it to the
        // identity. Enable the TRANSFORM_WRITE capability so that
        // our behavior code can modify it at run time. Add it to
        // the root of the subgraph.

        TransformGroup objTrans = new TransformGroup();
        objTrans.setCapability(
                            TransformGroup.ALLOW_TRANSFORM_WRITE);
        objRoot.addChild(objTrans);

        // Create a simple Shape3D node; add it to the scene graph.

        objTrans.addChild(new ColorCube().getShape());

        // Create a new Behavior object that will perform the
        // desired operation on the specified transform and add
        // it into the scene graph.

        Transform3D yAxis = new Transform3D();
        Alpha rotationAlpha = new Alpha(
                -1, Alpha.INCREASING_ENABLE,
                0, 0,            4000, 0, 0,                        0, 0, 0);
        RotationInterpolator rotator = new RotationInterpolator(
                rotationAlpha, objTrans, yAxis,
                0.0f, (float) Math.PI*2.0f);
        BoundingSphere bounds =
            new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
        rotator.setSchedulingBounds(bounds);
        objTrans.addChild(rotator);

        return objRoot;
    }

    public HelloUniverse() {
    	GraphicsConfiguration graphicsConfig = new GraphicsConfiguration() {
			@Override
			public AffineTransform getNormalizingTransform() {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public GraphicsDevice getDevice() {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public AffineTransform getDefaultTransform() {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public ColorModel getColorModel(int transparency) {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public ColorModel getColorModel() {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public Rectangle getBounds() {
				// TODO Auto-generated method stub
				return null;
			}
		};
        setLayout(new BorderLayout());
        Canvas3D c = new Canvas3D(graphicsConfig);
        add("Center", c);

        // Create a simple scene and attach it to the virtual
        // universe

        BranchGroup scene = createSceneGraph();
        UniverseBuilder u = new UniverseBuilder(c);
        u.addBranchGraph(scene);
    }

}



class UniverseBuilder extends Object {

    // User-specified canvas

    Canvas3D canvas;

    // Scene graph elements to which the user may want access

    VirtualUniverse                        universe;
    Locale                        locale;
    TransformGroup                        vpTrans;
    View                        view;

    public UniverseBuilder(Canvas3D c) {
        this.canvas = c;


        // Establish a virtual universe that has a single
        // hi-res Locale

        universe = new VirtualUniverse();
        locale = new Locale(universe);

        // Create a PhysicalBody and PhysicalEnvironment object

        PhysicalBody body = new PhysicalBody();
        PhysicalEnvironment environment =
                                            new PhysicalEnvironment();

        // Create a View and attach the Canvas3D and the physical
        // body and environment to the view.

        view = new View();
        view.addCanvas3D(c);
        view.setPhysicalBody(body);
        view.setPhysicalEnvironment(environment);

        // Create a BranchGroup node for the view platform

        BranchGroup vpRoot = new BranchGroup();

        // Create a ViewPlatform object, and its associated
        // TransformGroup object, and attach it to the root of the
        // subgraph. Attach the view to the view platform.

        Transform3D t = new Transform3D();
        t.set(new Vector3f(0.0f, 0.0f, 2.0f));
        ViewPlatform vp = new ViewPlatform();
        vpTrans = new TransformGroup(t);

        vpTrans.addChild(vp);
        vpRoot.addChild(vpTrans);

        view.attachViewPlatform(vp);

        // Attach the branch graph to the universe, via the
        // Locale. The scene graph is now live!

        locale.addBranchGraph(vpRoot);
    }

    public void addBranchGraph(BranchGroup bg) {
        locale.addBranchGraph(bg);
    }

}