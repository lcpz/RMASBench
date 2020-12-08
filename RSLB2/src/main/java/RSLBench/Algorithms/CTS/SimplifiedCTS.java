package RSLBench.Algorithms.dccf;

import RSLBench.Assignment.DCOP.DCOPAgent;
import RSLBench.Assignment.DCOP.DCOPSolver;
import rescuecore2.standard.entities.StandardEntityURN;

/**
 * Distributed Cluster-based Coalition Formation (S-CTS).
 *
 * @author lcpz
 */
public class SimplifiedCTS extends DCOPSolver {

	@Override
	protected DCOPAgent buildAgent(StandardEntityURN type) {
		switch (type) {
		case FIRE_BRIGADE:
			/*
			 * All agents already have the full problem knowledge (See
			 * DCOPSolver.initializeAgentType), hence we also give the global list of agents
			 * to each S-CTS fire agent to ease our computations (indeed, having access to
			 * the problem, each agent can re-build the list on its own) without affecting
			 * the metrics.
			 */
			return new SimplifiedCTSFireAgent(agents);
		default:
			throw new UnsupportedOperationException(String.format("The %s solver does not support agents of type %s",
					getClass().getSimpleName(), type));
		}
	}

	@Override
	public String getIdentifier() {
		return "S-CTS";
	}

}
