package com.gyeongbukSystem.management.mainSystem.crawling;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.gyeongbukSystem.management.mainSystem.dto.ServerResource;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.sun.management.OperatingSystemMXBean;
/**
 * description    : PC정보 모니터링
 * packageName    : com.gyeongbukSystem.management.mainSystem.crawling
 * fileName       : PCMonitor
 * author         : 김흥욱
 * date           : 2022-10-17
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-10-17        김흥욱            	최초 생성
 */
@Service
public class PCMonitor {
	/**
	 * 내 PC 모니터링(윈도우 서버)
	 * @return ServerResource
	 */
	public ServerResource getResourceData() {
		
		
		String cpuPer = "";
		int memPer = 0;
		int diskPer = 0;
		
		try {
			//CPU 사용량 측정
			String responseString = "";
			String cmd = "wmic cpu get loadpercentage";
			Process process = Runtime.getRuntime().exec("cmd /c " + cmd);
			
            
			BufferedReader reader = new BufferedReader(
			        new InputStreamReader(process.getInputStream()));
			String line = null;
			StringBuffer sb = new StringBuffer();
			//sb.append(cmd);
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			    sb.append("\n");
			    Thread.sleep(500);
			}
			responseString = sb.toString();
            responseString = responseString.replace("LoadPercentage", "");
            responseString = responseString.replaceAll("(\r\n|\r|\n|\n\r)", " ");
            responseString = responseString.replaceAll(" ", "");
            cpuPer = responseString;            
            
            //메모리 사용량 측정
            cmd = "systeminfo | find \"실제 메모리\"";
            process = Runtime.getRuntime().exec("cmd /c " + cmd);
            reader = new BufferedReader(
			        new InputStreamReader(process.getInputStream()));
            line = null;
            sb = new StringBuffer();
            //sb.append(cmd);
            while ((line = reader.readLine()) != null) {
			    sb.append(line);
			    sb.append("\r\n");
			    Thread.sleep(490);
			}
            responseString = sb.toString();
            String bline[] = responseString.split("\\r\\n");
            memPer = 0;
            for(int i=0;i<bline.length;i++) {
            	String intStr = bline[i].replaceAll("[^0-9]", "");
            	if(intStr!=null){
            		if(memPer == 0) {
            			memPer = Integer.parseInt(intStr);
            		}else {
            			memPer = 100-((int) Math.round( Double.valueOf(Integer.parseInt(intStr))/ Double.valueOf(memPer) * 100));
            		}
            	}
            }
            
            //디스크 사용량 측정
            cmd = "wmic logicaldisk where drivetype=3 get size,freespace";
            process = Runtime.getRuntime().exec("cmd /c " + cmd);
            reader = new BufferedReader(
			        new InputStreamReader(process.getInputStream()));
            line = null;
            sb = new StringBuffer();
            //sb.append(cmd);
            while ((line = reader.readLine()) != null) {
			    sb.append(line);
			    sb.append("\r\n");
			    Thread.sleep(480);
			}
            responseString = sb.toString();
            responseString = responseString.replaceAll("  ", "_");
            String dline[] = responseString.split("\\r\\n");
            double free=0;
            double all=0;            
            for(int i=0;i<dline.length;i++) {
            	String dline2[] = dline[i].split("_");
            	int chkj=0;
            	for(int ii=0;ii<dline2.length;ii++) {
            		if(dline2[ii].matches(".*[0-9].*")) {
	            		if(chkj%2==0) {
	            			free += Double.parseDouble(dline2[ii]);            			
	            		}else {
	            			all += Double.parseDouble(dline2[ii]);
	            		}
	            		chkj++;
            		}
            	}        
            }
            diskPer = (int) Math.round( Double.valueOf(all-free)/ Double.valueOf(all) * 100);
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	    ServerResource sr = new ServerResource();
	    sr.setPerCpu(cpuPer+"");
	    sr.setPerDsk(diskPer+"");
	    sr.setPerMem(memPer+"");
	    return sr;
	}
	
	
	
	/**
	 * SSH로 다른 PC 모니터링(윈도우 서버)
	 * @param ip
	 * @param port
	 * @param user
	 * @param pass
	 * @return ServerResource
	 * @throws IOException
	 */
	
