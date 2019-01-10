package org.dracula.test.dubbo.gateway.example;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author dk
 */
@Path("/TestInterface2")
public interface TestInterface2 {

    @POST
    @Path("/sayHello")
    @Consumes(ContentType.APPLICATION_JSON_UTF_8)
    @Produces(ContentType.APPLICATION_JSON_UTF_8)
    TestParam sayHello(TestParam testParam);

}
