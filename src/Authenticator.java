import java.util.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.io.*;


public class Authenticator {

	static final String FILE_PATH = "/tmp/print_server_users.csv";
	static final int HASH_LENGTH = 64; // 512 bits
	static final int SALT_LENGTH = 8;  // 64 bits

	private MessageDigest md;
	private SecureRandom rd;

	public Authenticator() {
		try {
			this.md = MessageDigest.getInstance("SHA-512");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.rd = new SecureRandom();

		File passwordFile = new File(FILE_PATH);
		if (!passwordFile.exists()) {
			this.addUser("Alice", "pwdalice");
			this.addUser("Bob", "pwdbob");
			this.addUser("Cecilia", "pwdcecilia");
			this.addUser("David", "pwddavid");
			this.addUser("Erica", "pwderica");
			this.addUser("Fred", "pwdfred");
			this.addUser("George", "pwdgeorge");
		}
	}

	private byte[] hashPassword(String password, byte[] salt) {
		this.md.update(salt);		
		this.md.update(password.getBytes(StandardCharsets.UTF_8));
		return this.md.digest();
	}

	private void addUser(String user, String password) {
		byte[] salt = new byte[SALT_LENGTH];
		this.rd.nextBytes(salt);
		byte[] hashedPassword = this.hashPassword(password, salt);


		String salt64 = Base64.getEncoder().encodeToString(salt);
		String password64 = Base64.getEncoder().encodeToString(hashedPassword);
		String newline = user + "," + salt64 + "," +  password64 + "\n";
		
		FileWriter myWriter;
		try {
			myWriter = new FileWriter(FILE_PATH, true);
			myWriter.append(newline);
			myWriter.close();
		} catch (Exception e) {
			System.err.println("Password file not found.");
			e.printStackTrace();
		}
	}

	public boolean authenticate(String user, String password) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(",");

				if (values[0].equals(user)) {
					byte[] salt = Base64.getDecoder().decode(values[1]);
					byte[] storedPassword = Base64.getDecoder().decode(values[2]);
					return this.md.isEqual(storedPassword, hashPassword(password, salt));
				}
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
