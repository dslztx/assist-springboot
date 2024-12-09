package dubbo.api.domain;

import java.io.Serializable;

public class DubboServiceResult implements Serializable {

    private static final long serialVersionUID = 7525160404990301344L;

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
