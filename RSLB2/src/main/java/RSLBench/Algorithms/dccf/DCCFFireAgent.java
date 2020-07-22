package RSLBench.Algorithms.dccf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import RSLBench.Constants;
import RSLBench.Algorithms.BMS.RSLBenchCommunicationAdapter;
import RSLBench.Assignment.Assignment;
import RSLBench.Assignment.DCOP.AbstractDCOPAgent;
import RSLBench.Assignment.DCOP.DCOPAgent;
import RSLBench.Comm.CommunicationLayer;
import RSLBench.Comm.Message;
import RSLBench.Helpers.Distance;
import RSLBench.Helpers.Utility.ProblemDefinition;
import rescuecore2.config.Config;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

/**
 * A D-CCF fire brigade agent.
 *
 * @author lcpz
 *
 */
public class DCCFFireAgent extends AbstractDCOPAgent {

	protected List<DCOPAgent> agents;
	protected RSLBenchCommunicationAdapter communicationAdapter;
	protected long constraintChecks;

	/* Adapted from RLSBench.Helpers.Utility.ThirdUtilityFunction.getFireUtility */
	public static int getMaxCoalitionSize(StandardWorldModel world, Config config, EntityID target) {
		Building b = (Building) world.getEntity(target);

		if (b == null)
			return 0;

		double neededAgents = Math
				.ceil(b.getTotalArea() / config.getFloatValue(Constants.KEY_AREA_COVERED_BY_FIRE_BRIGADE));

		if (b.getFieryness() == 1)
			neededAgents *= 2;
		else if (b.getFieryness() == 2)
			neededAgents *= 1.5;

		/*
		 * Building brokenness and importance should also be considered, but we don't,
		 * since they are irrelevant in the current version of RCRS.
		 */

		return (int) Math.round(neededAgents);
	}

	public DCCFFireAgent(List<DCOPAgent> agents) {
		this.agents = agents;
		constraintChecks = 0;
	}

	@Override
	public void initialize(Config config, EntityID agentID, ProblemDefinition problem) {
		super.initialize(config, agentID, problem);
		communicationAdapter = new RSLBenchCommunicationAdapter(config);
	}

	@Override
	public boolean improveAssignment() {
		target = Assignment.UNKNOWN_TARGET_ID;

		StandardWorldModel world = problem.getWorld();

		double minDistance = Double.POSITIVE_INFINITY;
		double d, u;
		double maxUtility = Double.NEGATIVE_INFINITY;

		for (EntityID fire : problem.getFireAgentNeighbors(id)) {
			d = Distance.humanToBuilding(id, fire, world);
			u = problem.getFireUtility(id, fire);

			if (d < minDistance && u > maxUtility) {
				minDistance = d;
				maxUtility = u;
				target = fire;
			}

			constraintChecks++;
		}

    // This can happen if we have no neighbors
    if (target == null)
        target = problem.getHighestTargetForFireAgent(id);

		return false; // only 1 DCOPSolver iteration
	}

	/**
	 * Sends a message to self, to simulate the O(|A|) messages per agent.
	 */
	@Override
	public Collection<DCCFMessage> sendMessages(CommunicationLayer com) {
		ArrayList<DCCFMessage> message = new ArrayList<DCCFMessage>(1);
		message.add(new DCCFMessage());
		com.send(id, message.get(0));
		return message;
	}

	@Override
	public void receiveMessages(Collection<Message> messages) {
	}

	@Override
	public long getConstraintChecks() {
		return constraintChecks;
	}

}
