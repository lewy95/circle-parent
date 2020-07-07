package cn.xzxy.lewy.framework.openfeign.handler;

import feign.RequestTemplate;

/**
 * 对中台请求进行拦截操作
 *
 * @author lewy95
 **/
public interface MdpAccessHandler {

    void doInterception(RequestTemplate requestTemplate);
}
