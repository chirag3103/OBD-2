package message;

import java.net.URLEncoder;

import com.util.ServerConstants;
import com.util.StringHelper;

public class Sender  {

    String recipient = null;
    String message = null;

    
    public Sender(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;
        if( this.message.length()>140){
        	 this.message= this.message.substring(0,140);
        }
    }
    public static void main(String[] args) {
        String sms[] = {"9766750000"};
        for (int i = 0; i < sms.length; i++) {

            Sender sender = new Sender(sms[i], "Hello VJ MH14AGPune Has Crossed Area,Current Address-Paud Road, Bhusari Colony, Kothrud, Pune, Maharashtra 411038, India 411038");
            try {
                sender.send();
               
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

	
  
    public int send() throws Exception {

    	String url=ServerConstants.SMS_URL+recipient.trim()+"&udh=&message="+URLEncoder.encode(message);
    	System.out.println(url);   
    	StringHelper.connect2Server(url);
        return 0;
    }
   
    /**
     * logging function, includes date and class name
     */
    private void log(String s) {
        System.out.println(new java.util.Date() + ":" + this.getClass().getName() + ":" + s);
    }
}