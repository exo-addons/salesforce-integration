require( ["SHARED/jquery"], function ( $){
	$( "#target" ).click(function() {
	
var conf;

    conf = new Object();
    conf.clientId = $("#clientId").val();
    conf.clientSecret = $("#clientSecret").val();
    conf.redirectUri = $("#redirectUri").val();
    conf.sfSecuritySID = $("#sfSecuritySID").val();
    conf.sfURL = $("#sfURL").val();

    $.ajax({
        url: "/rest/private/salesforce/config",
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(conf),
        contentType: 'application/json',
        mimeType: 'application/json',
        
        success: function (data) {},
        error:function(data,status,er) {}
    });
	});
});


require( ["SHARED/jquery"], function ( $){
$(document).ready(function() {
    $.ajax({
        url: "/rest/private/salesforce/getconfig",
        cache: true,
        success: function (data) {
            $("#clientId").val(data.clientId);
            $("#clientSecret").val(data.clientSecret);
            $("#redirectUri").val(data.redirectUri);
            $("#sfSecuritySID").val(data.sfSecuritySID);
            $("#sfURL").val(data.sfURL);

        }
    })


});

});