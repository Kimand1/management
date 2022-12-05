package com.gyeongbukSystem.management.mainSystem.controller;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gyeongbukSystem.management.mainSystem.crawling.BroadlogService;
import com.gyeongbukSystem.management.mainSystem.crawling.DeviceInfoService;
import com.gyeongbukSystem.management.mainSystem.crawling.FileCrawlingService;
import com.gyeongbukSystem.management.mainSystem.crawling.FireDepartmentService;
import com.gyeongbukSystem.management.mainSystem.dto.Broadlog;
import com.gyeongbukSystem.management.mainSystem.dto.BroadlogVO;
import com.gyeongbukSystem.management.mainSystem.dto.DeviceInfo;
import com.gyeongbukSystem.management.mainSystem.dto.DeviceInfoVO;
import com.gyeongbukSystem.management.mainSystem.dto.FireDepartment;
import com.gyeongbukSystem.management.mainSystem.dto.FireDepartmentVO;
import com.gyeongbukSystem.management.mainSystem.dto.LogCollectPnttm;
import com.gyeongbukSystem.management.mainSystem.dto.LogCollectPnttmVO;
import com.gyeongbukSystem.management.mainSystem.dto.ResponseJSON;

/**
 * description    : 메인 컨트롤러
 * packageName    : com.gyeongbukSystem.management.mainSystem.controller
 * fileName       : MainController
 * author         : 김흥욱
 * date           : 2022-09-27
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-09-27        김흥욱            	최초 생성
 */

@Controller
public class MainController {
	
	@Autowired
	FileCrawlingService fileCrawlingService;
	
	@Autowired
	BroadlogService broadlogService;
	
	@Autowired
	DeviceInfoService deviceInfoService;
	
	@Autowired
	FireDepartmentService fireDepartmentService;
	
	@RequestMapping("login.do")
	public String loginPage(HttpServletRequest request)	{		
		return "login.jsp";
	}
	
	@RequestMapping("loginOk.do")
	public String loginOk(HttpServletRequest request){
		HttpSession session = request.getSession();
		if(session.getAttribute("returnUrl")!=null) {
			return "redirect:"+(String)session.getAttribute("returnUrl");
		}
			
		return "redirect:/";
	}
		
	@RequestMapping("/")
	public String indexPage(@ModelAttribute("logCollectPnttmVO") LogCollectPnttmVO logCollectPnttmVO
			, HttpServletRequest request
			, ModelMap model) throws Exception {
		
		HttpSession session = request.getSession();
		/*if(session==null){
			session.setAttribute("returnUrl", "/");
			return "redirect:login.do";
		}else */if(session.getAttribute("adminId")==null) {
			session.setAttribute("returnUrl", "/");
			return "redirect:login.do";
		}
		
		List<LogCollectPnttm> lists = fileCrawlingService.listLogCollectPnttm(logCollectPnttmVO);
		model.addAttribute("logCollectPnttmLists", lists);
		
		
		/** 최신 디바이스 상태 리스트 **/
		List<Broadlog> listBroadlogDevSttus = broadlogService.listBroadlogDevSttus();
		model.addAttribute("listBroadlogDevSttus", listBroadlogDevSttus);
				
		return "index.jsp";
	}
	
	@RequestMapping("/miniDevLogTable.do")
	public String lastDevLogData(HttpServletRequest request
			, ModelMap model) throws Exception{
		BroadlogVO broadlogVO = new BroadlogVO();
		broadlogVO.setSearchLogType("1");
		broadlogVO.setStartNum(0);
		broadlogVO.setEndNum(10);
		List<Broadlog> lists = broadlogService.listBroadlog(broadlogVO);
		model.addAttribute("lists", lists);
		return "miniDevLog.jsp";
	}
	
	@RequestMapping("/miniProgLogTable.do")
	public String lastProgLogData(HttpServletRequest request
			, ModelMap model) throws Exception{
		BroadlogVO broadlogVO = new BroadlogVO();
		broadlogVO.setSearchLogType("2");
		broadlogVO.setStartNum(0);
		broadlogVO.setEndNum(10);
		List<Broadlog> lists = broadlogService.listBroadlog(broadlogVO);
		model.addAttribute("lists", lists);
		return "miniProgLog.jsp";
	}
	
	@RequestMapping("/miniBroadLogTable.do")
	public String lastBroadLogData(HttpServletRequest request
			, ModelMap model) throws Exception{
		BroadlogVO broadlogVO = new BroadlogVO();
		broadlogVO.setSearchLogType("3");
		broadlogVO.setStartNum(0);
		broadlogVO.setEndNum(10);
		List<Broadlog> lists = broadlogService.listBroadlog(broadlogVO);
		model.addAttribute("lists", lists);
		return "miniBroadLog.jsp";
	}
	
