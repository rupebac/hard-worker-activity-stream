class Filter {

    constructor(){
        this.$ = document.querySelector.bind(document);

        let cookieHelper = new Cookie();

        let filterCookie = cookieHelper.getCookie("filterCookie");

        if(filterCookie){
            let filterCookieArray  = filterCookie.split(":").map(s => s.split('='));
            this.$(".date").value = filterCookieArray[0][1];
            this.$(".jira-user").value = filterCookieArray[2][1];
            this.$(".repo-list").value = filterCookieArray[3][1];
        }else{
            let now = new Date();
            let day = ("0" + now.getDate()).slice(-2);
            let month = ("0" + (now.getMonth() + 1)).slice(-2);
            let today = now.getFullYear()+"-"+(month)+"-"+(day) ;
            this.$(".date").value = today;
        }
    }

    get date(){
        return this.$(".date").value;
    }

    get jiraUser(){
        return this.$(".jira-user").value;
    }

    get repoList(){
        return this.$(".repo-list").value;
    }

    disable(){
        document.querySelector('.date').setAttribute('disabled','');
        document.querySelector('.jira-user').setAttribute('disabled','');
        document.querySelector('.repo-list').setAttribute('disabled','');
        document.querySelector('.get-button').setAttribute('disabled','');
        document.querySelector('.clean-button').setAttribute('disabled','');
    }

    enable(){
        document.querySelector('.date').removeAttribute('disabled');
        document.querySelector('.jira-user').removeAttribute('disabled');
        document.querySelector('.repo-list').removeAttribute('disabled');
        document.querySelector('.get-button').removeAttribute('disabled');
        document.querySelector('.clean-button').removeAttribute('disabled');
    }

}