package io.aa.common;

import javax.annotation.Nullable;

interface HttpHeaderGetters extends StringMultimapGetters {

    @Nullable
    MediaType contentType();
}
