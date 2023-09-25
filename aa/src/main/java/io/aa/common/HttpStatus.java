package io.aa.common;

import static java.util.Objects.requireNonNull;

public final class HttpStatus {

    private static final HttpStatus[] map = new HttpStatus[1000];

    public static final HttpStatus CONTINUE = newConstant(100, "Continue");
    public static final HttpStatus SWITCHING_PROTOCOLS = newConstant(101, "Switching Protocols");
    public static final HttpStatus PROCESSING = newConstant(102, "Processing");
    public static final HttpStatus EARLY_HINTS = newConstant(103, "Early Hints");
    public static final HttpStatus OK = newConstant(200, "OK");
    public static final HttpStatus CREATED = newConstant(201, "Created");
    public static final HttpStatus ACCEPTED = newConstant(202, "Accepted");
    public static final HttpStatus NON_AUTHORITATIVE_INFORMATION =
            newConstant(203, "Non-Authoritative Information");
    public static final HttpStatus NO_CONTENT = newConstant(204, "No Content");
    public static final HttpStatus RESET_CONTENT = newConstant(205, "Reset Content");
    public static final HttpStatus PARTIAL_CONTENT = newConstant(206, "Partial Content");
    public static final HttpStatus MULTI_STATUS = newConstant(207, "Multi-Status");
    public static final HttpStatus ALREADY_REPORTED = newConstant(208, "Already Reported");
    public static final HttpStatus IM_USED = newConstant(226, "IM Used");
    public static final HttpStatus MULTIPLE_CHOICES = newConstant(300, "Multiple Choices");
    public static final HttpStatus MOVED_PERMANENTLY = newConstant(301, "Moved Permanently");
    public static final HttpStatus FOUND = newConstant(302, "Found");
    public static final HttpStatus SEE_OTHER = newConstant(303, "See Other");
    public static final HttpStatus NOT_MODIFIED = newConstant(304, "Not Modified");
    public static final HttpStatus USE_PROXY = newConstant(305, "Use Proxy");
    public static final HttpStatus TEMPORARY_REDIRECT = newConstant(307, "Temporary Redirect");
    public static final HttpStatus PERMANENT_REDIRECT = newConstant(308, "Permanent Redirect");
    public static final HttpStatus BAD_REQUEST = newConstant(400, "Bad Request");
    public static final HttpStatus UNAUTHORIZED = newConstant(401, "Unauthorized");
    public static final HttpStatus PAYMENT_REQUIRED = newConstant(402, "Payment Required");
    public static final HttpStatus FORBIDDEN = newConstant(403, "Forbidden");
    public static final HttpStatus NOT_FOUND = newConstant(404, "Not Found");
    public static final HttpStatus METHOD_NOT_ALLOWED = newConstant(405, "Method Not Allowed");
    public static final HttpStatus NOT_ACCEPTABLE = newConstant(406, "Not Acceptable");
    public static final HttpStatus PROXY_AUTHENTICATION_REQUIRED =
            newConstant(407, "Proxy Authentication Required");
    public static final HttpStatus REQUEST_TIMEOUT = newConstant(408, "Request Timeout");
    public static final HttpStatus CONFLICT = newConstant(409, "Conflict");
    public static final HttpStatus GONE = newConstant(410, "Gone");
    public static final HttpStatus LENGTH_REQUIRED = newConstant(411, "Length Required");
    public static final HttpStatus PRECONDITION_FAILED = newConstant(412, "Precondition Failed");
    public static final HttpStatus PAYLOAD_TOO_LARGE = newConstant(413, "Payload Too Large");
    public static final HttpStatus URI_TOO_LONG = newConstant(414, "URI Too Long");
    public static final HttpStatus UNSUPPORTED_MEDIA_TYPE = newConstant(415, "Unsupported Media Type");
    public static final HttpStatus RANGE_NOT_SATISFIABLE = newConstant(416, "Range Not Satisfiable");
    public static final HttpStatus EXPECTATION_FAILED = newConstant(417, "Expectation Failed");
    public static final HttpStatus IM_A_TEAPOT = newConstant(418, "I'm a teapot");
    public static final HttpStatus MISDIRECTED_REQUEST = newConstant(421, "Misdirected Request");
    public static final HttpStatus UNPROCESSABLE_CONTENT = newConstant(422, "Unprocessable Content");
    public static final HttpStatus LOCKED = newConstant(423, "Locked");
    public static final HttpStatus FAILED_DEPENDENCY = newConstant(424, "Failed Dependency");
    public static final HttpStatus TOO_EARLY = newConstant(425, "Too Early");
    public static final HttpStatus UPGRADE_REQUIRED = newConstant(426, "Upgrade Required");
    public static final HttpStatus PRECONDITION_REQUIRED = newConstant(428, "Precondition Required");
    public static final HttpStatus TOO_MANY_REQUESTS = newConstant(429, "Too Many Requests");
    public static final HttpStatus REQUEST_HEADER_FIELDS_TOO_LARGE =
            newConstant(431, "Request Header Fields Too Large");
    public static final HttpStatus UNAVAILABLE_FOR_LEGAL_REASONS =
            newConstant(451, "Unavailable For Legal Reasons");
    public static final HttpStatus INTERNAL_SERVER_ERROR = newConstant(500, "Internal Server Error");
    public static final HttpStatus NOT_IMPLEMENTED = newConstant(501, "Not Implemented");
    public static final HttpStatus BAD_GATEWAY = newConstant(502, "Bad Gateway");
    public static final HttpStatus SERVICE_UNAVAILABLE = newConstant(503, "Service Unavailable");
    public static final HttpStatus GATEWAY_TIMEOUT = newConstant(504, "Gateway Timeout");
    public static final HttpStatus HTTP_VERSION_NOT_SUPPORTED = newConstant(505, "HTTP Version Not Supported");
    public static final HttpStatus VARIANT_ALSO_NEGOTIATES = newConstant(506, "Variant Also Negotiates");
    public static final HttpStatus INSUFFICIENT_STORAGE = newConstant(507, "Insufficient Storage");
    public static final HttpStatus LOOP_DETECTED = newConstant(508, "Loop Detected");
    public static final HttpStatus NOT_EXTENDED = newConstant(510, "Not Extended");
    public static final HttpStatus NETWORK_AUTHENTICATION_REQUIRED =
            newConstant(511, "Network Authentication Required");

    private final int code;
    private final String reasonPhrase;

    public HttpStatus(int statusCode, String reasonPhrase) {
        code = statusCode;
        this.reasonPhrase = requireNonNull(reasonPhrase, "reasonPhrase");
    }

    private static HttpStatus newConstant(int statusCode, String reasonPhrase) {
        final HttpStatus status = new HttpStatus(statusCode, reasonPhrase);
        map[statusCode] = status;
        return status;
    }

    public static HttpStatus valueOf(int statusCode) {
        return map[statusCode];
    }

    public int code() {
        return code;
    }

    public String reasonPhrase() {
        return reasonPhrase;
    }
}
