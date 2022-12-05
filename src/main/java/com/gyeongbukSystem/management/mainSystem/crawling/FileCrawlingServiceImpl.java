package com.gyeongbukSystem.management.mainSystem.crawling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gyeongbukSystem.management.mainSystem.dto.BroadlogVO;
import com.gyeongbukSystem.management.mainSystem.dto.DeviceInfo;
import com.gyeongbukSystem.management.mainSystem.dto.DeviceInfoVO;
import com.gyeongbukSystem.management.mainSystem.dto.LogCollectPnttm;
import com.gyeongbukSystem.management.mainSystem.dto.LogCollectPnttmVO;
import com.gyeongbukSystem.management.mainSystem.util.DateUtil;
import com.zaxxer.hikari.util.SuspendResumeLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.java.Log;


/**
 * description    : 파일크롤링 서비스 임플
 * packageName    : com.gyeongbukSystem.management.mainSystem.crawling
 * fileName       : FileCrawlingServiceImpl
 * author         : 김흥욱
 * date           : 2022-10-17
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-10-17        김흥욱            	최초 생성
 */


@Service
public class FileCrawlingServiceImpl implements FileCrawlingService {

	protected final static Logger logger = LoggerFactory.getLogger(FileCrawlingService.class);
	
	
	@Value("${repository.logfile.dir1}") 
	private String logdir1;
		
	@Value("${admin1.server.ip}")
	private String admin1Ip;
	
	@Value("${admin2.server.ip}")
	private String admin2Ip;
	
	
	@Autowired
	private FileCrawlingDAO fileCrawlingDAO;	
	
	@Autowired
	private BroadlogDAO broadlogDAO;
	
	@Autowired
	private DeviceInfoServiceImpl deviceInfoServiceImpl;
	
	@Override
	public String FileCrawling(String types) throws Exception {
		// TODO Auto-generated method stub
		
		getFiles(logdir1, types, 1);
		//getFiles(logdir2, types, 2);
		//getFiles(logdir3, types, 3);
		return null;
	}

	@Override
	public List<LogCollectPnttm> listLogCollectPnttm(LogCollectPnttmVO logCollectPnttmVO) throws Exception {
		// TODO Auto-generated method stub
		List<LogCollectPnttm> lists = fileCrawlingDAO.listLogCollectPnttm(logCollectPnttmVO);
		return lists;
	}

	@Override
	public void insertLogCollectPnttm(LogCollectPnttmVO logCollectPnttmVO) throws Exception {
		// TODO Auto-generated method stub
		logCollectPnttmVO.setFrstRegisterPnttm(DateUtil.getNowDateTime("yyyyMMddHHmmss"));		
		fileCrawlingDAO.insertLogCollectPnttm(logCollectPnttmVO);
	}

	@Override
	public LogCollectPnttm selectLastLogCollectPnttm(LogCollectPnttmVO logCollectPnttmVO) throws Exception {
		// TODO Auto-generated method stub
		return fileCrawlingDAO.selectLastLogCollectPnttm(logCollectPnttmVO);
	}

	@Override
	public String selectLastUpdateTime() {
		// TODO Auto-generated method stub
		return fileCrawlingDAO.selectLastUpdateTime();
	}
	
