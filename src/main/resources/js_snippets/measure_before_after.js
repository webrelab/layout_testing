function beforeAfterWalker(node) {
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
        beforeAfterWalker(e);
    })
}

beforeAfterWalker(document.body);