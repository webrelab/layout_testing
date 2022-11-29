package ru.webrelab.layout_testing;

import lombok.SneakyThrows;
import ru.webrelab.layout_testing.ifaces.IFrameworkBasedBehavior;
import ru.webrelab.layout_testing.ifaces.IMeasuringType;
import ru.webrelab.layout_testing.ifaces.IMethodsInjection;
import ru.webrelab.layout_testing.ifaces.IScreenSize;
import ru.webrelab.layout_testing.repository.PositionRepository;
import ru.webrelab.layout_testing.repository.RawDataSet;
import ru.webrelab.layout_testing.screen_difference.DifferenceReport;
import ru.webrelab.layout_testing.snippets.Snippet;
import ru.webrelab.layout_testing.utils.DataTransformer;
import ru.webrelab.layout_testing.utils.ReadWriteUtils;
import ru.webrelab.layout_testing.utils.ScreenDraw;
import ru.webrelab.layout_testing.utils.ScreenSizeUtils;

import java.util.*;
import java.util.stream.Stream;

public class Executor {
    private final List<RawDataSet> dataSetList;
    private final String measureScenarioName;
    private final String[] storagePath;
    private final String browserName;
    private final PositionRepository container;
    private final IScreenSize currentScreenSize = ScreenSizeUtils.determineScreenSize();
    private final IMethodsInjection methods = LayoutConfiguration.INSTANCE.getMethodsInjection();
    private final Runnable prepareFunction;

    public Executor(
            final List<RawDataSet> dataSetList,
            final String measureScenarioName,
            final String storagePath,
            final String browserName,
            final Object containerElement
    ) {
        this(
                dataSetList,
                measureScenarioName,
                storagePath,
                browserName,
                containerElement,
                () -> {}
        );
    }

    public Executor(
            final List<RawDataSet> dataSetList,
            final String measureScenarioName,
            final String storagePath,
            final String browserName,
            final Object containerElement,
            final Runnable prepareFunction
    ) {
        this.dataSetList = dataSetList;
        this.measureScenarioName = measureScenarioName;
        this.storagePath = Stream.of(storagePath.split("[\\s,;]"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
        this.browserName = browserName;
        container = containerElement == null ? methods.getPosition(null, methods.findElementsByXpath("//body").get(0)) :
                methods.getPosition(null, containerElement);
        this.prepareFunction = prepareFunction;
    }

    @SneakyThrows
    public void execute() {
        prepare();
        final LayoutCollection actualLayoutElements = scanScreen();

        if (!ReadWriteUtils.isFileExist(measureScenarioName, browserName, currentScreenSize, storagePath)) {
            final ScreenDraw screenDraw = new ScreenDraw(container);
            actualLayoutElements.forEach((k, v) -> screenDraw.draw(ScreenDraw.CssClass.EXPECTED, v));
            ReadWriteUtils.write(
                    DataTransformer.serialize(actualLayoutElements),
                    measureScenarioName,
                    browserName,
                    currentScreenSize,
                    storagePath
            );
            methods.actionAfterSnapshotCreated();
            return;
        }
        final LayoutCollection expectedLayoutElements = DataTransformer.deserialize(
                ReadWriteUtils.read(
                        measureScenarioName,
                        browserName,
                        currentScreenSize,
                        storagePath
                )
        );
        final LayoutComparator comparator = new LayoutComparator(
                actualLayoutElements,
                expectedLayoutElements
        );

        final List<DifferenceReport> reports = comparator.compareRoot();
        if (!reports.isEmpty()) {
            new ScreenDraw(container).draw(reports);
            LayoutConfiguration.INSTANCE.getMethodsInjection().actionAfterTestFailed(reports);
        }
    }

    private void prepare() {
        prepareFunction.run();
        LayoutConfiguration.INSTANCE.getMethodsInjection().actionsBeforeTesting();
        final IFrameworkBasedBehavior behavior = LayoutConfiguration.INSTANCE.getFrameworkBasedBehavior();
        behavior.jsExecutor(Snippet.MEASURE_TEXT, null);
        behavior.jsExecutor(Snippet.MEASURE_PSEUDO_ELEMENTS, null);
        behavior.jsExecutor(Snippet.MEASURE_DECOR, null);
        behavior.jsExecutor(Snippet.MEASURE_PSEUDO_ELEMENTS, null);
    }

    private LayoutCollection scanScreen() {
        final LayoutCollection actualLayoutElements = new LayoutCollection();
        dataSetList.forEach(ds -> {
            for (final IMeasuringType type : ds.getMeasureTypes()) {
                final PageScanner scanner = new PageScanner(ds.getElementName(), container, ds.getElement(), type);
                final LayoutCollection elementsByType = scanner.scan();
                actualLayoutElements.putAll(elementsByType);
            }
        });
        final ElementsTreeGenerator elementsTreeGenerator = new ElementsTreeGenerator(actualLayoutElements);
        elementsTreeGenerator.createTree();
        elementsTreeGenerator.updateLayoutElements();
        return actualLayoutElements;
    }
}
