import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

/**
 * A CI server which acts as webhook for GitHub
 */
public class ContinousIntegrationServer {

    public abstract static class GeneralRequestHandler extends AbstractHandler {

        protected abstract String getResponse(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException;

        /**
         * {@inheritDoc}
         */
        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
                throws IOException {

            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);

            System.out.println(target);

            String responseString = getResponse(target, baseRequest, request, response);
            if (responseString == null) return;

            response.getWriter().println(responseString);
            response.getWriter().flush();
        }
    }

    /**
     * A class to handle request with "/webhook/" path.
     */
    public static class WebhookRequestHandler extends GeneralRequestHandler {
        private static final AtomicInteger counter = new AtomicInteger(0);// Used for unique work directory paths

        /**
         * Returns "CI Job Done" string as a response, to handle a request with path "/webhook/", if succeeded.
         * Otherwise, returns null.
         *
         * @param target  The target of the request - either a URI or a name.
         * @param baseRequest The original unwrapped request object.
         * @param request The request either as the Request object or a wrapper of that request.
         *                The HttpConnection.getCurrentConnection().getHttpChannel().getRequest() method can be used access the Request object if required.
         * @param response The response as the Response object or a wrapper of that request.
         *                 The HttpConnection.getCurrentConnection().getHttpChannel().getResponse() method can be used access the Response object if required.
         * @return "CI Job Done" string as a response, to handle a request with path "/webhook", if succeeded. Otherwise, null.
         * @see AbstractHandler#handle(String, Request, HttpServletRequest, HttpServletResponse) handle method in AbstraceHandler class.
         */
        @Override
        public String getResponse(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
            // if not POST request, return NOT_FOUND
            if (!request.getMethod().equals("POST")) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                baseRequest.setHandled(true);
                return null;
            }

            String jsonPayload = IOUtils.toString(request.getReader());
            Builder builder = new Builder(counter.getAndIncrement());
            builder.build(jsonPayload);
            return "CI Job Done";
        }
    }

    /**
     * A class to handle request with "/commit/?id=commit hash id" path.
     */
    public static class CommitHashRequestHandler extends GeneralRequestHandler {

        /**
         * This method is called when the URL has format ip_address:port/commit/?id="commit hash id",
         * where "commit hash id" is the commit hash id, and returns an HTML formatted string if succeeded.
         * Otherwise, returns null.
         *
         * @param target  The target of the request - either a URI or a name.
         * @param baseRequest The original unwrapped request object.
         * @param request The request either as the Request object or a wrapper of that request.
         *                The HttpConnection.getCurrentConnection().getHttpChannel().getRequest() method can be used access the Request object if required.
         * @param response The response as the Response object or a wrapper of that request.
         *                 The HttpConnection.getCurrentConnection().getHttpChannel().getResponse() method can be used access the Response object if required.
         * @return an HTML formatted string as a response to handle request with "/commit/?id=commit hash id" path, if succeeded. Otherwise, null.
         * "/commit/?id=commit hash id"
         * @see AbstractHandler#handle(String, Request, HttpServletRequest, HttpServletResponse) handle method in AbstraceHandler class.
         */
        @Override
        public String getResponse(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
            // if not GET request, return NOT_FOUND
            if (!request.getMethod().equals("GET")) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                baseRequest.setHandled(true);
                return null;
            }

            String commitID = request.getParameter("id");
            System.out.println("commitid " + commitID);
            // TODO: add method that takes commitID hash as a parameter and that method returns a string

            return "Commit hash response " + commitID;
        }
    }

    /**
     * A class to handle request with "/" path.
     */
    public static class ListHistoryRequestHandler extends GeneralRequestHandler {
        /**
         * Returns an HTML formatted string as a response, to handle a request with path "/", if succeeded. Otherwise, returns null.
         *
         * @param target  The target of the request - either a URI or a name.
         * @param baseRequest The original unwrapped request object.
         * @param request The request either as the Request object or a wrapper of that request.
         *                The HttpConnection.getCurrentConnection().getHttpChannel().getRequest() method can be used access the Request object if required.
         * @param response The response as the Response object or a wrapper of that request.
         *                 The HttpConnection.getCurrentConnection().getHttpChannel().getResponse() method can be used access the Response object if required.
         * @return an HTML formatted string as a response, to handle a request with path "/", if succeeded. Otherwise, null.
         * @see AbstractHandler#handle(String, Request, HttpServletRequest, HttpServletResponse) handle method in AbstraceHandler class.
         */
        @Override
        public String getResponse(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
            // if not GET request, return NOT_FOUND
            if (!request.getMethod().equals("GET")) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                baseRequest.setHandled(true);
                return null;
            }

            // TODO: call the function the returns the HTML string for this request type.
            HTML html = new HTML(false);
            String responseHTML = html.createHTMLPayload();
            return responseHTML;
        }
    }

    /**
     * Create a server that handles requests with paths "/webhook", "/commit", "/"
     * and listens on port 8017, and returns it.
     *
     * @return a server (listens on port 8017) that handles requests with paths "/webhook/", "/commit/", "/".
     */
    private static Server createServer() {
        Server server = new Server(8017);

        // path "/webhook/"
        ContextHandler webhookContext = new ContextHandler("/webhook");
        webhookContext.setHandler(new WebhookRequestHandler());

        // path "/commit/"
        ContextHandler commitContext = new ContextHandler("/commit");
        commitContext.setHandler(new CommitHashRequestHandler());

        // path "/"
        ContextHandler listHistoryContext = new ContextHandler("/");
        listHistoryContext.setHandler(new ListHistoryRequestHandler());

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { webhookContext, commitContext, listHistoryContext });

        server.setHandler(contexts);

        return server;
    }

    /**
     * Starts the CI Server
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) throws Exception {
        Server server = createServer();
        server.start();
        server.join();
    }
}
