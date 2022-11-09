package ru.webrelab.layout_testing.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SizeRepository extends DimensionRepository {
    private final int height;
    private final int width;

    public boolean isEmpty() {
        return height == 0 || width == 0;
    }
}
