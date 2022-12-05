package com.gyeongbukSystem.management.mainSystem.dto;

import lombok.Data;

@Data
public class Broadlog {
	int broadlogIdx;
	int logCollectPnttmIdx;
	int lineNum;
	String logType;
	String devId;
	String jId;
	String proType;
	String broadSttus;
	String broadType;
	String logTime;
	String logStr;
	String serverNo;
}
