package com.wiatec.btv_launcher.Utils.ImageDownload;

/**
 * Created by PX on 2016/9/12.
 */
public class DownloadInfo {
    private int id;
    private int status;
    private int progress;
    private String name;
    private String url;
    private String path;
    private long length;
    private long startPosition;
    private long endPosition;
    private long finishedPosition;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public long getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(long endPosition) {
        this.endPosition = endPosition;
    }

    public long getFinishedPosition() {
        return finishedPosition;
    }

    public void setFinishedPosition(long finishedPosition) {
        this.finishedPosition = finishedPosition;
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "id=" + id +
                ", status=" + status +
                ", progress=" + progress +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", path='" + path + '\'' +
                ", length=" + length +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                ", finishedPosition=" + finishedPosition +
                '}';
    }
}
