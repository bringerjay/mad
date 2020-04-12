package edu.neu.madcourse.numad20s_qizhou.repos;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.numad20s_qizhou.model.FamilyMember;

public class FamilyMemberRepository {

    private MemberDAO memberDAO;
    private LiveData<List<FamilyMember>> allMemebers;

    FamilyMemberRepository(Application application) {
        KanBanDatabase db = KanBanDatabase.getDatabase(application);
        memberDAO = db.memberDAO();
        allMemebers = memberDAO.getAllMembers();
    }

    LiveData<List<FamilyMember>> getAllMembers() {
        return allMemebers;
    }

    void insert(FamilyMember familyMember) {
        KanBanDatabase.databaseWriteExecutor.execute(() -> {
            memberDAO.insert(familyMember);
        });
    }

    public void deleteAll() {
        KanBanDatabase.databaseWriteExecutor.execute(() -> {
            memberDAO.deleteAll();
        });
    }

    public void deleteMemberByName(String name) {
        KanBanDatabase.databaseWriteExecutor.execute(() -> {
            memberDAO.deleteByName(name);
        });
    }

    public FamilyMember getMemberByName(String name) {
        return memberDAO.getMemberByName(name);
    }

}
