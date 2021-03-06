package ecumene.exo.view.rmap.surface;

import ecumene.exo.Main;
import ecumene.exo.sim.SimContext;
import ecumene.exo.sim.abstractions.surface.ExoSurfaceMap;
import ecumene.exo.runtime.viewer.ViewerRunnable;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.beans.ExceptionListener;
import java.io.File;

public class SMVSurfaceViewer extends ViewerRunnable {

    private ExceptionListener        listener;
    private ExoSurfaceMap            surfaceMap;
    private JSMVSurfaceRenderer      renderer;
    private JFrame                   frame;
    private SMVSurfaceRendererConfig config;
    private Vector3f                 navigation;

    public SMVSurfaceViewer(int id, ExceptionListener listener, ExoSurfaceMap map, SMVSurfaceRendererConfig config, Vector3f navigation){
        super(id, listener);
        this.navigation = navigation;
        this.surfaceMap = map;
        this.listener = listener;
        this.config = config;
    }

    @Override
    public void init() throws Throwable {
        frame = new JFrame("Surface Rendering System");
        frame.setVisible(false);
        if(renderer == null) renderer = new JSMVSurfaceRenderer(surfaceMap, config, navigation);
        {
            frame.setSize(600, 600);
            frame.setLocationRelativeTo(null);
            frame.setIconImage(ImageIO.read(Main.class.getResource("/logo.png")));
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(renderer);
        }
        frame.setVisible(true);
    }

    @Override
    public void kill(int id) {
        frame.dispose();
    }

    @Override
    public void onStep(SimContext context, int step) {
        if(surfaceMap != null) surfaceMap.onStep(context, step);
        frame.repaint();
    }

    @Override
    public void onContextChanged(SimContext context) {
        renderer.setMap(context.getSurface().getSurfaceMap());
        renderer.onContextChanged(context);
    }

    public ExceptionListener getExceptionListener() {
        return listener;
    }

}
