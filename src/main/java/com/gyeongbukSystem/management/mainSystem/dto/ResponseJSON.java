package com.gyeongbukSystem.management.mainSystem.dto;

import lombok.Data;

@Data
public class ResponseJSON {

	private int result;					/** 처리 결과 **/
	private int restInt;				/** 처리 결과 값이 있는 경우 (ex : 예약번호, 권한set 번호 등) INT  형 **/
	private String resultStr;		/** 처리 결과 값이 있는 경우 (ex : 예약번호, 권한set 번호 등) String 형 **/
	private String msg;				/** 처리 결과 메시지 **/
	
	private String url;				/** 리턴url **/
}
