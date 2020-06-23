package org.horoyoii.http;

import lombok.Getter;
import lombok.Setter;
import org.horoyoii.exception.ReadTimeoutException;
import org.horoyoii.http.header.Header;
import org.horoyoii.http.constants.HttpDirective;
import org.horoyoii.http.startLine.StartLine;
import org.horoyoii.utils.HttpParseHelper;

import java.io.InputStream;
import java.nio.ByteBuffer;

@Getter
@Setter
public abstract class HttpMessage {

  StartLine startLine;
  Header header;
  public ByteBuffer body; //TODO : why this is public?


  public HttpMessage(){ }


  public HttpMessage(InputStream ins) throws ReadTimeoutException{

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
  abstract StartLine buildStartLine(InputStream ins) throws ReadTimeoutException;


  /**
   * Build Headers
   *
   */
  void buildHeader(InputStream inputStream) throws ReadTimeoutException {
    header = new Header(inputStream);
  }


  public void addHeader(HttpDirective key, HttpDirective value){
    header.setHeader(key, value);
  }


  /**
   * Build a body if it exists
   *
   */
  void buildBody(InputStream inputStream) throws ReadTimeoutException{

    body = ByteBuffer.allocate(Integer.parseInt(header.getHeader(HttpDirective.CONTENT_LENGTH.getDirective())));
    HttpParseHelper.parseBody(inputStream, body);

  }


  /**
   * Check this http message has a body.
   *  If "content-length" or "transfer-encoding" field exists,
   *
   *  @return
   */
  boolean hasBody(){
    return header.getHeader(HttpDirective.CONTENT_LENGTH.getDirective()) != null;
  }


  String getStartLineBuffer(InputStream inputStream) throws ReadTimeoutException{
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
