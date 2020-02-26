package com.quyuanjin.imsix.contract.utils;

import android.util.Log;

import com.github.promeg.pinyinhelper.Pinyin;
import com.quyuanjin.imsix.Constant;
import com.quyuanjin.imsix.contract.PojoContract;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PinYinSortUtils {
    private ArrayList<PojoContract> entityList;
    private static HashMap<String, Integer> locMap2 = new HashMap<>();

  /*  public static void main(String args[]) {
        ArrayList<PojoContract> entityList2 = new ArrayList<>()

        PinYinSortUtils pinYinSortUtils = new PinYinSortUtils(entityList2);
        //  pinYinSortUtils.sort();
        // pinYinSortUtils.add();
        entityList2 = pinYinSortUtils.sortAndAdd();
        locMap2 = pinYinSortUtils.getPosList();
        System.out.println(locMap2.toString());

        for (int i = 0; i < entityList2.size(); i++) {
           System.out.println(entityList2.get(i).getPinyin());
            System.out.println(entityList2.get(i).getTag());



        }
    }*/


    public PinYinSortUtils(ArrayList<PojoContract> entityList) {
        this.entityList = entityList;
    }

    private List<String> arr = new ArrayList<>();
    private String[] alphaTable = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private ArrayList<PojoContract> entityList3 = new ArrayList<>();
    private HashMap<String, Integer> locMap = new HashMap<>();

    public ArrayList<PojoContract> sortAndAdd() {
        for (int i = 0; i < entityList.size(); i++) {
            // System.out.println("entity的size为："+entityList.size()+"......"+entityList.get(i).getNameTextView().charAt(0));
                String firstAlpha = Pinyin.toPinyin(entityList.get(i).getNameTextView().charAt(0));
                entityList.get(i).setPinyin(firstAlpha.subSequence(0, 1).toString());
                Log.d("eventt","entityList.get(i).getNameTextView()不为空，是："+entityList.get(i).getNameTextView());


        }
        Collections.sort(entityList, new Comparator<PojoContract>() {
            @Override
            public int compare(PojoContract o1, PojoContract o2) {
                return o1.getPinyin().compareTo(o2.getPinyin());
            }
        });
        int j = entityList.size();
        for (int i = 0; i < j; i++) {
            if (!(arr.contains(entityList.get(i).getPinyin()))) {//arr里没有该首字母
                System.out.println("开始执行内部：arr里没有该首字母");

                for (String at : alphaTable) {
                    if (entityList.get(i).getPinyin().equals(at)) {
                        System.out.println("是字母表内");

                        //是字母表内，加进去
                        arr.add(at);
                    } else { //不是字母表内，设置默认值
                        System.out.println("不是字母表内");
                    }
                }

                entityList3.add(new PojoContract(null, "",
                        entityList.get(i).getNameTextView(), "", "", "",
                        Constant.TAG_STICKY, entityList.get(i).getPinyin(), "", ""));
                entityList3.add(entityList.get(i));


            } else {//arr里有该字符
                System.out.println("开始执行内部循环：arr里有该首字母");

                entityList3.add(entityList.get(i));

            }

        }
        for (int i = 0; i < entityList3.size(); i++) {

            boolean res = Arrays.asList(alphaTable).contains(entityList3.get(i).getPinyin());

            if ((!res) && (entityList3.get(i).getTag().equals(Constant.TAG_STICKY))) {
               entityList3.remove(i);
                //   entityList3.get(i).setPinyin("#");
            }
        }
      //  entityList3.add(0, new PojoContract(null, "", "*嘿嘿八", "", "", "", Constant.TAG_STICKY, "#", "", ""));

        return entityList3;
    }

    public HashMap getPosList() {

        for (int i = 0; i < entityList3.size(); i++) {
            if (entityList3.get(i).getTag().equals(Constant.TAG_STICKY)) {
                locMap.put(entityList3.get(i).getPinyin(), i);
            }
        }
        return locMap;
    }
}
