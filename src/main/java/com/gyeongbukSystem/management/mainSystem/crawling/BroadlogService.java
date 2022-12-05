package com.gyeongbukSystem.management.mainSystem.crawling;

import java.util.List;

import com.gyeongbukSystem.management.mainSystem.dto.Broadlog;
import com.gyeongbukSystem.management.mainSystem.dto.BroadlogVO;

public interface BroadlogService {
	public void insertBroadlog(BroadlogVO broadlogVO) throws Exception;
	public void updateBroadlog(BroadlogVO broadlogVO) throws Exception;
	public List<Broadlog> listBroadlog(BroadlogVO broadlogVO) throws Exception;
	public Broadlog selectBroadlog(BroadlogVO broadlogVO) throws Exception;
	public int selectLastLineNum(BroadlogVO broadlogVO) throws Exception;
	public void deleteBroadlog(BroadlogVO broadlogVO) throws Exception;
	public List<Broadlog> listBroadlogDevSttus() throws Exception;
	public int listBroadlogCnt(BroadlogVO broadlogVO) throws Exception;
}
