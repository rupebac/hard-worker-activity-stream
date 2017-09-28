class Alert {

    constructor(featuredMessage, message, type ){
        let $ = document.querySelector.bind(document);
        this._success_alerts = $("#alerts");

        if (featuredMessage && message && type){
            this._render( featuredMessage, message , type );
        }
    }


    _render( featuredMessage, message , type ){
        this._success_alerts.innerHTML = 
        `<div id="${type}-alert" class="alert alert-${type} 
        alert-dismissible fade show fixed-top" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <strong>${featuredMessage}</strong> ${message}
        </div>`
    }

    close() {
        if($("#alerts .close")){
            $("#alerts .close").click();
        }
    };

}