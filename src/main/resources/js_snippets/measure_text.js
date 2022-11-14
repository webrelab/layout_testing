function f(arg) {
    let t = arg.innerText.replace(/[\n\s]/g, '').toLowerCase();
    if (t === undefined || t.length === 0) {
        return;
    }
    [...arg.children]
        .forEach(e => {
            if (e.tagName === 'STYLE' || e.tagName === 'SCRIPT') return
            let r = e.innerText;
            if (r === undefined || r.length === 0) {
                return;
            }
            t = t.replace(r.replace(/[\n\s]/g, '').toLowerCase(), '');
            f(e);
        });
    if (t.trim().length > 0) {
        arg.classList.add('measuringTypeText');
    }
}

f(document.body);