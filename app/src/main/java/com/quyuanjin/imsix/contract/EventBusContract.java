package com.quyuanjin.imsix.contract;

import java.util.ArrayList;
import java.util.List;

public class EventBusContract {

   private ArrayList<PojoContract> list2;

    public EventBusContract(ArrayList<PojoContract> list2) {
        this.list2=list2;
    }
    public ArrayList<PojoContract> getList2() {
        return list2;
    }
    public void setList2(ArrayList<PojoContract> list2) {
        this.list2 = list2;
    }

}
