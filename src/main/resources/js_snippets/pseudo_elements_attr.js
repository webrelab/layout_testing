function handler(element) {
    return {
        before: collect(element, ':before'),
        after: collect(element, ':after')
    };
}

function collect(element, type) {
    let rect = element.getClientRects();
    let elementStyles = window.getComputedStyle(element, null);
    let attrStyles = window.getComputedStyle(element, type);
    let elementTop = rect[0].top + window.scrollY - document.documentElement.clientTop;
    let elementLeft = rect[0].left + window.scrollX - document.documentElement.clientLeft;
    let mT = parseInt(attrStyles.marginTop, 10);
    let mL = parseInt(attrStyles.marginLeft, 10);
    let t = parseInt(attrStyles.top,10);
    if (isNaN(t)) t = parseInt(elementStyles.paddingTop, 10);
    let l = parseInt(attrStyles.left,10);
    if (isNaN(l)) l = parseInt(elementStyles.paddingLeft, 10);
    return {
        content: attrStyles.content,
        height: parseInt(attrStyles.height, 10),
        width: parseInt(attrStyles.width, 10),
        top: Math.round( t + mT + elementTop),
        left: Math.round( l + mL + elementLeft),
        background: attrStyles.background,
        color: attrStyles.color,
        transform: attrStyles.transform
    }
}