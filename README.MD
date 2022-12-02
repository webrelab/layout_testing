### Plug-in for testing the layout of web application screens

This component allows you to detect changes in the visual display of the page based on a comparison of the current state
with the reference one.

This component does not use screenshots. The work is based on the calculation of the values of the attributes of styles,
sizes and arrangement of elements.

The component automatically finds all visually significant elements of the screen in the specified area and performs all
necessary measurements.

Testing can be done in one of the aspects or in all at once: design (border, border-radius, background, box-shadow),
typography (font, size, color, text-decoration, content, etc), images, SVG, pseudo-elements. You can add your own
aspects of testing.

You can test the entire screen or fragments. Testing outside the viewport is allowed.

Component mark all found elements

![snapshot](https://user-images.githubusercontent.com/9144386/205214281-d16f8945-6cc2-45c2-a447-c3bd21f9580d.png)

Component mark all wrong elements

![defect_found](https://user-images.githubusercontent.com/9144386/205214367-c590152e-6b81-4d57-b3c4-89f8f18d2290.png)

Comparison with testing by screenshots.

| Attributes                                                                                                          | Screenshots                                                                                                                                                                                                                 |
|---------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Whole page testing                                                                                                  | Testing in the viewport                                                                                                                                                                                                     |
| Testing on one or more aspects                                                                                      | Aspects cannot be distinguished from the screenshot                                                                                                                                                                         |
| Any elements can be excluded from testing                                                                           | You can't make exceptions in a screenshot                                                                                                                                                                                   |
| You can replace volatile data with asterisks                                                                        | It's not possible in the screenshot                                                                                                                                                                                         |
| You can include several elements located in different parts of the screen in one test at once                       | Separate elements are tested in several steps                                                                                                                                                                               |
| Allows you to ignore small deviations in the position and size of elements, while revealing any other discrepancies | Allows you to set the percentage of mismatched pixels, but it can include both acceptable deviations in position or size, as well as obvious errors such as missing rounding of corners or changing the color of the stroke |
| The speed of work directly depends on the number of elements included in the test                                   | High operating speed                                                                                                                                                                                                        |
| With a large number of elements of the same type, it can compare poorly and produce multiple discrepancies          | Does not depend on the number of elements                                                                                                                                                                                   |

#### Configuration

Create a class that implements IMethodsInjection and implement all the necessary methods in it.

In the LayoutConfiguration class, set the object of the class implementing IMethodsInjection and, if necessary, override default values.

Set the framework used on the project (two options are currently supported: Selenium WebDriver and Microsoft playwright):

```
LayoutConfiguration.INSTANCE.setFrameworkBasedBehavior(new SeleniumFrameworkBehavior()); // for Selenium
LayoutConfiguration.INSTANCE.setFrameworkBasedBehavior(new PlaywrightFrameworkBehavior()); // for Playwright
```

#### Preparing to run the test and run

Before starting the layout test, you must set one of the standard screen sizes. Can be used sizes from enum ScreenSize, or define your own class and pass it to LayoutConfiguration

```
ScreenSizeUtils.setWindowSize(ScreenSize.DESKTOP); // for Selenium
page.setViewportSize(ScreenSize.FULL_HD.getWidth(), ScreenSize.FULL_HD.getHeight()); // for Playwright
```

Next, you need to prepare a list of configurations for performing the layout test and run the test

```
// get the element inside which you want to perform the layout test
WebElement container = driver.findElement(By.xpath("//div[@id = 'container']"));
WebElement element1 = container.findElement(By.xpath("//div[contains(@class, 'element1')]"));
WebElement element2 = container.findElement(By.xpath("//div[contains(@class, 'element2')]"));
WebElement element3 = container.findElement(By.xpath("//div[contains(@class, 'element3')]"));

// add one or more items to test
final List<RawDataSet> dataSetList = new ArrayList<>();
dataSetList.add(new RawDataSet("element 1", element1, "TEXT, DECOR"))
           .add(new RawDataSet("element 2", element2, "IMAGE"))
           .add(new RawDataSet("element 3", element3, "ALL"));

// create an executor instance
final Executor executorDesktop = new Executor(
                dataSetList,
                "scenario_name",
                "path; to; file",
                "CHROME",
                container
        );

// run a test
executorDesktop.execute();
```

At the first launch, a JSON file will be created with a snapshot of the data received from the application page.\
The file will be written to src/test/resources/data/layouts/CHROME/DESKTOP/path/to/file/scenario_name.json\
according to the data from the example above.\
When restarted, the data received from the screen will be compared with those that were previously written to the json file.

#### Available test options

| Check Type    | Description                                                                                                                                                                  |
|---------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| POSITION      | Checking the position and size of a single element                                                                                                                           |
| ALL           | Includes verification of all aspects listed below at once                                                                                                                    |
| TEXT          | Checking elements containing text. The snapshot contains data about the font, text design and content                                                                        |
| IMAGE         | Checking <img> elements. The snapshot contains a relative path to the image or a base64 code                                                                                 |
| SVG           | Validation of SVG elements. The snapshot contains vector data with visual styles. If the element is hidden in the shadow dom - there will be an attempt to pull it out by id |
| DECOR         | Checking the visible appearance of elements. All screen elements that have visible styles such as border, background, box-shadow, radius will be found                       |
| PSEUDO_BEFORE | Checking for ':before' and ':after' pseudo-elements. Content, font color and background styles are written to the snapshot                                                   |
| PSEUDO_AFTER  |                                                                                                                                                                              |

Any check other than POSITION will automatically search for all matching elements within the element passed to check.
Data about all elements passed to one executor will be written to a single json file.
When searching for elements, the relationship between elements is also determined by the parent-child type.
Subsequently, this is used to limit the circle of matching elements when checking the layout. First, all elements from the highest level (root, parent) are checked.
Next, for the matched elements, the descendants are checked, then their descendants are checked.

#### Element Matching Mechanism

After the first run of the test, all measurements are taken and the data obtained is written to the json file.
With each subsequent launch, all measurements and comparison of data with the previously recorded json will be performed.
In view of the fact that tens and hundreds of elements can be checked at the same time on one screen, while some of the elements may not be 100% consistent with the size and location due to the difference in rendering on different operating systems, and also contain actual discrepancies both in location and size, and by specific parameters, here the mechanism of sifting all possible combinations of elements is used.

First, from two sets of elements (actual, received from the screen now and expected, received from the json file), all possible pairs of elements are generated.

Next comes the sifting:

- First, all pairs containing 100% match in all parameters are eliminated. Elements that also contain identified elements are removed from the common heap.
- Next, pairs containing identical data and an allowable deviation in position / size (admissible volatility of elements) are eliminated. Irrelevant elements are also removed from the general heap.
- Next, pairs are eliminated that have discrepancies in data (design, content), but are in the same position and have a similar size (taking into account the allowable volatility). These items are written to the bug report. The shared heap is cleared of irrelevant elements.
- Next, pairs are eliminated that have identical data, but differ in position / size. These pairs are written to the bug report. The heap is cleared of irrelevant elements.
- If unmatched elements remain after sifting, all of them are recorded in errors according to the principle "The element on the screen was not found to match from the file" and "The element from the file was not found on the screen"

If errors were found during sifting, colored grids will be drawn on the browser page over the elements in which errors were found. Based on this information (plus error reporting), layout problems can be quickly identified.

#### Restrictions

Due to the difficulty of identifying a large number of elements superimposed on each other, it is recommended to do atomic checks on rich interfaces (add one element or a block with a small number of elements to one test) for better test passing stability.
In different browsers (Chrome, Firefox, Safari) there may be significant differences in layout, so layout data is recorded separately for different browsers.

#### Functionality extension

You can use your permission sets, add support for other frameworks, add your own testing aspects.\
To do this, just add classes that implement this functionality and inject them into LayoutConfiguration.\
The IScreenSize interface is responsible for the set of screen sizes. For an example, you can see the implementation in enum ScreenSize.\
The IMeasuringType interface is responsible for a set of layout testing methods. For an example, you can see the implementation in enum MeasuringType. Important note - POSITION and ALL types need to be copied in your enum for compatibility.\
The IRepository interface is responsible for implementing the testing aspect of layout. For an example, you can look at any classes that inherit AttributeRepository.
