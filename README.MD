### Подключаемый модуль для тестирования верстки экранов веб приложений.

#### Настройка конфигурации
Создать класс, имплементирующий IMethodsInjection и реализовать в нём все необходимые методы

В класс LayoutConfiguration установить объект класса имплементирующего IMethodsInjection и по необходимости переопределить стандартные значения

Перед запуском теста верстки необходимо установить один из стандартных размеров экрана. Можно использовать
размеры из enum ScreenSize, либо определить свой класс и передать его в LayoutConfiguration

```
ScreenSizeUtils.setWindowSize(ScreenSize.DESKTOP);
```

Далее нужно подготовить список конфигураций для выполнения теста верстки и запустить тест

```
// получаем элемент, внутри которого нужно выполнить тест верстки
WebElement container = driver.findElement(By.xpath("//div[@id = 'container']"));
WebElement element1 = container.findElement(By.xpath("//div[contains(@class, 'element1')]"));
WebElement element2 = container.findElement(By.xpath("//div[contains(@class, 'element2')]"));
WebElement element3 = container.findElement(By.xpath("//div[contains(@class, 'element3')]"));
final List<RawDataSet> dataSetList = new ArrayList<>();
// добавляем один или более элементов для тестирования
dataSetList.add(new RawDataSet("element 1", container, "TEXT, DECOR"))
           .add(new RawDataSet("element 2", container, "IMAGE"))
           .add(new RawDataSet("element 3", container, "ALL"));
final Executor executorDesktop = new Executor(
                dataSetList,
                "scenario_name",
                "path; to; file",
                "CHROME",
                container
        );
executorDesktop.execute();
```
При первом запуске будет создан JSON файл со снапшотом полученных со страницы приложения данными.\
Файл будет записан в src/test/resources/data/layouts/CHROME/DESKTOP/path/to/file/scenario_name.json\
в соответствии с данными из примера выше.\
При повторном запуске будет выполнено сравнение данных полученных с экрана с теми, что были ранее записаны в json файл.