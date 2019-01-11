package org.dracula.test.dubbo.gateway.example.subpack;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import org.dracula.test.dubbo.gateway.example.TestParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author dk
 */
@Path("/TestInterface3")
public interface TestInterface3 {

    @POST
    @Path("/sayHello")
    @Consumes(ContentType.APPLICATION_JSON_UTF_8)
    @Produces(ContentType.APPLICATION_JSON_UTF_8)
    TestParam sayHello(TestParam testParam);

}
