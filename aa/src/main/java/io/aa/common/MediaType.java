package io.aa.common;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;

public final class MediaType {

    private static final String CHARSET_ATTRIBUTE = "charset";
    private static final String CHARSET_UTF_8 = "utf-8";
    private static final ImmutableListMultimap<String, String> UTF_8_CONSTANT_PARAMETERS =
            ImmutableListMultimap.of(CHARSET_ATTRIBUTE, CHARSET_UTF_8);

    private static final String TEXT_TYPE = "text";
    private static final String IMAGE_TYPE = "image";
    private static final String AUDIO_TYPE = "audio";
    private static final String VIDEO_TYPE = "video";
    private static final String APPLICATION_TYPE = "application";
    private static final String FONT_TYPE = "font";

    private static final String WILDCARD = "*";

    public static final MediaType ANY_TYPE = createConstant(WILDCARD, WILDCARD);
    public static final MediaType ANY_TEXT_TYPE = createConstant(TEXT_TYPE, WILDCARD);
    public static final MediaType ANY_IMAGE_TYPE = createConstant(IMAGE_TYPE, WILDCARD);
    public static final MediaType ANY_AUDIO_TYPE = createConstant(AUDIO_TYPE, WILDCARD);
    public static final MediaType ANY_VIDEO_TYPE = createConstant(VIDEO_TYPE, WILDCARD);
    public static final MediaType ANY_APPLICATION_TYPE = createConstant(APPLICATION_TYPE, WILDCARD);
    public static final MediaType ANY_FONT_TYPE = createConstant(FONT_TYPE, WILDCARD);

    public static final MediaType CSS_UTF_8 = createConstantUtf8(TEXT_TYPE, "css");
    public static final MediaType CSV_UTF_8 = createConstantUtf8(TEXT_TYPE, "csv");
    public static final MediaType HTML_UTF_8 = createConstantUtf8(TEXT_TYPE, "html");
    public static final MediaType PLAIN_TEXT_UTF_8 = createConstantUtf8(TEXT_TYPE, "plain");
    public static final MediaType JSON_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "json");

    private final String type;
    private final String subtype;
    private final ImmutableListMultimap<String, String> parameters;

    private static MediaType createConstant(String type, String subtype) {
        return new MediaType(type, subtype, ImmutableListMultimap.of());
    }

    private static MediaType createConstantUtf8(String type, String subtype) {
        return new MediaType(type, subtype, UTF_8_CONSTANT_PARAMETERS);
    }

    private MediaType(String type, String subtype, ImmutableListMultimap<String, String> parameters) {
        this.type = requireNonNull(type, "type");
        this.subtype = requireNonNull(subtype, "subtype");
        this.parameters = requireNonNull(parameters, "parameters");
    }

    public String type() {
        return type;
    }

    public String subtype() {
        return subtype;
    }

    public Multimap<String, String> parameters() {
        return parameters;
    }
}
