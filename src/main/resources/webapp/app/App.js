class App{

	constructor(){
		this._activity = new Activity();
		this._actsService = new ActsService();
		this._filter = new Filter();
		this._loader = new Loader();
	}

    getActivities(){
        let calendarAccessToken, bitbucketAccessToken;

        let cookieHelper = new Cookie();

        let cookie = `date=${this._filter.date}:calendar=${this._filter.calendar}:jiraUser=${this._filter.jiraUser}:repoList=${this._filter.repoList}`;

        cookieHelper.setCookie("filterCookie", cookie, 30);

        this.clearList();

        if (!this._filter.repoList && !this._filter.jiraUser) {
             new Alert('Warning!', 'Blank filter.', 'warning');
             return;
        }


        bitbucketAccessToken = cookieHelper.getCookie('bitbucketAccessToken');
        if(this._filter.repoList && !bitbucketAccessToken){
            new BitbucketAuth().authenticate();
            return;
        }

        calendarAccessToken = cookieHelper.getCookie('calendarAccessToken');
        if(!calendarAccessToken){
            new CalendarAuth().authenticate();
            return;
        }

        this._loader.show();
        this._filter.disable();
        this._actsService.getActs( this._filter )
                            .then( acts => {
                                if (acts.length === 0) {
                                    new Alert('Warning!', 'Don\'t you work at this date?.', 'warning');
                                } else {
                                    new Alert('You\'re a hard worker!', `${acts.length} activities found.`, 'success');
                                    acts.forEach( a => this._activity.update( a ));
                                }
                            }).catch ( error => new Alert('Oh snap! Something went wrong...', error, 'danger'))
                            .then ( () => {
                                    this._loader.hide();
                                    this._filter.enable();
                                }
                            );
    }

    clearList(){
        new Alert().close();
        this._activity.clean();
    }

}