let decorMeasureClasses = function (argument) {
    let nodes = ['div', 'a', 'p', 'button', 'span', 'section', 'aside', 'input', 'textarea'];
    nodes.forEach(node => {
        [...argument.getElementsByTagName(node)]
            .forEach(e => {
                let c = window.getComputedStyle(e);
                if (c['display'] !== 'none' && (c['borderRadius'] !== '0px' || c['borderWidth'] !== '0px' ||
                    (c['backgroundColor'] !== 'rgba(0, 0, 0, 0)' || c['backgroundImage'] !== 'none') ||
                    c['boxShadow'] !== 'none')) {
                    e.classList.add('measuringTypeDecor');
                }
            });
    });
};
decorMeasureClasses(document.body);