package org.diablitozzz.jera.process;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

public class ProcessSpecification {

	private String command;
	private OutputStream out;
	private OutputStream err;
	private Map<String, String> env;
	private File dir;

	public String getCommand() {
		return this.command;
	}

	public File getDir() {
		return this.dir;
	}

	public Map<String, String> getEnv() {
		return this.env;
	}

	public OutputStream getErr() {
		return this.err;
	}

	public OutputStream getOut() {
		return this.out;
	}

	public void setCommand(final String command) {
		this.command = command;
	}

	public void setDir(final File dir) {
		this.dir = dir;
	}

	public void setEnv(final Map<String, String> env) {
		this.env = env;
	}

	public void setErr(final OutputStream err) {
		this.err = err;
	}

	public void setOut(final OutputStream out) {
		this.out = out;
	}

}
