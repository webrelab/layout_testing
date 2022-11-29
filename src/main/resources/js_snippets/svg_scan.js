function handler(element) {
    let data = [];
    let tag = element.tagName.toLowerCase();
    switch (tag) {
        case 'filter':
        case 'lineargradient':
            data.push({
                type: tag,
                defs: element.outerHTML.replace(/\n|\s{2}/g, '')
            });
            break;
        case 'g':
        case 'svg':
        case 'defs':
            [...element.children].forEach(e => {
                [...handler(e)].forEach(d => data.push(d));
            })
            break;
        case 'title':
            break;
        case 'use':
            let id = element.getAttribute("xlink:href").replace("#", "");
            [...handler(document.getElementById(id))].forEach(d => data.push(d));
            break;
        default:
            recordData(data, element, tag);
    }
    return data;
}

function recordData(data, e, tag) {
    data.push({
        type: tag,
        fill: window.getComputedStyle(e, null)['fill'],
        stroke: window.getComputedStyle(e, null)['stroke'],
        strokeWidth: window.getComputedStyle(e, null)['strokeWidth'],
        d: e.getAttribute('d'),
        cx: e.getAttribute('cx'),
        cy: e.getAttribute('cy'),
        r: e.getAttribute('r'),
        width: e.getAttribute('width'),
        height: e.getAttribute('height'),
        rx: e.getAttribute('rx'),
        ry: e.getAttribute('ry'),
        x1: e.getAttribute('x1'),
        y1: e.getAttribute('y1'),
        x2: e.getAttribute('x2'),
        y2: e.getAttribute('y2'),
        points: e.getAttribute('points'),
        x: e.getAttribute('x'),
        y: e.getAttribute('y'),
        content: e === 'text' ? e.text() : ''
    })
}