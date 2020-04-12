package edu.neu.madcourse.numad20s_qizhou.repos;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.numad20s_qizhou.model.FamilyMember;

public class MemberViewModel extends AndroidViewModel {

    private FamilyMemberRepository familyMemberRepository;

    private LiveData<List<FamilyMember>> allMembers;

    public MemberViewModel (Application application) {
        super(application);
        familyMemberRepository = new FamilyMemberRepository(application);
        allMembers = familyMemberRepository.getAllMembers();
    }

    LiveData<List<FamilyMember>> getAllMembers() { return allMembers; }

    public void insert(FamilyMember familyMember) { familyMemberRepository.insert(familyMember); }

    public void deleteAll() {
        familyMemberRepository.deleteAll();
    }

    public FamilyMember getMemberByName(String name) {
        return familyMemberRepository.getMemberByName(name);
    }

    public ArrayList<FamilyMember> getCurrentMemebers(){
        List<FamilyMember> currentTasks = familyMemberRepository.getAllMembers().getValue();
        if (familyMemberRepository.getAllMembers().getValue() == null) {
            ArrayList<FamilyMember> arrayList = new ArrayList<>();
            return arrayList;
        }
        ArrayList<FamilyMember> arrayList = new ArrayList<>(currentTasks.size());
        arrayList.addAll(currentTasks);
        return arrayList;
    }
    public void deleteMemberByName(String name) {
        KanBanDatabase.databaseWriteExecutor.execute(() -> {
            familyMemberRepository.deleteMemberByName(name);
        });
    }
}
