package com.gyeongbukSystem.management.mainSystem.crawling;

import java.util.List;

import com.gyeongbukSystem.management.mainSystem.dto.LogCollectPnttm;
import com.gyeongbukSystem.management.mainSystem.dto.LogCollectPnttmVO;

public interface FileCrawlingService {

	public String FileCrawling(String types) throws Exception;
	public List<LogCollectPnttm> listLogCollectPnttm(LogCollectPnttmVO logCollectPnttmVO) throws Exception;
	public void insertLogCollectPnttm(LogCollectPnttmVO logCollectPnttmVO) throws Exception;
	
	public LogCollectPnttm selectLastLogCollectPnttm(LogCollectPnttmVO logCollectPnttmVO) throws Exception;
	public String selectLastUpdateTime();
	
	public LogCollectPnttm selectLogCollectPnttm(LogCollectPnttmVO logCollectPnttmVO) throws Exception;
}
