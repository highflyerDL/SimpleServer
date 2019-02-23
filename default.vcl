vcl 4.0;
backend default {
  .host = "http-server";
  .port = "9000";
}

sub vcl_recv {
    if (req.method == "GET" && req.http.X-cached-enabled) {
	return (hash);
    }
    return (pass);
}

sub vcl_backend_response {
    if (beresp.ttl <= 0s ||
      beresp.http.Set-Cookie ||
      beresp.http.Surrogate-control ~ "no-store" ||
      (!beresp.http.Surrogate-Control &&
        beresp.http.Cache-Control ~ "no-cache|no-store|private") ||
      beresp.http.Vary == "*") {
        /*
        * Mark as "Hit-For-Pass" for the next 2 minutes
        */
        set beresp.ttl = 30m;
        set beresp.uncacheable = true;
    }
    return (deliver);
}

sub vcl_deliver {
    if (obj.hits > 0) {
        set resp.http.X-Cache = "HIT";
    } else {
        set resp.http.X-Cache = "MISS";
    }
    return (deliver);
}

