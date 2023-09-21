package io.aa.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MediaTypeTest {

    @Test
    void parse() {
        final var input = "multipart/related"
                          + "; type=\"application/xop+xml\""
                          + "; start=\"<rootpart@here.com>\""
                          + "; start-info=\"application/soap+xml\""
                          + "; boundary=\"----=_Part_1_701508.1145579811786\"";
        final var parsed = MediaType.parse(input);
        assertEquals("multipart", parsed.type());
        assertEquals("related", parsed.subtype());

        final var parameters = parsed.parameters();
        assertEquals("application/xop+xml", parameters.get("type").get(0));
        assertEquals("<rootpart@here.com>", parameters.get("start").get(0));
        assertEquals("application/soap+xml", parameters.get("start-info").get(0));
        assertEquals("----=_Part_1_701508.1145579811786", parameters.get("boundary").get(0));
    }

    @Test
    void _toString() {
        final var input = "multipart/related"
                          + "; type=\"application/xop+xml\""
                          + "; start=\"<rootpart@here.com>\""
                          + "; start-info=\"application/soap+xml\""
                          + "; boundary=\"----=_Part_1_701508.1145579811786\"";
        final var parsed = MediaType.parse(input);
        assertEquals(input, parsed.toString());
    }
}
