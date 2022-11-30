package ru.webrelab.layout_testing.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.webrelab.layout_testing.ifaces.IMeasuringType;
import ru.webrelab.layout_testing.ifaces.IRepository;
import ru.webrelab.layout_testing.repository.*;

@RequiredArgsConstructor
@Getter
public enum MeasuringType implements IMeasuringType {
    POSITION(".", "#000", 45,false, null),
    COMPLEX("", "#000", 45, false, null),
    ALL(COMPLEX.xpath, "#000", 45, COMPLEX.complex, COMPLEX.repositoryClass),
    DECOR("descendant-or-self::*[contains(@class, 'measuringTypeDecor')]", "#6c3483", 60, true, DecorRepository.class),
    TEXT("descendant-or-self::*[contains(@class, 'measuringTypeText')]", "#1f618d", 30, true, TextRepository.class),
    IMAGE("descendant-or-self::img", "#148f77", 0, true, ImageRepository.class),
    PSEUDO_BEFORE("descendant-or-self::*[contains(@class, 'measuringBeforeElement')]", "#b9770e", -30, true, PseudoBeforeRepository.class),
    PSEUDO_AFTER("descendant-or-self::*[contains(@class, 'measuringAfterElement')]", "#b9770e", -60, true, PseudoAfterRepository.class),
    SVG("descendant-or-self::*[name() = 'svg']", "#cd6155", -90, true, SvgRepository.class),
    ;

    private final String xpath;
    private final String color;
    private final int deg;
    private final boolean complex;
    private final Class<? extends IRepository> repositoryClass;
}
