import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 * A CI server which acts as webhook for GitHub
 */
public class ContinousIntegrationServer extends AbstractHandler {
    private static final AtomicInteger counter = new AtomicInteger(0);// Used for unique work directory paths

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)

            throws IOException, ServletException {

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println(target);

        String jsonPayload = IOUtils.toString(request.getReader());
        Builder builder = new Builder(counter.getAndIncrement());
        builder.build(jsonPayload);

        response.getWriter().println("CI job done");
    }

    /**
     * Starts the CI Server
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) throws Exception {
        Server server = new Server(8017);
        server.setHandler(new ContinousIntegrationServer());
        server.start();
        server.join();
    }
}
