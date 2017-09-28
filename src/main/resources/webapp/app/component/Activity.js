class Activity {

    constructor(element) {
        let $ = document.querySelector.bind(document);
        this._element = $(".list-group");
    }

    _template(act) {
        return `
			<div class="list-group-item list-group-item-action flex-column align-items-start" style="margin-top:5px">
				<div class="d-flex w-100 justify-content-between">
				    <a href=${act.link} target="_blank">
				        <img height="30px" width="30px" title="${act.type}" alt="${act.type}" src="/images/${act.type}.png"/>
                    </a>
					<small>${act.date}</small>
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