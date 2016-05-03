package built;

public class QuoteNode extends Node
{
	public Node value;

	@Override
	public boolean equals(Object obj)
	{
		if(this==obj)
		{
			return true;
		}
		if(!(obj instanceof QuoteNode))
		{
			return false;
		}
		if(!super.equals(obj))
		{
			return false;
		}
		
		QuoteNode tmp = (QuoteNode) obj;
		
		return this.value.equals(tmp.value);
	}

	@Override
	public void copyValue(Node node)
	{
		this.value = ((QuoteNode) node).value;
	}
}