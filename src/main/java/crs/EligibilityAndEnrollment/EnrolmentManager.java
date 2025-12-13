package crs.EligibilityAndEnrollment;

import java.util.ArrayList;
import java.util.List;

public class EnrolmentManager {
    
    private List<Student> eligibleStudents;
    private List<Student> ineligibleStudents;
    
    public EnrolmentManager() {
        eligibleStudents = new ArrayList<>();
        ineligibleStudents = new ArrayList<>();
    }
    
    public void checkAllStudents(EligibilityChecker checker) {
        eligibleStudents.clear();
        ineligibleStudents.clear();
        
        for (Student student : checker.getAllStudents()) {
            if (checker.isEligible(student.getStudentID())) {
                eligibleStudents.add(student);
            } else {
                ineligibleStudents.add(student);
            }
        }
    }
    
    public List<Student> getEligibleStudents() {
        return eligibleStudents;
    }
    
    public List<Student> getIneligibleStudents() {
        return ineligibleStudents;
    }
}