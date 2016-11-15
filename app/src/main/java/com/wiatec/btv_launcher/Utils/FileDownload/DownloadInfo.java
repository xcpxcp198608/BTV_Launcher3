package com.wiatec.btv_launcher.Utils.FileDownload;

/**
 * Created by PX on 2016/9/12.
 */
public class DownloadInfo {
    private int downloadId;
    private String fileName;
    private String downloadUrl;
    private long fileLength;
    private long startPosition;
    private long stopPosition;
    private long completePosition;

    public int getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public long getStopPosition() {
        return stopPosition;
    }

    public void setStopPosition(long stopPosition) {
        this.stopPosition = stopPosition;
    }

    public long getCompletePosition() {
        return completePosition;
    }

    public void setCompletePosition(long completePosition) {
        this.completePosition = completePosition;
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "downloadId=" + downloadId +
                ", fileName='" + fileName + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", fileLength=" + fileLength +
                ", startPosition=" + startPosition +
                ", stopPosition=" + stopPosition +
                ", completePosition=" + completePosition +
                '}';
    }
}
