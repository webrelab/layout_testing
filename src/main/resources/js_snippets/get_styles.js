function handler(element) {
    let v = window.getComputedStyle(element, null), d = [];
    for (let q in v) {
        if (!isNaN(parseFloat(q)) && isFinite(parseFloat(q))) continue;
        if (q === undefined || v[q] === null || typeof v[q] === 'function') continue;
        d.push({name: q, value: v[q]});
    }
    return d;
}
