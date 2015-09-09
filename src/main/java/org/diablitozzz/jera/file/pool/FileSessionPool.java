package org.diablitozzz.jera.file.pool;

import java.io.InputStream;
import java.io.OutputStream;

import org.diablitozzz.jera.file.FileException;
import org.diablitozzz.jera.file.FileListener;
import org.diablitozzz.jera.file.FileSession;
import org.diablitozzz.jera.file.FileSessionRepository;
import org.diablitozzz.jera.file.FileVisitor;
import org.diablitozzz.jera.pool.Pool;
import org.diablitozzz.jera.pool.PoolResource;

public class FileSessionPool implements FileSession, PoolResource {

	private FileSession session;
	private boolean pooled;
	private final Pool<FileSessionPool> pool;
	private final FileSessionRepository sessionRepository;

	public FileSessionPool(final FileSessionRepository sessionRepository, final Pool<FileSessionPool> pool) {

		this.session = null;
		this.pool = pool;
		this.pooled = false;
		this.sessionRepository = sessionRepository;
	}

	@Override
	public void begin() throws FileException {
		this.getSession().begin();
	}

	@Override
	public void close() {
		//rollback
		try {
			if (this.session != null && this.isTransactionStarted()) {
				this.rollback();
			}
		} catch (final Exception e) {
		}

		//release connection
		this.pool.add(this);
	}

	@Override
	public void closePoolResource() {
		try {
			if (this.session != null) {
				this.getSession().close();
				this.session = null;
			}
		} catch (final FileException e) {
		}
	}

	@Override
	public void commit() throws FileException {
		this.getSession().commit();
	}

	@Override
	public boolean contains(final String path) throws FileException {
		return this.getSession().contains(path);
	}

	@Override
	public boolean contains(final String path, final boolean lockExclusively) throws FileException {
		return this.getSession().contains(path, lockExclusively);
	}

	@Override
	public void copy(final String src, final String dest, final boolean override) throws FileException {
		this.getSession().copy(src, dest, override);
	}

	@Override
	public void copy(final String src, final String dest, final boolean override, final FileListener fileListener) throws FileException {
		this.getSession().copy(src, dest, override, fileListener);
	}

	@Override
	public void createDirectory(final String path) throws FileException {
		this.getSession().createDirectory(path);
	}

	@Override
	public void createFile(final String path) throws FileException {
		this.getSession().createFile(path);
	}

	@Override
	public InputStream createInputStream(final String path) throws FileException {
		return this.getSession().createInputStream(path);
	}

	@Override
	public InputStream createInputStream(final String path, final boolean lockExclusively) throws FileException {
		return this.getSession().createInputStream(path, lockExclusively);
	}

	@Override
	public OutputStream createOutputStream(final String path) throws FileException {
		return this.getSession().createOutputStream(path);
	}

	@Override
	public OutputStream createOutputStream(final String path, final boolean lockExclusively) throws FileException {
		return this.getSession().createOutputStream(path, lockExclusively);
	}

	@Override
	public void delete(final String path) throws FileException {
		this.getSession().delete(path);
	}

	@Override
	public void delete(final String path, final FileListener fileListener) throws FileException {
		this.getSession().delete(path, fileListener);
	}

	protected FileSession getSession() throws FileException {

		if (this.pooled) {
			throw new FileException("Can't get session from resource in pool");
		}
		if (this.session == null) {
			this.session = this.sessionRepository.getSession();
		}
		return this.session;
	}

	@Override
	public long getSize(final String path) throws FileException {
		return this.getSession().getSize(path);
	}

	@Override
	public long getSize(final String path, final boolean lockExclusively) throws FileException {
		return this.getSession().getSize(path, lockExclusively);
	}

	@Override
	public boolean isDirectory(final String path) throws FileException {
		return this.getSession().isDirectory(path);
	}

	@Override
	public boolean isDirectory(final String path, final boolean lockExclusively) throws FileException {
		return this.getSession().isDirectory(path, lockExclusively);
	}

	@Override
	public boolean isFile(final String path) throws FileException {
		return this.getSession().isFile(path);
	}

	@Override
	public boolean isFile(final String path, final boolean lockExclusively) throws FileException {
		return this.getSession().isFile(path, lockExclusively);
	}

	@Override
	public boolean isPooled() {
		return this.pooled;
	}

	@Override
	public boolean isTransactionStarted() throws FileException {
		return this.getSession().isTransactionStarted();
	}

	@Override
	public void move(final String src, final String dest, final boolean override) throws FileException {
		this.getSession().move(src, dest, override);
	}

	@Override
	public void move(final String src, final String dest, final boolean override, final FileListener fileListener) throws FileException {
		this.getSession().move(src, dest, override, fileListener);
	}

	@Override
	public void prepare() throws FileException {
		this.getSession().prepare();
	}

	@Override
	public void rollback() throws FileException {
		this.getSession().rollback();
	}

	@Override
	public void scan(final String path, final FileVisitor fileVisitor) throws FileException {
		this.getSession().scan(path, fileVisitor);
	}

	@Override
	public void scan(final String path, final FileVisitor fileVisitor, final boolean lockExclusively) throws FileException {
		this.getSession().scan(path, fileVisitor, lockExclusively);
	}

	@Override
	public void setPooled(final boolean pooled) {
		this.pooled = pooled;
	}

	@Override
	public void truncate(final String path, final long newLength) throws FileException {
		this.getSession().truncate(path, newLength);
	}
}
