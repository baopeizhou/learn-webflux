package org.bob.learn.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.bob.learn.common.constant.RedisKey;
import org.bob.learn.common.util.RedisUtils;
import org.bob.learn.service.TestService;
import org.bob.learn.web.model.Result;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.LongAdder;

@Slf4j
@EnableAsync
@Service
public class TestServiceImpl implements TestService {

    private static volatile LongAdder counter = new LongAdder();

    @Override
    public Result get() {
        Result result = new Result();
        result.setCode("000000");
        result.setMsg("success");
        counter.increment();
        log.info(String.valueOf(counter.longValue()));
        return result;
    }

    @Async
    @Override
    public void print(String mac) {
        RedisUtils.set(String.format(RedisKey.GATEWAY_PROVINCE.getPattern(),mac,null),"54");
        counter.increment();
        if(counter.longValue()%5==0){
            RedisUtils.set(String.format(RedisKey.GATEWAY_BIND.getPattern(),mac,null),"1");
        }
    }
}
