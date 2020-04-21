// Java Socket code for Receiver

import java.net.*; 
import java.io.*; 
import java.util.*; 
   
public class Receiver { 
      
    // Initialize socket and I/O streams 
    private Socket s = null; 
    private DataInputStream dis = null; 
    private DataOutputStream dos = null; 
      
    // Constructor to put ip address and port 
    public Receiver(InetAddress ip,int port)throws IOException 
    { 
          
        // Opens a socket for connection 
        s = new Socket(ip,port); 
        System.out.println("Receiver listening to port "+ port);
          
        dis = new DataInputStream(s.getInputStream()); 
        dos = new DataOutputStream(s.getOutputStream()); 
          
        while (true) 
        {   Scanner scan = new Scanner(System.in); 
            long sum=0; 
              
            // Reads the data length sent by sender 
            int length = dis.readInt(); 
              
            // Initializes the arrays based on data length received 
            int data[] = new int[length]; 
              
            System.out.println("Data received (Last data represents the checksum) is"); 
              
            for(int i = 0; i< data.length; i++) 
            {    
                // Reading the data being sent one by one 
                data[i] = dis.readInt(); 
                System.out.println(Integer.toBinaryString(data[i]));                 
                sum+=data[i];
            } 
            sum%=65536; //To remove carry and overflow
            System.out.println("Sum = " + "(" + sum + ")10" + " = " + "(" + Long.toBinaryString(sum) + ")2");

            long output=0;
            
            for(int i=data.length-2;i>=0;i--){
                output*=65536;
                output+=(long)data[i];
            }
            System.out.println("=======================================================");

            System.out.println("Final Decoded Data :: "+ output);


            // Checking whether final result is 65535 or something else and sending feedback accordingly

            if(sum == 65535) // Since (65535)10 = (1111111111111111)2
            {    
                dos.writeUTF("success");    // Since no error was found and the data was received successfully 
                break; 
            }      
            else
            {    
                dos.writeUTF("failure");  // There was some error in transmitting or sending the data 
                break; 
            }

             
        } 
          
        // Closing all connections 
        dis.close(); 
        dos.close(); 
        s.close(); 
    } 
      
    // Main method to set up port and calling the constructor of receiver.
    public static void main(String args[])throws IOException 
    {    
        // Getting ip address on which the receiver is running 

        InetAddress ip = InetAddress.getLocalHost(); // This give "localhost" as the ip address  
        Receiver receive = new Receiver(ip,8080); 
    }     
} 