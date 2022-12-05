package com.gyeongbukSystem.management.mainSystem.dto;

import lombok.Data;

/**
 * description    : 로그수집일시 클래스
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
public class LogCollectPnttm {
	int idx;
	String logFileName;
	String deleteAt;
	String frstRegisterId;
	String frstRegisterPnttm;
	String lastUpdusrId;
	String lastUpdusrPnttm;	
	int lineCnt;
	int serverNo;
}
