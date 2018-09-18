var a = $('#RozkladContent').find('a');
var api_key = 'e476edb4-e08c-4adc-ade1-c9b22ce99c64';
var link_root = 'https://api.um.warszawa.pl/api/action/dbtimetable_get/?id=b27f4c17-5c50-4a5b-89dd-236b282bc499&apikey=' + api_key + '&name=';

$('body').html("");
var stations = [];
function getAjax(callback) {
    a.each(function () {
        var station = [];
        var t = $(this).text();
        if (t.indexOf("Warszawa") != -1) {
            var href = $(this).attr('href');
            var fullHref = 'http://www.ztm.waw.pl/' + href;
            var id = getURLParameter(href, 'a');
            var stationName = t.split(' (')[0];
            station['name'] = stationName;
            station['id'] = id;
            station['platforms'] = [];
            $.get(fullHref, function (response) {
                response = $($.parseHTML(response));
                var s = response.find('a').filter(':contains(' + stationName + ')');
                s.each(function () {
                    var platform = [];
                    var text = $(this).text();
                    var text_split = text.split(" ");
                    var len = text_split.length;
                    var platform_number = text_split[len - 1];
                    var direction = $(this).next().text();
                    if (direction.indexOf('_______') == -1) {
                        platform['number'] = platform_number;
                        platform['direction'] = direction;
                        station['platforms'].push(platform);
                    }
                });
                //console.log(station);
                stations.push(station);
            });
        }

    });
    callback();
    return stations;
}


getAjax(print_stations);

function print_stations(){
    console.log(stations);
}
//stations = JSON.stringify(stations);
//$('body').html(stations);
// $(document).ajaxComplete(function(){
//     console.log(stations);
// });


function getURLParameter(url, name) {
    return (RegExp(name + '=' + '(.+?)(&|$)').exec(url) || [, null])[1];
}



