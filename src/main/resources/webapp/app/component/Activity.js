class Activity {

    constructor(element) {
        let $ = document.querySelector.bind(document);
        this._element = $(".list-group");
    }

    hashCode(str) { // java String#hashCode
        var hash = 0;
        for (var i = 0; i < str.length; i++) {
           hash = str.charCodeAt(i) + ((hash << 5) - hash);
        }
        return hash;
    }

    intToRGB(i){
        var c = (i & 0x00FFFFFF)
            .toString(16)
            .toUpperCase();

        return "00000".substring(0, 6 - c.length) + c;
    }

    _template(act) {
        return `
			<div class="list-group-item list-group-item-action flex-column align-items-start"
			style="margin-top:5px" title="${act.description}">
				<div class="d-flex w-100 justify-content-between">
				    <a href=${act.link} target="_blank">
				        <img height="30px" width="30px" title="${act.type}" alt="${act.type}" src="/images/${act.type}.png"/>
                    </a>
					<small><a href=${act.link} target="_blank">link</a></small>
				</div>
				<p class="mb-1">${act.summary}</p>
				<small>${act.userName}</small>
			</div>
		`
    };

    update(act) {
        this._element.insertAdjacentHTML('beforeend', this._template(act));
    };

    clean() {
        this._element.innerHTML = '';
    };

}