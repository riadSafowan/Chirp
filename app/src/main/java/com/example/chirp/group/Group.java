package com.example.chirp.group;

public class Group {
    String groupsName;
    String groupsID;
    String creatorsName;
    String creatorsID;

    public Group(String groupsName, String groupsID) {
        this.groupsName = groupsName;
        this.groupsID = groupsID;
    }

    public Group(String groupsName) {
        this.groupsName = groupsName;
    }

    public Group(String groupsName, String groupsID, String creatorsName, String creatorsID) {
        this.groupsName = groupsName;
        this.groupsID = groupsID;
        this.creatorsName = creatorsName;
        this.creatorsID = creatorsID;

    }

    public String getGroupsName() {
        return groupsName;
    }

    public String getGroupsID() {
        return groupsID;
    }

    public String getCreatorsName() {
        return creatorsName;
    }

    public String getCreatorsID() {
        return creatorsID;
    }


}
