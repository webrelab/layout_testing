function handler(arg) {
    let div = document.createElement('div');
    div.classList.add(arg['className'] + "_grid");
    div.id = arg['id'];
    div.style.cssText =
        'top:' + arg['top'] + 'px;' +
        'left:' + arg['left'] + 'px;' +
        'width:' + arg['width'] + 'px;' +
        'height:' + arg['height'] + 'px;' +
        'transform:' + arg['transform'] + ';';
    document.getElementById('visualisationContainer').appendChild(div);
}