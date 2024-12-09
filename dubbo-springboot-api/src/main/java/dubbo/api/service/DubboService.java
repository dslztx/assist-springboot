package dubbo.api.service;

import dubbo.api.domain.DubboServiceQuery;
import dubbo.api.domain.DubboServiceResult;

public interface DubboService {

    DubboServiceResult invoke(DubboServiceQuery query);

}
