package org.horoyoii.http;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.horoyoii.http.header.Header;
import org.horoyoii.http.header.HeaderDirective;
import org.horoyoii.http.startLine.StartLine;
import org.horoyoii.utils.HttpParseHelper;

import java.io.InputStream;

@Getter
@NoArgsConstructor
public abstract class HttpMessage {

  StartLine startLine;

  Header header;

  StringBuilder body;

  public HttpMessage(InputStream ins) {

    /*
      build a start line
     */
    startLine = buildStartLine(ins);

    /*
      build headers
     */
    buildHeader(ins);

    /*
      build a body
     */
    if(hasBody())
      buildBody(ins);
  }


  /**
   * Build a start line.
   *
   * @return StartLine
   */
  abstract StartLine buildStartLine(InputStream ins);


  /**
   * Build Headers
   *
   */
  void buildHeader(InputStream inputStream) {
    header = new Header(inputStream);
  }

  public void addHeader(HeaderDirective key, HeaderDirective value){
    header.setHeader(key, value);
  }


  /**
   * Build a body if it exists
   *
   */
  void buildBody(InputStream inputStream) {
    // hasBody() booel
    body = new StringBuilder(Integer.parseInt(header.getHeader(Header.CONTENT_LENGTH)));
    HttpParseHelper.parseBody(inputStream, body);
  }


  /**
   * Check this http message has a body.
   *
   *  @return
   */
  boolean hasBody(){
    return header.getHeader(Header.CONTENT_LENGTH) != null;
  }


  public void showAllHeaders(){
    System.out.println(header);
  }


  String getStartLineBuffer(InputStream inputStream) {
    return HttpParseHelper.getOneLine(inputStream);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(startLine);
    sb.append(HttpParseHelper.CRLF);
    sb.append(header);

    if(hasBody())
      sb.append(body);

    return sb.toString();
  }

}
