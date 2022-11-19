function handler(map) {
    let a = [];
    for (let parentId in map) {
        for (let childId in map) {
            if (parentId === childId) continue
            if (map[parentId] === map[childId]) continue;
            if (map[parentId].contains(map[childId])) {
                a.push({'parent': parentId, 'child': childId})
            }
        }
    }
    return a;
}