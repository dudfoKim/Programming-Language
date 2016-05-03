package project1;

public class ListNode extends Node
{
	public Node value;

	@Override
	public boolean equals(Object obj)
	{
		if(this==obj)
		{
			return true;
		}
		if(!(obj instanceof ListNode))
		{
			return false;
		}
		
		ListNode tmp = (ListNode) obj;
		
		if(this.value.equals(tmp.value))
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public void copyValue(Node node)
	{
		this.value = ((ListNode) node).value;
	}
}