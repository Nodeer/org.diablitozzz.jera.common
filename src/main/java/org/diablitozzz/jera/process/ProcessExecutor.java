package org.diablitozzz.jera.process;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.diablitozzz.jera.io.IoUtils;
import org.diablitozzz.jera.temporary.TemporaryResourceAsFile;

public class ProcessExecutor {
    
    private static boolean isBlank(final String value) {
        if (value == null) {
            return true;
        }
        return value.trim().isEmpty();
    }
    
    private static List<String> split(final String command) {
        
        final ArrayList<String> out = new ArrayList<String>();
        final String val = command.trim().replaceAll("[ \t\n\r]{1,}", " ").trim();
        for (final String v : val.split(" ")) {
            out.add(v);
        }
        return out;
    }
    
    public int execute(final ProcessSpecification spec) throws ProcessException {
        
        this.validate(spec);
        
        final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        
        final ProcessBuilder builder = new ProcessBuilder(ProcessExecutor.split(spec.getCommand()));
        
        if (spec.getEnv() != null) {
            for (final Map.Entry<String, String> entry : spec.getEnv().entrySet()) {
                builder.environment().put(entry.getKey(), entry.getValue());
            }
        }
        if (spec.getDir() != null) {
            builder.directory(spec.getDir());
        }
        //redirects
        final TemporaryResourceAsFile errTmp = (spec.getErr() == null) ? null : new TemporaryResourceAsFile(tmpDir);
        final TemporaryResourceAsFile outTmp = (spec.getOut() == null) ? null : new TemporaryResourceAsFile(tmpDir);
        
        if (errTmp != null) {
            builder.redirectError(errTmp.getFile());
        }
        if (outTmp != null) {
            builder.redirectOutput(outTmp.getFile());
        }
        
        try {
            final Process process = builder.start();
            process.waitFor();
            
            //copy out
            if (outTmp != null && outTmp.getFile().exists()) {
                
                try (InputStream outStream = outTmp.getInputStreamSource().getInputStream()) {
                    IoUtils.copy(outStream, spec.getOut());
                }
            }
            //copy err
            if (errTmp != null && errTmp.getFile().exists()) {
                
                try (InputStream errStream = errTmp.getInputStreamSource().getInputStream()) {
                    IoUtils.copy(errStream, spec.getErr());
                }
            }
            
            return process.exitValue();
            
        } catch (final Throwable e) {
            throw new ProcessException(e.getMessage(), e);
        } finally {
            if (errTmp != null) {
                errTmp.close();
            }
            if (outTmp != null) {
                outTmp.close();
            }
        }
    }
    
    private void validate(final ProcessSpecification spec) throws ProcessException {
        
        if (ProcessExecutor.isBlank(spec.getCommand())) {
            throw new ProcessException("Command is blank");
        }
    }
    
}
