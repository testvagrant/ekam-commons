package com.testvagrant.ekam.commons.path;

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

    boolean trailingSlash = sb.charAt(sb.length() - 1) == '/';
    boolean leadingSlash = path.charAt(0) == '/';

    if (trailingSlash && leadingSlash) {
      sb.append(path.substring(1));
    } else if (!trailingSlash && !leadingSlash) {
      sb.append('/').append(path);
    } else {
      sb.append(path);
    }

    return this;
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
