package ru.webrelab.layout_testing.ifaces;

public interface IMeasuringType {
    String getXpath();
    boolean isComplex();
    Class<? extends IRepository> getRepositoryClass();
}
