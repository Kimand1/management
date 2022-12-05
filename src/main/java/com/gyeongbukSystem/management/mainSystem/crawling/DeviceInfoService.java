package com.gyeongbukSystem.management.mainSystem.crawling;

import java.util.List;

import com.gyeongbukSystem.management.mainSystem.dto.DeviceInfo;
import com.gyeongbukSystem.management.mainSystem.dto.DeviceInfoVO;

public interface DeviceInfoService {
	public List<DeviceInfo> listDeviceInfo(DeviceInfoVO deviceInfoVO);
	public DeviceInfo selectDeviceInfo(DeviceInfoVO deviceInfoVO);
}
