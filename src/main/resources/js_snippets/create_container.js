function handler(arg) {
    let styleText = [];
    let gridStyle = ".{TYPE}_grid{" +
        "position: absolute;" +
        "background-image: repeating-linear-gradient({DEG}deg, transparent 0 10px, {COLOR} 10.2px 11.5px);" +
        "border: 1px solid {COLOR};}";
    styleText.push('#visualisationContainer{' +
        'overflow:hidden;' +
        'z-index:9999999;' +
        'position:absolute;' +
        'top:0;' +
        'left:0;' +
        'right:0;' +
        'height:' + getMaxHeight(document.body) + 'px;}');
    for (let n in arg) {
        styleText.push(
            gridStyle
                .replaceAll('{TYPE}', arg[n]['type'])
                .replaceAll('{DEG}', arg[n]['deg'])
                .replaceAll('{COLOR}', arg[n]['color'])
        );
    }
    let style = document.createElement("style");
    style.innerText = styleText.join(" ");
    let div = document.createElement('div');
    div.id = 'visualisationContainer';
    div.appendChild(style);
    document.body.appendChild(div);
}

function getMaxHeight(node) {
    let max = 'scrollHeight' in node ? node.scrollHeight : node.clientHeight;
    [...node.children].forEach(e => max = Math.max(max, getMaxHeight(e)));
    return max;
}