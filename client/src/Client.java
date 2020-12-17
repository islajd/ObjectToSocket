import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

public class Client {
    private String host;
    private int port;
    private Socket socket;
    private BufferedReader bufferedReader;

    private void initHost() throws IOException {
        File configFile = new File("resource/config.properties");
        FileReader reader = new FileReader(configFile);
        Properties props = new Properties();
        props.load(reader);
        String portStr = props.getProperty("port");
        port = Integer.parseInt(portStr);
        reader.close();
    }

    public Client() throws IOException {
        initHost();
        try{
            socket = new Socket(host,port);
            System.out.println("Client started on port " + socket.getLocalPort());
            System.out.println("Connected to server[host: " + socket.getInetAddress().toString()
                    + ", port: " + socket.getPort() +"]");

            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
            Scanner in = new Scanner(System.in);
            while (true) {
                System.out.print("Message to server : ");
                String messageToServer = in.nextLine();
                if(messageToServer.equals("exit")){
                    break;
                }
                pw.println(messageToServer);
                Object obj = is.readObject();

                if(obj instanceof Date){
                    Date date = (Date) obj;
                    System.out.println("server date: " + date.toString());
                }
                else{
                    System.out.println("server response: " + (String) obj);
                }
            }

            socket.close();
        }catch (IOException | ClassNotFoundException e){
            System.out.println("Something went wrong.");
            System.out.println(e);
        }
    }
}
