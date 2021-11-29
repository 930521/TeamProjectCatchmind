package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;


public class ServerTask implements Runnable {

	Socket client;	
	
	Hashtable<String,PrintWriter> ht;
	
	ServerController sc;
	
	PrintWriter pw;		
	BufferedReader br;	
	
	String nickName;	
	
	boolean isRun = true;
	
	
	public ServerTask(
			Socket client, 
			Hashtable<String,PrintWriter> ht,
			ServerController sc) {
		this.client = client;
		this.ht = ht;
		this.sc = sc;
		
		try {
			OutputStream os = client.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
			pw = new PrintWriter(bw,true);
			
			InputStream is = client.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
		} catch (IOException e) {
			sc.displayText("Client 연결 오류 : "+e.getMessage());
		}
		
	}


	@Override
	public void run() {
		System.out.println("Client의 receive 시작");
		while(isRun) {
			try {
				String receiveData = br.readLine();
				if(receiveData == null) {
					isRun = false;
				}
				System.out.println(receiveData);
				
				String[] data = receiveData.split("\\|");

				String code = data[0];
				String text = data[1];
				switch(code) {
					case "0" :
						if(ht.containsKey(text)) {
							this.pw.println("1|사용할 수 없는 아이디입니다. 다시요청해주세요.");
							sc.displayText(text+"-사용할 수 없는 아이디 요청");
							isRun = false;
							client.close();
							break;
						}						
						nickName = text;
						ht.put(text, pw);
						String sendData = "";
						for(String s : ht.keySet()) {
							sendData += s+",";
						}
						broadCast(0,sendData);
						broadCast(1,text+"님이 입장하셨습니다.");
						
						break;
					case "1" : 
						if(text.startsWith("/w")) {
							int begin = text.indexOf(" ")+1;
							int end = text.indexOf(" ", begin);					
							if(begin != -1 && end != -1) {
								String nick = text.substring(begin,end);
								String message = text.substring(end);
								PrintWriter pw = ht.get(nick);
								if(pw != null) {
									pw.println("1|"+this.nickName+"님의 귓속말 : "+message);
								}else {
									this.pw.println("1|"+nick+"사용자가 존재하지 않습니다.");
								}
							}
						}else {
							broadCast(1,nickName+" : "+text);
						}
						
						break;
				}
			} catch (Exception e) {
				System.out.println("client 연결 오류 "+e.getMessage());
				isRun = false;
			}
		} // end while
		
		if(client != null && !client.isClosed()) {
			try {
				client.close();
			} catch (IOException e) {}
		}
		
		ht.remove(nickName);
		String list ="";
		for(String s : ht.keySet()) {
			list+= s+",";
		}
		broadCast(0,list);		
		broadCast(1,nickName+"님이 나가셨습니다.");
		
	}// end Run
	public void broadCast(int code, String message) {
		for(PrintWriter p : ht.values()) {
			p.println(code+"|"+message);
		}
	}

}












