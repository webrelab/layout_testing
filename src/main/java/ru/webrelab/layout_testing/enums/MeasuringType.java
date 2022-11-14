package ru.webrelab.layout_testing.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.webrelab.layout_testing.ifaces.IMeasuringType;
import ru.webrelab.layout_testing.ifaces.IRepository;
import ru.webrelab.layout_testing.repository.DecorRepository;
import ru.webrelab.layout_testing.repository.TextRepository;

@RequiredArgsConstructor
@Getter
public enum MeasuringType implements IMeasuringType {
    POSITION(".", false, null),
    COMPLEX("", false, null),
    ALL(COMPLEX.xpath, COMPLEX.complex, COMPLEX.repositoryClass),
    TEXT("descendant-or-self::*[contains(@class, 'measuringTypeText')]", true, TextRepository.class),
    DECOR("descendant-or-self::*[contains(@class, 'measuringTypeDecor')]", true, DecorRepository.class),
    ;

    private final String xpath;
    private final boolean complex;
    private final Class<? extends IRepository> repositoryClass;
}
