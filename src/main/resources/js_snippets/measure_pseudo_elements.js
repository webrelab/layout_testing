function handler(arg) {
    walker(document.body);
}

function walker(node) {
    [...node.children].forEach(e => {
        try {
            if (window.getComputedStyle(e).display === 'none') return;
            let after = window.getComputedStyle(e, ':after')
            if (
                after.getPropertyValue('content') !== 'none' &&
                after.getPropertyValue('height') !== '0px' &&
                after.getPropertyValue('width') !== '0px'
            ) {
                e.classList.add('measuringAfterElement');
            }
            let before = window.getComputedStyle(e, ':before');
            if (
                before.getPropertyValue('content') !== 'none' &&
                before.getPropertyValue('height') !== '0px' &&
                before.getPropertyValue('width') !== '0px'
            )  {
                e.classList.add('measuringBeforeElement');
            }
        } catch (f) {
        }
        walker(e);
    })
}