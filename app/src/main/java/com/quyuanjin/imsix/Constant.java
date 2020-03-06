package com.quyuanjin.imsix;

public class Constant {
    public static final String INTENT_IP = "INTENT_IP";
    public static final String INTENT_PORT = "INTENT_PORT";

    public static final String TAG_STICKY = "TAG_STICKY";
    public static final String TAG_ITEM = "TAG_ITEM";
 //    public static final String URL = "http://120.79.178.226:8080/";
   public static final String URL = "http://192.168.43.75:8080/";
 //    public static final String HOST = "120.79.178.226:8080";
 public static final String HOST = "192.168.43.75";
    public static final String addfriend_send_but_wait_to_answer = "0";
    public static final String  addFriend_answer_agree= "1";
    public static final String addfriend_answerr_disagree = "2";
    public static final String be_asked_to_addfriend  = "3";//被加好友的人




    public static final int DECODE = 1;
    public static final int DECODE_FAILED = 2;
    public static final int DECODE_SUCCEEDED = 3;
    public static final int LAUNCH_PRODUCT_QUERY = 4;
    public static final int QUIT = 5;
    public static final int RESTART_PREVIEW = 6;
    public static final int RETURN_SCAN_RESULT = 7;
    public static final int FLASH_OPEN = 8;
    public static final int FLASH_CLOSE = 9;
    public static final int REQUEST_IMAGE = 10;
    public static final String CODED_CONTENT = "codedContent";
    public static final String CODED_BITMAP = "codedBitmap";


    /*传递的zxingconfing*/

    public static final String INTENT_ZXING_CONFIG = "zxingConfig";


    public Constant() {
    }
    public static final String my_robot_ai_server_headpicture_url  = "http://192.168.43.75:8080/File/image/upload/666.png";
    public static final String sex_male  = "male";
    public static final String sex_female  = "female";

    public static final int item_text_receive= 0;
    public static final int item_text_send= 1;
    public static final int item_image_receive= 2;
    public static final int item_image_send= 3;
    public static final int item_audio_receive= 4;
    public static final int item_audio_send= 5;
    public static final int item_video_receive= 6;
    public static final int item_video_send= 7;
    public static final int item_location_receive= 8;

    public static final String send_state_succeed= "1";
    public static final String send_state_failed= "0";
    public static final String slient_state_succeed= "1";
    public static final String slient_state_failed= "0";


    public static final int time_equal= 0;
    public static final int time_early= -1;
    public static final int time_late= 1;
    public static final int time_error= 2;

}
