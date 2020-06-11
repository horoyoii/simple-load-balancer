package org.horoyoii.http;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.horoyoii.http.header.Header;
import org.horoyoii.http.header.HeaderDirective;
import org.horoyoii.http.startLine.StartLine;
import org.horoyoii.utils.HttpParseHelper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

@Getter
@NoArgsConstructor
public abstract class HttpMessage {

  StartLine startLine;

  Header header;

  public ByteBuffer body;

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

    body = ByteBuffer.allocate(Integer.parseInt(header.getHeader(HeaderDirective.CONTENT_LENGTH.getDirective())));
    HttpParseHelper.parseBody(inputStream, body);

  }



  /**
   * Check this http message has a body.
   *  If "content-length" or "transfer-encoding" field exists,
   *
   *  @return
   */
  boolean hasBody(){
    return header.getHeader(HeaderDirective.CONTENT_LENGTH.getDirective()) != null;
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

//    if (hasBody()) {
//      System.out.println(new String(body.array()));
//      sb.append(Arrays.toString(body.array()));
//    }

    return sb.toString();
  }

}
