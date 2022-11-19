function handler(element) {
    let data = [];
    [...element.children].forEach(e => {
        let tag = e.tagName.toLowerCase();
        if (tag === 'defs') {
            data.push({
                type: tag,
                defs: e.innerHTML
            });
            return;
        }
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
    })
    return data;
}