package ru.webrelab.layout_testing.repository;

import lombok.Getter;
import lombok.SneakyThrows;
import ru.webrelab.layout_testing.utils.ElementAttributesUtil;

import java.net.URI;

@Getter
public class ImageRepository extends AttributeRepository {
    /**
     * The field stores the src value of the img tag. If the base64 image is stored in src, then we
     * save it in the same form. If the link is to an image - save only the relative path without
     * the host to avoid the problem of testing on test benches with different domains
     */
    private final String src;

    @SneakyThrows
    public ImageRepository(final Object webElement) {
        final String srcAttr = (String) ElementAttributesUtil.getAttribute(webElement, "src");
        src = (srcAttr.startsWith("http://") || srcAttr.startsWith("https://")) ?
                new URI(srcAttr).getPath() :
                srcAttr;

    }

    @Override
    public boolean check() {
        return true;
    }
}
