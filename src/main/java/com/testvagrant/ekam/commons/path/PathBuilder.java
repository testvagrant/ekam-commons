package com.testvagrant.ekam.commons.path;

import com.testvagrant.ekam.commons.os.OsUtils;

import java.io.File;

/*
 * Copied from Apache sling path builder to remove dependency
 */
public final class PathBuilder {

    private final StringBuilder sb = new StringBuilder();

    /**
     * Creates a new {@code PathBuilder} instance
     *
     * @param path the initial path
     */
    public PathBuilder(final String path) {

        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path '" + path + "' is not absolute");
        }

        sb.append(path);
    }

    /**
     * Appends a new path fragment
     *
     * @param path the path fragment to append
     * @return this instance
     */

    public PathBuilder append(final String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path '" + path + "' is null or empty");
        }

        final String windowsPath;
        if (path.contains(":") && OsUtils.isWindows()) {
            windowsPath = path.replaceAll(":", "-");
            getPath(windowsPath);
        } else {
            getPath(path);
        }
        return this;
    }

    private void getPath(final String path) {
        boolean trailingSlash = sb.charAt(sb.length() - 1) == File.separator.charAt(0);
        boolean leadingSlash = path.charAt(0) == File.separator.charAt(0);

        if (trailingSlash && leadingSlash) {
            sb.append(path.substring(1));
        } else if (!trailingSlash && !leadingSlash) {
            sb.append(File.separator.charAt(0)).append(path);
        } else {
            sb.append(path);
        }
    }

    /**
     * Returns the path
     *
     * @return the path
     */
    @Override
    public String toString() {
        return sb.toString();
    }
}
