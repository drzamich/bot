        var api_url = 'https://api.um.warszawa.pl/api/action/dbtimetable_get/?id=b27f4c17-5c50-4a5b-89dd-236b282bc499&apikey=e476edb4-e08c-4adc-ade1-c9b22ce99c64&name=Adamieckiego'
		
		
		$.ajax({
            url: api_url,
            type: 'GET',
            dataType: 'json',
			 crossDomain: true,
			  contentType: 'application/json',
            success: function(json) {
            var json = parseJson(json);
            console.log(json);
            },
        });

