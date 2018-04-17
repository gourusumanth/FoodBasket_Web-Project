function init() {
    autoCompletionTable = document.getElementById("completion-table");
};


function doCompletion() {
    var url = "AutoCompleteServlet?restaurantSearchId=" + escape(search.value);
    req = initRequest();
    req.open("GET", url, true);
    req.onreadystatechange = callback;
    req.send(null);
};

function initRequest() {
    isIE = false;
    if (window.XMLHttpRequest) {
        if (navigator.userAgent.indexOf('MSIE') != -1) {
            isIE = true;
        }
        return new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        isIE = true;
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
};

function callback() {
    clearTable();
    if (req.readyState == 4) {
        if (req.status == 200) {
            parseMessages(req.responseXML);
        }
    }
};

function clearTable() {
    if (autoCompletionTable.getElementsByTagName("tr").length > 0) {
        autoCompletionTable.style.display = 'none';
        for (loop = autoCompletionTable.childNodes.length - 1; loop >= 0; loop--) {
            autoCompletionTable.removeChild(autoCompletionTable.childNodes[loop]);
        }
    }
};

function parseMessages(responseXML) {
    if (responseXML == null) {
        return false;
    } else {
        var restaurants = responseXML.getElementsByTagName("restaurants")[0];
        if (restaurants.childNodes.length > 0) {
            for (var i = 0; i < restaurants.childNodes.length; i++) {
                var restaurant = restaurants.childNodes[i];
                var restaurantName = restaurant.getElementsByTagName("name")[0];
                var restaurantId = restaurant.getElementsByTagName("id")[0];
                appendRestaurant(restaurantName.childNodes[0].nodeValue,restaurantId.childNodes[0].nodeValue);
            }
        }
    }
};

function appendRestaurant(restaurantName, restaurantId) {
    var row;
    var cell;
    var linkElement;
    if(isIE){
        autoCompletionTable.style.display = 'block';
        row = autoCompletionTable.insertRow(autoCompletionTable.rows.length);
        cell = row.insertCell(0);
    }else{
        autoCompletionTable.style.display = 'table';
        row = document.createElement("tr");
        cell = document.createElement("td");
        row.appendChild(cell);
        autoCompletionTable.appendChild(row);
    }
    cell.className = "popupCell";
    cell.style.backgroundColor = "#23527c";
    cell.style.color="#FFFFFF";
    cell.style.border="0px";
    cell.style.padding = "10px 10px 10px 10px";
    row.style.border="0px";
    linkElement = document.createElement("a");
    linkElement.setAttribute("title",restaurantName);
    linkElement.setAttribute("style","color:#FFFFFF");
	linkElement.className = "popupItem";
	linkElement.setAttribute("href", "RestaurantMenuServlet?restaurantId="+restaurantId);
	linkElement.appendChild(document.createTextNode(restaurantName));
	cell.appendChild(linkElement);
};
