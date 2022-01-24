import java.util.*;


interface ServerInterface {
	void print(String filename, String printer) throws ServerNotStartedException;
	ArrayList<String> queue(String printer) throws ServerNotStartedException;
	boolean topQueue(String printer, int job) throws ServerNotStartedException;
	void start() throws ServerAlreadyStartedException;
	void stop() throws ServerNotStartedException;
	void restart() throws ServerNotStartedException;
	int status(String printer) throws ServerNotStartedException;
	String readConfig(String parameter) throws ServerNotStartedException;
	void setConfig(String parameter, String value) throws ServerNotStartedException;
}


public class BaseServer implements ServerInterface {

	private Map<String, ArrayList<String>> printers;
	private Map<String, String> parameters;
	private boolean started;

	public BaseServer() {
		this.printers = new HashMap<>();
		this.parameters = new HashMap<>();
		this.started = false;
	}


	public void print(String filename, String printer) throws ServerNotStartedException {
		if (!this.started) {
			throw new ServerNotStartedException();
		}
		if (this.printers.get(printer) == null) {
			this.printers.put(printer, new ArrayList<String>());
		}
		this.printers.get(printer).add(filename);
	}

	public ArrayList<String> queue(String printer) throws ServerNotStartedException {
		if (!this.started) {
			throw new ServerNotStartedException();
		}
		ArrayList<String> result = this.printers.get(printer);
		if (result == null) {
			result = new ArrayList<String>();
		}
		return result;
	}

	public boolean topQueue(String printer, int job) throws ServerNotStartedException {
		if (!this.started) {
			throw new ServerNotStartedException();
		}
		ArrayList<String> queue = this.printers.get(printer);
		if (queue == null || queue.size() < job) {
			return false;
		}
		String filename = queue.get(job-1);
		queue.remove(job-1);
		queue.add(0, filename);
		return true;
	}

	public void start() throws ServerAlreadyStartedException {
		if (this.started) {
			throw new ServerAlreadyStartedException();
		}
		this.started = true;
	}

	public void stop() throws ServerNotStartedException {
		if (!this.started) {
			throw new ServerNotStartedException();
		}
		this.started = false;
		this.printers = new Hashtable<String, ArrayList<String>>();
		this.parameters = new Hashtable<String, String>();
	}

	public void restart() throws ServerNotStartedException {
		if (!this.started) {
			throw new ServerNotStartedException();
		}
		this.printers = new Hashtable<String, ArrayList<String>>();
		this.parameters = new Hashtable<String, String>();
		this.started = true;
	}

	public int status(String printer) throws ServerNotStartedException {
		if (!this.started) {
			throw new ServerNotStartedException();
		}
		int jobs = 0;
		ArrayList<String> queue = this.printers.get(printer);
		if (queue != null) {
			jobs = queue.size();
		}
		return jobs;
	}

	public String readConfig(String parameter) throws ServerNotStartedException {
		if (!this.started) {
			throw new ServerNotStartedException();
		}
		String value = this.parameters.get(parameter);
		if (value == null) {
			value = "";
		}
		return value;
	}

	public void setConfig(String parameter, String value) throws ServerNotStartedException {
		if (!this.started) {
			throw new ServerNotStartedException();
		}
		this.parameters.put(parameter, value);
	}
}
