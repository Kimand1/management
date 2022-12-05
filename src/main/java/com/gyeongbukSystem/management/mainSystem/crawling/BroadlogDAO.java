package com.gyeongbukSystem.management.mainSystem.crawling;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gyeongbukSystem.management.mainSystem.dto.Broadlog;
import com.gyeongbukSystem.management.mainSystem.dto.BroadlogVO;

/**
 * description    : 방송정보로그 DAO
 * packageName    : com.gyeongbukSystem.management.mainSystem.crawling
 * fileName       : BroadlogDAO
 * author         : 김흥욱
 * date           : 2022-10-17
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-10-17        김흥욱            	최초 생성
 */

@Mapper
public interface BroadlogDAO {
	public void insertBroadlog(BroadlogVO broadlogVO);
	public void updateBroadlog(BroadlogVO broadlogVO);
	public List<Broadlog> listBroadlog(BroadlogVO broadlogVO);
	public Broadlog selectBroadlog(BroadlogVO broadlogVO);
	public int selectLastLineNum(BroadlogVO broadlogVO);
	public void deleteBroadlog(BroadlogVO broadlogVO);
	public List<Broadlog> listBroadlogDevSttus();
	public int listBroadlogCnt(BroadlogVO broadlogVO);
}
