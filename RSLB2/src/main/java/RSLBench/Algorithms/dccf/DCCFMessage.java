package RSLBench.Algorithms.dccf;

import RSLBench.Comm.Message;

public class DCCFMessage implements Message {

	/**
	 * A D-CCF message always contains:
	 * 1. A node address.
	 * 2. A boolean/message flag (1 byte).
	 * 3. An integer value.
	 */
	@Override
	public int getBytes() {
		return Message.BYTES_ENTITY_ID + 1 + Message.BYTES_UTILITY_VALUE;
	}

}