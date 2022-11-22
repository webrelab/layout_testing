package ru.webrelab.layout_testing.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.webrelab.layout_testing.utils.ElementAttributesUtil;

import java.net.URI;

@Getter
@RequiredArgsConstructor
public class ImageRepository extends AttributeRepository {
    /**
     * Поле хранит значение src тега img
     * Если в src хранится base64 картинка, то сохраняем в такм же виде.
     * Если ссылка на картинку - сохраняем только относительный путь без хоста
     * чтобы избежать проблему с тестированием на тестовых стендах с разными доменами
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
