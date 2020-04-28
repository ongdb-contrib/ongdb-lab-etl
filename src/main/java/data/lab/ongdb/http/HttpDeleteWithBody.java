package data.lab.ongdb.http;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

public class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {

    /**
     * 使用HttpDelete执行DELETE操作的时候，不能携带body信息，
     * 通过继承类HttpEntityEnclosingRequestBase重写getMethod，使其可以携带body信息
     **/

    public static final String METHOD_NAME = "DELETE";

    public String getMethod() {
        return METHOD_NAME;
    }

    public HttpDeleteWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    public HttpDeleteWithBody(final URI uri) {
        super();
        setURI(uri);
    }

    public HttpDeleteWithBody() {
        super();
    }

}

