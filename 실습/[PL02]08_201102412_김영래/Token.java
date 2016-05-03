package built;

public class Token
{
	public final TokenType type;
	public final String lexeme;

	Token(TokenType type, String lexme)
	{
		this.type = type;
		this.lexeme = lexme;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s     %s\n", type.toString(), lexeme);
	}
}