package com.gyeongbukSystem.management.mainSystem.dto;

import lombok.Data;

@Data
public class BroadlogVO extends Broadlog{
	String searchBroadlogId;
	String searchLogCollectPnttmIdx;
	String searchLineNum;
	String searchType;
	String searchDevId;
	String searchJId;
	String searchProType;
	String searchBroadSttus;
	String searchBroadType;
	String searchLogTime;	
	String searchLogStr;

	String searchLogType;
	int startNum;
	int endNum;
	
	String bgnde;
	String endde;
	String searchServerNo;
	String searchStationId;
}
