class CalendarAuth {

    authenticate() {
        let xhttp = new XMLHttpRequest();

        xhttp.open("GET", "http://localhost:8080/api/v0/calendar/auth", true);

        xhttp.onreadystatechange = () => {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                let responseText = JSON.parse(xhttp.responseText);
                window.location.href = responseText.data;
            }
        }


        xhttp.send();
    }


}