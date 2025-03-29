package com.alex.user.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "action-service")
public interface ActionClient {

}
