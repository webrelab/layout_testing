function handler(element) {
    return {
        width: element.clientWidth === 0 ? element.offsetWidth : element.clientWidth,
        height: element.clientHeight === 0 ? element.offsetHeight : element.clientHeight
    }
}