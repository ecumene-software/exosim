package ecumene.exo.sim.abstractions;

import ecumene.exo.sim.SimContext;
import ecumene.exo.sim.abstractions.galaxy.ExoGalaxyMap;

public class SimGalaxyContext {
	
	private SimContext parent;
	private ExoGalaxyMap map;
	private int          follow = -1;

	public SimGalaxyContext(SimContext parent, ExoGalaxyMap map) {
		this.parent = parent;
		this.map    = map;
	}
	
	public ExoGalaxyMap getMap() {
		return map;
	}

	public int getFollow() {
		return follow;
	}

	public void setFollow(int follow) {
		this.follow = follow;
	}

	public SimContext getParent() {
		return parent;
	}
	
}