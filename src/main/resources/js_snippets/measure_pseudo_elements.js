function handler(node) {
    if (node === null || node === undefined) node = document.body;
    [...node.children].forEach(e => {
        try {
            let after = window.getComputedStyle(e, ':after')
            if (
                after.getPropertyValue('content') !== 'none' &&
                after.getPropertyValue('height') !== '0px' &&
                after.getPropertyValue('width') !== '0px' &&
                after.getPropertyValue('height') !== 'auto' &&
                after.getPropertyValue('width') !== 'auto'
            ) {
                e.classList.add('measuringAfterElement');
            }
            let before = window.getComputedStyle(e, ':before');
            if (
                before.getPropertyValue('content') !== 'none' &&
                before.getPropertyValue('height') !== '0px' &&
                before.getPropertyValue('width') !== '0px' &&
                before.getPropertyValue('height') !== 'auto' &&
                before.getPropertyValue('width') !== 'auto'
            )  {
                e.classList.add('measuringBeforeElement');
            }
        } catch (f) {
        }
        handler(e);
    })
}