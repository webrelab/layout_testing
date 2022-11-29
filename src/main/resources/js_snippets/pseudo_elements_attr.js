function handler(element) {
    return {
        before: collect(element, ':before'),
        after: collect(element, ':after')
    };
}

function collect(element, type) {
    let elementStyles = window.getComputedStyle(element, null);
    let pseudoElementStyles = window.getComputedStyle(element, type);
    let mT = parseInt(pseudoElementStyles.marginTop);
    let mL = parseInt(pseudoElementStyles.marginLeft);
    let t = parseInt(pseudoElementStyles.top);
    if (isNaN(t)) t = parseInt(elementStyles.paddingTop);
    let l = parseInt(pseudoElementStyles.left);
    if (isNaN(l)) l = parseInt(elementStyles.paddingLeft);
    let height = pseudoElementStyles.height === 'auto' ?
        parseInt(pseudoElementStyles.lineHeight)
        : parseInt(pseudoElementStyles.height);
    let width = pseudoElementStyles.width === 'auto' ?
        parseInt(elementStyles.width)
        - parseInt(elementStyles.paddingLeft)
        - parseInt(elementStyles.paddingRight)
        - mL
        - parseInt(pseudoElementStyles.marginRight)
        : parseInt(pseudoElementStyles.width);
    return {
        content: pseudoElementStyles.content,
        height: height,
        width: width < 0 ? 0 : width,
        topOffset: Math.round(t + mT),
        leftOffset: Math.round(l + mL),
        background: pseudoElementStyles.background,
        color: pseudoElementStyles.color,
        transform: pseudoElementStyles.transform
    }
}