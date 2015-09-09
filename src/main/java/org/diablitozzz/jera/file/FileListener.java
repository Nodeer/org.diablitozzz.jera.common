package org.diablitozzz.jera.file;

public interface FileListener {

	public void onDirectoryDone(String path);

	public void onDirectoryFail(String path, Throwable error);

	public void onFileDone(String path);

	public void onFileFail(String path, Throwable error);

}
