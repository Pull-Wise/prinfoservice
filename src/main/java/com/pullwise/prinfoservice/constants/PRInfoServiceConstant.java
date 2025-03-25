package com.pullwise.prinfoservice.constants;

public class PRInfoServiceConstant {

    //"https://api.github.com/repos/{owner}/{repo}/pulls/{pull_number}/files"
    public static final String GITHUB_API_URL = "https://api.github.com/repos/%s/%s/pulls/%s/files";

    //"https://api.github.com/repos/{owner}/{repo}/pulls/{pull_number}/comments"
    public static final String GITHUB_PR_POST_COMMENTS = "https://api.github.com/repos/%s/%s/issues/%s/comments";

    //"https://api.github.com/repos/" + OWNER + "/" + REPO + "/installation";
    public static final String GITHUB_INSTALLATION_ID_URL = "https://api.github.com/repos/%s/%s/installation";

    public static final String GITHUB_ACTION_OPENED = "opened";
    public static final String GITHUB_ACTION_REOPENED = "reopened";
    public static final String GITHUB_INSTALLATION_ACTION_CREATED = "created";
    public static final String GITHUB_INSTALLATION_ACTION_DELETED = "deleted";
    public static final boolean ACTIVE_F_TRUE = true;
    public static final boolean ACTIVE_F_FALSE = false;
    public static final String GITHUB_INSTALLATION_ACTION_REPO_ADDED = "added";
    public static final String GITHUB_INSTALLATION_ACTION_REPO_REMOVED = "removed";



}
