package com.gyeongbukSystem.management.mainSystem.crawling;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gyeongbukSystem.management.mainSystem.dto.LogCollectPnttm;
import com.gyeongbukSystem.management.mainSystem.dto.LogCollectPnttmVO;

/**
 * description    : 파일크롤링 DAO
 * packageName    : com.gyeongbukSystem.management.mainSystem.crawling
 * fileName       : FileCrawlingDAO
 * author         : 김흥욱
 * date           : 2022-10-17
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-10-17        김흥욱            	최초 생성
 */

@Mapper
public interface FileCrawlingDAO {
	public int insertLogCollectPnttm(LogCollectPnttmVO logCollectPnttm);
	public void updateLogCollectPnttm(LogCollectPnttmVO logCollectPnttm);
	public List<LogCollectPnttm> listLogCollectPnttm(LogCollectPnttmVO logCollectPnttm);
	public LogCollectPnttm selectLogCollectPnttm(LogCollectPnttmVO logCollectPnttm);
	public void deleteLogCollectPnttm(LogCollectPnttmVO logCollectPnttm);
	public LogCollectPnttm selectLastLogCollectPnttm(LogCollectPnttmVO logCollectPnttm);
	public String selectLastUpdateTime();
}
