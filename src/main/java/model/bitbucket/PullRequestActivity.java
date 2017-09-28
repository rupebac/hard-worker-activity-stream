package model.bitbucket;

import com.google.gson.internal.LinkedTreeMap;
import model.Act;

import java.time.ZonedDateTime;

public class PullRequestActivity extends Act {

    public PullRequestActivity(LinkedTreeMap pr) {
        LinkedTreeMap prType;
        try
        {
            setType(getPRType(pr));
            prType = (LinkedTreeMap) pr.get(getType());
        } catch (NullPointerException e){
            return;
        }

        try
        {
            setId(((LinkedTreeMap) prType.get("pullrequest")).get("id").toString());
        } catch (NullPointerException e){
            //Gotcha!
        }

        try
        {
            setLink(((LinkedTreeMap) ((LinkedTreeMap) ((LinkedTreeMap) prType.get("pullrequest")).get("links")).get("html")).get("href").toString());
        }catch (NullPointerException e){
            //Gotcha!;
        }

        try
        {
            setSummary((((LinkedTreeMap) prType.get("pullrequest")).get("title").toString()));
        }catch (NullPointerException e){
            //Gotcha!
        }

        try
        {
            setDate(getValueDate(pr));
            if (getDate() != null) {
                getDate().minusHours(3);
            }
        }catch (NullPointerException e){
            //Gotcha!
        }

        try
        {
            setUserName((((LinkedTreeMap) prType.get("user")).get("username").toString()));
        }catch (NullPointerException e){
            //Gotcha!
        }
    }

    private static String getPRType(LinkedTreeMap value){
        try {
            return value.keySet().toArray()[0].toString();
        }catch (Exception e){
            //Gotcha!
            return null;
        }
    }

    public static  ZonedDateTime getValueDate(LinkedTreeMap value){
        try {
            String type = getPRType(value);
            LinkedTreeMap pr = (LinkedTreeMap) value.get(type);
            if(pr.containsKey("created_on")){
                return ZonedDateTime.parse(pr.get("created_on").toString());
            }else{
                return ZonedDateTime.parse(pr.get("date").toString());
            }

        }catch (Exception e){
            //Gotcha!
            return null;
        }
    }
}
