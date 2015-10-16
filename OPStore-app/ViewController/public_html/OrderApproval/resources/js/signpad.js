(function () {
    getRenderSignatureBlock = function () {
        return function (amxNode, id) {
            width = amxNode.getAttribute('width'); 
            height = amxNode.getAttribute('height');
            
            // element to contain the SVG
            rootElement = document.createElement('div');
            rootElement.setAttribute('class', 'sig-container');
            // use pixel values to make sure the signature doesn't get squished
            rootElement.setAttribute('style', 'width: ' + width + 'px; height: ' + height + 'px;');

            // namespace is important when creating SVG elements
            var svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
            svg.setAttribute('id', amxNode.getAttribute('id') || 'svgSig');//FIXME: should increment or rand
            svg.setAttribute('width', width);
            svg.setAttribute('height', height);
            svg.setAttribute('value', amxNode.getAttribute('value'));
            // may or may not need to set this
            // svg.setAttribute('preserveAspectRatio', '');
            // may or may not be needed, however setting this way removes camel case
            // svg.setAttribute('viewBox', '0 0 ' + width + ' ' + height);
            rootElement.appendChild(svg);
            
            svg.appendChild(document.createElementNS('http://www.w3.org/2000/svg', 'defs'));

            // if there is a value returned from the EL value, it will set here, otherwise it will be empty
            var signaturePath = amxNode.getAttribute('value') || '';
            var isDown = false;

            function clearSignature(e) {
                signaturePath = '';
                p.setAttribute('d', signaturePath);
                amxNode.setAttribute('value', signaturePath);
                // don't let the events cause signature to be drawn when the clear button is pressed
                if(e) e.preventDefault();
            }

            function isTouchEvent(e) {
                return e.type.match(/^touch/);
            }

            function getCoords(e) {
                if (isTouchEvent(e)) {
                // must get screen position so the ink shows where the touch is accurately
                    br = e.touches[0].target.getBoundingClientRect();
                    return (e.targetTouches[0].clientX - br.left) + ',' + (e.targetTouches[0].clientY - br.top);
                }
                return e.clientX + ',' + e.clientY;
            }

            function down(e) {
                signaturePath += 'M' + getCoords(e) + ' ';
                p.setAttribute('d', signaturePath);
                isDown = true;
                if (isTouchEvent(e))
                    e.preventDefault();
            }

            function move(e) {
                if (isDown) {
                    signaturePath += 'L' + getCoords(e) + ' ';
                    p.setAttribute('d', signaturePath);
                }
                if (isTouchEvent(e))
                    e.preventDefault();
            }

            function up(e) {
                isDown = false;
                // don't need to update the value until drawing stops, update now
                amxNode.setAttribute('value', signaturePath);
                if (isTouchEvent(e))
                    e.preventDefault();
            }

            var r = document.createElementNS('http://www.w3.org/2000/svg', 'rect');
            r.setAttribute('id', 'rb');
            r.setAttribute('class', 'sig-block');
            r.setAttribute('width', width);
            r.setAttribute('height', height);

            // TODO: use AMX event bubbling here
            r.addEventListener('mousedown', down, false);
            r.addEventListener('mousemove', move, false);
            r.addEventListener('mouseup', up, false);
            r.addEventListener('touchstart', down, false);
            r.addEventListener('touchmove', move, false);
            r.addEventListener('touchend', up, false);
            r.addEventListener('mouseout', up, false);

            g = document.createElementNS('http://www.w3.org/2000/svg', 'g');
            g.setAttribute('role', 'group');
            g.appendChild(r);
            
            clrBtn = document.createElementNS('http://www.w3.org/2000/svg', 'text');
            clrBtn.setAttribute('class', 'clr-sig');
            clrBtn.setAttribute('x', '25');
            clrBtn.setAttribute('y', '25');
            
            clrBtn.addEventListener('touchstart', clearSignature, false);
            g.appendChild(clrBtn);
            clrBtn.appendChild(document.createTextNode('Clear'));

            svg.appendChild(g);

            var p = document.createElementNS('http://www.w3.org/2000/svg', 'path');
            p.setAttribute('class', 'sig-ink');
            p.setAttribute('pointer-events', 'none');
            p.setAttribute('d', signaturePath);
            g.appendChild(p);

            return rootElement;
        };
    }
    

    signatureBlock = adf.mf.api.amx.TypeHandler.register("http://xmlns.oracle.com/auraplayer",  'signature');
    signatureBlock.prototype.render = getRenderSignatureBlock();
    signatureBlock.prototype.getInputValueAttribute = function () {
            return 'value';
    }
    
    

})();