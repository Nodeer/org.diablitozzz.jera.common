/*
 * Copyright (c) 2012, diablitozzz.org All rights reserved. Redistribution
 * and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met: * Redistributions
 * of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form
 * must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of diablitozzz.org nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.diablitozzz.jera.util;

public class UtilException {

	public static String toString(final Throwable e) {

		final StringBuilder buffer = new StringBuilder(64);
		UtilException.writeAsString(buffer, e);
		return buffer.toString();
	}

	public static void writeAsString(final StringBuilder builder, final Throwable error) {

		// сама ошибка
		builder.append(error.getClass().getCanonicalName());
		builder.append(": ");
		builder.append(error.getLocalizedMessage());
		builder.append('\n');

		// stack trace
		for (final StackTraceElement stackTrace : error.getStackTrace()) {

			if (stackTrace.isNativeMethod()) {
				continue;
			}
			builder.append('\t');
			builder.append(stackTrace.getClassName());
			builder.append('.');
			builder.append(stackTrace.getMethodName());
			builder.append(": ");
			builder.append(stackTrace.getLineNumber());
			builder.append('\n');
		}

		// cause
		final Throwable cause = error.getCause();
		if (cause != null) {
			builder.append('\n');
			builder.append('\n');
			UtilException.writeAsString(builder, cause);
		}
	}
}
