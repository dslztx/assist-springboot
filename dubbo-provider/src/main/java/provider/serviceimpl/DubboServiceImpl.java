package provider.serviceimpl;


public class DubboServiceImpl implements dubbo.service.DubboService {

    @Override
    public String helloWorld() {
        return "hello world";
    }

}
