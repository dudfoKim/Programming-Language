package cute14_Parser;




public class Token {
	public final TokenType type;
	public final String lexme;
	Token(TokenType type, String lexme) {
		this.type = type;
		this.lexme = lexme;
	}
	@Override
	public String toString() {
		return String.format("[%s: %s]", type.toString(), lexme);
	}
}