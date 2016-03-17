package com.socnet.aspect;

import com.socnet.entity.asset.Asset;
import com.socnet.entity.asset.Attached;
import com.socnet.service.AssetService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@Aspect
public class AttachedAspect {

    @Autowired
    public AssetService assetService;

    @Pointcut("execution(* com.socnet.controller.*.*(..))")
    public void controllerPointcut() {
    }

    @AfterReturning(value = "controllerPointcut()", returning = "result")
    public void attachedPost(JoinPoint joinPoint, Attached result) {
        List<Asset> assets = assetService.findAssetsByAttachedId(result.getId());
        result.setAssets(assets);
    }

    @AfterReturning(value = "controllerPointcut()", returning = "result")
    public void attached(JoinPoint joinPoint, Collection<? extends Attached> result) {
        result.forEach(item -> {
            List<Asset> assets = assetService.findAssetsByAttachedId(item.getId());
            item.setAssets(assets);
        });
    }
}
