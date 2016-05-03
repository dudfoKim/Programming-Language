package project1;

public class IdNode extends Node
{
	public String value;
	
	@Override
	public String toString()
	{
			return "ID: " + value;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this==obj)
		{
			return true;
		}
		if(!(obj instanceof IdNode))
		{
			return false;
		}
		if(!super.equals(obj))
		{
			return false;
		}
		
		IdNode tmp = (IdNode) obj;
		
		if(this.value.equals(tmp.value))
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public void copyValue(Node node)
	{
		this.value = ((IdNode) node).value;
	}
}