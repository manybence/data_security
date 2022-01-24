import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.*;

public class Client {

    public static void main (String [] args) {
		
        try(Scanner sc = new Scanner(System.in)) {
            Registry registry = LocateRegistry.getRegistry("localhost");
            IPrintServer server = (IPrintServer) registry.lookup("IPrintServer");

			System.out.println("Print client CLI\n");
			System.out.print("User: ");
			String user = sc.nextLine().trim();
			System.out.print("Password: ");
			String password = sc.nextLine().trim();
			String token = server.startSession(user, password);
			if (token == null) {
				System.out.println("Invalid user or password");
				return;
			}
			System.out.println("Welcome " + user);

			while (true) {
				System.out.print(" > ");
				String cmd = sc.nextLine().trim();
				if (cmd.equals("exit") || cmd.equals("quit") || cmd.equals("q")) {
					System.out.println("Goodbye " + user);
					return;
				} else if (cmd.equals("")) {
					continue;
				}
				run_command(server, token, cmd);
			}

        } catch (Exception e) {
            System.err.println("Client exception: ");
            e.printStackTrace();
        }
    }

	public static void run_command(IPrintServer server, String token, String cmd) throws RemoteException {
		String[] args = cmd.split(" ");
		try {
			switch (args[0]) {

			case "print":
				if (args.length != 3) {
					System.out.println("Invalid or missing arguments");
					return;
				}
				server.print(args[1], args[2], token);
				break;

			case "queue":
				if (args.length != 2) {
					System.out.println("Invalid or missing arguments");
					return;
				}
				ArrayList<String> files = server.queue(args[1], token);
				if (files.isEmpty()) {
					System.out.printf("Printer %s queue is empty\n", args[1]);
				} else {
					System.out.printf("Printer %s queue:\n", args[1]);
					for (int i = 0; i < files.size(); i++) {
						System.out.printf("%d %s\n", i+1, files.get(i));
					}
				}
				break;

			case "top_queue":
				if (args.length != 3) {
					System.out.println("Invalid or missing arguments");
					return;
				}
				try{
					int job = Integer.parseInt(args[2]);
					if (server.topQueue(args[1], job, token)) {
						System.out.printf("Job %d of printer %s moved to the top\n", job, args[1]);
					} else {
						System.out.printf("Invalid job for printer %s\n", args[1]);
					}
				}
				catch (NumberFormatException ex){
					System.out.println("Invalid job number");
				}
				break;

			case "start":
				if (args.length != 1) {
					System.out.println("Invalid or missing arguments");
					return;
				}
				server.start(token);
				break;

			case "stop":
				if (args.length != 1) {
					System.out.println("Invalid or missing arguments");
					return;
				}
				server.stop(token);
				break;

			case "restart":
				if (args.length != 1) {
					System.out.println("Invalid or missing arguments");
					return;
				}
				server.restart(token);
				break;

			case "status":
				if (args.length != 2) {
					System.out.println("Invalid or missing arguments");
					return;
				}
				int jobs = server.status(args[1], token);
				System.out.printf("Printer %s has %d jobs\n", args[1], jobs);
				break;

			case "read_config":
				if (args.length != 2) {
					System.out.println("Invalid or missing arguments");
					return;
				}
				String value = server.readConfig(args[1], token);
				System.out.printf("Read %s: %s\n", args[1], value);
				break;

			case "set_config":
				if (args.length != 3) {
					System.out.println("Invalid or missing arguments");
					return;
				}
				server.setConfig(args[1], args[2], token);
				System.out.printf("Set %s: %s\n", args[1], args[2]);
				break;

			default:
				System.out.printf("Unknown command \"%s\"\n", args[0]);
			}
		} catch (ServerNotStartedException e) {
			System.out.println("Server is not started");
		} catch (ServerAlreadyStartedException e) {
			System.out.println("Server is already started");
		}
	}
}
