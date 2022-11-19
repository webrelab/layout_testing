function handler(node) {
    if (node === null || node === undefined) node = document.body;
    [...node.children].forEach(e => {
        try {
            if (window.getComputedStyle(e, ':after')
                .getPropertyValue('content') !== 'none') {
                e.classList.add('layoutAfterElement');
            }
            if (window.getComputedStyle(e, ':before').getPropertyValue('content') !== 'none') {
                e.classList.add('layoutBeforeElement');
            }
        } catch (f) {
        }
        handler(e);
    })
}