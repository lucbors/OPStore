(function () {

    // This method clears the signature area
    doClear = function () {
        var sigElement = document.getElementById("sig");
        if (sigElement == null)
            alert("sigElement not found");
        var sig = $(sigElement);
        sig.signature('clear');
        //adf.mf.api.invokeMethod("mobile", "FetchCallback", "", onInvokeSuccess, onFail);
    };

    // This method gets the signature as a JSON string.  As an example, it does an alert of the string and then sends it to a Java handler for further processing
    doFetch = function () {
        var sigElement = document.getElementById("sig");
        if (sigElement == null)
            alert("sigElement not found");
        var sig = $(sigElement);
        var fetchData = sig.signature('toJSON');
        adf.mf.api.invokeMethod("mobile", "FetchCallback", fetchData, onInvokeSuccess, onFail);
    };

    doAlert = function (message) {
        //navigator.notification.alert(message);
        alert("hello");
    };

    function onInvokeSuccess(param) {
    };

    function onFail() {
        alert("It failed");
    };

})();