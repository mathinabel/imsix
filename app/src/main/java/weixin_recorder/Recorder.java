package weixin_recorder;

public class Recorder {

    float time;
    String filePath;

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Recorder(float time, String filePath) {
        super();
        this.time = time;
        this.filePath = filePath;
    }
}

