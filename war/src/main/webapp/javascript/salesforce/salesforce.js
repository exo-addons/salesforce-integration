$(document).ready(function() {
	var space;
	space = new Object();
	space.oppid = $("#oppid").val();
    setInterval(function() {
        $.ajax({
        	 url: "/rest/private/salesforce/update?"+space.oppid,
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