	public ServerResource getDifData(String ip, int port, String user, String pass) throws IOException {
		
		JSch jsch = new JSch();
		Session session = null;
		Channel channel = null;
		String cpuPer = "";
		int memPer = 0;
		int diskPer = 0;
		
		try {
			session = jsch.getSession(user, ip, port);
			session.setPassword(pass);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			
			//CPU 사용량 측정
			String responseString = "";
			ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
			channel = session.openChannel("exec");
			ChannelExec channelExec = (ChannelExec) channel;
			channelExec.setOutputStream(responseStream);
           
			channelExec.setCommand("wmic cpu get loadpercentage");
            
			//콜백을 받을 준비.
            StringBuilder outputBuffer = new StringBuilder();
            InputStream in = channel.getInputStream();
            ((ChannelExec) channel).setErrStream(System.err);       
            
            channelExec.connect();
            
            byte[] tmp = new byte[1024];
            int a=0;
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    outputBuffer.append(new String(tmp, 0, i));
                    if (i < 0) break;
                }
                if (channel.isClosed()) {
                    responseString = outputBuffer.toString();
                    channel.disconnect();
                    break;
                }
                a++;
                try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            responseString = responseString.replace("LoadPercentage", "");
            responseString = responseString.replaceAll("(\r\n|\r|\n|\n\r)", " ");
            responseString = responseString.replaceAll(" ", "");
            cpuPer = responseString;            
            
            //메모리 사용량 측정
            responseStream = new ByteArrayOutputStream();
			channel = session.openChannel("exec");
			channelExec = (ChannelExec) channel;
			channelExec.setOutputStream(responseStream);
            channelExec.setCommand("systeminfo | find \"실제 메모리\"");
            outputBuffer = new StringBuilder();
            in = channel.getInputStream();
            ((ChannelExec) channel).setErrStream(System.err); 
            channelExec.connect();
            
            tmp = new byte[1024];
            a=0;
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    outputBuffer.append(new String(tmp, 0, i));
                    if (i < 0) break;
                }
                if (channel.isClosed()) {
                    responseString = outputBuffer.toString();
                    channel.disconnect();
                    break;
                }
                a++;
                try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
              
            String bline[] = responseString.split("\\r\\n");
            memPer = 0;
            for(int i=0;i<bline.length;i++) {
            	String intStr = bline[i].replaceAll("[^0-9]", "");
            	if(intStr!=null){
            		if(memPer == 0) {
            			memPer = Integer.parseInt(intStr);
            		}else {
            			memPer = 100-((int) Math.round( Double.valueOf(Integer.parseInt(intStr))/ Double.valueOf(memPer) * 100));
            		}
            	}
            }
            
            //디스크 사용량 측정
            responseStream = new ByteArrayOutputStream();
			channel = session.openChannel("exec");
			channelExec = (ChannelExec) channel;
			channelExec.setOutputStream(responseStream);
            channelExec.setCommand("wmic logicaldisk where drivetype=3 get size,freespace");
            outputBuffer = new StringBuilder();
            in = channel.getInputStream();
            ((ChannelExec) channel).setErrStream(System.err); 
            channelExec.connect();
            
            tmp = new byte[1024];
            a=0;
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    outputBuffer.append(new String(tmp, 0, i));
                    if (i < 0) break;
                }
                if (channel.isClosed()) {
                    responseString = outputBuffer.toString();
                    channel.disconnect();
                    break;
                }
                a++;
                try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
            responseString = responseString.replaceAll("  ", "_");
            String dline[] = responseString.split("\\r\\n");
            double free=0;
            double all=0;            
            for(int i=0;i<dline.length;i++) {
            	String dline2[] = dline[i].split("_");
            	int chkj=0;
            	for(int ii=0;ii<dline2.length;ii++) {
            		if(dline2[ii].matches(".*[0-9].*")) {
	            		if(chkj%2==0) {
	            			free += Double.parseDouble(dline2[ii]);            			
	            		}else {
	            			all += Double.parseDouble(dline2[ii]);
	            		}
	            		chkj++;
            		}
            	}        
            }
            diskPer = (int) Math.round( Double.valueOf(all-free)/ Double.valueOf(all) * 100);
		} catch (JSchException e1) {
			e1.printStackTrace();
		} finally {
			try {channel.disconnect();} catch (Exception e) {}
			try {session.disconnect();} catch (Exception e) {}
		}
	    ServerResource sr = new ServerResource();
	    sr.setPerCpu(cpuPer+"");
	    sr.setPerDsk(diskPer+"");
	    sr.setPerMem(memPer+"");
	    
	    return sr;
	}
}
