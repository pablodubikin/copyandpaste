$(function() {

	// https://github.com/MatheusAvellar/textarea-line-numbers
	TLN.append_line_numbers( 'my-forms-id:main-textarea' );

	// https://stackoverflow.com/a/6637396
	$(document).delegate('#my-forms-id\\:main-textarea', 'keydown', function(e) {
		var keyCode = e.keyCode || e.which;

		if (keyCode == 9) {
			e.preventDefault();
			var start = this.selectionStart;
			var end = this.selectionEnd;

			// set textarea value to: text before caret + tab + text after caret
			$(this).val($(this).val().substring(0, start)
					+ "\t"
					+ $(this).val().substring(end));

			// put caret at right position again
			this.selectionStart =
				this.selectionEnd = start + 1;
		}
	});
	
});

function copyLink() {
	
	var dummy = document.createElement('input'),
	text = window.location.href;

	document.body.appendChild(dummy);
	dummy.value = text;
	dummy.select();
	document.execCommand('copy');
	document.body.removeChild(dummy);
}

function onMessageMine(beanSSId, beanEntryId, message, channel, event) {

	message = JSON.parse(message);
	
    if(beanSSId!=message.sessionId&&beanEntryId==message.entryId){

    	console.log("Update Is TRUE!");
    	
	    var c = document.getElementById("my-forms-id:main-textarea");
	    $(c).val(message.newContent);
	    TLN.remove_line_numbers( 'my-forms-id:main-textarea' );
	    TLN.append_line_numbers( 'my-forms-id:main-textarea' );
    }
}

function websocketCloseListener(code, channel, event) {
    if (code == -1) {
		// Web sockets not supported by client.
    	console.log("Web socket closed with code [" + code + "]. You're out!");
    } else if (code == 1000) {
        // Normal close (as result of expired session or view).
   	 	console.log("Web socket closed with code [" + code + "]");
		alert("Please, refresh!");
    } else {
        // Abnormal close reason (as result of an error).
   	 	console.log("Web socket closed with ERROR code [" + code + "]");
		alert("Please, refresh!");
    }
}

function navigateToDoc(docsUrl) {
	if (event.keyCode === 13) {
		window.location.assign(docsUrl);
	}
}