	@RequestMapping("/dashboardDevSttus.do")
	public String dashboardDevSttus(HttpServletRequest request
			, ModelMap model) throws Exception {
		
		/** 소방본부 리스트 **/
		FireDepartmentVO fireDepartmentVO = new FireDepartmentVO();
		List<FireDepartment> fireList = fireDepartmentService.listFireDepartment(fireDepartmentVO);
		model.addAttribute("fireList", fireList);
		
		/** 디바이스 리스트 **/
		DeviceInfoVO deviceInfoVO = new DeviceInfoVO();
		List<DeviceInfo> devList = deviceInfoService.listDeviceInfo(deviceInfoVO);
		model.addAttribute("devList", devList);
		
		/** 로그 리스트 **/
		List<Broadlog> lists = broadlogService.listBroadlogDevSttus();
		model.addAttribute("lists", lists);
		return "dashboardDevSttus.jsp";
	}
	
	@RequestMapping("/dashboardDevSttusIn.do")
	public String dashboardDevSttusIn(HttpServletRequest request
			, ModelMap model) throws Exception {
		
		/** 소방본부 리스트 **/
		FireDepartmentVO fireDepartmentVO = new FireDepartmentVO();
		List<FireDepartment> fireList = fireDepartmentService.listFireDepartment(fireDepartmentVO);
		model.addAttribute("fireList", fireList);
		
		/** 디바이스 리스트 **/
		DeviceInfoVO deviceInfoVO = new DeviceInfoVO();
		List<DeviceInfo> devList = deviceInfoService.listDeviceInfo(deviceInfoVO);
		model.addAttribute("devList", devList);
		
		/** 로그 리스트 **/
		List<Broadlog> lists = broadlogService.listBroadlogDevSttus();
		model.addAttribute("lists", lists);
		
		model.addAttribute("fidx", request.getParameter("fidx"));
		
		return "dashboardDevSttusIn.jsp";
	}
	
	@RequestMapping("/logList.do")
	public String logListjsp(@ModelAttribute("broadlogVO") BroadlogVO broadlogVO
			, HttpServletRequest request, ModelMap model) throws Exception{
		
		String pageNum = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");		
		
		HttpSession session = request.getSession();
		/*if(session==null){
			session.setAttribute("returnUrl", "/logList.do");
			session.setAttribute("broadlogVO", broadlogVO);
			session.setAttribute("pageNum", pageNum);
			session.setAttribute("pageSize", pageSize);
			return "redirect:login.do";
		}else */if(session.getAttribute("adminId")==null) {
			session.setAttribute("returnUrl", "/logList.do");
			session.setAttribute("broadlogVO", broadlogVO);
			session.setAttribute("pageNum", pageNum);
			session.setAttribute("pageSize", pageSize);
			return "redirect:login.do";
		}else if(session.getAttribute("broadlogVO")!=null) {			
			broadlogVO = (BroadlogVO)session.getAttribute("broadlogVO");
			pageNum = (String)session.getAttribute("pageNum");
			pageSize = (String)session.getAttribute("pageSize");
			session.setAttribute("broadlogVO", null);
			session.setAttribute("pageNum", null);
			session.setAttribute("pageSize", null);
		}
				
		if(pageNum == null || "".equals(pageNum)) {
			pageNum = "1";
		}
		model.addAttribute("pageNum", pageNum);
		
		int pageNo = Integer.parseInt(pageNum);
		if(pageSize == null || "".equals(pageSize)) {
			pageSize = "10";
		}
		model.addAttribute("pageSize", pageSize);
		
		int pageSizeNo = Integer.parseInt(pageSize);
		
		int startNum = (pageNo - 1) * pageSizeNo;
		int endNum = pageSizeNo;
		
		
		broadlogVO.setStartNum(startNum);
		broadlogVO.setEndNum(endNum);
		
		
		List<Broadlog> lists = broadlogService.listBroadlog(broadlogVO);
		model.addAttribute("lists", lists);
		
		int cnt = broadlogService.listBroadlogCnt(broadlogVO); 
		model.addAttribute("totalCnt", cnt);
		
		//페이징 처리
		int pageCount = cnt / pageSizeNo + (cnt%pageSizeNo==0?0:1);
		int pageBlock = 10;
		
		// 한 페이지에 보여줄 페이지 블럭 시작번호 계산
		int startPage = ((pageNo-1)/pageBlock)*pageBlock+1;
		
		// 한 페이지에 보여줄 페이지 블럭 끝 번호 계산
		int endPage = startPage + pageBlock-1;
		if(endPage > pageCount){
			endPage = pageCount;
		}	
		
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("pageBlock", pageBlock);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		
		DeviceInfoVO deviceInfoVO = new DeviceInfoVO();
		List<DeviceInfo> devList = deviceInfoService.listDeviceInfo(deviceInfoVO);
		model.addAttribute("devList", devList);
		
		return "logList.jsp";
	}

