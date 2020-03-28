package edu.neu.madcourse.numad20s_qizhou.model;
import java.io.Serializable;
public class Card implements Serializable {

 public FamilyMember familyMember;
 public Task task;
 public Boolean isCardAssigned;
 public CardPriority cardPriority;
 public CardStatus cardStatus;


 private Card(FamilyMember familyMember, Task task, CardPriority cardPriority,
              CardStatus cardStatus, Boolean isCardAssigned)
 {
     this.familyMember = familyMember;
     this.task = task;
     this.cardPriority = cardPriority;
     this.cardStatus = cardStatus;
     this.isCardAssigned = isCardAssigned;
 }

    public enum CardPriority {LOW, MEDIUM, HIGH;}
    public enum CardStatus {TODO, INPROGRESS, COMPLETED}

}

