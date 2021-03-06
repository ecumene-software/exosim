package ecumene.exo.sim.abstractions.planet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ecumene.exo.sim.SimContext;
import ecumene.exo.sim.common.map.real.RMap;
import ecumene.exo.sim.common.map.real.RPoint;

public class ExoPlanetMap {

	private List<IExoPlanetObject> objects;
	private RMap displayMap;
	public static float G = 6.67f; // Really 6.67x10^-11

	private ExoPlanetMap() {
		objects = new ArrayList<IExoPlanetObject>();
	}

	public ExoPlanetMap(ExoPlanet planet) {
		this();
		setPlanet(planet);
		for(int i = 0; i < planet.getMoonList().size(); i++){ // I know array.addAll(...) exists, but I need to set the parents also
			this.objects.add(planet.getMoonList().get(i));
			planet.getMoonList().get(i).setParent(planet);
		}
	}

	public ExoPlanet getPlanet(){
		return (ExoPlanet) objects.get(0);
	}

	public void setPlanet(ExoPlanet planet){
		planet.onAddedTo(this);
		objects.add(0, planet);
	}

	public List<IExoPlanetObject> getObjects(){
		return objects;
	}

	public RMap step(SimContext context, int steps){
		RPoint[] points = new RPoint[((ExoPlanet) objects.get(0)).getMoonList().size() + 1]; // For all the moons + the planet
		points[0] = getPlanet().step(context, steps);

		for(int i = 0; i < ((ExoPlanet) objects.get(0)).getMoonList().size(); i++){
			((ExoPlanet) objects.get(0)).getMoonList().get(i).onStep(context, steps);
			points[i + 1] = ((ExoPlanet) objects.get(0)).getMoonList().get(i); // RPoint ID = XMLObject list ID + 1
		}

		return new RMap(points);
	}

	public void clearTrackedPositions(){
		Iterator it = getPlanet().getTrackedMoons().entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			((TrackingParameters) pair.getValue()).getPreviousPositions().clear(); // Clears last positions
		}
	}
}
