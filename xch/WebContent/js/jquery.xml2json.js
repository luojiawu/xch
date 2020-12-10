function XmlToJson() {
}
XmlToJson.prototype.setXml = function(xml) {
    if(xml && typeof xml == "string") {
        this.xml = document.createElement("div");
        this.xml.innerHTML = xml;
        this.xml = this.xml.getElementsByTagName("*")[0];
    }
    else if(typeof xml == "object"){
        this.xml = xml;
    }
};
XmlToJson.prototype.getXml = function() {
    return this.xml;
};
XmlToJson.prototype.parse = function(xml) {
    this.setXml(xml);
    return this.convert(this.xml);
};
XmlToJson.prototype.convert = function(xml) {
    if (xml.nodeType != 1) {
        return null;
    }
    var obj = {};
    obj.xtype = xml.nodeName.toLowerCase();
    var nodeValue = (xml.textContent || "").replace(/(\r|\n)/g, "").replace(/^\s+|\s+$/g, "");
    
    if(nodeValue && xml.childNodes.length == 1) {
        obj.text = nodeValue;
    }
    if (xml.attributes.length > 0) {
        for (var j = 0; j < xml.attributes.length; j++) {
            var attribute = xml.attributes.item(j);
            obj[attribute.nodeName] = attribute.nodeValue;
        }
    }
    if (xml.childNodes.length > 0) {
        var items = [];
        for(var i = 0; i < xml.childNodes.length; i++) {
            var node = xml.childNodes.item(i);
            var item = this.convert(node);
            if(item) {
                items.push(item);
            }
        }
        if(items.length > 0) {
            obj.items = items;
        }
    }
    return obj;
};