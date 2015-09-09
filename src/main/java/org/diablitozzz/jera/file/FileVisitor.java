package org.diablitozzz.jera.file;

public interface FileVisitor {

	public boolean afterDirectory(String path, FileSession session) throws Exception;

	public boolean beforeDirectory(String path, FileSession session) throws Exception;

	public boolean onError(String path, FileSession session, Throwable cause);

	public boolean onFile(String path, FileSession session) throws Exception;

}
