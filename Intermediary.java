import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;

public class Intermediary{
	private static Socket socket_with_client;
 	private static Socket socket_with_server;

    public static void main(String args[])
    {
        Util tools = new Util();
        try
        {
            //Server_intermediary Init
            int port_intermediary = 25002;
            ServerSocket intermediarySocket = new ServerSocket(port_intermediary);
            System.out.println("Intermediary Started and listening to the port " + port_intermediary);

        	//Listening the Server
            String host = "localhost";
            int port_server = 25001;
            InetAddress address = InetAddress.getByName(host);
            socket_with_server = new Socket(address, port_server);

            //Receiving message from client
            socket_with_client = intermediarySocket.accept();
            Package received = tools.receivePackage(socket_with_client);
            String message_from_client = received.getPackage();
            System.out.println("Message received from client is "+ message_from_client);
            //Send the message to the server
            String sendMessage = message_from_client + "Intermediary \n";
            Package sending = new Package(sendMessage);
            tools.sendPackage(socket_with_server,sending);
            System.out.println("Message sent to the server : "+ sendMessage);
 
            //Get the return message from the server
            InputStream is_s = socket_with_server.getInputStream();
            InputStreamReader isr_s = new InputStreamReader(is_s);
            BufferedReader br_s = new BufferedReader(isr_s);
            String message_received = br_s.readLine();
            System.out.println("Message received from the server : " + message_received);

            //Modifying the message from server 
            message_received = message_received + "Intermediary \n";

            //Sending the answer from the server back to the client
            OutputStream os_s = socket_with_client.getOutputStream();
            OutputStreamWriter osw_s = new OutputStreamWriter(os_s);
            BufferedWriter bw_s = new BufferedWriter(osw_s);
            bw_s.write(message_received);
            System.out.println("Message from server sent to the client is "+ message_received);
            bw_s.flush();

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            //Closing the socket
            try
            {
                socket_with_client.close();
                socket_with_server.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}