	public void getFiles(String logdir, String types, int serverNo) throws UnknownHostException {
		
		String serverIp = InetAddress.getLocalHost().getHostAddress();
		try {
			if(serverIp.indexOf(admin1Ip)>-1) {
				serverNo = 2;
			}else if(serverIp.indexOf(admin2Ip)>-1) {
				serverNo = 3;
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		
		DeviceInfoVO deviceInfoVO = new DeviceInfoVO();
		List<DeviceInfo> devList = deviceInfoServiceImpl.listDeviceInfo(deviceInfoVO);
		
		File dir = new File(logdir);
		File files[] = dir.listFiles();
		logger.info("*********************************************************************************************");
		logger.info("**********************************크론 도는중*"+logdir+"************************************"+sdf1.format(now));
		logger.info("*********************************************************************************************");
		
		LogCollectPnttmVO logCollectPnttmVO = new LogCollectPnttmVO();
		logCollectPnttmVO.setSearchServerNo(Integer.toString(serverNo));
		logCollectPnttmVO.setServerNo(serverNo);
		LogCollectPnttm lcp = fileCrawlingDAO.selectLastLogCollectPnttm(logCollectPnttmVO);
		String lastLogFile = "";
		String dbLogFileName = "";
		int lineCnt = 0;
		int lastIdx = 0;
		if(lcp == null) {
			lastLogFile = "20000101";
		}else {
			lastLogFile = lcp.getLogFileName();
			dbLogFileName = lcp.getLogFileName();
			lastLogFile = lastLogFile.substring(0,10).replaceAll("-", "").replaceAll(" ", "").replace("[", "").replace("]", "");
			lineCnt = lcp.getLineCnt();
			lastIdx = lcp.getIdx();
		}
		
		try {
			int fl = files.length;
		}catch (Exception e) {
			logger.info("*********************************************************************************************");
			logger.info("**********************************파일읽어오기 애러"+e.toString()+"******************************************");
			logger.info("*********************************************************************************************");
			e.printStackTrace();
			return;
		}
		
		for(int i=0;i<files.length;i++) {
			File file = files[i];
			String filename = file.getName();
			String realfilename[] = filename.split("]");
			String logdate = "";
			try {
				logdate = filename.substring(0, 9);
				logdate = logdate.replace("[", "");
				logdate = logdate.replace("]", "");
			}catch (Exception e) {
				logger.info("["+(i+1)+"번째 파일]date 리플레이스 Error : "+filename+"=>"+filename);
				continue;
			}
			if(Integer.parseInt(lastLogFile)>Integer.parseInt(logdate)) {
				continue;
			}
			
			if(realfilename[1].indexOf("Vbs")>-1) {
				LogCollectPnttmVO lvo = new LogCollectPnttmVO();
				lvo.setSearchLogFileName(filename);
				LogCollectPnttm lcp2 = fileCrawlingDAO.selectLastLogCollectPnttm(lvo);
				if(lcp2 != null) {
					lastIdx = lcp2.getIdx();
					dbLogFileName = lcp2.getLogFileName();
					lineCnt = lcp2.getLineCnt();
					lastIdx = lcp2.getIdx();
					logger.info("파일명 : "+filename);
				}else {
					logCollectPnttmVO.setLogFileName(filename);
					logCollectPnttmVO.setFrstRegisterPnttm(DateUtil.getNowDateTime("yyyyMMddHHmmss"));
					logCollectPnttmVO.setFrstRegisterId(types);
					logCollectPnttmVO.setLineCnt(0);
					fileCrawlingDAO.insertLogCollectPnttm(logCollectPnttmVO);
					lastIdx = logCollectPnttmVO.getIdx();
				}
				
				
				int line = 0;		
				try {
					FileInputStream input = new FileInputStream(file);
					InputStreamReader read = new InputStreamReader(input, "EUC-KR");
					BufferedReader reader = new BufferedReader(read);
					String str;			
					while((str = reader.readLine()) != null) {
						line++;
						if(filename.equals(dbLogFileName)) {
							logger.info("[ 파일명 있는지 체크 통과 ] : "+line + ", "+lineCnt);
							if(line <= lineCnt) {
								continue;								
							}
						}
						
						String strArr[] = str.split(",");
						
						String time = ""; 	//로깅 시간
						String logType = "";//로그 종류
						String devId = ""; 	//디바이스 아이디
						String jId = "";	//접수대 아이디
						String proType = "";//작성프로그램
						String broadSttus = "";//방송 상태
						String broadType = "";//방송 종류
						try {
							time = strArr[0];
							time = time.replace("[", "");
							time = time.replace("]", "");
							time = time.replace(" ", "");
							time = logdate+" "+time;
						}catch (Exception e) {
							logger.info("["+line+"]time 리플레이스 Error : "+filename+"=>"+str);
							continue;	
						}			
						if(strArr.length<3) {
							continue;
						}
						
						logType = strArr[1];
						devId = strArr[2];						
						
						if("1".equals(logType)) {	//디바이스 상태 확인
							broadSttus = strArr[3];
							BroadlogVO broadlogVO = new BroadlogVO();
							broadlogVO.setLogCollectPnttmIdx(lastIdx); //파일테이블 인덱스
							broadlogVO.setLineNum(line);	//라인 번호
							broadlogVO.setLogType(logType);	//로그 종류
							broadlogVO.setDevId(devId);		//디바이스 아이디
							broadlogVO.setBroadSttus(broadSttus);	//디바이스상태(1:연결, 2:끊어짐)
							broadlogVO.setLogTime(time);	//로그상 시간
							broadlogVO.setLogStr(str);		//로그 내용
							broadlogDAO.insertBroadlog(broadlogVO);
						}else if("2".equals(logType)) {	//프로그램 기록							
							devId = strArr[3];
							proType = strArr[2];							
							//broadType = strArr[5];
							broadSttus = strArr[4];
							if("1".equals(proType)) {
								proType = "M";
							}else if("2".equals(proType)) {
								proType = "G";
							}else if("3".equals(proType)) {
								proType = "R";
							}else if("4".equals(proType)) {
								proType = "C";
							}
							BroadlogVO broadlogVO = new BroadlogVO();
							broadlogVO.setLogCollectPnttmIdx(lastIdx); 	//파일테이블 인덱스
							broadlogVO.setLineNum(line);				//라인 번호
							broadlogVO.setLogType(logType);				//로그 종류
							broadlogVO.setDevId(devId);					//오브젝트 아이디
							broadlogVO.setProType(proType);				//프로그램 코드 G:게이트웨이,M:매니저,R:레코더,C:콘솔
							broadlogVO.setBroadSttus(broadSttus);		//디바이스 상태 1:연결,2:연결끊어짐
							broadlogVO.setLogTime(time);	//로그상 시간
							broadlogVO.setLogStr(str);		//로그 원문
							broadlogDAO.insertBroadlog(broadlogVO);
						}else if("3".equals(logType)) {	//방송정보 기록
							jId = strArr[3];
							proType = strArr[4];
							broadSttus = strArr[5];
							broadType = strArr[6];
							
							if("R".equals(proType)) {
								boolean chks=true;
								for(DeviceInfo dev:devList) {
									if(dev.getStationId().equals(devId)) {
										devId = dev.getDevId();
										chks = false;
										break;
									}
								}
								if(chks) {
									devId = "[S]"+devId;
								}
							}
							
							BroadlogVO broadlogVO = new BroadlogVO();
							broadlogVO.setLogCollectPnttmIdx(lastIdx);
							broadlogVO.setLineNum(line);
							broadlogVO.setLogType(logType);
							broadlogVO.setDevId(devId);
							broadlogVO.setJId(jId);
							broadlogVO.setProType(proType);
							broadlogVO.setBroadSttus(broadSttus);
							broadlogVO.setBroadType(broadType);
							broadlogVO.setLogTime(time);
							broadlogVO.setLogStr(str);
							broadlogDAO.insertBroadlog(broadlogVO);
						}						
					}
					reader.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
				logCollectPnttmVO.setIdx(lastIdx);				
				logCollectPnttmVO.setLogFileName(filename);
				logCollectPnttmVO.setLineCnt(line);
				logCollectPnttmVO.setLastUpdusrId(types);
				logCollectPnttmVO.setLastUpdusrPnttm(DateUtil.getNowDateTime("yyyyMMddHHmmss"));
				fileCrawlingDAO.updateLogCollectPnttm(logCollectPnttmVO);
			}
		}
	}

	@Override
	public LogCollectPnttm selectLogCollectPnttm(LogCollectPnttmVO logCollectPnttmVO) throws Exception {
		return fileCrawlingDAO.selectLogCollectPnttm(logCollectPnttmVO) ;
	}

}
