$(document).ready(function() {
	var conf;
    setInterval(function() {
        $.ajax({
        	 url: "/rest/private/salesforce/update",
             cache: true,
            success: function() {
                //handle success

                //alert(good)
            },
            failure: function() {
 

            }
        });
    }, 10000);
});
