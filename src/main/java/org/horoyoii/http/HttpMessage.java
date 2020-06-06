package org.horoyoii.http;

import lombok.Getter;
import org.horoyoii.http.startLine.Header;
import org.horoyoii.http.startLine.StartLine;
import org.horoyoii.utils.HttpParseHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Getter
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
    buildBody(ins);
  }

  abstract StartLine buildStartLine(InputStream ins);

  String getStartLineBuffer(InputStream inputStream) {
    return HttpParseHelper.getOneLine(inputStream);
  }


  void buildHeader(InputStream inputStream) {
    header = new Header(inputStream);
  }


  void buildBody(InputStream inputStream) {
    // hasBody() booelean
    body = new StringBuilder(Integer.parseInt(header.getHeader("contents-length")));
    HttpParseHelper.parseBody(inputStream, body);
  }

}
