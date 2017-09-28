class BitbucketAuth{

		authenticate(){
		    let xhttp = new XMLHttpRequest();

		    xhttp.open("GET", "http://localhost:8080/api/v0/bitbucket/auth", true);

		    xhttp.onreadystatechange = function()
		    {
		        window.location.href = xhttp.responseURL;
		    };

		    xhttp.send();
		}


}