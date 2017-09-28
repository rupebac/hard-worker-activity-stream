class ActsService {

  getActs( filter ){

	return new Promise( ( resolve, reject ) => {

		this._xhttp = new XMLHttpRequest();

        this._xhttp.open("GET", `http://localhost:8080/api/v0/acts?
									date=${new Date(filter.date).toJSON()}
									&jiraUser=${filter.jiraUser}
									&repoList=${filter.repoList}`, true);

        this._xhttp.send();

        this._xhttp.onreadystatechange = () => {
            if (this._xhttp.readyState == 4 && this._xhttp.status == 200) {
                let responseText = JSON.parse(this._xhttp.responseText);

                if(responseText.status == 200){
                    resolve(responseText.data);
                    return;
                }

            }

            if (this._xhttp.readyState == 4) {
                reject(this._xhttp.responseText);
                return;
            }
        };
	});
  }

}