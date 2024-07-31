package com.jspcafe.test_util;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StubHttpServletResponse implements HttpServletResponse {

  private final Map<String, String> headers = new HashMap<>();
  private int status;
  private String redirectLocation;
  private String forwardPath;
  private String contentType;
  private final StringWriter stringWriter;
  private final PrintWriter writer;

  public StubHttpServletResponse() {
    stringWriter = new StringWriter();
    writer = new PrintWriter(stringWriter);
  }

  @Override
  public int getStatus() {
    return status;
  }

  @Override
  public void setStatus(int status) {
    this.status = status;
  }

  @Override
  public void setHeader(String name, String value) {
    headers.put(name, value);
  }

  public String getRedirectLocation() {
    return redirectLocation;
  }

  public String getForwardPath() {
    return forwardPath;
  }

  public void setForwardPath(String path) {
    this.forwardPath = path;
  }

  public String getWriterContent() {
    return stringWriter.toString();
  }

  @Override
  public String getHeader(String name) {
    return headers.get(name);
  }

  @Override
  public void sendRedirect(String location) {
    this.redirectLocation = location;
  }

  @Override
  public void addHeader(String s, String s1) {

  }

  @Override
  public void setIntHeader(String s, int i) {

  }

  @Override
  public void addIntHeader(String s, int i) {

  }

  @Override
  public Collection<String> getHeaders(String s) {
    return List.of();
  }

  @Override
  public Collection<String> getHeaderNames() {
    return List.of();
  }

  @Override
  public void addCookie(Cookie cookie) {

  }

  @Override
  public boolean containsHeader(String s) {
    return false;
  }

  @Override
  public String encodeURL(String s) {
    return "";
  }

  @Override
  public String encodeRedirectURL(String s) {
    return "";
  }

  @Override
  public void sendError(int i, String s) throws IOException {

  }

  @Override
  public void sendError(int i) throws IOException {

  }

  @Override
  public void setDateHeader(String s, long l) {

  }

  @Override
  public void addDateHeader(String s, long l) {

  }

  @Override
  public String getCharacterEncoding() {
    return "";
  }

  @Override
  public void setCharacterEncoding(String s) {

  }

  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public void setContentType(String s) {
    this.contentType = s;
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException {
    return null;
  }

  @Override
  public PrintWriter getWriter() throws IOException {
    return writer;
  }

  @Override
  public void setContentLength(int i) {

  }

  @Override
  public void setContentLengthLong(long l) {

  }

  @Override
  public int getBufferSize() {
    return 0;
  }

  @Override
  public void setBufferSize(int i) {

  }

  @Override
  public void flushBuffer() throws IOException {

  }

  @Override
  public void resetBuffer() {

  }

  @Override
  public boolean isCommitted() {
    return false;
  }

  @Override
  public void reset() {

  }

  @Override
  public Locale getLocale() {
    return null;
  }

  @Override
  public void setLocale(Locale locale) {

  }
}
