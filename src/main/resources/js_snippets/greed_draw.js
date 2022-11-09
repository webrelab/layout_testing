function greed_draw(arg) {
    let div = document.createElement('div');
    div.classList.add(arg['color']);
    div.style.cssText =
        'top:' + arg['top'] + 'px;' +
        'left:' + arg['left'] + 'px;' +
        'width:' + arg['width'] + 'px;' +
        'height:' + arg['height'] + 'px;';
    document.getElementById('visualisationContainer').appendChild(div);
}

greed_draw(arguments[0]);