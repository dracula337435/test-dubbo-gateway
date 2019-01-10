package org.dracula.test.dubbo.gateway.example;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author dk
 */
@Path("/TestInterface")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface TestInterface {

    @POST
    @Path("/sayHello")
    TestParam sayHello(TestParam testParam);

    @POST
    @Path("/sayHello2")
    TestParam sayHello2(TestParam testParam);

}
