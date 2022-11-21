function handler(attributeToElementNameMap) {
    let keys = Object.keys(attributeToElementNameMap);
    return attributeToElementNameMap[keys[0]].getAttribute(keys[0]);
}