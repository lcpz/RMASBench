/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RSLBench.Helpers.Utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import RSLBench.Constants;
import RSLBench.PlatoonFireAgent;
import RSLBench.PlatoonPoliceAgent;
import RSLBench.Helpers.Distance;
import rescuecore2.misc.Pair;
import rescuecore2.standard.entities.Building;
import rescuecore2.worldmodel.EntityID;

/**
 * Utility function that mimicks the pre-utility functions evaluation.
 *
 * @author Marc Pujol <mpujol@iiia.csic.es>
 */
public class ThirdUtilityFunction extends AbstractUtilityFunction {
    private static final Logger Logger = LogManager.getLogger(ThirdUtilityFunction.class);

    private Double maxDistance = null;

    @Override
    public double getFireUtility(EntityID agent, EntityID target) {
        if (maxDistance == null)
            maxDistance = getMaxDistance();

        Building b = (Building) world.getEntity(target);

        if (b == null)
        	return 0.0;

        double utility = 1.0;

        if (b.isFierynessDefined()) {
        	double f = b.getFieryness();
			if (f == 1.0)
				utility = 3;
			else if (f == 2.0)
				utility = 2;
        }

        double distance = Distance.humanToBuilding(agent, target, world);
        double threshold = config.getFloatValue(PlatoonFireAgent.MAX_DISTANCE_KEY);

        if (distance < threshold)
            distance = 0;

        double factor = distance/maxDistance;
        factor = Math.pow(factor, 2);

        // Add some noise to break ties
        factor += config.getRandom().nextDouble()/10000;

        double tradeoff = config.getFloatValue(Constants.KEY_UTIL_TRADEOFF);
        utility = utility - factor * tradeoff;

        //Logger.warn("Distance {}, factor {}, utility {}", distance, factor, utility);
        return utility;
    }

    @Override
    public double getPoliceUtility(EntityID policeAgent, EntityID blockade) {
        if (maxDistance == null)
            maxDistance = getMaxDistance();

        double threshold = config.getFloatValue(PlatoonPoliceAgent.DISTANCE_KEY);
        double distance = Distance.humanToBlockade(policeAgent, blockade, world, threshold);
        Logger.debug("Distance from police {} to blockade {}: {}", policeAgent, blockade, distance);

        double utility = -distance/maxDistance;
        utility = -Math.pow(utility, 2);

        // Add some noise to break ties
        utility += config.getRandom().nextDouble()/1000;

        // Downscale police utilities to subjugate them to fire agents
        utility *= config.getFloatValue(Constants.KEY_POLICE_ETA);

        Logger.debug("Utility from police {} to blockade {}: {}", policeAgent, blockade, utility);
        return utility;
    }

    public double getMaxDistance() {
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> bounds = world.getWorldBounds();
        final int xmin = bounds.first().first();
        final int ymin = bounds.first().second();
        final int xmax = bounds.second().first();
        final int ymax = bounds.second().second();
        final double dx = xmax - xmin;
        final double dy = ymax - ymin;
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    @Override
    public int getRequiredAgentCount(EntityID target) {
        Building b = (Building) world.getEntity(target);

        if (b == null)
        	return 0;

        int area = b.getTotalArea();
        double neededAgents = Math.ceil(area / config.getFloatValue(Constants.KEY_AREA_COVERED_BY_FIRE_BRIGADE));

        if (b.isFierynessDefined()) {
			if (b.getFieryness() == 1)
				neededAgents *= 2;
			 else if (b.getFieryness() == 2)
				neededAgents *= 1.5;
        }

        //Logger.debugColor("BASE: " + base + " | FIERYNESS: " + b.getFieryness() + " | NEEEDED AGENTS: " + neededAgents, Logger.BG_RED);
        return (int) Math.round(neededAgents);
    }

}