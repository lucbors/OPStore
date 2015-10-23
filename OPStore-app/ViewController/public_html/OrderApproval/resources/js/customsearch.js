(function()
{
  var timeout;

  try
  {
    // TypeHandler for custom "csearch" tags:
    var csearch = adf.mf.api.amx.TypeHandler.register("http://xmlns.example.com/custom", "csearch");

    /**
     * This method renders the search component
     */
    csearch.prototype.render = function(amxNode, id)
    {
      var rootElement = document.createElement("div");
      var subElement = document.createElement("div");

      try
      {
        rootElement.className = "csearch";

        var hintText = amx.getTextValue(amxNode.getAttribute("hintText"));
        var inputElement = document.createElement("input");

        inputElement.id = id+"inputSearchElement";
        inputElement.setAttribute("type", "text");
        inputElement.setAttribute("class", "search rounded");
        inputElement.setAttribute("placeholder", hintText);
        inputElement.setAttribute("role", "textbox");
        inputElement.setAttribute("autocomplete", "off");
        inputElement.setAttribute("autocorrect", "off");
        inputElement.setAttribute("autocapitalize", "off");

        subElement.appendChild(inputElement);

        var anchorElement = document.createElement("a");
        anchorElement.id = id+"anchor";
        anchorElement.setAttribute("href","#");
        anchorElement.setAttribute("class","clear_button");
        subElement.appendChild(anchorElement);
        rootElement.appendChild(subElement);

        var eventData =
          {
            "inputElement.id": inputElement.id,
            "anchorElement.id": anchorElement.id,
            "amxNode": amxNode,
            "context": this
          };

        adf.mf.api.amx.addBubbleEventListener(
          inputElement,
          "keyup",
          this._handleTextChange,
          eventData);

        adf.mf.api.amx.addBubbleEventListener(
          anchorElement,
          "tap",
          this._clearText,
          eventData);
      }
      catch (problem)
      {
        adf.mf.log.Framework.logp(
          adf.mf.log.level.SEVERE,
          "example",
          "render",
          "Problem with custom component creation: " + problem);
      }
      return rootElement;
    };

    csearch.prototype.createChildrenNodes = function(amxNode)
    {
      // Call the register input value during node creation as it requires the EL context
      // to be setup and rendering is not performed in EL context (expects all EL to already
      // be resolved during rendering)
      amx.registerInputValue(amxNode, "value");

      // Return false to let the framework create the children
      return false;
    };


    /**
     * This method is called by the framework while executing the _handleText method to refresh the
     * search component without rerendering it
     */
    csearch.prototype.updateChildren = function(amxNode, attributeChanges)
    {
      if (attributeChanges.hasChanged("value"))
      {
        return adf.mf.api.amx.AmxNodeChangeResult["REFRESH"];
      }
      return adf.mf.api.amx.AmxNodeChangeResult["RERENDER"];
    };

    /**
     * This method is called when the updateChildren method returns a refresh
     * result.
     */
    csearch.prototype.refresh = function(amxNode, attributeChanges)
    {
      if (attributeChanges.hasChanged("value"))
      {
        var inputField = document.getElementById(amxNode.getId() + "inputSearchElement");
        var newValue = amxNode.getAttribute("value");
        inputField.value = newValue;
      }
    };

    /**
     * This method is invoked on keyup event (while entering the text within the search component).
     */
    csearch.prototype._handleTextChange = function(event, forced)
    {
      var inputElementId = event.data["inputElement.id"];
      var amxNode = event.data["amxNode"];
      var anchorElementId = event.data["anchorElement.id"];
      var anchorElement = document.getElementById(anchorElementId);
      var inputElement = document.getElementById(inputElementId);
      var inputElementValue = inputElement.value;

      if (inputElementValue != null && inputElementValue.length > 0)
        anchorElement.className = "clear_button_visible";
      else
        anchorElement.className = "clear_button";

      var oldInputElementValue = null;
      var vs = amxNode.getVolatileState();
      if (vs != null)
      oldInputElementValue = ["oldInputElementValue"];


      if (oldInputElementValue != inputElementValue || forced)
      {
        amxNode.setVolatileState({ "oldInputElementValue": inputElementValue });
        inputElement.setAttribute("value", inputElementValue);

        if (timeout)
        {
          clearTimeout(timeout);
        }

        // Start new timeout
        timeout = setTimeout(function()
          {
            var vce = new adf.mf.api.amx.ValueChangeEvent(oldInputElementValue, inputElementValue);
            var success = function() {}; // do nothing
            adf.mf.api.amx.processAmxEvent(
              amxNode,
              "valueChange",
              "value",
              inputElementValue,
              vce,
              success);
          },
          400);
      }
    };


    /**
     * This method clears the text within the search input text compoent on
     * tapping the X icon.
     */
    csearch.prototype._clearText = function(event)
    {
      var inputElementId = event.data["inputElement.id"];
      var inputElement = document.getElementById(inputElementId);
      inputElement.value = "";
      var anchorElementId = event.data["anchorElement.id"];
      var anchorElement = document.getElementById(anchorElementId);
      anchorElement.className = "clear_button";
      var context = event.data["context"];
      context._handleTextChange(event, true);
    };
  }
  catch (problem)
  {
    adf.mf.log.Framework.logp(
      adf.mf.log.level.SEVERE,
      "Custom.js",
      "*",
      "Problem with custom code: " + problem);
  }

})();