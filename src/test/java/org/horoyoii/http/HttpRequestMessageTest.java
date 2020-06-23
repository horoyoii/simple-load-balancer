//package org.horoyoii.http;
//
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.jboss.shrinkwrap.api.spec.JavaArchive;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static org.junit.Assert.*;
//
//@RunWith(Arquillian.class)
//public class HttpRequestMessageTest {
//    @Deployment
//    public static JavaArchive createDeployment() {
//        return ShrinkWrap.create(JavaArchive.class)
//                .addClass(HttpRequestMessage.class)
//                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
//    }
//
//  @Before
//  public void setUp() throws Exception {}
//
//  @Test
//  public void getStartLineBuffer() {}
//
//  @Test
//  public void buildHeader() {
//
//
//
//  }
//
//  @Test
//  public void buildBody() {}
//
//  @Test
//  public void buildStartLine() {}
//}
