package com.gyeongbukSystem.management.mainSystem.dto;

import lombok.Data;

/**
 * description    : 로그수집일시 VO
 * packageName    : com.gyeongbukSystem.management.mainSystem.dto
 * fileName       : LogCollectPnttm
 * author         : 김흥욱
 * date           : 2022-10-17
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-10-17        김흥욱            	최초 생성
 */

@Data
public class LogCollectPnttmVO extends LogCollectPnttm{
	String bgnde;
	String endde;
	String searchLogFileName;
	String searchDeleteAt;
	
	String searchIdx;
	String searchFrstRegisterId;
	String searchFrstRegisterPnttm;
	String searchLastUpdusrId;
	String searchLastUpdusrPnttm;		
	String searchServerNo;
}
