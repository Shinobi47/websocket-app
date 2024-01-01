function createStompClient(brokerURL, returnEndpoint){
	const stompClient = new StompJs.Client({
		//brokerURL: 'ws://localhost:8080/topic-channel'
		//brokerURL: 'ws://localhost:8080/user-channel'
		brokerURL: brokerURL,
		connectHeaders: {
			Authorization: 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzdWJqZWN0MSIsIm5hbWUiOiJKb2huIERvZSIsImlhdCI6MTUxNjIzOTAyMiwiYXV0aG9yaXRpZXMiOlsiUk9PVCJdfQ.ea9kCyGG8oEVVEMAWGN99Y-s9AEJtECPI2xro9X68UU',
		},
		beforeConnect: (headers, url) => {
			headers = headers || {};
            // Authorization header for subsequent HTTP requests
            headers.Authorization = 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzdWJqZWN0MSIsIm5hbWUiOiJKb2huIERvZSIsImlhdCI6MTUxNjIzOTAyMiwiYXV0aG9yaXRpZXMiOlsiUk9PVCJdfQ.ea9kCyGG8oEVVEMAWGN99Y-s9AEJtECPI2xro9X68UU';
            return headers;
        }
	});


	stompClient.onConnect = (frame) => {
		setConnected(true);
		console.log('Connected: ' + frame);
		stompClient.subscribe(returnEndpoint, (result) => {
			console.log(result)
			console.log(JSON.parse(result.body).body)
			showResult(JSON.parse(result.body).body);
		});
	};

	stompClient.onWebSocketError = (error) => {
		console.error('Error with websocket', error);
	};

	stompClient.onStompError = (frame) => {
		console.error('Broker reported error: ' + frame.headers['message']);
		console.error('Additional details: ' + frame.body);
	};
	return stompClient;
}



function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#response").html("");
}

function connect() {
	const brokerURL = 'ws://localhost:8080/' + $("#brokerEndpoint").val()
	const returnEndpoint = $("#returnEndpoint").val() //'/user/topic/u1';

	stompClient = createStompClient(brokerURL, returnEndpoint);
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
	console.log($("#destinationEndpoint").val())
	console.log($("#msg").val())
    stompClient.publish({

        //destination: "/app/hello",
		destination: $("#destinationEndpoint").val(),
        body: JSON.stringify({'name': $("#msg").val()}),
		headers: { Authorization: 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzdWJqZWN0MSIsIm5hbWUiOiJKb2huIERvZSIsImlhdCI6MTUxNjIzOTAyMiwiYXV0aG9yaXRpZXMiOlsiUk9PVCJdfQ.ea9kCyGG8oEVVEMAWGN99Y-s9AEJtECPI2xro9X68UU' }
    });

}

function showResult(message) {
    $("#response").append("<tr><td> User id: " + message.id + "</td></tr>");
	$("#response").append("<tr><td> User name: " + message.name + "</td></tr>");
	$("#response").append("<tr><td> User email: " + message.email + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
});