package io.aa.common;

import static com.google.common.base.CharMatcher.ascii;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Multimaps.transformValues;
import static java.util.Objects.requireNonNull;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableListMultimap.Builder;

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

    public static final MediaType ANY_TYPE = newConstant(WILDCARD, WILDCARD);
    public static final MediaType ANY_TEXT_TYPE = newConstant(TEXT_TYPE, WILDCARD);
    public static final MediaType ANY_IMAGE_TYPE = newConstant(IMAGE_TYPE, WILDCARD);
    public static final MediaType ANY_AUDIO_TYPE = newConstant(AUDIO_TYPE, WILDCARD);
    public static final MediaType ANY_VIDEO_TYPE = newConstant(VIDEO_TYPE, WILDCARD);
    public static final MediaType ANY_APPLICATION_TYPE = newConstant(APPLICATION_TYPE, WILDCARD);
    public static final MediaType ANY_FONT_TYPE = newConstant(FONT_TYPE, WILDCARD);

    public static final MediaType CSS_UTF_8 = newConstantUtf8(TEXT_TYPE, "css");
    public static final MediaType CSV_UTF_8 = newConstantUtf8(TEXT_TYPE, "csv");
    public static final MediaType HTML_UTF_8 = newConstantUtf8(TEXT_TYPE, "html");
    public static final MediaType PLAIN_TEXT_UTF_8 = newConstantUtf8(TEXT_TYPE, "plain");
    public static final MediaType JSON_UTF_8 = newConstantUtf8(APPLICATION_TYPE, "json");

    private final String type;
    private final String subtype;
    private final ImmutableListMultimap<String, String> parameters;

    private static MediaType newConstant(String type, String subtype) {
        return new MediaType(type, subtype, ImmutableListMultimap.of());
    }

    private static MediaType newConstantUtf8(String type, String subtype) {
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

    public ImmutableListMultimap<String, String> parameters() {
        return parameters;
    }

    public static MediaType parse(String input) {
        return Parser.parse(input);
    }

    private String cachedToString;

    @Override
    public String toString() {
        String result = cachedToString;
        if (result == null) {
            result = As.string(this);
            cachedToString = result;
        }
        return result;
    }

    private static final class Tokenizer {

        final String input;
        int position;

        Tokenizer(String input) {
            this.input = requireNonNull(input, "input");
            position = 0;
        }

        String consumeToken(CharMatcher matcher) {
            requireNonNull(matcher, "matcher");
            checkState(hasMore());
            final int start = position;
            position = matcher.negate().indexIn(input, start);
            return hasMore() ? input.substring(start, position) : input.substring(start);
        }

        char consumeChar(CharMatcher matcher) {
            requireNonNull(matcher, "matcher");
            checkState(hasMore());
            final char c = previewChar();
            checkState(matcher.matches(c));
            position++;
            return c;
        }

        char consumeChar(char c) {
            checkState(hasMore());
            checkState(previewChar() == c);
            position++;
            return c;
        }

        char previewChar() {
            checkState(hasMore());
            return input.charAt(position);
        }

        boolean hasMore() {
            return position >= 0 && position < input.length();
        }
    }

    private static final class Parser {

        static final CharMatcher TOKEN_MATCHER = ascii().and(CharMatcher.javaIsoControl().negate())
                                                        .and(CharMatcher.isNot(' '))
                                                        .and(CharMatcher.noneOf("()<>@,;:\\\"/[]?="));
        static final CharMatcher LINEAR_WHITESPACE_MATCHER = CharMatcher.anyOf(" \t\r\n");
        static final CharMatcher QUOTED_TEXT_MATCHER = ascii().and(CharMatcher.noneOf("\"\\\r"));

        static MediaType parse(String input) {
            final Tokenizer tokenizer = new Tokenizer(input);
            try {
                final String type = tokenizer.consumeToken(TOKEN_MATCHER);
                tokenizer.consumeChar('/');
                final String subtype = tokenizer.consumeToken(TOKEN_MATCHER);
                final ImmutableListMultimap<String, String> parameters = consumeParameters(tokenizer);
                return new MediaType(type, subtype, parameters);
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not parse '" + input + '\'', e);
            }
        }

        static ImmutableListMultimap<String, String> consumeParameters(Tokenizer tokenizer) {
            requireNonNull(tokenizer, "tokenizer");
            final Builder<String, String> parameters = ImmutableListMultimap.<String, String>builder();
            while (tokenizer.hasMore()) {
                consumeParameterSeparator(tokenizer);
                final String attribute = consumeAttributeKey(tokenizer);
                tokenizer.consumeChar('=');
                parameters.put(attribute, consumeAttributeValue(tokenizer));
            }
            return parameters.build();
        }

        static void consumeParameterSeparator(Tokenizer tokenizer) {
            requireNonNull(tokenizer, "tokenizer");
            tokenizer.consumeToken(LINEAR_WHITESPACE_MATCHER);
            tokenizer.consumeChar(';');
            tokenizer.consumeToken(LINEAR_WHITESPACE_MATCHER);
        }

        static String consumeAttributeKey(Tokenizer tokenizer) {
            requireNonNull(tokenizer, "tokenizer");
            return tokenizer.consumeToken(TOKEN_MATCHER);
        }

        static String consumeAttributeValue(Tokenizer tokenizer) {
            requireNonNull(tokenizer, "tokenizer");
            final String value;
            if ('"' == tokenizer.previewChar()) {
                tokenizer.consumeChar('"');
                final StringBuilder valueBuilder = new StringBuilder();
                while ('"' != tokenizer.previewChar()) {
                    if ('\\' == tokenizer.previewChar()) {
                        tokenizer.consumeChar('\\');
                        valueBuilder.append(tokenizer.consumeChar(ascii()));
                    } else {
                        valueBuilder.append(tokenizer.consumeToken(QUOTED_TEXT_MATCHER));
                    }
                }
                value = valueBuilder.toString();
                tokenizer.consumeChar('"');
            } else {
                value = tokenizer.consumeToken(TOKEN_MATCHER);
            }
            return value;
        }
    }

    private static final class As {

        static final MapJoiner PARAMETER_JOINER = Joiner.on("; ").withKeyValueSeparator('=');

        static String string(MediaType mediaType) {
            requireNonNull(mediaType, "mediaType");
            final StringBuilder builder = new StringBuilder().append(mediaType.type())
                                                             .append('/')
                                                             .append(mediaType.subtype());
            final ImmutableListMultimap<String, String> parameters = mediaType.parameters();
            if (!parameters.isEmpty()) {
                appendParameters(builder, parameters);
            }
            return builder.toString();
        }

        static void appendParameters(StringBuilder builder, ImmutableListMultimap<String, String> parameters) {
            requireNonNull(builder, "builder");
            requireNonNull(parameters, "parameters");
            builder.append("; ");
            PARAMETER_JOINER.appendTo(builder, transformValues(parameters,
                                                               As::escapeAndQuoteIfNeeded).entries());
        }

        static String escapeAndQuoteIfNeeded(String value) {
            requireNonNull(value, "value");
            if (!value.isEmpty() && Parser.TOKEN_MATCHER.matchesAllOf(value)) {
                return value;
            } else {
                return escapeAndQuote(value);
            }
        }

        static String escapeAndQuote(String value) {
            requireNonNull(value, "value");
            final StringBuilder quoted = new StringBuilder(value.length() + 16).append('"');
            appendEscaped(quoted, value);
            return quoted.append('"').toString();
        }

        static void appendEscaped(StringBuilder builder, String value) {
            requireNonNull(builder, "builder");
            requireNonNull(value, "value");
            for (int i = 0; i < value.length(); i++) {
                final char c = value.charAt(i);
                if (c == '"' || c == '\\' || c == '\r') {
                    builder.append('\\');
                }
                builder.append(c);
            }
        }
    }
}
