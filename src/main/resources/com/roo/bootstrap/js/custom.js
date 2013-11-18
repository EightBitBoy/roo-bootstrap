$(document).ready(function(){
	$('.collapsible')
		.on('hidden.bs.collapse', function() {
			// delete this.id
			var cookies = document.cookie;
			console.log(cookies);
		})
		.on('shown.bs.collapse', function() {
			// store this.id
			document.cookie = this.id + "_isShown=true";
		});
});

/*
$(document).ready(function(){
    $(".collapse").collapse().each(function(){
        if(isStored(this.id)){
            $(this).collapse('hide');
        }
    });
});
*/