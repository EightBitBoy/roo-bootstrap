//persist collapsible menu
$(document).ready(function(){
	$('.collapsible')
		//set cookie
		.on('shown.bs.collapse', function() {
			$.cookie(this.id + "_isShown", "true");
		})
		//delete cookie
		.on('hidden.bs.collapse', function() {
			$.removeCookie(this.id + "_isShown");
		});
});

//expand collapsed menu
$(document).ready(function(){
    $(".collapsible").each(function(){
    	if($.cookie(this.id + "_isShown") == "true"){
    		console.log("foo");
    		$(this).addClass("in");
    	}
    });
});