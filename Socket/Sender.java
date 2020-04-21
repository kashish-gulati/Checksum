// Java Socket code for Sender 

import java.io.*; 
import java.net.*; 
import java.util.*; 

public class Sender 
{ 
	// initialize socket and I/O streams 
	private Socket socket = null; 
	private ServerSocket servsock = null; 
	private DataInputStream dis = null; 
	private DataOutputStream dos = null; 
	
	public Sender(int port) throws IOException 
	{ 
		servsock = new ServerSocket(port); 
        System.out.println("Server start at port "+port+".");
		
		//Block until a client connects to the server 
		socket = servsock.accept(); 
		
		dis = new DataInputStream(socket.getInputStream()); 
		dos = new DataOutputStream(socket.getOutputStream()); 
		
		while (true) 
		{  
			Scanner scan = new Scanner(System.in);
			System.out.println("Enter data to be sent"); 
            long data = scan.nextLong();
		
			
			int size=(int)Math.ceil(Math.log(data)/Math.log(65536)); //To find the number of Datawords to to be sent 
            System.out.println("Number of datawords = "+size);
            int output[]= new int[size+1];   // In order to store the checksum as well and subsequently send it to reciever for error detection
            long sum=0;
            int i=0;
            System.out.println("====================================================================");
            System.out.println("Breaking the data to be sent in units of 16 bits ........");
            
            while(data!=0){   //initialized the counter for output array and added cond. for data to be divided into 16 bits
                
                output[i]=(int)(data%65536);   //taken rightmost 16 bits 
                
                System.out.println((i+1)+ " Unit = " + output[i]);
                
                sum+=output[i++];
                
                sum%=65536;         //to remove overflow and carry
                
                data/=65536;
        }

        output[i]=(int)sum^65535;
        System.out.println("Checksum = "+ output[i]);

        // Sends the data length to Receiver 
        dos.writeInt(output.length); 

        System.out.println("Datawords to be sent in binary :: ");
              
			
        // Sends the data one by one to receiver
        System.out.print("| "); 
        for (int j = 0; j < output.length; j++){
            System.out.print(Long.toBinaryString(output[j])+" | ");
            dos.writeInt(output[j]); 
        } 

        System.out.println();
		System.out.println("======================================================================");
		System.out.println("Data being sent along with Checkum.....");
        System.out.println("======================================================================");

		// Displaying appropriate message depending on feedback received from the receiver end. 
			if (dis.readUTF().equals("success")) 
			{ 
				System.out.println("Feedback :: Message received Successfully!"); 
				break; 
			} 
			
			else if (dis.readUTF().equals("failure")) 
			{ 
				System.out.println("Message was not received successfully!"); 
				break; 
			} 
		} 
		
		// Closing all connections 
		dis.close(); 
		dos.close(); 
		socket.close(); 
	} 

	//Main method for invoking the Sender constructor and initializing all the values
	public static void main(String args[]) throws IOException 
	{ 
		Sender send = new Sender(8080); 
	} 
} 
