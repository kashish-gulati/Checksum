import java.util.*;
public class Checksum{
    public static int[] sender(int data){
        int size=(int)Math.ceil(Math.log(data)/Math.log(65536));
        int output[]= new int[size+1];
        int sum=0;
        int i=0;
        while(data!=0){//initialized the counter for output array and added cond. for data to be divided into 16 bits
            output[i]=data%65536;//taken rightmost 16 bits 
            sum+=output[i++];
            sum%=65536;//to remove overflow and carry
            data/=65536;
        }
        output[i]=sum^65535;
        return output;
    }
    public static int reciever(int data[]) throws CheckSumException{
        int sum=0;
        for(int i=0;i<data.length;i++){
            sum+=data[i];
        }
        sum%=65536;
        //System.out.println(sum);
        if(sum!=65535) throw new CheckSumException();
        int output=0;
        for(int i=data.length-2;i>=0;i--){
            output*=65536;
            output+=data[i];
        }
        return output;
    }
    public static void main(String args[]){
        Scanner scan = new Scanner(System.in);
        int data = scan.nextInt();
        try{
            System.out.println(reciever(sender(data)));
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
class CheckSumException extends Exception{
    public String toString() {
        return "Checksum Invalid";
    }
}