package provider.serviceimpl;

import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class DubboServiceImpl implements dubbo.service.DubboService {

    @Override
    public String helloWorld() {
        return "hello world";
    }
}
