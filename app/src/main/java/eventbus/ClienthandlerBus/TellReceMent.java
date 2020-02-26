package eventbus.ClienthandlerBus;

import com.quyuanjin.imsix.chatsession.Msg;
// to tell recementmsg you gitt a new msg
public class TellReceMent {
    private Msg msg;

    public TellReceMent(Msg msg1) {
        this.msg = msg1;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
