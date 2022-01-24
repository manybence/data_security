import java.security.SecureRandom;
import java.util.*;



class TokenInfo {
	static final int VALIDITY_MS = 5*30*1000;
	public String user;
	public Date validity;

	public TokenInfo(String user) {
		this.user = user;
		this.validity = new Date(System.currentTimeMillis() + VALIDITY_MS);
	}
}


public class TokenChecker {

	static final int TOKEN_LENGTH = 16; // 128 bits

	private HashMap<String, TokenInfo> sessions;
	private SecureRandom rd;

	private String bytesToHex(byte[] a) {
		StringBuilder sb = new StringBuilder(a.length * 2);
		for(byte b: a)
			sb.append(String.format("%02x", b));
		return sb.toString();
	}

	public TokenChecker() {
		this.sessions = new HashMap<>();
		this.rd = new SecureRandom();
	}


	public String newSession(String user) {
		byte[] token = new byte[TOKEN_LENGTH];
		this.rd.nextBytes(token);
		String stringToken = this.bytesToHex(token);
		this.sessions.put(stringToken, new TokenInfo(user));

		return stringToken;
	}

	public String checkToken(String token) {
		TokenInfo tokenInfo = this.sessions.get(token);
		if (tokenInfo == null) {
			return null;
		}
		Date now = new Date(System.currentTimeMillis());
		if (now.compareTo(tokenInfo.validity) > 0) {
			return null;
		}
		tokenInfo.validity = new Date(System.currentTimeMillis() + TokenInfo.VALIDITY_MS);
		return tokenInfo.user;
	}
}
