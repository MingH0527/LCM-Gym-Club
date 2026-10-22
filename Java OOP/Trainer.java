import java.util.ArrayList;
import java.util.List;

public class Trainer extends Account {

    // --  ATTRIBUTES ----------------------------------------------
    private String specialty;
    private int experienceYears;
    private double ratingAvg;
    private String motto;
    private String availabilityStatus;
    private MyProfile profile;
    private int ratingCount;


    // -- CONSTRUCTOR --------------------------------------------------
    /**
     * Uses super() to pass credentials up to Account - fixing the original
     * code which extended Account but did not explicitly call super().
     */
  public Trainer(String trainerId, String passwordHash, String accountStatus,MyProfile profile, String specialty, int experienceYears,double ratingAvg, String motto) {
        this(trainerId, passwordHash, accountStatus, profile, specialty,
             experienceYears, ratingAvg, motto, 0);
    }

    public Trainer(String trainerId, String passwordHash, String accountStatus,MyProfile profile, String specialty, int experienceYears,double ratingAvg, String motto, int ratingCount) {
        super(trainerId, passwordHash, accountStatus);
        this.profile = profile;
        this.specialty = specialty;
        this.experienceYears = experienceYears;
        this.ratingAvg = ratingAvg;
        this.motto = motto;
        this.ratingCount = ratingCount;
        this.availabilityStatus = accountStatus;
    }


    // -- OPERATIONS -----------------------------------------------

    /**
     * +viewSchedule(month:int, week:int) : List<ClassSession>
     * Returns classes for this trainer in the given month/week.
     */
    public List<ClassSession> viewSchedule(int month, int week,
                                           List<ClassSession> allSessions) {
        List<ClassSession> result = new ArrayList<>();
        for (ClassSession cs : allSessions) {
            if (cs.getTrainerId().equalsIgnoreCase(getAccountId())) {
                result.add(cs);
            }
        }
        return result;
    }

    /**
     * +viewClassRoster(classNo:int) : List<Member>
     * Returns all members who have an UPCOMING or CONFIRMED class booking for classNo.
     */
    public List<Member> viewClassRoster(int classNo, List<Member> allMembers) {
        List<Member> roster = new ArrayList<>();
        for (Member m : allMembers) {
            for (ClassBooking cb : m.getClassBookings()) {
                if (cb.getClassNo() == classNo
                        && !ClassBooking.CANCELLED.equals(cb.getStatus())) {
                    roster.add(m);
                    break;
                }
            }
        }
        return roster;
    }

    /**
     * +viewAssignedClients() : List<Member>
     * Returns all members who have a non-cancelled trainer appointment with this trainer.
     */
    public List<Member> viewAssignedClients(List<Member> allMembers) {
        List<Member> clients = new ArrayList<>();
        for (Member m : allMembers) {
            for (TrainerAppointment ta : m.getAppointments()) {
                if (ta.getTrainerId().equalsIgnoreCase(getAccountId())
                        && !TrainerAppointment.CANCELLED.equals(ta.getStatus())) {
                    clients.add(m);
                    break;
                }
            }
        }
        return clients;
    }

    /**
     * +updateClientWeight(memberId:String, newWeightKg:double) : void
     * Finds the member in the list and updates their health profile weight.
     */
    public void updateClientWeight(String memberId, double newWeightKg,
                                   String date, List<Member> allMembers) {
        for (Member m : allMembers) {
            if (m.getAccountId().equalsIgnoreCase(memberId.trim())) {
                if (m.getHealthProfile() != null) {
                    m.getHealthProfile().updateWeight(newWeightKg, date);
                }
                return;
            }
        }
    }

    /** +editMotto(newMotto:String) : void */
    public void editMotto(String newMotto) {
        this.motto = newMotto;
    }

    /**
     * Recalculates the running average rating when a new rating is submitted.
     * Called from Member.rateTrainer().
     */
    public void addRating(double newRating) {
        ratingCount++;
        ratingAvg = ((ratingAvg * (ratingCount - 1)) + newRating) / ratingCount;
    }

    // -- GETTERS / SETTERS --------------------------------------------
    public MyProfile getProfile() { 
    	return profile;
    	 }
    public String getSpecialty() { 
    	return specialty; 
    		}
    public int getExperienceYears() { 
    	return experienceYears;
    	 }
    public double getRatingAvg() { 
    	return ratingAvg;
    	 }
    public String getMotto() { 
    	return motto;
    	 }
    public String getAvailabilityStatus() { 
    	return availabilityStatus;
    	 }
    public int getRatingCount() { 
    	return ratingCount;
    	 }

    public void setSpecialty(String specialty) {
    	 this.specialty = specialty; 
    	 	}
    public void setExperienceYears(int years) {
    	 this.experienceYears = years; 
    	 	}
    public void setRatingAvg(double r) { 
    	this.ratingAvg = r; 
    		}
    public void setRatingCount(int c) { 
    	this.ratingCount = c; 
    		}
    public void setAvailabilityStatus(String s) { 
    	this.availabilityStatus = s; 
    		}
    
    public String getStatus() {
    return availabilityStatus;
     }

    public void setStatus(String status) {
    this.availabilityStatus = status;
     }
     @Override
public String getRole() {
    return "Trainer";
}
}