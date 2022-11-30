function handler() {
    walker(document.body);
}

function walker(node) {
    if (node.tagName === 'STYLE' || node.tagName === 'SCRIPT') return
    let t = node.innerText.replace(/[\n\s]/g, '').toLowerCase();
    if (t === undefined || t.length === 0) return;
    if (window.getComputedStyle(node).display === 'none') return;
    [...node.children]
        .forEach(e => {
            let r = e.innerText;
            if (r === undefined || r.length === 0) return;
            t = t.replace(r.replace(/[\n\s]/g, '').toLowerCase(), '');
            walker(e);
        });
    if (t.trim().length > 0) {
        node.classList.add('measuringTypeText');
    }
}