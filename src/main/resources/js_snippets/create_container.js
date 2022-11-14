(function () {
    let t = document.createElement('div');
    t.id = 'visualisationContainer';
    t.innerHTML = '<style>\n' +
        '#visualisationContainer{overflow:hidden;z-index:9999999;position:absolute;top:0;left:0;right:0;height:' + getMaxHeight() + 'px;}\n' +
        '.actual_grid{position: absolute;background-image: repeating-linear-gradient(45deg, transparent 0 10px, ACTUAL_COLOR_VALUE 10.2px 10.96px);' +
        'border: 1px solid ACTUAL_COLOR_VALUE;}\n' +
        '.expected_grid{position: absolute;background-image: repeating-linear-gradient(-45deg, transparent 0 10px, EXPECTED_COLOR_VALUE 10.2px 10.96px);' +
        'border: 1px solid EXPECTED_COLOR_VALUE;}\n' +
        '</style>';
    document.body.appendChild(t);
}());

function getMaxHeight() {
    let currentMax = {'value': 0};
    getMaxHeightWalker(document.body, currentMax);
    return currentMax.value;
}

function getMaxHeightWalker(currentNode, currentMax) {
    [...currentNode.children]
        .forEach(c => {
            if ('scrollHeight' in c && c.scrollHeight > currentMax.value) {
                currentMax.value = c.scrollHeight;
            }
            getMaxHeightWalker(c, currentMax);
        })
}