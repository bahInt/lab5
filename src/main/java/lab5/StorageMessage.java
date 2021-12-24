package lab5;

public class StorageMessage {
    private final String url;
    private final float avgTine;

    public StorageMessage(String url, float avgTime) {
        this.url = url;
        this.avgTine = avgTime;
    }

    public float getAvgTine() {
        return avgTine;
    }

    public String getUrl() {
        return url;
    }
}
