package ru.webrelab.layout_testing;

import ru.webrelab.layout_testing.ifaces.IMeasuringType;
import ru.webrelab.layout_testing.ifaces.IMethodsInjection;
import ru.webrelab.layout_testing.ifaces.IScreenSize;
import ru.webrelab.layout_testing.repository.PositionRepository;
import ru.webrelab.layout_testing.repository.RawDataSet;
import ru.webrelab.layout_testing.screen_difference.DifferenceReport;
import ru.webrelab.layout_testing.snippets.Snippet;
import ru.webrelab.layout_testing.snippets.SnippetsRepository;
import ru.webrelab.layout_testing.utils.DataTransformer;
import ru.webrelab.layout_testing.utils.ReadWriteUtils;
import ru.webrelab.layout_testing.utils.ScreenDraw;
import ru.webrelab.layout_testing.utils.ScreenSizeUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Executor {
    private final List<RawDataSet> dataSetList;
    private final String measureScenarioName;
    private final String[] storagePath;
    private final String browserName;
    private final PositionRepository container;
    private final IScreenSize currentScreenSize = ScreenSizeUtils.determineScreenSize();
    private final IMethodsInjection methods = LayoutConfiguration.INSTANCE.getMethods();

    public Executor(
            final List<RawDataSet> dataSetList,
            final String measureScenarioName,
            final String storagePath,
            final String browserName,
            final Object containerElement
    ) {
        this.dataSetList = dataSetList;
        this.measureScenarioName = measureScenarioName;
        this.storagePath = Stream.of(storagePath.split("[\\s,;]"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
        this.browserName = browserName;
        final PositionRepository initialPosition = new PositionRepository(0, 0);
        container = containerElement == null ? initialPosition :
                methods.getPosition(initialPosition, containerElement);
    }

    public void execute() {
        prepare();
        final List<LayoutElement> actualLayoutElements = scanScreen();

        if (!ReadWriteUtils.isFileExist(measureScenarioName, browserName, currentScreenSize, storagePath)) {
            ReadWriteUtils.write(
                    DataTransformer.serialize(actualLayoutElements),
                    measureScenarioName,
                    browserName,
                    currentScreenSize,
                    storagePath
            );
            return;
        }
        final List<LayoutElement> expectedLayoutElements = DataTransformer.deserialize(
                ReadWriteUtils.read(
                        measureScenarioName,
                        browserName,
                        currentScreenSize,
                        storagePath
                )
        );
        final LayoutComparator comparator = new LayoutComparator(
                createTree(actualLayoutElements),
                createTree(expectedLayoutElements)
        );

        final List<DifferenceReport> reports = comparator.compare();
        if (!reports.isEmpty()) {
            new ScreenDraw(container).draw(reports);
        }
    }

    private void prepare() {
        methods.executeJs(SnippetsRepository.INSTANCE.getSnippet(Snippet.MEASURE_TEXT.getSnippet()));
        methods.executeJs(SnippetsRepository.INSTANCE.getSnippet(Snippet.MEASURE_BEFORE_AFTER.getSnippet()));
        methods.executeJs(SnippetsRepository.INSTANCE.getSnippet(Snippet.MEASURE_DECOR.getSnippet()));
    }

    private List<LayoutElement> scanScreen() {
        final List<LayoutElement> actualLayoutElements = new ArrayList<>();
        dataSetList.forEach(ds -> {
            for (final IMeasuringType type : ds.getMeasureTypes()) {
                final PageScanner scanner = new PageScanner(ds.getElementName(), container, ds.getElement(), type);
                actualLayoutElements.addAll(scanner.scan());
            }
        });
        final Map<String, LayoutElement> uuidMap = actualLayoutElements
                .stream()
                .collect(Collectors.toMap(LayoutElement::getUuid, e -> e, (a, b) -> a));
        final ElementsTreeGenerator elementsTreeGenerator = new ElementsTreeGenerator();
        actualLayoutElements.forEach(e -> elementsTreeGenerator.put(e.getUuid(), e.getElement()));
        final Tree<String> tree = elementsTreeGenerator.createTree();
        final Stack<Tree<String>> stack = new Stack<>();
        stack.push(tree);
        while (!stack.isEmpty()) {
            final Tree<String> temp = stack.pop();
            if (temp.hasParent()) {
                uuidMap.get(temp.getValue()).setParent(temp.getParent().getValue());
            }
            if (temp.hasChildren()) {
                temp.getChildren().stream()
                        .map(Tree::getValue)
                        .forEach(uuidMap.get(temp.getValue())::addChild);
                temp.getChildren().forEach(stack::push);
            }
        }
        if (actualLayoutElements.isEmpty()) {
            throw new LayoutTestingException("No one element found");
        }
        return actualLayoutElements;
    }

    private Tree<LayoutElement> createTree(final List<LayoutElement> elements) {
        final Map<String, LayoutElement> uuidMap = new HashMap<>();
        elements.forEach(e -> uuidMap.put(e.getUuid(), e));
        final Tree<LayoutElement> treeRoot = new Tree<>(null);
        elements.stream()
                .filter(e -> e.getParent() == null)
                .forEach(e -> new Tree<>(e, treeRoot));
        final Queue<Tree<LayoutElement>> queue = new LinkedList<>();
        queue.offer(treeRoot);
        while (!queue.isEmpty()) {
            final Tree<LayoutElement> temp = queue.poll();
            if (!temp.getValue().getChildren().isEmpty()) {
                temp.getValue().getChildren().forEach(ch -> {
                    final Tree<LayoutElement> childTree = new Tree<>(uuidMap.get(ch), temp);
                    queue.offer(childTree);
                });
            }
        }
        return treeRoot;
    }


}
