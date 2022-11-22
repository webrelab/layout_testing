package ru.webrelab.layout_testing.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.webrelab.layout_testing.ifaces.IMeasuringType;
import ru.webrelab.layout_testing.ifaces.IRepository;
import ru.webrelab.layout_testing.repository.*;

@RequiredArgsConstructor
@Getter
public enum MeasuringType implements IMeasuringType {
    POSITION(".", false, null),
    COMPLEX("", false, null),
    ALL(COMPLEX.xpath, COMPLEX.complex, COMPLEX.repositoryClass),
    DECOR("descendant-or-self::*[contains(@class, 'measuringTypeDecor')]", true, DecorRepository.class),
    TEXT("descendant-or-self::*[contains(@class, 'measuringTypeText')]", true, TextRepository.class),
    IMAGE("descendant-or-self::img", true, ImageRepository.class),
    PSEUDO_BEFORE("descendant-or-self::*[contains(@class, 'measuringBeforeElement')]", true, PseudoBeforeRepository.class),
    PSEUDO_AFTER("descendant-or-self::*[contains(@class, 'measuringAfterElement')]", true, PseudoAfterRepository.class),
    SVG("descendant-or-self::*[name() = 'svg']", true, SvgRepository.class),
    ;

    private final String xpath;
    private final boolean complex;
    private final Class<? extends IRepository> repositoryClass;
}
