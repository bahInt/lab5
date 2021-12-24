package lab5;

public class StorageMessage {
    private final String url;
    private final float avgTime;

    public StorageMessage(String url, float avgTime) {
        this.url = url;
        this.avgTime = avgTime;
    }

    public float getAvgTime() {
        return avgTime;
    }

    public String getUrl() {
        return url;
    }
}
