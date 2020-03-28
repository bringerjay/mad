package edu.neu.madcourse.numad20s_qizhou.model;

import java.io.Serializable;

public class FamilyMember implements Serializable {
    public String memberName;
    public String memberEmail;
    public String memberEmployeeId;
    public int memberImage;

    private FamilyMember(
            String memberName,
            String memberEmail,
            String memberEmployeeId,
            int memberImage
    )
    {
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberEmployeeId = memberEmployeeId;
        this.memberImage = memberImage;
    }
}
