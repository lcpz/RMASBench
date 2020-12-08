:ackage RSLBench.Algorithms.CTS;

import RSLBench.Comm.Message;

public class CTSMessage implements Message {

	/**
	 * A S-CTS message always contains:
	 * 1. A node address.
	 * 2. A boolean/message flag (1 byte).
	 * 3. An integer value (8 bytes).
	 */
	@Override
	public int getBytes() {
		return Message.BYTES_ENTITY_ID + 9;
	}

}
