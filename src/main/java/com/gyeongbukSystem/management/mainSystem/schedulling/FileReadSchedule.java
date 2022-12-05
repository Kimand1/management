package com.gyeongbukSystem.management.mainSystem.schedulling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gyeongbukSystem.management.mainSystem.crawling.FileCrawlingService;

/**
 * description    : 파일 크롤링 스케롤러
 * packageName    : com.gyeongbukSystem.management.mainSystem.schedulling
 * fileName       : FileReadSchedule
 * author         : 김흥욱
 * date           : 2022-09-27
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-09-27        김흥욱            	최초 생성
 */
@Component
public class FileReadSchedule {
	
	@Autowired
	FileCrawlingService fileCrawlingService;
	
	@Scheduled(cron = "0 0 * * * *") //초 분 시간 일 월 요일 (매분 실행) 1시간 단위 예정
	public void logFileRead() throws InterruptedException{		
		try {
			fileCrawlingService.FileCrawling("cron");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
