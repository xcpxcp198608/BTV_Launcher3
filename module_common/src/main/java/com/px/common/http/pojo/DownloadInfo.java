package com.px.common.http.pojo;

public class DownloadInfo {
    private String name;
    private String url;
    private String path;
    private String message;
    private int id;
    private int status;
    private int progress;
    private long length;
    private long startPosition;
    private long finishedPosition;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    public long getFinishedPosition() {
        return finishedPosition;
    }

    public void setFinishedPosition(long finishedPosition) {
        this.finishedPosition = finishedPosition;
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", path='" + path + '\'' +
                ", message='" + message + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", progress=" + progress +
                ", length=" + length +
                ", startPosition=" + startPosition +
                ", finishedPosition=" + finishedPosition +
                '}';
    }
}
