import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IPrintServer extends Remote {
    void print(String filename, String printer, String token) throws RemoteException, ServerNotStartedException;
    ArrayList<String> queue(String printer, String token) throws RemoteException, ServerNotStartedException;
    boolean topQueue(String printer, int job, String token) throws RemoteException, ServerNotStartedException;
    void start(String token) throws RemoteException, ServerAlreadyStartedException;
    void stop(String token) throws RemoteException, ServerNotStartedException;
    void restart(String token) throws RemoteException, ServerNotStartedException;
    int status(String printer, String token) throws RemoteException, ServerNotStartedException;
    String readConfig(String parameter, String token) throws RemoteException, ServerNotStartedException;
    void setConfig(String parameter, String value, String token) throws RemoteException, ServerNotStartedException;
    String startSession(String username, String password) throws RemoteException;
}
