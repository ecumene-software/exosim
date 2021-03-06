package ecumene.exo.sim.common.map.heightmap.voronoi;

import ecumene.exo.sim.common.map.heightmap.channel.HeightChannel;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class VoronoiEuclid extends Voronoi {
    private HeightChannel pDistance;
    private HeightChannel pHit;

    private Vector2i dimension;

    public VoronoiEuclid(Vector2i size, boolean denormalizePoints, VoronoiPoint ... points){
        super(points);
        this.dimension = size;

        if(denormalizePoints)                 // If points are normalized, multiply them by size
            for(VoronoiPoint point : points){ // Itr and multiply
                point.getPoint().x *= size.x;
                point.getPoint().y *= size.y;
            }

        pDistance = new HeightChannel(dimension.x, dimension.y);
        pHit      = new HeightChannel(dimension.x, dimension.y);

        float pmax = 0;
        for(int x = 0; x < dimension.x; x++){
            for(int y = 0; y < dimension.y; y++){
                // For each pixel locate the closest point
                float closest       = Float.MAX_VALUE;
                float secondClosest = closest;
                float pixelWeight   = 0;

                // All points are in screen space
                for(VoronoiPoint point : points) {
                    float distance = new Vector2f(x, y).distance(point.getPoint());
                    if(distance < closest){
                        pixelWeight = (point.getWeight() * 2) - 1;
                        secondClosest = closest;
                        closest       = distance;
                    } else if(distance < secondClosest)
                        secondClosest = distance;
                }

                float d1 = (255 - closest);
                float d2 = (255 - secondClosest);
                float distance = (((d1 - d2 ) / 255) * 2) - 1; // From (0,1) range to (-1,1) range
                if(distance > pmax) pmax = distance;

                pDistance.putPixel(x, y, distance);
                pHit     .putPixel(x, y, pixelWeight);
            }
        }

        pDistance.channelDivide(pDistance.copy().fill(pmax));
    }

    public HeightChannel getDistance(){
        return pDistance;
    }

    public HeightChannel getHit() {
        return pHit;
    }

    public Vector2i getDimensions() {
        return dimension;
    }
}
