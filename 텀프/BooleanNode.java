package project2;

public class BooleanNode extends Node
{
	public boolean value;

	@Override
	public String toString()
	{
		return value ? "#T" : "#F";
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this==obj)
		{
			return true;
		}
		if(!(obj instanceof BooleanNode))
		{
			return false;
		}
		if(!super.equals(obj))
		{
			return false;
		}
		
		BooleanNode tmp = (BooleanNode) obj;
		
		if(this.value==tmp.value)
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public void copyValue(Node node)
	{
		this.value = ((BooleanNode) node).value;
	}
}