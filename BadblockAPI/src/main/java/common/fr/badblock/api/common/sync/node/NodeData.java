package fr.badblock.api.common.sync.node;

import fr.badblock.api.common.utils.GsonUtils;
import fr.badblock.api.common.utils.TimeUtils;
import lombok.Setter;

@Setter
public class NodeData
{

	private NodeIdentifier			nodeIdentifier;
	private long					lastProofOfExistence;
	private ToengaInstanceStatus	toengaInstanceStatus;

	public NodeData(NodeIdentifier nodeIdentifier, long keepAlive, ToengaInstanceStatus toengaInstanceStatus)
	{
		setNodeIdentifier(nodeIdentifier);
		setToengaInstanceStatus(toengaInstanceStatus);
		update(keepAlive);
	}

	/**
	 * Update
	 */
	void update(long keepAlive)
	{
		setLastProofOfExistence(TimeUtils.nextTimeWithSeconds(keepAlive));
	}
	
	public NodeIdentifier getNodeIdentifier()
	{
		return this.nodeIdentifier;
	}
	
	public ToengaInstanceStatus getToengaInstanceStatus()
	{
		return this.toengaInstanceStatus;
	}
	
	/**
	 * Returns if the node still exists.
	 * @return
	 */
	public boolean isValid()
	{
		return TimeUtils.isValid(getLastProofOfExistence());
	}
	
	/**
	 * Returns if the existence of the node is expired
	 * @return
	 */
	public boolean isExpired()
	{
		return !this.isValid();
	}
	
	/**
	 * Returns the last proof of existence (timestamp)
	 * @return
	 */
	public long getLastProofOfExistence()
	{
		return this.lastProofOfExistence;
	}

	/**
	 * Returns json data
	 * @return
	 */
	public String toJson()
	{
		return GsonUtils.getGson().toJson(this);
	}
	
}
