package com.gyeongbukSystem.management.mainSystem.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gyeongbukSystem.management.mainSystem.crawling.BroadlogService;
import com.gyeongbukSystem.management.mainSystem.crawling.FileCrawlingService;
import com.gyeongbukSystem.management.mainSystem.crawling.PCMonitor;
import com.gyeongbukSystem.management.mainSystem.dto.Broadlog;
import com.gyeongbukSystem.management.mainSystem.dto.BroadlogVO;
import com.gyeongbukSystem.management.mainSystem.dto.LogCollectPnttm;
import com.gyeongbukSystem.management.mainSystem.dto.LogCollectPnttmVO;
import com.gyeongbukSystem.management.mainSystem.dto.ServerResource;

@RestController
public class RestAPIController {
	
	@Autowired
	PCMonitor pCMonitor;
	
	@Autowired
	BroadlogService broadlogService;
	
	@Autowired
	FileCrawlingService fileCrawlingService;
	
	@Value("${admin1.server.ip}")
	private String admin1Ip;
	
	@Value("${admin1.server.port}")
	private String admin1Port;
	
	@Value("${admin2.server.ip}")
	private String admin2Ip;
	
	@Value("${admin2.server.port}")
	private String admin2Port;
	
	@Value("${admin.Id}")
	private String[] adminId;
	
	@Value("${admin.pass}")
	private String[] adminPass;
	
	@RequestMapping(value="/api/pcMonitor")
	public ResponseEntity<?> pcMonitorData() {
		ServerResource sr = pCMonitor.getResourceData();
		if(sr == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(sr);
		}		
		return ResponseEntity.ok(sr);
	}
	
	@RequestMapping(value="/api/pcMonitor1")
	public String pcMonitorData1() {
		String result = "";
		try {
			CloseableHttpClient client;
			client = HttpClientBuilder.create().build();
			String url = "http://"+admin1Ip+":"+admin1Port+"/api/pcMonitor";
			HttpGet get = new HttpGet(url);
			ResponseHandler<String> rh = new BasicResponseHandler();
			result = client.execute(get, rh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value="/api/pcMonitor2")
	public String pcMonitorData2() {
		String result = "";
		try {
			CloseableHttpClient client;
			client = HttpClientBuilder.create().build();
			String url = "http://"+admin2Ip+":"+admin2Port+"/api/pcMonitor";
			HttpGet get = new HttpGet(url);
			ResponseHandler<String> rh = new BasicResponseHandler();
			result = client.execute(get, rh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value="/api/lastLogData")
	public ResponseEntity<?> lastLogData() throws Exception{
		BroadlogVO broadlogVO = new BroadlogVO();
		broadlogVO.setStartNum(0);
		broadlogVO.setEndNum(10);
		List<Broadlog> lists = broadlogService.listBroadlog(broadlogVO);
		if(lists == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(lists);
		}
		return ResponseEntity.ok(lists);
	}
	
	@RequestMapping(value="/api/lastLogTime")
	public ResponseEntity<?> lastLogTime() throws Exception{
		String lastLogTime = fileCrawlingService.selectLastUpdateTime();
		if(lastLogTime==null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(lastLogTime);
		}
		return ResponseEntity.ok(lastLogTime);
	}
	
	@GetMapping("/reUpdate.do")
	public void iframePage(@ModelAttribute("logCollectPnttmVO") LogCollectPnttmVO logCollectPnttmVO
			, HttpServletRequest request
			, ModelMap model) {
		String result;
		try {
			CloseableHttpClient client;
			client = HttpClientBuilder.create().build();
			String url = "http://localhost:8080/reUpdateThis.do";
			HttpGet get = new HttpGet(url);
			ResponseHandler<String> rh = new BasicResponseHandler();
			result = client.execute(get, rh);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			CloseableHttpClient client;
			client = HttpClientBuilder.create().build();
			String url = "http://"+admin1Ip+":"+admin1Port+"/reUpdateThis.do";
			HttpGet get = new HttpGet(url);
			ResponseHandler<String> rh = new BasicResponseHandler();
			result = client.execute(get, rh);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			CloseableHttpClient client;
			client = HttpClientBuilder.create().build();
			String url = "http://"+admin2Ip+":"+admin2Port+"/reUpdateThis.do";
			HttpGet get = new HttpGet(url);
			ResponseHandler<String> rh = new BasicResponseHandler();
			result = client.execute(get, rh);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	@GetMapping("/reUpdateThis.do")
	public void reUpdateThis(@ModelAttribute("logCollectPnttmVO") LogCollectPnttmVO logCollectPnttmVO
			, HttpServletRequest request
			, ModelMap model) {
		try {
			fileCrawlingService.FileCrawling("urlSet");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	@RequestMapping("/loginAction.do")
	public boolean loginAction(HttpServletRequest request) throws Exception{
		boolean result = false;
		String getId = request.getParameter("adminId");
		String getPass = request.getParameter("adminPass");
		for(int i=0;i<adminId.length;i++) {
			if(adminId[i].equals(getId)) {
				if(adminPass[i].equals(getPass)) {
					HttpSession session = request.getSession();
					session.setAttribute("adminId", getId);
					result = true;
					break;
				}
			}
		}
		
		
		return result;
	}

	@RequestMapping("logout.do")
	public boolean logout(HttpServletRequest request)	{		
		HttpSession session = request.getSession();
		session.invalidate();
		return true;
	}
	
	@RequestMapping("/nowMaster.do")
	public String getMasterServer(HttpServletRequest request) {
		String result = "";
		BroadlogVO broadlogVO = new BroadlogVO();
		broadlogVO.setSearchLogType("2");
		broadlogVO.setSearchProType("M");
		broadlogVO.setSearchBroadSttus("1");
		broadlogVO.setStartNum(0);
		broadlogVO.setEndNum(1);
		try {
			int idx = 0;
			List<Broadlog> lists = broadlogService.listBroadlog(broadlogVO);
			for(Broadlog bl : lists) {
				idx = bl.getLogCollectPnttmIdx();
			}
			LogCollectPnttmVO logCollectPnttmVO = new LogCollectPnttmVO();
			logCollectPnttmVO.setIdx(idx);
			LogCollectPnttm lp = fileCrawlingService.selectLogCollectPnttm(logCollectPnttmVO);
			result = Integer.toString(lp.getServerNo());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpSession session = request.getSession();
		String stime = (String)session.getAttribute("stime");
		int st = 0;
		if(stime!=null) {
			st = Integer.parseInt(stime);
		}
		st++;
		session.setAttribute("stime", Integer.toString(st));
		
		return result;
	}
}