	@GetMapping("/iframe.do")
	public String iframePage(@ModelAttribute("logCollectPnttmVO") LogCollectPnttmVO logCollectPnttmVO
			, HttpServletRequest request
			, ModelMap model) {
		try {
			logCollectPnttmVO.setFrstRegisterId("urlSet");
			fileCrawlingService.insertLogCollectPnttm(logCollectPnttmVO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "iframe.html";
	}
	@GetMapping("/iframedark.do")
	public String iframeDarkPage() {
		return "iframe-dark.html";
	}
	@GetMapping("/index2.do")
	public String index2Page() {
		return "index2.html";
	}
	@GetMapping("/index3.do")
	public String index3Page() {
		return "index3.html";
	}
	
	/**
	 * 로그파일 크롤링 테스트
	 * @return
	 */
	@GetMapping("/texttest.do")
	public String texttest(HttpServletRequest request) {
		try {
			//HttpSession session = request.getSession();
			String ip = request.getRemoteAddr();
			fileCrawlingService.FileCrawling(ip);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return "index3.html";
	}
	
	/**
	 * 소켓보내기 테스트 페이지
	 * @return
	 */
	@GetMapping("/jcuTest.do")
	public String jcuTest() {
		return "jctTest.jsp";
	}
	
	/**
	 * 소켓보내기 테스트 (json방식)
	 * @param request
	 * @return
	 */
	@ResponseBody
	@PostMapping("/apiTest.do")
	public ResponseJSON apiTest(HttpServletRequest request) {
		ResponseJSON rs = new ResponseJSON();
		rs.setResult(0);
		
		String addr = "127.0.0.1";		
		//String addr = "192.168.244.81";
		String myIp = request.getParameter("myIp");
		if(myIp != null) {
			addr = myIp;
		}
		int port = 10001;
				
		String a = request.getParameter("a");
		String b = request.getParameter("b");
		String c = request.getParameter("c");
		String d = request.getParameter("d");
		String e = request.getParameter("e");
		
		//{, CMD, (byte)0xEE, (byte)0x0D, (byte)0x0A};
		byte A = (byte)0x01;
		byte B = 0x10;
		if(b.equals("0x10")) {
			B = (byte)0x10;
		}else if(b.equals("0x20")) {
			B = (byte)0x20;
		}else if(b.equals("0x30")) {
			B = (byte)0x30;
		}else if(b.equals("0x40")) {
			B = (byte)0x40;
		}else if(b.equals("0xA0")) {
			B = (byte)0xA0;
		}else if(b.equals("0xB0")) {
			B = (byte)0xB0;
		}else if(b.equals("0x50")) {
			B = (byte)0x50;
		}else if(b.equals("0xC0")) {
			B = (byte)0xC0;
		}
		byte C;
		if(c.equals("0xEE")) {
			C = (byte)0xEE;
		}else {
			C = (byte)0x02;
		}
		byte D = (byte)0x0D;
		byte E = (byte)0x0A;
		if(e.equals("0x10")) {
			E = (byte)0x10;
		}else if(e.equals("0x20")) {
			E = (byte)0x20;
		}else if(e.equals("0x30")) {
			E = (byte)0x30;
		}else if(e.equals("0x40")) {
			E = (byte)0x40;
		}else if(e.equals("0xA0")) {
			E = (byte)0xA0;
		}else if(e.equals("0xB0")) {
			E = (byte)0xB0;
		}else if(e.equals("0x50")) {
			E = (byte)0x50;
		}else if(e.equals("0xC0")) {
			E = (byte)0xC0;
		}
				
		Socket socket = new Socket();
		SocketAddress address = new InetSocketAddress(addr, port);
		try {
			socket.connect(address);
			sends(socket, A, B, C, D, E );
			System.out.println("송신성공");
			rs.setResult(1);
		} catch (Exception ex) {
			ex.printStackTrace();
			rs.setMsg(ex.toString());
		}
		try {
			socket.close();
		}catch (Exception ex) {
			// TODO: handle exception
			rs.setMsg(ex.toString());
		}		
		
		
		return rs;
	}
	
	/**
	 * 소켓 보내기 테스트
	 * @return
	 */
	@GetMapping("/socketTest.do")
	public String socketTestPage(HttpServletRequest request) {
		String addr = "127.0.0.1";		
		//String addr = "192.168.244.81";
		String myIp = request.getRemoteAddr();
		
		int port = 10001;
		Socket socket = new Socket();
		SocketAddress address = new InetSocketAddress(addr, port);
		byte CMD = (byte)0x10;
		try {
			socket.connect(address);
			send(socket, CMD);
			System.out.println("송신성공");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return "index3.html";
	}
	
	/**
	 * 소켓 발송 함수
	 * @param socket
	 * @param CMD
	 */
	public static void send(Socket socket, byte CMD) {
		
		byte[] data = {(byte)0x01, CMD, (byte)0xEE, (byte)0x0D, (byte)0x0A};
		try {
			OutputStream os = socket.getOutputStream();
			os.write(data);
			os.flush();
			os.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 소켓 발송 함수2
	 * @param socket
	 * @param CMD
	 */
	public static void sends(Socket socket, byte A, byte B, byte C, byte D, byte E) {
		
		byte[] data = {A, B, C, D, E};
		try {
			OutputStream os = socket.getOutputStream();
			os.write(data);
			os.flush();
			os.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
