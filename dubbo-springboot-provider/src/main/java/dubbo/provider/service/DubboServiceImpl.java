package dubbo.provider.service;

import javax.annotation.PostConstruct;

import dubbo.api.domain.DubboServiceQuery;
import dubbo.api.domain.DubboServiceResult;
import dubbo.api.service.DubboService;
import lombok.extern.slf4j.Slf4j;
import me.dslztx.assist.util.ObjectAssist;

@Slf4j
public class DubboServiceImpl implements DubboService {

    @PostConstruct
    public void init() {
        log.info("dubbo service impl instance init successfully");
    }

    @Override
    public DubboServiceResult invoke(DubboServiceQuery query) {
        try {
            return invoke0(query);
        } catch (Throwable e) {
            log.error("", e);
            return null;
        }
    }

    private DubboServiceResult invoke0(DubboServiceQuery query) {
        if (ObjectAssist.isNull(query) || ObjectAssist.isNull(query.getPerson())) {
            throw new RuntimeException("illegal query argument");
        }

        log.info("receive query : {}", query.toString());

        String msg = "success";

        DubboServiceResult result = new DubboServiceResult();
        result.setMsg(msg);

        return result;
    }
}
