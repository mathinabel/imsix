package eventbus.ClienthandlerBus;

import com.quyuanjin.imsix.chatsession.Msg;

public class TellChatAc {
    private Msg msg;

    public TellChatAc(Msg msg1) {
        this.msg = msg1;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
