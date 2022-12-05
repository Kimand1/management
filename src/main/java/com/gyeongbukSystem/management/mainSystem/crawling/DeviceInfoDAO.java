package com.gyeongbukSystem.management.mainSystem.crawling;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gyeongbukSystem.management.mainSystem.dto.DeviceInfo;
import com.gyeongbukSystem.management.mainSystem.dto.DeviceInfoVO;

@Mapper
public interface DeviceInfoDAO {
	public List<DeviceInfo> listDeviceInfo(DeviceInfoVO deviceInfoVO);
	public DeviceInfo selectDeviceInfo(DeviceInfoVO deviceInfoVO);
}
