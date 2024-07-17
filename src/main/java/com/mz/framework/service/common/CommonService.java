package com.mz.framework.service.common;

import java.util.Map;

/**
 * 公用服务接口接口
 */
public interface CommonService {

    Map<String, Object> getOpenUserAccessToken(String clientId);

    Map<String, Object> getAccessToken();

    Map<String, Object> getAccessToken(String appId, String appKey);
}
